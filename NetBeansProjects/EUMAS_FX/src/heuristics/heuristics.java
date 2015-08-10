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
    private  h2_OwnPreferences h2;
    private h3_Contribution h3;
    private h4_DefeasibleFacts h4;
    private h5_DefeasibleRules h5;
    private h6_OthersPreferences h6;
    private final rule_engine.RuleEngine originalKB;
    private  HashMap<Integer,RuleEngine> candidatesKB;
    private  Boolean indifferent;
//    private String preferedChoise="none";
    private int compromiselvl=0;
    private String prefered_choise="indiff";
    
    
    public heuristics(rule_engine.RuleEngine originalKB){
       this.candidatesKB= new HashMap<>();
       this.h1 = new h1_TotalRules(originalKB);
       this.originalKB= originalKB; 
       this.indifferent= true;
        
    }
    /**
     * @return true if the agent is indifferent between two choices
     */
    private boolean isIndifferentGeneral(){
        HashSet <String> Indiff_set = new HashSet<>();
        for(Integer i : candidatesKB.keySet()){
            RuleEngine KB=candidatesKB.get(i);
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
    private boolean isIndifferent_old(String question){
            HashSet <String> Indiff_set = new HashSet<>();
            
        for(Integer i : candidatesKB.keySet()){
            RuleEngine KB=candidatesKB.get(i);
                for (String str : KB.return_inferted()){
                    Indiff_set.add(str);
                
             }
            
        }
        System.out.println("indif_set :  "+Indiff_set);
        
        
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
       
       return false;
      
    }
    
    
    private boolean isIndifferent (String question){
    
        String question1;
        StringTokenizer st2 = new StringTokenizer (question, "-");
        int question_marker = 0;
        int question1_marker = 0;
        char c = question.charAt(0);
        
        if (c=='-'){
            question1=st2.nextToken("-");
        
        }

        else{
            question1="-"+question;
        }
        
        for (Integer i : candidatesKB.keySet()){
            RuleEngine KB=candidatesKB.get(i);
            for (String str: KB.return_inferted()){
                if (str.equals(question)){
                    question_marker++;
                }
                else if (str.equals(question1)){
                    question1_marker++;
                }
            }
        }
        
//        if ((question_marker==0) && (question1_marker==0)){
//            prefered_choise = "none";
//            return false;
//        }
        if (question_marker==question1_marker){
            return true;
        }
        else if (question_marker>question1_marker){
            this.prefered_choise= question;
            return false;
        }
        else{
//            System.out.println(");
            this.prefered_choise = question1; 
            return false;
        }
    
    }
    

    private String getPreferedChoise(String question){
        HashSet <String> Indiff_set = new HashSet<>();
        
        for(Integer i : candidatesKB.keySet()){
            RuleEngine KB=candidatesKB.get(i);
                for (String str : KB.return_inferted()){
                    Indiff_set.add(str);
                
             }
            
        }
        StringTokenizer st2 = new StringTokenizer(question, "-");
        char c = question.charAt(0);
        if (c=='-'){
            String question1= st2.nextToken("-");
            if (Indiff_set.contains(question)){
                return question;
            }
            else if (Indiff_set.contains(question1)){
                return question1;
            }
        }
        else{
            String question1="-"+question;
            if (Indiff_set.contains(question)){
                return question;
            }
            else if (Indiff_set.contains(question1)){
                return question1;
            }
        
        }
        return "not";
    
    }
    
    public int getCompromiselvl(){
        return this.compromiselvl;
    }
    public String getPreferedliteral(){
        return this.prefered_choise;
    }
    /**
     * The call method is 
     */
    public void call(String question){
        
        while (indifferent){
            if (!candidatesKB.isEmpty()){

                    candidatesKB.clear();

            }
            h1.increaseCompromiselvl();
            this.candidatesKB=this.h1.core(originalKB);
                        System.out.println("--------h1---------");
            System.out.println(candidatesKB);
            this.h2 = new h2_OwnPreferences(this.candidatesKB);

            this.candidatesKB= this.h2.core(candidatesKB);
                        System.out.println("--------h2---------");
            System.out.println(candidatesKB);
            this.h3 = new h3_Contribution(candidatesKB);
            this.candidatesKB= this.h3.core(candidatesKB);
                        System.out.println("--------h3---------");
            System.out.println(candidatesKB);
            this.h4= new h4_DefeasibleFacts(candidatesKB);
            this.candidatesKB = this.h4.core(candidatesKB);
                        System.out.println("--------h4---------");
            System.out.println(candidatesKB);
            this.h5 = new h5_DefeasibleRules(candidatesKB);
            this.candidatesKB= this.h5.core(candidatesKB);
                        System.out.println("--------h5---------");
            System.out.println(candidatesKB);
            this.h6 = new h6_OthersPreferences(candidatesKB);
            this.candidatesKB = this.h6.core(candidatesKB);
//            this.candidatesKB= this
            
            this.indifferent = this.isIndifferent(question);
            System.out.println(question);
            System.out.println("+++++++++++++++++++++++++indiffernet => "+ this.indifferent);
            System.out.println(candidatesKB);
            for(int i : candidatesKB.keySet()){
            rule_engine.RuleEngine KB = candidatesKB.get(i);
                
                KB.print_facts();
                KB.print_rules();
                KB.print_preferences();
                KB.print_inferted();
                System.out.println(KB.isConsistentGeneral());
                System.out.println("______________________________");
            
        }
        } 
        //this.preferedChoise=this.getPreferedChoise(question);
        this.compromiselvl=h1.getCompromiselvl();
        System.out.println(candidatesKB);

    }
    
}
