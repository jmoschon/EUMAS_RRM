/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import eumas_rrm.EUMAS_RRM;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import rule_engine.RuleEngine;

/**
 *
 * @author jmoschon
 */
public class Main1 {

    private static HashMap<String, Integer> countHeadLiterals(rule_engine.RuleEngine engine, eumas_rrm.EUMAS_RRM rrm) {

        HashMap<Integer, HashMap<String, Boolean>> facts = engine.return_facts();
        HashMap<Integer, HashMap<String, HashMap<String, Boolean>>> preference = engine.return_preference();
        HashMap<Integer, HashMap<HashSet<String>, HashMap<String, Boolean>>> rules = engine.return_rules();

        HashMap<String, Integer> counter = new HashMap<>();
        ArrayList<String> literals = rrm.getLiterals();
//        System.out.println("literals :  "+literals);

        System.out.println();

        for (String literal : literals) {

            int negatedLiteralCounter = 0;
            int LiteralCounter = 0;

            for (Integer i : facts.keySet()) {
                for (String str : facts.get(i).keySet()) {
                    if (str.equals(literal)) {
                        LiteralCounter++;
                    }
                    if (str.equals("-" + literal)) {
                        negatedLiteralCounter++;
                    }
                }
            }

            for (Integer i : rules.keySet()) {
                for (HashSet str : rules.get(i).keySet()) {
                    for (String str1 : rules.get(i).get(str).keySet()) {
                        if (str1.equals(literal)) {
                            LiteralCounter++;
                        }
                        if (str1.equals("-" + literal)) {
                            negatedLiteralCounter++;
                        }
                    }
                }
            }

            for (Integer i : preference.keySet()) {
                for (String str : preference.get(i).keySet()) {
                    for (String str1 : preference.get(i).get(str).keySet()) {
                        if (str1.equals(literal)) {
                            LiteralCounter++;
                        }
                        if (str1.equals("-" + literal)) {
                            negatedLiteralCounter++;
                        }
                    }
                }
            }

            counter.put(literal, LiteralCounter);
            counter.put("-" + literal, negatedLiteralCounter);

        }
//        System.out.println(counter);
        return counter;
    }

    private static HashMap<String, Integer> countBodyLiterals(rule_engine.RuleEngine engine, eumas_rrm.EUMAS_RRM rrm) {

        HashMap<Integer, HashMap<String, Boolean>> facts = engine.return_facts();
        HashMap<Integer, HashMap<String, HashMap<String, Boolean>>> preference = engine.return_preference();
        HashMap<Integer, HashMap<HashSet<String>, HashMap<String, Boolean>>> rules = engine.return_rules();

        HashMap<String, Integer> counter = new HashMap<>();
        ArrayList<String> literals = rrm.getLiterals();
//        System.out.println("literals :  "+literals);

        System.out.println();

        for (String literal : literals) {

            int negatedLiteralCounter = 0;
            int LiteralCounter = 0;

            for (Integer i : rules.keySet()) {
                for (HashSet str : rules.get(i).keySet()) {

                    if (str.contains(literal)) {
                        LiteralCounter++;
                    }
                    if (str.contains("-" + literal)) {
                        negatedLiteralCounter++;
                    }

                }
            }

            for (Integer i : preference.keySet()) {
                for (String str : preference.get(i).keySet()) {
                    if (str.equals(literal)) {
                        LiteralCounter++;
                    }
                    if (str.equals("-" + literal)) {
                        negatedLiteralCounter++;
                    }

                }
            }

            counter.put(literal, LiteralCounter);
            counter.put("-" + literal, negatedLiteralCounter);

        }
        System.out.println(counter);
        return counter;
    }

