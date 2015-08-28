/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

//import conflict_resolution.conflict_resolution;
//import conflict_resolution.conflict_resolution;
import com.sun.xml.internal.ws.api.pipe.Engine;
import conflict_resolution.conflict_resolution;
import eumas_rrm.EUMAS_RRM;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import rule_engine.RuleEngine;

/**
 *
 * @author jmoschon
 */
public class Main {


    public static void main(String[] args) throws IOException {
                RuleEngine engine;
        engine = new RuleEngine();
        
        engine.readKB("/home/jmoschon/Desktop/praktikh/test");
                System.out.println("===============KB=================");
        engine.print_facts();
        engine.print_strict_facts();
        engine.print_rules();
        engine.print_strict_rules();
        engine.print_preferences();
        engine.init_reasoner();
        engine.print_inferted();

          conflict_resolution crp = new conflict_resolution(engine);
          crp.setQuestion("go_party");
          crp.CRP(engine);
          System.out.println("crp lvl : "+crp.getCompromiselvl());
          System.out.println("optimal choice: "+crp.getPreferedliteral());
    }

}
