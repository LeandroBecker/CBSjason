import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.FileWriter;
import java.io.IOException;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class MarsEnv extends Environment {

    public static final int GSize = 7; // grid size
    public static final int GARB  = 16; // garbage code in grid model

    public static final Term    ns = Literal.parseLiteral("next(slot)");
    public static final Term    pg = Literal.parseLiteral("pick(garb)");
    public static final Term    dg = Literal.parseLiteral("drop(garb)");
    public static final Term    bg = Literal.parseLiteral("burn(garb)");
    public static final Literal g1 = Literal.parseLiteral("garbage(r1)"); //garbage(r1)"
    public static final Literal g2 = Literal.parseLiteral("garbage(r2)"); //garbage(r2)"

    public static final Term    cr0 = Literal.parseLiteral("critReac0");
    public static final Literal cp0 = Literal.parseLiteral("cr0Per"); 
    public static final Literal the = Literal.parseLiteral("theEnd(r1)"); //to end the agent

    static Logger logger = Logger.getLogger(MarsEnv.class.getName());

    private MarsModel model;
    private MarsView  view;
    
    // LBB: following variables added for the criticalThings experiment
    int stepCtd = 0; // time in milliseconds

    List<Long> perception_times = new ArrayList<>();
    List<Long> reaction_times = new ArrayList<>();

    List<Long> beginAkP_times = new ArrayList<>();
    List<Long> endAkP_times = new ArrayList<>();

    List<Long> cbsUpdate_times = new ArrayList<>();

    // private TimeOutThread timeoutThread = null;
    private long stepTimeout = 200;
    private boolean flagCvEv = Boolean.FALSE;

    @Override
    public void init(String[] args) {
        model = new MarsModel(); 
        // view  = new MarsView(model);
        // model.setView(view);
        updatePercepts();

        // LBB: implementation of a new thread for the critical perceptions
        // v0: C&P from 'TimeSteppedEnvironment extends Environment'
        // if (timeoutThread == null) {
        //     if (stepTimeout > 0) {
        //         timeoutThread = new TimeOutThread(stepTimeout);
        //         timeoutThread.start();
        //     }
        // } else {
        //     timeoutThread.allAgFinished();
        // } 

    }

    @Override
    public void stop() {
        String fileName = "reacTimes.log";
        try (FileWriter writer = new FileWriter(fileName)) {
            //writer.write(System.lineSeparator());
            //LB: log perception times
            int i=0; 
            long sumT = 0;
            long avgT = 0;
            writer.write("Qtd updates:  "+ beginAkP_times.size());  writer.write(System.lineSeparator());
            writer.write("Qtd CBSupdat: "+ cbsUpdate_times.size());  writer.write(System.lineSeparator());
            writer.write("Qtd perceive: "+ perception_times.size());  writer.write(System.lineSeparator());
            writer.write("Qtd reaction: "+ reaction_times.size());  writer.write(System.lineSeparator());
            for (Long perT : perception_times) {
                if (i == 0){
                    i++;
                }
                else if (i < reaction_times.size()){
                    Long diff = reaction_times.get(i++) - perT;
                    if(avgT == 0){
                        avgT = diff;
                        writer.write(i+"th reacTime: "+ diff);  writer.write(System.lineSeparator());
                    }
                    else if (diff < avgT*5){
                        sumT = sumT + diff;
                        avgT = sumT/i-1;   
                        writer.write(i+"th reacTime: "+ diff);  writer.write(System.lineSeparator());
                    }
                    else{
                        i--;
                    }    
                }
            }
            if(i<perception_times.size()){ 
                writer.write("Missing reactions: "+ (perception_times.size()-i));  writer.write(System.lineSeparator());
            }
            writer.write("Avg reaction times: "+ avgT);  writer.write(System.lineSeparator()); 

            //LB: log Avg time to K perceptions
            i = 0;
            sumT = 0;
            for (Long perT : beginAkP_times) {
                if (i < endAkP_times.size()-1){
                    sumT = sumT + endAkP_times.get(i++) - perT;   
                    //logger.info(i + " update times: "+ (beginAkP_times.get(i+1) - beginAkP_times.get(i++)));
                }
            }
            if(i>0){
                writer.write("Avg time to K Percepts: "+ sumT/i);  writer.write(System.lineSeparator()); 
            }
        // END log writting
    } catch (IOException e) {
        System.err.println("Error writing to file: " + e.getMessage()); 
    }    

        //Single original function
        super.stop();
        //if (timeoutThread != null) timeoutThread.interrupt(); //LBB new
    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        //logger.info(ag+" doing: "+ action);
        try {
            if (action.equals(ns)) {
                model.nextSlot();
            } else if (action.getFunctor().equals("move_towards")) {
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                model.moveTowards(x,y);
            } else if (action.equals(pg)) {
                model.pickGarb();
            } else if (action.equals(dg)) {
                model.dropGarb();
            } else if (action.equals(bg)) { // critical action
                model.burnGarb();
            } else if (action.equals(cr0)) { 
                criticalAction(); //LB fix here for func of interest
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int sleepT = 50; //MUST be a multiple of 50
        try { //LB comment: this sleep was originally here with 200ms
            Thread.sleep(sleepT);
            stepCtd = stepCtd + (sleepT/50); //1 step is 50ms
        } catch (Exception e) {}

        updatePercepts();
        informAgsEnvironmentChanged();
        return true;
    }

    void criticalAction(){
        // long t_curr = System.nanoTime(); //LB: current time
        // logger.info("LBB manualAction " + String.valueOf(0) + " time (ms): " + String.valueOf(t_curr - t_init));   
        //synchronized (cbsArray) {
            //cbsArray[0] = Boolean.FALSE; // reset perception after the action
        //}
        reaction_times.add(System.nanoTime()); //LB: saves perception time
    }

    /** creates the agents perception based on the MarsModel */
    void updatePercepts() {
        // if((cbsArray[0] == Boolean.FALSE))
            clearPercepts();
        //updateCBS();

        Location r1Loc = model.getAgPos(0);
        Location r2Loc = model.getAgPos(1);

        // LBB: condition to finish the program
        if((r1Loc.x >= 6) && (r1Loc.y >= 6)){
            Literal result = new LiteralImpl("theEnd"); 
            result.addTerm(new NumberTermImpl(beginAkP_times.size())); 
            addPercept(result);
            //logger.info("END END END END END");
        }

        Literal pos1 = Literal.parseLiteral("pos(r1," + r1Loc.x + "," + r1Loc.y + ")");
        Literal pos2 = Literal.parseLiteral("pos(r2," + r2Loc.x + "," + r2Loc.y + ")");

        addPercept(pos1);
        addPercept(pos2);

        if (model.hasObject(GARB, r1Loc)) {
            addPercept(g1);
        }
        if (model.hasObject(GARB, r2Loc)) {
            addPercept(g2);
        }
        
        // //LB: this sleep was added here for testing
        // try {
        //     Thread.sleep(50);
        // } catch (Exception e) {}

        beginAkP_times.add(System.nanoTime()); //LB: saves perception time
        addKpercepts(1000);
        endAkP_times.add(System.nanoTime()); //LB: saves perception time 
    }

    void addKpercepts(int x){
        if(stepCtd >= 10) {
            stepCtd = 0;
            // LBB: Incomming two lines for Std-Jas, third for Critical-Jas
            perception_times.add(System.nanoTime()); //LB: saves perception time
            addPercept(cp0); 
            // flagCvEv = Boolean.TRUE;
        }

        for(int i=0; i<x; i++){
            Literal lit = Literal.parseLiteral("fakeP(" + i + ")");
            addPercept(lit);
        }
        return;
    }

    // @Override
    // public boolean updateCBS() {
        // Location r2Loc = model.getAgPos(1);

        // if (model.hasObject(GARB, r2Loc)) {
        //     perception_times.add(System.nanoTime()); //LB: saves perception time
        //     cbsArray[0] = Boolean.TRUE;
        //     //addPercept(g2);
        // }
        // //LBB: for testing, only 1 CBS set TRUE
        // cbsArray[0] = Boolean.TRUE;
        // logger.info("Correct updateCBS");

        // long t_curr = System.currentTimeMillis(); //LB: current time
        // //logger.info("LBBegin Env - updatePercepts(); elapsed time (ms): " + String.valueOf(t_curr - t_init));
        // t_init = t_curr;

        // Location r2Loc = model.getAgPos(1);
        // if (model.hasObject(GARB, r2Loc)) {
        // cbsUpdate_times.add(System.nanoTime()); //LB: saves perception time

        // if((cbsArray[0] == Boolean.FALSE) && (stepCtd >= 10) ) {
        //     stepCtd = 0;        // LB: next 10 lines commented because do not serve in Std Jason
        // if((cbsArray[0] == Boolean.FALSE) && flagCvEv ) {
        //     flagCvEv = Boolean.FALSE;
        //     perception_times.add(System.nanoTime()); //LB: saves perception time
        //     // LBB: bellow used for critical things
        //     synchronized (cbsArray) {
        //         cbsArray[0] = Boolean.TRUE;    
        //     }
        //     // Literal lit = Literal.parseLiteral("cr0Per");
        //     // addPercept(lit); 
        //     informAgsEnvironmentChanged();
        // }
        // return true;
    //}   

    // LBB: new implementation for the critical updates
    // private void startNewStep() {
    //     if (!isRunning()) return;

    //     // synchronized (requests) {
    //     synchronized (cbsArray) {
    //         //step++;
    //         //logger.info("#"+requests.size());
    //         //logger.info("#"+overRequests.size());
    //         try {
    //             // execute all scheduled actions
    //             // for (ActRequest a: requests.values()) {
    //             //     a.remainSteps--;
    //             //     if (a.remainSteps == 0) {
    //             //         // calls the user implementation of the action
    //             //         a.success = executeAction(a.agName, a.action);
    //             //     }
    //             // }

    //             // updateAgsPercept();
    //             updateCBS();

    //             // notify the agents about the result of the execution
    //             // Iterator<ActRequest> i = requests.values().iterator();
    //             // while (i.hasNext()) {
    //             //     ActRequest a = i.next();
    //             //     if (a.remainSteps == 0) {
    //             //         getEnvironmentInfraTier().actionExecuted(a.agName, a.action, a.success, a.infraData);
    //             //         i.remove();
    //             //     }
    //             // }

    //             // clear all requests
    //             //requests.clear();

    //             // add actions waiting in over requests into the requests
    //             // Iterator<ActRequest> io = overRequests.iterator();
    //             // while (io.hasNext()) {
    //             //     ActRequest a = io.next();
    //             //     if (requests.get(a.agName) == null) {
    //             //         requests.put(a.agName, a);
    //             //         io.remove();
    //             //     }
    //             // }

    //             // the over requests could complete the requests
    //             // so test end of step again
    //             // if (nbAgs > 0 && testEndCycle(requests.keySet())) 
    //             //    startNewStep();
    //             // }

    //             // getEnvironmentInfraTier().informAgsEnvironmentChanged();

    //             // stepStarted(step);
    //         } catch (Exception ie) {
    //             if (isRunning() && !(ie instanceof InterruptedException)) {
    //                 logger.log(Level.WARNING, "act error in TimeoutThread !",ie);
    //             }
    //         }
    //     }
    // } 

    /** to be overridden by the user class /
    protected void stepStarted(int step) {
    } */

    /** to be overridden by the user class /
    protected void stepFinished(int step, long elapsedTime, boolean byTimeout) {
    } */


    // class TimeOutThread extends Thread {
    //     Lock lock = new ReentrantLock();
    //     Condition agActCond = lock.newCondition();
    //     long timeout = 200;
    //     boolean allFinished = false;

    //     public TimeOutThread(long to) {
    //         super("EnvironmentTimeOutThread");
    //         timeout = to;
    //     }

    //     public void allAgFinished() {
    //         lock.lock();
    //         allFinished = true;
    //         agActCond.signal();
    //         lock.unlock();
    //     }

    //     public void run() {
    //         try {
    //             while (true) {
    //                 // lock.lock();
    //                 // long lastStepStart = System.currentTimeMillis();
    //                 // boolean byTimeOut = false;
    //                 // if (!allFinished) {
    //                 //     byTimeOut = !agActCond.await(timeout, TimeUnit.MILLISECONDS);
    //                 // }
    //                 // allFinished = false;
    //                 // long now  = System.currentTimeMillis();
    //                 // long time = (now-lastStepStart);
    //                 // //stepFinished(step, time, byTimeOut); //FIX? see in the example if this is needed
    //                 // lock.unlock();
    //                 startNewStep();
    //                 Thread.sleep(timeout);
    //             }
    //         } catch (InterruptedException e) {
    //         } catch (Exception e) {
    //             logger.log(Level.SEVERE, "Error in timeout thread!",e);
    //         }
    //     }
    // } 

    class MarsModel extends GridWorldModel {

        public static final int MErr = 2; // max error in pick garb
        int nerr; // number of tries of pick garb
        boolean r1HasGarb = false; // whether r1 is carrying garbage or not

        Random random = new Random(System.currentTimeMillis());

        private MarsModel() {
            super(GSize, GSize, 2);

            // initial location of agents
            try {
                setAgPos(0, 0, 0);

                Location r2Loc = new Location(GSize/2, GSize/2);
                setAgPos(1, r2Loc);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // initial location of garbage
            // add(GARB, 3, 0);
            // add(GARB, GSize-1, 0);
            // add(GARB, 1, 2);
            // add(GARB, 0, GSize-2);
            // add(GARB, GSize-1, GSize-1);

            //LBB: modified to add more garbage
            add(GARB, 1, 0); add(GARB, 3, 0); add(GARB, 5, 0); 
            add(GARB, 0, 1); add(GARB, 2, 1); add(GARB, 4, 1); add(GARB, 6, 1); 
            add(GARB, 1, 2); add(GARB, 3, 2); add(GARB, 5, 2); 
            add(GARB, 0, 3); add(GARB, 2, 3); add(GARB, 4, 3); add(GARB, 6, 3); 
            add(GARB, 1, 4); add(GARB, 3, 4); add(GARB, 5, 4); 
            add(GARB, 0, 5); add(GARB, 2, 5); add(GARB, 4, 5); add(GARB, 6, 5); 
            add(GARB, 1, 6); add(GARB, 3, 6); add(GARB, 5, 6); 
//            add(GARB, 0, 6); add(GARB, 2, 6); add(GARB, 4, 6);   
}

        void nextSlot() throws Exception {
            Location r1 = getAgPos(0);
            r1.x++;
            if (r1.x == getWidth()) {
                r1.x = 0;
                r1.y++;
            }
            // finished searching the whole grid
            if (r1.y == getHeight()) {
                r1.y--; //by LB
                r1.x = getWidth()-1; //by LB
                return;
            }
            setAgPos(0, r1);
            setAgPos(1, getAgPos(1)); // just to draw it in the view
        }

        void moveTowards(int x, int y) throws Exception {
            Location r1 = getAgPos(0);
            if (r1.x < x)
                r1.x++;
            else if (r1.x > x)
                r1.x--;
            if (r1.y < y)
                r1.y++;
            else if (r1.y > y)
                r1.y--;
            setAgPos(0, r1);
            setAgPos(1, getAgPos(1)); // just to draw it in the view
        }

        void pickGarb() {
                // r1 location has garbage
            if (model.hasObject(GARB, getAgPos(0))) {
                // sometimes the "picking" action doesn't work
                // but never more than MErr times
                if (random.nextBoolean() || nerr == MErr) {
                    remove(GARB, getAgPos(0));
                    nerr = 0;
                    r1HasGarb = true;
                } else {
                    nerr++;
                }
            }
        }
        void dropGarb() {
            if (r1HasGarb) {
                r1HasGarb = false;
                add(GARB, getAgPos(0));
            }
        }
        void burnGarb() {
            // r2 location has garbage
            if (model.hasObject(GARB, getAgPos(1))) {
                remove(GARB, getAgPos(1));
            }
        }
    }

    class MarsView extends GridWorldView {

        public MarsView(MarsModel model) {
            super(model, "Mars World", 600);
            defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
            setVisible(true);
            repaint();
        }

        /** draw application objects */
        @Override
        public void draw(Graphics g, int x, int y, int object) {
            switch (object) {
            case MarsEnv.GARB:
                drawGarb(g, x, y);
                break;
            }
        }

        @Override
        public void drawAgent(Graphics g, int x, int y, Color c, int id) {
            String label = "R"+(id+1);
            c = Color.blue;
            if (id == 0) {
                c = Color.yellow;
                if (((MarsModel)model).r1HasGarb) {
                    label += " - G";
                    c = Color.orange;
                }
            }
            super.drawAgent(g, x, y, c, -1);
            if (id == 0) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.white);
            }
            super.drawString(g, x, y, defaultFont, label);
            repaint();
        }

        public void drawGarb(Graphics g, int x, int y) {
            super.drawObstacle(g, x, y);
            g.setColor(Color.white);
            drawString(g, x, y, defaultFont, "G");
        }

    }

}
