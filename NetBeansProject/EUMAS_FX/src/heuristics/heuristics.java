/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heuristics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;
import rule_engine.RuleEngine;


/**
 *
 * @author jmoschon
 */
public class heuristics {
   
//    private final HashMap <rule_engine.RuleEngine,Integer> heuristicMap ;
//    private final ArrayList<> heuristics;
    private final h1_TotalRules h1;
    private final rule_engine.RuleEngine originalKB;
    private  ArrayList<RuleEngine> candidatesKB;
    private  Boolean indifferent;
    
    
    public heuristics(rule_engine.RuleEngine originalKB){
       this.candidatesKB= new ArrayList<>();
       this.h1 = new h1_TotalRules(originalKB);
       this.originalKB= originalKB; 
       this.indifferent= true;
        
    }
    /**
     * @return true if the agent is indifferent between two choices
     */
    private boolean isIndifferent(){
        HashSet <String> Indiff_set = new HashSet<>();
        
        for ( RuleEngine KB : candidatesKB){
            for (String str : KB.return_inferted()){
                Indiff_set.add(str);
                
            }
        }
        System.out.println("indif_set :  "+Indiff_set);
        
        for(String question :Indiff_set){
            StringTokenizer st2 = new StringTokenizer(question, "-");
            char c = question.charAt(0);

            if (c=='-'){
                String question1= st2.nextToken("-");
                if(Indiff_set.contains(question) && 
                        Indiff_set.contains(question1)){
                    return true;
                }
            }
            else{
                if(Indiff_set.contains(question) && 
                        Indiff_set.contains("-"+question)){
                    return true;
                }

            }
       }
       return false;
        
    }
    /**
     * The call method is 
     */
    public void call(){
        
        while (indifferent){
            if (candidatesKB.isEmpty()){
                candidatesKB.removeAll(candidatesKB);
            }
            
            this.candidatesKB=this.h1.core(originalKB);
            
            this.indifferent = this.isIndifferent();
            System.out.println("+++++++++++++++++++++++++indiffernet => "+ this.indifferent);
                    for (rule_engine.RuleEngine KB : candidatesKB) {
                
                KB.print_facts();
                KB.print_rules();
                KB.print_preferences();
                KB.print_inferted();
                System.out.println(KB.isConsistentGeneral());
                System.out.println("______________________________");
            
        }
        } 
        
        System.out.println(candidatesKB);

    }
    
}