    private static HashMap<String, Integer> generateCsvFile_HEAD(HashMap<String, Integer> totalMap,
            Integer run, FileWriter writer, EUMAS_RRM rrm, HashMap<String, Integer> HeadCounter) {

//        HashMap<String,Integer> totalMap= new HashMap<>();
        try {
//	    FileWriter writer = new FileWriter(sFileName);

            writer.append(run.toString());
            writer.append(",");
            writer.append("Head");
//            writer.append(",");

            for (int i = 0; i < rrm.getM(); i++) {
                String key = "RR_" + i;
                String negatedKey = "-RR_" + i;
                writer.append(",");
                writer.append(HeadCounter.get(key).toString());
                writer.append(",");
                writer.append(HeadCounter.get(negatedKey).toString());
                int newtotal = totalMap.get(key) + HeadCounter.get(key);
                totalMap.put(key, newtotal);

                newtotal = totalMap.get(negatedKey) + HeadCounter.get(negatedKey);
                totalMap.put(negatedKey, newtotal);

            }

            writer.append("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return totalMap;
    }

    private static void generateCsvFile_Total_Avg(int totalRuns,
            FileWriter writer, int M, HashMap<String, Integer> TotalMap) {

//        HashMap<String,Integer> totalMap= new HashMap<>();
        try {
//	    FileWriter writer = new FileWriter(sFileName);
            writer.append("\n");
            writer.append("Total");
            writer.append(",");
            writer.append(" ");
//            writer.append("\n");

//            writer.append(run.toString());
//            writer.append(",");
//            writer.append("Head");
//            writer.append(",");
            for (int i = 0; i < M; i++) {
                String key = "RR_" + i;
                String negatedKey = "-RR_" + i;
                writer.append(",");
                writer.append(TotalMap.get(key).toString());
                writer.append(",");
                writer.append(TotalMap.get(negatedKey).toString());

            }

            writer.append("\n");
            writer.append("Avg.");
            writer.append(",");
            writer.append(" ");

            for (int i = 0; i < M; i++) {
                String key = "RR_" + i;
                String negatedKey = "-RR_" + i;
                writer.append(",");
                Float avg = (float) TotalMap.get(key) / totalRuns;
                writer.append(avg.toString());
                writer.append(",");
                avg = (float) TotalMap.get(negatedKey) / totalRuns;
                writer.append(avg.toString());

            }
            writer.append("\n\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static HashMap<String, Integer> generateCsvFile_BODY(HashMap<String, Integer> totalMap,
            Integer run, FileWriter writer, EUMAS_RRM rrm, HashMap<String, Integer> BodyCounter) {
        try {

////            writer.append("\n");
            writer.append(run.toString());
            writer.append(",");
            writer.append("Body");
//            writer.append(",");

            for (int i = 0; i < rrm.getM(); i++) {
                String key = "RR_" + i;
                String negatedKey = "-RR_" + i;
                writer.append(",");
                writer.append(BodyCounter.get(key).toString());
                writer.append(",");
                writer.append(BodyCounter.get(negatedKey).toString());
                int newtotal = totalMap.get(key) + BodyCounter.get(key);
                totalMap.put(key, newtotal);

                newtotal = totalMap.get(negatedKey) + BodyCounter.get(negatedKey);
                totalMap.put(negatedKey, newtotal);

            }
            writer.append("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalMap;
    }

    public static HashMap<String, Integer> init_totalMap(EUMAS_RRM rrm) {
        HashMap<String, Integer> totalMap = new HashMap<>();
        for (int i = 0; i < rrm.getM(); i++) {
            String key = "RR_" + i;
            String negatedKey = "-RR_" + i;
            totalMap.put(key, 0);
            totalMap.put(negatedKey, 0);

        }
        return totalMap;

    }

    public static void writeHeaders(FileWriter writer, EUMAS_RRM rrm) throws IOException {

        writer.append("Run No.");
        writer.append(",");
        writer.append(" ");

        for (int i = 0; i < rrm.getM(); i++) {
            String key = "RR_" + i;
            String negatedKey = "-RR_" + i;
            writer.append(",");
            writer.append(key);
            writer.append(",");
            writer.append(negatedKey);
        }

        writer.append("\n");
    }

    public static void printInferted(FileWriter writer, RuleEngine engine, EUMAS_RRM rrm) throws IOException {

//    writer.append(",");
        Integer conflict_counter = 0;
        HashSet<String> inferted = engine.return_inferted();

        ArrayList<String> literals = rrm.getLiterals();
        for (String str : literals) {
            if (inferted.contains(str) && inferted.contains("-" + str)) {
                conflict_counter++;
            }

        }
        writer.append("Conflicts No");
        writer.append(",");
        writer.append(conflict_counter.toString());
        writer.append(",");
        writer.append("Inferted,");
        for (String str : inferted) {
            writer.append(str);
            writer.append(",");

        }
        writer.append("\n");
    }

    public static String getQuestion(RuleEngine engine) {
        Random generator = new Random();
        int nextInt = generator.nextInt(engine.return_inferted().size());
        int counter = 0;
        for (String str : engine.return_inferted()) {
            if (counter == nextInt) {
                return str;
            }
            counter++;
        }
        System.out.println("nothing happens");
        return null;

    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic her
        int totalRuns = 10;
        int M = 0;
        HashMap<String, Integer> totalMap_HEAD = null;
        HashMap<String, Integer> totalMap_BODY = null;

        FileWriter writer = new FileWriter("AgentA_Head_N10.csv");
        FileWriter writer1 = new FileWriter("AgentA_Body_N10.csv");
        FileWriter writer2 = new FileWriter("crp.csv");
        FileWriter infertedA = new FileWriter("InfertedA.csv");

//        F
        writer.append("Agent A\n");
        writer1.append("Agent A\n");

        for (int i = 0; i < 10; i++) {
            System.err.println("Run"+i);
            HashMap<String, Integer> BodyCounter;
            HashMap<String, Integer> HeadCounter;
            HashMap<String, Integer> BodyCounter1;
            HashMap<String, Integer> HeadCounter1;

            String optimalChoice;
            String question;
            Integer compromiselvl;

            EUMAS_RRM rrmA = new EUMAS_RRM();
            rrmA.core();

            rrmA.makeSingleAgent(i);

            RuleEngine engineA;
            engineA = new RuleEngine();
            engineA.readKB("AgentA_" + i);
            engineA.init_reasoner();

            if (engineA.return_inferted().size() > 0) {
                conflict_resolution.conflict_resolution crp = new conflict_resolution.conflict_resolution(engineA);

                question = getQuestion(engineA);
                crp.setQuestion(question);
                crp.CRP(engineA);
                compromiselvl = crp.getCompromiselvl();
                optimalChoice = crp.getPreferedliteral();

                writer2.append(question + "," + compromiselvl + "," + optimalChoice);
                writer2.append("\n");
            } else {
                writer2.append("\n");
            }

//            engineA.print_inferted();
            BodyCounter = countBodyLiterals(engineA, rrmA);
            HeadCounter = countHeadLiterals(engineA, rrmA);
            if (i == 0) {
                writeHeaders(writer, rrmA);
                writeHeaders(writer1, rrmA);

                totalMap_HEAD = init_totalMap(rrmA);
                totalMap_BODY = init_totalMap(rrmA);

            }
            M = rrmA.getM();
            totalMap_HEAD = generateCsvFile_HEAD(totalMap_HEAD, i, writer, rrmA, HeadCounter);
            totalMap_BODY = generateCsvFile_BODY(totalMap_BODY, i, writer1, rrmA, BodyCounter);

            printInferted(infertedA, engineA, rrmA);

        }
        generateCsvFile_Total_Avg(totalRuns, writer, M, totalMap_HEAD);
        generateCsvFile_Total_Avg(totalRuns, writer1, M, totalMap_BODY);

        writer.flush();
        writer.close();
        writer1.flush();
        writer1.close();

        infertedA.flush();
        infertedA.close();

        writer2.flush();
        writer2.close();
    }
}
