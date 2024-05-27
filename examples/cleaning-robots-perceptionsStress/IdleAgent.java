import jason.asSemantics.*;
import jason.asSyntax.*;
import java.util.*;

public class IdleAgent extends Agent {

 static Term idle = ASSyntax.createAtom("idle");
    
 @Override
 public Intention selectIntention(Queue<Intention> intentions) {
        
   Iterator<Intention> ii = intentions.iterator();
   while (ii.hasNext()) {
     Intention i = ii.next();
     if (isIdle(i)) {
       if (intentions.size() == 1) {
         // there is only one intention and it has 
         // the "idle" annotation, returns that one
         ii.remove();
         return i;
       }
     } else {
       // the current intention is not idle, 
       // so it has higher priority
       ii.remove();
       return i;
     }
   }
   return null;
 }
    
 /** returns true if the intention has a "idle" annotation */
 private boolean isIdle(Intention i) {
   // looks for an "idle" annotation in every 
   // intended means of the intention stack
   //for (IntendedMeans im: i) {
   for (IntendedMeans im : i) {
     Pred label = im.getPlan().getLabel();
     if (label.hasAnnot(idle)) {
       return true;
     }
   }
   return false;
 }
}
