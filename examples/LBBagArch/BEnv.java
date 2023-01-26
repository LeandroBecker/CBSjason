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
            if (action.getFunctor().equals("manual")) {
                manualAction(); //LB here is another possible critical funcion
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
    void manualAction(){
        //logger.info("LBB fazAction " + String.valueOf(i) + " " + String.valueOf(cont+1) + " (ms): " + String.valueOf(t_curr)); // - t_init));   
        //logger.info("LBB manualAction " + String.valueOf(0) + " time (ms): " + String.valueOf(t_curr - t_init));   
        //logger.info("LBB TransitionS, perceive+buf time (ns): " + String.valueOf(end-start)); //LB  
        //if(i<7) cont++;
        //else cont = 0;
        //cont++;
        //logger.info("LBB e2eAction " + String.valueOf(cont) + " time (ms): " + String.valueOf(System.nanoTime() - t_updt));   
        logger.info("LBB e2eAction " + String.valueOf(cont++) + " time (ms): " + String.valueOf(System.nanoTime() - t_updt));   
    }
    
    @Override
    public boolean updateCBS() {
        //LBB: for testing, only 1 CBS set TRUE
        if((ctdUpd++ % 2) == 0){
            logger.info("TRUE updateCBS "+ ctdUpd);
            cbsArray[0] = Boolean.TRUE;
        }
        else{
            logger.info("FALSE updateCBS "+ ctdUpd);
            cbsArray[0] = Boolean.FALSE;
        }       

        t_updt = System.nanoTime(); //LB: collects updt time

        return true;
    }   

}
	
