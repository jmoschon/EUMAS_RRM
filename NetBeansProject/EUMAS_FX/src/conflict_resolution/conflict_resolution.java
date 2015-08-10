/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conflict_resolution;

import heuristics.heuristics;
import java.util.HashMap;

/**
 *
 * @author jmoschon
 */
public class conflict_resolution {
    
    private final rule_engine.RuleEngine originalKB ;
    private final heuristics heuristics;
    private String Question=null;
    private int compromiselvl;
    private String preferedLiteral;
//    private final HashMap <rule_engine.RuleEngine,heuristic_set1> d;
    
    public conflict_resolution(rule_engine.RuleEngine KB,String Question){
        this.originalKB=KB;
        this.heuristics= new heuristics(KB);
        this.Question=Question;

    }
    
    public conflict_resolution(rule_engine.RuleEngine KB){
        this.originalKB=KB;
        this.heuristics= new heuristics(KB);

    } 
    
    public int getCompromiselvl(){
        return this.compromiselvl;
    }
    public String getPreferedliteral(){
        return this.preferedLiteral;
    }
    public void setQuestion(String question){
        this.Question=question;
    }
    public String getQuestion(){
        return this.Question;
    }
    public  void CRP(rule_engine.RuleEngine KB){

        if (originalKB.isConsistentGeneral()==false){
            System.out.println("The KB is not Consistent");
            this.heuristics.call(this.Question);
        }
        this.compromiselvl=heuristics.getCompromiselvl();
        this.preferedLiteral=heuristics.getPreferedliteral();
    }
}
