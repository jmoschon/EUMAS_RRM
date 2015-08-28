/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eumas_rrm;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
//import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 *
 * @author jmoschon
 */
public class EUMAS_RRM {
    /*MUST://  defeasible_chance + strict_chance + preference_chance = 100*/

    private final int defeasible_chance = 70;
    private final int strict_chance = 10;
    private final int preference_chance = 20;

    private final int negated_chance = 50;

    private final int Y = 25;

    /*MUST :// chance_agentA + chance_agentB + chance_bothAgent = 100*/
    private final int chance_agentA = 30;
    private final int chance_agentB = 30;
    private final int chance_bothAgent = 40;

    private final int N = 50;                        // arithmos twn kanonwn
    private final int M = (int) (this.N * 0.4) + 1;      // aritmow twn literals poy tha exoun oi kanones

    private final int exange_rules = N / 10;  // poso % twn rules na stelnondai ston allon agent

    private final ArrayList<String> M_set;
    private final HashSet<String> rr;

    public EUMAS_RRM() {
//    
//        this.N=100;
//        this.M=4* (this.N/10);

//        this.M=(int) (this.N*0.4)+1;
//        System.out.println(this.M);
//        this.M=2;
        this.M_set = new ArrayList<>();
        this.rr = new HashSet<>();

        makeLiterals();
    }

    private void makeLiterals() {

        for (Integer i = 0; i < this.M; i++) {
            this.M_set.add("RR_" + i.toString());

        }

    }

    private String chooseRuleType() {

        Random generator = new Random();
        int p = generator.nextInt(100);
//        System.out.println("choose rule type");
        if (p < this.strict_chance) {
            return "strict";
        } else if (p >= this.strict_chance && p < this.preference_chance + this.strict_chance) {
            return "preference";
        } //        else if (p>=this.defeasible_chance && p<this.M){
        else {
            return "defeasible";
        }
    }

    /**
     * this method decides if literal is negated or not
     *
     * @param l literal
     * @return l or -l
     */
    private String finalLiteral(String l) {
//        System.out.println("choose final literal");
        Random generator = new Random();
        int p = generator.nextInt(100);

        if (p < this.negated_chance) {
            return l; // l
        } else {
            return ("-" + l); // -l
        }

    }

    //chance for every literal is P=1/M
    private String chooseHead() {

        Random generator = new Random();
        int p = generator.nextInt(this.M);

        return finalLiteral(this.M_set.get(p));
    }

    private String chooseBodyLiteral(String head) {

        Random generator = new Random();
        int counter = 0;
        int p = generator.nextInt(this.M);
        if (head.charAt(0) == '-') {
            head = head.substring(1);
        }
        while (this.M_set.get(p).equals(head) && counter < 2 * this.M) {
            counter++;
//            System.out.println("124");
            p = generator.nextInt(this.M);
        }
        return this.M_set.get(p);

    }

    private String choosePreferenceBodyLiteral(String head) {

        Random generator = new Random();

        int p = generator.nextInt(this.M);
        if (head.charAt(0) == '-') {
            head = head.substring(1);
        }
        while (this.M_set.get(p).equals(head)) {
//            System.out.println("139");
            p = generator.nextInt(this.M);
        }
        return this.finalLiteral(this.M_set.get(p));

    }

    private String makeBody(String head) {

        String body = null;
        String literal;
        String finalLiteral;
        int counter = 0;
        int p;
        HashSet<String> bodySet = new HashSet<>();

        Random generator = new Random();
        p = generator.nextInt(100);

        if (p > this.Y) {
            literal = this.chooseBodyLiteral(head);
            while (bodySet.contains(literal)) {
//            System.out.println("160");
                literal = this.chooseBodyLiteral(head);
            }

            body = this.finalLiteral(literal);
            bodySet.add(literal);

            while (p < this.Y && counter < this.M * 2) {
//            System.out.println("169");
                counter++;
                literal = this.chooseBodyLiteral(head);
                while (bodySet.contains(literal)) {
//                System.out.println("172");
                    literal = this.chooseBodyLiteral(head);
                }
                finalLiteral = this.finalLiteral(literal);
                bodySet.add(literal);
                body = body + "," + finalLiteral;
                p = generator.nextInt(100);
            }
        }
        return body;
    }
    private String makeBody1(String head) {

        String body = null;
        String literal;
        String finalLiteral;
        int counter = 0;
        int p;
        HashSet<String> bodySet = new HashSet<>();

        Random generator = new Random();
        p = generator.nextInt(100);

//        if (p > this.Y) {
            literal = this.chooseBodyLiteral(head);
            while (bodySet.contains(literal)) {
//            System.out.println("160");
                literal = this.chooseBodyLiteral(head);
            }

            body = this.finalLiteral(literal);
            bodySet.add(literal);

            while (p < this.Y && counter < this.M * 2) {
//            System.out.println("169");
                counter++;
                literal = this.chooseBodyLiteral(head);
                while (bodySet.contains(literal)) {
//                System.out.println("172");
                    literal = this.chooseBodyLiteral(head);
                }
                finalLiteral = this.finalLiteral(literal);
                bodySet.add(literal);
                body = body + "," + finalLiteral;
                p = generator.nextInt(100);
            }
//        }
        return body;
    }
    private String chooseDefeasibleBody(String head) {

        String body;
        String literal;
        String finalLiteral;
        int counter = 0;
        int p;
        HashSet<String> bodySet = new HashSet<>();

        Random generator = new Random();
        p = generator.nextInt(100);

        literal = this.chooseBodyLiteral(head);
        while (bodySet.contains(literal)) {
//            System.out.println("160");
            literal = this.chooseBodyLiteral(head);
        }

        body = this.finalLiteral(literal);
        bodySet.add(literal);

        while (p < this.Y && counter < this.M * 2) {
//            System.out.println("169");
            counter++;
            literal = this.chooseBodyLiteral(head);
            while (bodySet.contains(literal)) {
//                System.out.println("172");
                literal = this.chooseBodyLiteral(head);
            }
            finalLiteral = this.finalLiteral(literal);
            bodySet.add(literal);
            body = body + "," + finalLiteral;
            p = generator.nextInt(100);
        }

        return body;
    }

