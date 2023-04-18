import java.util.*;

import jason.architecture.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class StatArch extends AgArch {

    //private transient Logger logger     = null;

    Map<String,Integer> msgCount = new HashMap<>();
    Map<String,Integer> actCount = new HashMap<>();

    @Override
    public Boolean[] perceiveCBS() {
      //System.out.println("perceiveCBS: ");   
      //Boolean[] percepts = infraEnv.getUserEnvironment().getPerceptsCBS(getAgName());
      Boolean[] percepts = new Boolean[8];
      return percepts;
 }

    @Override
    public void stop() {
        System.out.println("Sent messages: "+msgCount);
        int t = 0;
        for (int v: msgCount.values())
            t += v;
        System.out.println("Total sent messages: "+t);

        System.out.println("Actions: "+actCount);
        t = 0;
        for (int v: actCount.values())
            t += v;
        System.out.println("Total actions: "+t);
    }

    @Override
    public void sendMsg(Message m) throws Exception {
        super.sendMsg(m);

        Integer c = msgCount.get(m.getReceiver());
        if (c == null)
          c = 0;
        msgCount.put(m.getReceiver(),c+1);
    }

    @Override
    public void act(ActionExec action) {
      //super.act(action);

      String str = action.getActionTerm().getFunctor();
      Integer c = actCount.get(str);
      if (c == null)
        c = 0;
      actCount.put(str,c+1);
      //logger.info("Action-executed: " + str); 
    }

    @Override
    public void actCR(Boolean[] cActions) {    
      //FIX: should in fact only trigger those (critical) actions considered TRUE
      String str = "critReac0"; //action.getActionTerm().getFunctor();
      Integer c = actCount.get(str);
      if (c == null)
        c = 0;
      actCount.put(str,c+1);
      //System.out.println("Action-executed: " + str); 
    }

}
