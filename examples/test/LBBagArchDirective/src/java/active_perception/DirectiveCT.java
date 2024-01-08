    // 3 & Beliefs & Individual & All & Time & Natural
package critical_things;

import jason.asSemantics.Agent;
import jason.asSyntax.*;
import jason.asSyntax.directives.*;
import jason.asSyntax.BodyLiteral.BodyType;
import static jason.asSyntax.ASSyntax.*;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import jason.asSyntax.Trigger.TEType;
import jason.asSyntax.Trigger.TEOperator;
import jason.JasonException;
import jason.architecture.AgArch;

import critical_things.CriticalThings;

public class DirectiveCT extends DefaultDirective implements Directive {

    public static void searchCTPlan(Agent outContext){
        Atom cp_atom = createAtom("cp"); 
        // Pred cp_label = new Pred(createLiteral("cp"));
        for(Plan p: outContext.getPL()){
            //if(p.getTrigger().hasAnnot()){
            if(p.getTrigger().getLiteral().hasAnnot(cp_atom)){
                System.out.println("v.1: Achou " + p.getTrigger().toString());
                p.getTrigger().getLiteral().delAnnot(cp_atom);
                //p.getTrigger().getLiteral().addAnnots(xx_atom);
            }
            else System.out.println("NAO  " + p.getTrigger().toString() + "; Literal: " + p.getTrigger().getLiteral().toString());
        }
    }

    public Agent process(Pred directive, Agent outerContent, Agent innerContent){
        if (outerContent == null)
            return null;

        //1. Testing searching and replacing cp annotation
        searchCTPlan(outerContent);

        if(false){
        //Get plans that has any belief marked with ap in context
        PlansApBel plans_ap_bel = CriticalThings.getPlansApBel(outerContent);

        Atom ap_atom = createAtom("ap");
        Atom rp_atom = createAtom("rp");
        Atom cp_atom = createAtom("cp"); //by LBB

        //Annotating inital goals with ap and first label
        CriticalThings.annotInitGoalsLabel(outerContent, plans_ap_bel, ap_atom);

        //add [ap] to !g inside plans body and first label
        CriticalThings.annotPlanBodyAchieveLabel(outerContent, plans_ap_bel, ap_atom);

        //Annotating plans with rp and label. e.g +!g[rp,l_3]
        // CriticalThings.annotTriggersLabel(outerContent, plans_ap_bel.getTriggerSet(), rp_atom);
        Map<Trigger, LinkedList<Term>> triggers_labels = new HashMap<Trigger, LinkedList<Term>>();
        for(Plan p: outerContent.getPL()){
            if(plans_ap_bel.getTriggerSet().contains(p.getTrigger()) && p.getTrigger().getOperator()!= Trigger.TEOperator.del){
                Term label = p.getLabel().clone();
                Trigger t = p.getTrigger().clone();
                // t.getLiteral().clearAnnots();

                triggers_labels.computeIfAbsent(t, k -> new LinkedList<Term>()).add(label);

                p.getTrigger().getLiteral().addAnnots(rp_atom, label);
            }
        }

        // Annotate del triggers with rp and last label -!g[rp, l_last]
        for(Plan p: outerContent.getPL()){
            Trigger t = p.getTrigger();
            // t.getLiteral().delAnnot(rp_atom);
            if(plans_ap_bel.getTriggerSet().contains(t) && t.getOperator()== Trigger.TEOperator.del){
                for(Trigger t2: triggers_labels.keySet()){
                    if(t.getLiteral().getPredicateIndicator().equals(t2.getLiteral().getPredicateIndicator())){
                        t.getLiteral().addAnnots(rp_atom, triggers_labels.get(t2).getLast());
                    }
                }
            }
        }

        // +!g[rp,l_x] <- !g[ap,l_x+1].
        for(Trigger t: triggers_labels.keySet()){
            if(t.getOperator()!= Trigger.TEOperator.del){
                for (int i=0; i<triggers_labels.get(t).size(); ++i) {
                    Term l = triggers_labels.get(t).get(i);
                    Trigger t_rp = t.clone();
                    t_rp.getLiteral().addAnnots(rp_atom, l);

                    if((i+1)<triggers_labels.get(t).size()){
                        Literal g_ap_next = (Literal)t.getLiteral().copy();
                        g_ap_next.addAnnots(ap_atom,  triggers_labels.get(t).get(i+1));
                        PlanBodyImpl achieve_next_ap = new PlanBodyImpl(jason.asSyntax.PlanBody.BodyType.achieve, g_ap_next);
                        CriticalThings.addNewPlan(outerContent, t_rp, null, achieve_next_ap);
                    }else{
                        CriticalThings.addNewPlan(outerContent, t_rp, null);
                    }

                }
                for(Term l: triggers_labels.get(t)) {
                }
            }
        }

        //+!g(X)[ap,l_x] <- !update(X); !g(X)[rp,l_x].
        for(Trigger t: triggers_labels.keySet()){
            if(t.getOperator()!= Trigger.TEOperator.del){
                for (Term label: triggers_labels.get(t)) {
                    Trigger t_ap = t.clone();
                    t_ap.getLiteral().addAnnots(ap_atom, label);

                    List<PlanBodyImpl> pb_list = new ArrayList<PlanBodyImpl>();

                    for(Literal b : plans_ap_bel.getApBelsLabel(label).ap_set){
                        Literal update_literal = createLiteral("update").addAnnots(ap_atom);
                        update_literal.addTerm(b.clone());

                        PlanBodyImpl bl = new PlanBodyImpl(jason.asSyntax.PlanBody.BodyType.achieve, update_literal);
                        pb_list.add(bl);
                    }

                    Literal g_rp = (Literal)t.getLiteral().clone();
                    PlanBodyImpl bl = new PlanBodyImpl(jason.asSyntax.PlanBody.BodyType.achieve, g_rp.addAnnots(rp_atom, label));
                    pb_list.add(bl);

                    CriticalThings.addNewPlan(outerContent, t_ap, null, pb_list.toArray(new PlanBodyImpl[pb_list.size()]));
                }
            }
        } 
        } //CriticalThings.addUpdatePlan(outerContent);
        return null;
    }
}
