/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import rule_engine.RuleEngine;

/**
 *
 * @author jmoschon
 */
public class h3_Contribution {
    
    private rule_engine.RuleEngine originalKB;
    private HashMap<Integer,rule_engine.RuleEngine> candidatesKB;
    
    public h3_Contribution(rule_engine.RuleEngine originalKB){
        this.originalKB=originalKB;    
    }
    
    public h3_Contribution(HashMap<Integer,rule_engine.RuleEngine> candidatesKB){
        this.candidatesKB=candidatesKB;
    }    
    public HashMap<Integer, RuleEngine> core(HashMap <Integer,rule_engine.RuleEngine> candidatesKB){
        
        ArrayList <Integer> literal_count= new ArrayList<>();
        ArrayList<Integer> candidateID = new ArrayList<>();
        ArrayList <Integer> toberemoved= new ArrayList<>();
        
        
        for (Integer i : candidatesKB.keySet() ){
            RuleEngine KB = candidatesKB.get(i);
            if (!literal_count.isEmpty()){
                if (KB.return_inferted().size()>Collections.max(literal_count)){
                    literal_count.removeAll(literal_count);
                    candidateID.removeAll(candidateID);
                    literal_count.add(KB.return_inferted().size());
                    candidateID.add(i);
                }
                else if (KB.return_inferted().size()==Collections.max(literal_count)){
                    literal_count.add(KB.return_inferted().size());
                    candidateID.add(i);
                }                
            }
            else{
                literal_count.add(KB.return_inferted().size());
                candidateID.add(i);
            }
            
        }
        for (int i : candidatesKB.keySet()){
            if (!candidateID.contains(i)){

                toberemoved.add(i);
            }        
        }
        for (int i : toberemoved){
            candidatesKB.remove(i);
        }        
    return candidatesKB;
    }
}