    public HashSet<String> core() {

        System.out.println(this.M_set);
        HashSet<String> rr = new HashSet<>();
        String head;
        String body;

        for (int i = 0; i < this.N; i++) {
            String ruleType = this.chooseRuleType();

            switch (ruleType) {
                case "strict":
                    head = this.chooseHead();
                    body = this.makeBody(head);
                    if (body == null) {
                        while (this.rr.contains("-> " + head)) {
//                        System.out.println("198");
                            head = this.chooseHead();
                        }
                        this.rr.add("-> " + head);
                    } 
                    else { 
                        while (this.rr.contains(body+" -> "+head)){
                            body=this.makeBody1(head);
                        
                        }
                        this.rr.add(body+" -> "+head);

                    }
                    break;
                case "preference":
                    head = this.chooseHead();
                    body = finalLiteral(this.chooseBodyLiteral(head));
                    while (this.rr.contains(body + " > " + head)) {
//                        System.out.println("206");
                        head = this.chooseHead();
                        body = finalLiteral(this.chooseBodyLiteral(head));
                    }
                    this.rr.add(body + " > " + head);
                    break;
                case "defeasible":
                    head = this.chooseHead();
                    body = this.makeBody(head);
                    if (body == null) {
                        while (this.rr.contains("=> " + head)) {
//                        System.out.println("198");
                            head = this.chooseHead();
                        }
                        this.rr.add("-> " + head);
                    } 
                    else { 
                        while (this.rr.contains(body+" => "+head)){
                            body=this.makeBody1(head);
                        
                        }
                        this.rr.add(body+" -> "+head);

                    }
                break;
                default:
                    System.err.println("error at rule type");
                    break;

            }

        }

        System.out.println(this.rr);
        System.out.println(this.rr.size());
//        try {
//            makeAgentsKB();
////        return rr;
//        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
//            Logger.getLogger(EUMAS_RRM.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return rr;
    }

    public void makeAgentsKB() throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter agentA = new PrintWriter("AgentA", "UTF-8");
        PrintWriter agentB = new PrintWriter("AgentB", "UTF-8");
        int p;
        Random generator = new Random();

        for (String str : this.rr) {
            p = generator.nextInt(100);

            if (p < this.chance_agentA) {
                agentA.println(str);
            } else if (p >= this.chance_agentA && p < this.chance_agentB + this.chance_agentA) {
                agentB.println(str);

            } else {
                agentB.println(str);
                agentA.println(str);
            }
        }
        agentA.close();
        agentB.close();

    }

    public void makeAgentsKB_1(EUMAS_RRM other, int run) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter agentA = new PrintWriter("AgentA_" + run, "UTF-8");
        PrintWriter agentB = new PrintWriter("AgentB_" + run, "UTF-8");

        int p;
        Random generator = new Random();
        HashSet<Integer> ex_rulesA = new HashSet<>();
        HashSet<String> rulesToExA = new HashSet<>();
        for (int i = 0; i < this.exange_rules; i++) {
            p = generator.nextInt(this.N);
            while (ex_rulesA.contains(p)) {
                p = generator.nextInt(this.N);
                ex_rulesA.add(p);
            }
            int counter = 0;
            for (String str : this.rr) {
                if (p == counter) {
                    rulesToExA.add(str);
                }
                counter++;

            }
        }

        HashSet<Integer> ex_rulesB = new HashSet<>();
        HashSet<String> rulesToExB = new HashSet<>();
        for (int i = 0; i < other.exange_rules; i++) {
            p = generator.nextInt(other.N);
            while (ex_rulesB.contains(p)) {
                p = generator.nextInt(other.N);
                ex_rulesB.add(p);
            }
            int counter = 0;
            for (String str : other.rr) {
                if (p == counter) {
                    rulesToExB.add(str);
                }
                counter++;

            }
        }

        for (String str : rulesToExA) {
            other.rr.add(str);
        }
        for (String str : rulesToExB) {
            this.rr.add(str);
        }

        for (String str : this.rr) {
            agentA.println(str);
        }
        for (String str : other.rr) {
            agentB.println(str);
        }
        agentA.close();
        agentB.close();
    }

    public void makeSingleAgent(int run) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter agentA = new PrintWriter("AgentA_" + run, "UTF-8");

        for (String str : this.rr) {
            agentA.println(str);
        }

        agentA.close();

    }

    public ArrayList<String> getLiterals() {
        return this.M_set;
    }

    public int getM() {
        return this.M;
    }
    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
//        // TODO code application logic here
//        EUMAS_RRM rrmA = new EUMAS_RRM();
//        rrmA.core();
//        EUMAS_RRM rrmB = new EUMAS_RRM();
//        rrmB.core();
//        
//        rrmA.makeAgentsKB_1(rrmB);
//    }

}
