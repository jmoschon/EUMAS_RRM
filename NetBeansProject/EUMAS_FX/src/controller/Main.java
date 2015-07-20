/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

//import conflict_resolution.conflict_resolution;
import conflict_resolution.conflict_resolution;
import java.io.IOException;
import rule_engine.RuleEngine;

/**
 *
 * @author jmoschon
 */
public class Main {
    
        
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic her
        
        
        RuleEngine engine;
        engine = new RuleEngine();
        
        engine.readKB("/home/jmoschon/Desktop/praktikh/test");
//        engine.add_rules("a => b");
//        engine.add_rules("a => k");
//        engine.add_rules("b => c");
//        engine.add_rules("d => -c");
//        engine.add_rules("-d => c");
//        engine.add_rules("=> a");
//        engine.add_rules("=> e");
//        engine.add_rules("=> b");
//        engine.add_rules("e => -d");
//        engine.add_rules("b => d");
//        engine.add_rules("d > b");
//        engine.add_rules("a > -d");
////        
        
        System.out.println("===============KB=================");
        engine.print_facts();
        engine.print_rules();
        engine.print_preferences();
        engine.init_reasoner();
        engine.print_inferted();
        
        //ask if the KB is consistent
        System.out.print("consistency :  ");
        System.out.println(engine.isConsistent("go_party"));
        System.out.print("genarl consistency :  ");
        System.out.println(engine.isConsistentGeneral());
        System.out.println("=======================================");
        engine.returnIDset();
        
         //if the KB is inconsistent sent the KB to to the CR model
         conflict_resolution CR= new conflict_resolution(engine);
//         CR.CRP(engine);
//         engine.print_inferted();
            
        
    }
    
}
