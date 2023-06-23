import jason.asSyntax.Structure;

import java.util.logging.Logger;

public class BEnv extends jason.environment.Environment {
    static Logger logger = Logger.getLogger(BEnv.class.getName());
    private long t_updt = 0; //LB: update timestamp
    int cont = 0;
    int ctdUpd = 0;

    @Override
    public boolean executeAction(String ag, Structure action) {
        logger.info(ag+" doing: "+ action);

        try {
            if (action.getFunctor().equals("critReac0")) {
                critReac0(); 
            } 
            else if (action.getFunctor().equals("dummy")) {
                dummy(); 
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
    void critReac0(){
        //logger.info("LBB fazAction " + String.valueOf(i) + " " + String.valueOf(cont+1) + " (ms): " + String.valueOf(t_curr)); // - t_init));   
        //logger.info("LBB manualAction " + String.valueOf(0) + " time (ms): " + String.valueOf(t_curr - t_init));   
        //logger.info("LBB TransitionS, perceive+buf time (ns): " + String.valueOf(end-start)); //LB  
        //if(i<7) cont++;
        //else cont = 0;
        //cont++;
        //logger.info("LBB e2eAction " + String.valueOf(cont) + " time (ms): " + String.valueOf(System.nanoTime() - t_updt));   
        logger.info("LBB e2eAction " + String.valueOf(cont++) + " time (ms): " + String.valueOf(System.nanoTime() - t_updt));   
    }
    
    void dummy(){
        logger.info("Dummy " + String.valueOf(cont) + " time (ms): " + String.valueOf(System.nanoTime() - t_updt));   
    }
    
    @Override
    public boolean updateCBS() {
        //LBB: for testing, only 1 CBS set TRUE
        // if((ctdUpd++ % 2) == 0){
        //     logger.info("TRUE updateCBS "+ ctdUpd);
        //     cbsArray[0] = Boolean.TRUE;
        // }
        // else{
        //     logger.info("FALSE updateCBS "+ ctdUpd);
        //     cbsArray[0] = Boolean.FALSE;
        // }       

        t_updt = 10; //System.nanoTime(); //LB: collects updt time
        cbsArray[0] = Boolean.TRUE;

        return true;
    }   

    /** creates the agents perception /
    void updatePercepts() {
        //long t_curr = System.currentTimeMillis(); //LB: current time
        //logger.info("LBBegin Env - updatePercepts(); elapsed time (ms): " + String.valueOf(t_curr - t_init));
        //t_init = t_curr;
        clearPercepts();

        //LB: adding 10 "DISturbing" beliefs
        // for(int i=10; i<20; i++){
        //     Literal lit = Literal.parseLiteral("dis(" + i +")");
        //     addPercept(lit);
        // }        

        //adding the belief that triggers the next action
        //addPercept(bArray[cont%8]);
    } */
}
    
