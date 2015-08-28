/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import rule_engine.RuleEngine;

/**
 *
 * @author jmoschon
 */
public class h5_DefeasibleRules {
    
    private rule_engine.RuleEngine originalKB;
    private HashMap<Integer,rule_engine.RuleEngine> candidatesKB;
    
    public h5_DefeasibleRules(rule_engine.RuleEngine originalKB){
        this.originalKB=originalKB;    
    }
    
    public h5_DefeasibleRules(HashMap<Integer,rule_engine.RuleEngine> candidatesKB){
        this.candidatesKB=candidatesKB;
    }
    
    
    /**this is the core if the h2_OwnPreferences is not the first for CMP to call
     * @param candidatesKB
     */
    
    public HashMap<Integer, RuleEngine> core(HashMap <Integer,rule_engine.RuleEngine> candidatesKB){
        
        ArrayList <Integer> rules_count= new ArrayList<>();
        ArrayList<Integer> candidateID = new ArrayList<>();
        ArrayList <Integer> toberemoved= new ArrayList<>();
        for (int i : candidatesKB.keySet()){
            if (!rules_count.isEmpty()){
                if (countInActiveRules(candidatesKB.get(i))<Collections.min(rules_count)){
                    rules_count.removeAll(rules_count);
                    candidateID.removeAll(candidateID);
                    rules_count.add(countInActiveRules(candidatesKB.get(i)));
                    candidateID.add(i);
                }
                else if (countInActiveRules(candidatesKB.get(i))==Collections.min(rules_count)){
                    rules_count.add(countInActiveRules(candidatesKB.get(i)));
                    candidateID.add(i);
                }                
            }
            else{
                rules_count.add(countInActiveRules(candidatesKB.get(i)));
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
    
    private int countInActiveRules(RuleEngine KB){
        int counter=0;
        HashMap<Integer,HashMap <HashSet<String>,HashMap<String,Boolean>>> rules;
        rules= KB.return_rules();
        for (int i : rules.keySet()){
            for (HashSet str : rules.get(i).keySet()){
                for (String str1 : rules.get(i).get(str).keySet()){    
                    if (rules.get(i).get(str).get(str1)){
                        counter++;
                    }
                }
            }
        }
    return counter;
    }    
}
