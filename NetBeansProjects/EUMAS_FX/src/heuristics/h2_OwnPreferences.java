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
public class h2_OwnPreferences {
    
    private rule_engine.RuleEngine originalKB;
    private HashMap<Integer,rule_engine.RuleEngine> candidatesKB;
    
    public h2_OwnPreferences(rule_engine.RuleEngine originalKB){
        this.originalKB=originalKB;    
    }
    
    public h2_OwnPreferences(HashMap<Integer,rule_engine.RuleEngine> candidatesKB){
        this.candidatesKB=candidatesKB;
    }
    
    
    /**this is the core if the h2_OwnPreferences is not the first for CMP to call
     * @param candidatesKB
     */
    
    public HashMap<Integer, RuleEngine> core(HashMap <Integer,rule_engine.RuleEngine> candidatesKB){
        
        ArrayList <Integer> preference_count= new ArrayList<>();
        ArrayList<Integer> candidateID = new ArrayList<>();
        ArrayList <Integer> toberemoved= new ArrayList<>();
        for (int i : candidatesKB.keySet()){
            if (!preference_count.isEmpty()){
                if (countActiveOwnPreferences(candidatesKB.get(i))<Collections.min(preference_count)){
                    preference_count.removeAll(preference_count);
                    candidateID.removeAll(candidateID);
                    preference_count.add(countActiveOwnPreferences(candidatesKB.get(i)));
                    candidateID.add(i);
                }
                else if (countActiveOwnPreferences(candidatesKB.get(i))==Collections.min(preference_count)){
                    preference_count.add(countActiveOwnPreferences(candidatesKB.get(i)));
                    candidateID.add(i);
                }                
            }
            else{
                preference_count.add(countActiveOwnPreferences(candidatesKB.get(i)));
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
            

    private int countActiveOwnPreferences(RuleEngine KB){
        int counter=0;
        System.out.println("own preferences : "+KB.getOwnPreferenceIDs());
        for (Integer ID: KB.getOwnPreferenceIDs()){
            HashMap <String , HashMap<String,Boolean>> pref= KB.getPreference(ID);
            for (String str : pref.keySet()){
                for (String str1 : pref.get(str).keySet()){
                    if (pref.get(str).get(str1)){
                        counter++;

                    }
                }
            }
        }
    return counter;
    }
}
