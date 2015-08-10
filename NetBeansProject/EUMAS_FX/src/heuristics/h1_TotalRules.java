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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import rule_engine.RuleEngine;

/**
 *
 * @author jmoschon
 */
public class h1_TotalRules {
    
    private rule_engine.RuleEngine originalKB;
    private HashMap <Integer,rule_engine.RuleEngine> candidatesKB;
    private int compromise_lvl ; 
    
    public h1_TotalRules(rule_engine.RuleEngine originalKB){
        this.originalKB=originalKB;
        this.compromise_lvl=-1;
        
    } 
    
    public h1_TotalRules (HashMap<Integer,rule_engine.RuleEngine> candidatesKB){
        this.candidatesKB=candidatesKB;
    
    }
    public int getCompromiselvl(){
         return this.compromise_lvl;
    }
    
    /**this is the core if the h1_TotalRules is the first for CMP to call
    * @param originalKB
     * @return core
    */
    public HashMap<Integer,RuleEngine> core(rule_engine.RuleEngine originalKB){
//        System.out.println("peos");
       
        HashMap <Integer,rule_engine.RuleEngine> candidatesKB = new HashMap<>();
        int numbeOfRules = originalKB.returnIDset().size();
        int key=0;
        for (int i=this.compromise_lvl; i<numbeOfRules; i++){
            System.out.println("=========>>>"+compromise_lvl);
            
            List<Integer>superset = originalKB.returnIDset();
            List<Set<Integer>> subset = getSubsets(superset, i);
            
            for (Set<Integer> subset1 : subset) {
                rule_engine.RuleEngine KB = new RuleEngine(originalKB);
                Iterator itr = subset1.iterator();
                while (itr.hasNext()){
                    int ID =(int) itr.next();       
                    KB.setInActive(ID);
                }
                KB.init_reasoner();

                if (KB.isConsistentGeneral()){
                    KB.CheckPreferences();
                                    System.out.print("h1 :");
                        KB.print_inferted();
                    candidatesKB.put(key++, KB);
                }
            }
            if (!candidatesKB.isEmpty()){
               core(candidatesKB);
               if (RulesNumber(candidatesKB)==i)
                   this.compromise_lvl=i+1;
                return candidatesKB; 
            }
        }
        core(candidatesKB);
        this.compromise_lvl=numbeOfRules;
        return candidatesKB;
        
    }
        
    
    
    /**this is the core if the h1_TotaleRules is not the first for CMP to call
     * @param candidatesKB
     */
    public void core(HashMap <Integer,rule_engine.RuleEngine> candidatesKB){
        
        ArrayList<Integer> rules_counter= new ArrayList<>();
        ArrayList<Integer> cantidadeID = new ArrayList<>();
        ArrayList<Integer> toberemoved = new ArrayList<>();
        //System.out.println("candit size before: "+ candidatesKB.size());
        for (int i :candidatesKB.keySet()) {
           if(!rules_counter.isEmpty()){
               if (countActiveRules(candidatesKB.get(i))<Collections.min(rules_counter)){
                   rules_counter.removeAll(rules_counter);
                   cantidadeID.removeAll(cantidadeID);
                   rules_counter.add(countActiveRules(candidatesKB.get(i)));
                   cantidadeID.add(i);
               }
               else if (countActiveRules(candidatesKB.get(i))==Collections.min(rules_counter)){
                   rules_counter.add(countActiveRules(candidatesKB.get(i)));
                   cantidadeID.add(i);
               }
           }
           else{
               rules_counter.add(countActiveRules(candidatesKB.get(i)));
           }
           
        }

        for (int i : candidatesKB.keySet()){
            if (!cantidadeID.contains(i)){
                toberemoved.add(i);
            }
        
        }
        for (int i : toberemoved){
            candidatesKB.remove(i);
        }
    }
    
    public void increaseCompromiselvl(){
        this.compromise_lvl++;
    }
    private Integer RulesNumber(HashMap <Integer,rule_engine.RuleEngine> candidatesKB){
        ArrayList<Integer> rules_counter= new ArrayList<>();
        for (int i:candidatesKB.keySet()) {
           rules_counter.add(countActiveRules(candidatesKB.get(i)));
        }
        if (rules_counter.isEmpty()){
        return 0;
        }
        
        return Collections.min(rules_counter);
    }
    private int countActiveRules(rule_engine.RuleEngine KB){
        
        HashMap <Integer,HashMap<HashSet<String>,HashMap<String,Boolean>>>rules;
        HashMap <Integer,HashMap<String,HashMap<String,Boolean>>>preferences;
        HashMap <Integer,HashMap<String,Boolean>> facts;
        
        int NumberOfInActiveRules=0;
        
        rules= KB.return_rules();
        preferences= KB.return_preference();
        facts= KB.return_facts();
        
        //count active rules 
        for (int i: rules.keySet()){
            for (HashSet str :rules.get(i).keySet()){
                for (String str1 : rules.get(i).get(str).keySet()){
                    if(!rules.get(i).get(str).get(str1)){
                        NumberOfInActiveRules++;
                    }
                }
            }
        }
        for (int i: preferences.keySet()){
            for (String str :preferences.get(i).keySet()){
                for (String str1 : preferences.get(i).get(str).keySet()){
                    if(!preferences.get(i).get(str).get(str1)){
                        NumberOfInActiveRules++;
                    }
                }
            }
        }        
        for (int i : facts.keySet()){
            for (String str : facts.get(i).keySet()){
                if (!facts.get(i).get(str)){
                    NumberOfInActiveRules++;
                }
            }
        }
        return NumberOfInActiveRules;
    }
    
    private static void getSubsets(List<Integer> superSet, int k, int idx, Set<Integer> current,List<Set<Integer>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unseccessful stop clause
        if (idx == superSet.size()) return;
        Integer x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx+1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx+1, current, solution);
    }

    private static List<Set<Integer>> getSubsets(List<Integer> superSet, int k) {
        List<Set<Integer>> res = new ArrayList<>();
        getSubsets(superSet, k, 0, new HashSet<Integer>(), res);
        return res;
    }
 
}
