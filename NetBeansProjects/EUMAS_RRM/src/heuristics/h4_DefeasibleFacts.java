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
public class h4_DefeasibleFacts {

    private rule_engine.RuleEngine originalKB;
    private HashMap<Integer,rule_engine.RuleEngine> candidatesKB;
    
    public h4_DefeasibleFacts(rule_engine.RuleEngine originalKB){
        this.originalKB=originalKB;    
    }
    
    public h4_DefeasibleFacts(HashMap<Integer,rule_engine.RuleEngine> candidatesKB){
        this.candidatesKB=candidatesKB;
    }
    
    
    /**this is the core if the h2_OwnPreferences is not the first for CMP to call
     * @param candidatesKB
     */
    
    public HashMap<Integer, RuleEngine> core(HashMap <Integer,rule_engine.RuleEngine> candidatesKB){
        
        ArrayList <Integer> facts_count= new ArrayList<>();
        ArrayList<Integer> candidateID = new ArrayList<>();
        ArrayList <Integer> toberemoved= new ArrayList<>();
        for (int i : candidatesKB.keySet()){
            if (!facts_count.isEmpty()){
                if (countInActiveFacts(candidatesKB.get(i))<Collections.min(facts_count)){
                    facts_count.removeAll(facts_count);
                    candidateID.removeAll(candidateID);
                    facts_count.add(countInActiveFacts(candidatesKB.get(i)));
                    candidateID.add(i);
                }
                else if (countInActiveFacts(candidatesKB.get(i))==Collections.min(facts_count)){
                    facts_count.add(countInActiveFacts(candidatesKB.get(i)));
                    candidateID.add(i);
                }                
            }
            else{
                facts_count.add(countInActiveFacts(candidatesKB.get(i)));
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
    
    private int countInActiveFacts(RuleEngine KB){
        int counter=0;
        HashMap <Integer,HashMap<String,Boolean>> facts;
        facts= KB.return_facts();
        for (int i : facts.keySet()){
            for (String str : facts.get(i).keySet()){
                if (!facts.get(i).get(str)){
                    counter++;
                }
            }
        }
    return counter;
    }
}
