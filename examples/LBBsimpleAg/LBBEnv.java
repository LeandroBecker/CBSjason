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

public class LBBEnv extends Environment {

    static Logger logger = Logger.getLogger(LBBEnv.class.getName());

    //private long t_init = System.nanoTime(); //LB: initial time
    private long t_updt = 0; //LB: update timestamp
    private int cont = 0;
    private Literal bArray[];

    @Override
    public void init(String[] args) {
        bArray = new Literal[8];
        for(int i=0; i<8; i++){
            int v = i+1;
            Literal lit = Literal.parseLiteral("belief(" + v +")");
            bArray[i] = lit;
        }
        
        updatePercepts();
    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        //logger.info(ag+" doing: "+ action);
        try {
            if (action.getFunctor().equals("faz")) {
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                //int y = (int)((NumberTerm)action.getTerm(1)).solve();
                fazAction(x); //LB here is a possible critical funcion
            } else if (action.getFunctor().equals("manual")) {
                //int x = (int)((NumberTerm)action.getTerm(0)).solve();
                //int y = (int)((NumberTerm)action.getTerm(1)).solve();
                manualAction(); //LB here is another possible critical funcion
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updatePercepts(); 

        try {
            Thread.sleep(200);
        } catch (Exception e) {}
        informAgsEnvironmentChanged();
        return true;
    }
    
    void manualAction(){
        //logger.info("LBB fazAction " + String.valueOf(i) + " " + String.valueOf(cont+1) + " (ms): " + String.valueOf(t_curr)); // - t_init));   
        //logger.info("LBB manualAction " + String.valueOf(0) + " time (ms): " + String.valueOf(t_curr - t_init));   
        //logger.info("LBB TransitionS, perceive+buf time (ns): " + String.valueOf(end-start)); //LB  
        //if(i<7) cont++;
        //else cont = 0;
        //cont++;
        logger.info("LBB e2eAction " + String.valueOf(cont) + " time (ms): " + String.valueOf(System.nanoTime() - t_updt));   
    }

    void fazAction(int i){
        //long t_curr = System.nanoTime(); //LB: current time
        //logger.info("LBB fazAction " + String.valueOf(i) + " " + String.valueOf(cont+1) + " (ms): " + String.valueOf(t_curr)); // - t_init));   
        //logger.info("LBB TransitionS, perceive+buf time (ns): " + String.valueOf(end-start)); //LB  
        //if(i<7) cont++;
        //else cont = 0;
        //cont++;
    }

    /** creates the agents perception */
    void updatePercepts() {
        //long t_curr = System.currentTimeMillis(); //LB: current time
        //logger.info("LBBegin Env - updatePercepts(); elapsed time (ms): " + String.valueOf(t_curr - t_init));
        //t_init = t_curr;
        clearPercepts();

        //LB: adding 10 "DISturbing" beliefs
        for(int i=10; i<20; i++){
            Literal lit = Literal.parseLiteral("dis(" + i +")");
            addPercept(lit);
        }

        t_updt = System.nanoTime(); //LB: collects updt time

        //adding the belief that triggers the next action
        //addPercept(bArray[cont%8]);
    }

    @Override
    public boolean updateCBS() {
        //LBB: for testing, only 1 CBS set TRUE
        if((cont++ % 2) == 0){
            logger.info("TRUE updateCBS "+ cont);
            cbsArray[0] = Boolean.TRUE;
        }
        else{
            logger.info("FALSE updateCBS "+ cont);
            cbsArray[0] = Boolean.FALSE;
        }
        

        //long t_curr = System.currentTimeMillis(); //LB: current time
        //t_init = t_curr;

        return true;
    }   
}

