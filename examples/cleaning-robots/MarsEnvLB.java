import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class MarsEnvLB extends Environment {

    public static final int GSize = 7; // grid size
    public static final int GARB  = 16; // garbage code in grid model

    public static final Term    ns = Literal.parseLiteral("next(slot)");
    public static final Term    pg = Literal.parseLiteral("pick(garb)");
    public static final Term    dg = Literal.parseLiteral("drop(garb)");
    public static final Term    bg = Literal.parseLiteral("burn(garb)");
    public static final Literal g1 = Literal.parseLiteral("garbage(r1)");
    public static final Literal g2 = Literal.parseLiteral("garbage(r2)"); 

    public static final Term   cr0 = Literal.parseLiteral("critReac0");

    static Logger logger = Logger.getLogger(MarsEnvLB.class.getName());

    private MarsModel model;
    private MarsView  view;
    
    private long t_init = System.currentTimeMillis(); //LB: initial time
    List<Long> perception_times = new ArrayList<>();
    List<Long> reaction_times = new ArrayList<>();

    @Override
    public void init(String[] args) {
        model = new MarsModel(); 
        view  = new MarsView(model);
        model.setView(view);
        updatePercepts();
    }

    @Override
    public void stop() {
        int i=0;
        logger.info("Qtd perceive: "+ perception_times.size());
        logger.info("Qtd reaction: "+ reaction_times.size());
        for (Long perT : perception_times) {
            if (i < reaction_times.size()){
                Long diff = reaction_times.get(i++) - perT;
                logger.info(i+"th reacTime: "+ diff);    
            }
        }
        super.stop();
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
            } else if (action.equals(bg)) { 
                model.burnGarb();
                cbsArray[0] = Boolean.FALSE;
                reaction_times.add(System.nanoTime()); //LB: saves perception time
            } else if (action.getFunctor().equals("manual")) {
                manualAction(); //LB fix here for func of interest
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(50);
        } catch (Exception e) {}

        updatePercepts();
        informAgsEnvironmentChanged();
        return true;
    }

    void manualAction(){
        long t_curr = System.nanoTime(); //LB: current time
        logger.info("LBB manualAction " + String.valueOf(0) + " time (ms): " + String.valueOf(t_curr - t_init));   
    }

    /** creates the agents perception based on the MarsModel */
    void updatePercepts() {
        clearPercepts();

        Location r1Loc = model.getAgPos(0);
        Location r2Loc = model.getAgPos(1);

        Literal pos1 = Literal.parseLiteral("pos(r1," + r1Loc.x + "," + r1Loc.y + ")");
        Literal pos2 = Literal.parseLiteral("pos(r2," + r2Loc.x + "," + r2Loc.y + ")");

        addPercept(pos1);
        addPercept(pos2);

        if (model.hasObject(GARB, r1Loc)) {
            addPercept(g1);
        }
        if (model.hasObject(GARB, r2Loc)) {
            if(cbsArray[0] == Boolean.FALSE){
                perception_times.add(System.nanoTime()); //LB: saves perception time
                cbsArray[0] = Boolean.TRUE;    
                //addPercept(g2);
            }
        }
        
        // try {
        //     Thread.sleep(50);
        // } catch (Exception e) {}

        addKpercepts(1000);
    }

    void addKpercepts(int x){
        for(int i=0; i<x; i++){
            Literal lit = Literal.parseLiteral("fakeP(" + i + ")");
            addPercept(lit);
        }
        return;
    }

    @Override
    public boolean updateCBS() {
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

        return true;
    }   

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
            case MarsEnvLB.GARB:
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
