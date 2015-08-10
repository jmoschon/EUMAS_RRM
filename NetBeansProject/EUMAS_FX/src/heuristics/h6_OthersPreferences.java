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
public class h6_OthersPreferences {
    private rule_engine.RuleEngine originalKB;
    private HashMap<Integer,rule_engine.RuleEngine> candidatesKB;
    
    public h6_OthersPreferences(rule_engine.RuleEngine originalKB){
        this.originalKB=originalKB;    
    }
    
    public h6_OthersPreferences(HashMap<Integer,rule_engine.RuleEngine> candidatesKB){
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
                if (countActiveOthersPreferences(candidatesKB.get(i))<Collections.min(preference_count)){
                    preference_count.removeAll(preference_count);
                    candidateID.removeAll(candidateID);
                    preference_count.add(countActiveOthersPreferences(candidatesKB.get(i)));
                    candidateID.add(i);
                }
                else if (countActiveOthersPreferences(candidatesKB.get(i))==Collections.min(preference_count)){
                    preference_count.add(countActiveOthersPreferences(candidatesKB.get(i)));
                    candidateID.add(i);
                }                
            }
            else{
                preference_count.add(countActiveOthersPreferences(candidatesKB.get(i)));
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
            

    
    private int countActiveOthersPreferences(RuleEngine KB){
        int counter=0;
        for (Integer ID : KB.return_preference().keySet()){
            if (!KB.getOwnPreferenceIDs().contains(ID)){
                HashMap <String , HashMap<String,Boolean>> pref= KB.getPreference(ID);
                for (String str : pref.keySet()){
                    for (String str1 : pref.get(str).keySet()){
                        if (pref.get(str).get(str1)){
                            counter++;
                        }
                    }
                }
            }
        }
        return counter;
    }
    
//    private int countActiveOwnPreferences(RuleEngine KB){
//        int counter=0;
//        System.out.println("own preferences : "+KB.getOwnPreferenceIDs());
//        for (Integer ID: KB.getOwnPreferenceIDs()){
//            HashMap <String , HashMap<String,Boolean>> pref= KB.getPreference(ID);
//            for (String str : pref.keySet()){
//                for (String str1 : pref.get(str).keySet()){
//                    if (pref.get(str).get(str1)){
//                        counter++;
//
//                    }
//                }
//            }
//        }
//    return counter;
//    }   
}
