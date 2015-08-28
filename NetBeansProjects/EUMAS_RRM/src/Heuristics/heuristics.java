/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import conflict_resolution.Candidates;
import java.util.HashSet;
import rule_engine.RuleEngine;

/**
 *
 * @author jmoschon
 */
public class heuristics {
    
    
    
   private  final HashSet<Candidates> candidatesList; 
   private final RuleEngine originalKB ;
   
   
    public heuristics(RuleEngine orignalKB){
    
       this.candidatesList= new HashSet<>();
       this.originalKB = orignalKB;
       
    }
    
    
    
    public void call(String Question){
    
    
    }
    
}
