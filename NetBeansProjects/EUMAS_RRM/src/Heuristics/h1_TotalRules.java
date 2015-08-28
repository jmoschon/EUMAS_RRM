/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import conflict_resolution.Candidates;
import heuristics.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
//    private HashMap<Integer, rule_engine.RuleEngine> candidatesKB;
    private HashSet<Candidates> candidatesKB;
//    private int compromise_lvl;

    public h1_TotalRules(rule_engine.RuleEngine originalKB) {
        this.originalKB = originalKB;
//        this.compromise_lvl = -1;

    }

    public h1_TotalRules(HashSet<Candidates> candidatesKB) {
        this.candidatesKB = candidatesKB;

    }
//
//    public int getCompromiselvl() {
//        return this.compromise_lvl;
//    }

    /**
     * this is the core if the h1_TotalRules is the first for CMP to call
     *
     * @param originalKB
     * @return core
     */
    public HashSet<Candidates> core(rule_engine.RuleEngine originalKB, int count) {
//        System.out.println("peos");

        HashSet <Candidates> candidatesKB = new HashSet<>();
        int numbeOfRules = originalKB.returnIDset().size();
        int key = 0;
        for (int i = count; i < numbeOfRules; i++) {
            List<Integer> superset = originalKB.returnIDset();
            List<Set<Integer>> subset = getSubsets(superset, i);
            System.out.println(subset);
            for (Set<Integer> subset1 : subset) {
                rule_engine.RuleEngine KB = new RuleEngine(originalKB);
                Iterator itr = subset1.iterator();
                while (itr.hasNext()) {
                    int ID = (int) itr.next();
                    KB.setInActive(ID);
                }

                KB.init_reasoner();
                KB.CheckPreferences();
                KB.init_reasoner();

                if (KB.isConsistentGeneral()) {
//                    KB.CheckPreferences();
//                    System.out.print("h1 :");
//                        KB.print_inferted();
//                     KB.print_facts();
//                     KB.print_preferences();
//                     KB.print_rules();
                    RuleEngine KB1 = new RuleEngine(KB);
                    Candidates candidate = new Candidates(KB1);
                    candidate.setH1Score(countInActiveRules(KB1));
                    candidatesKB.add(candidate);
                }
            }
            if (!candidatesKB.isEmpty()) {

                return candidatesKB;
            }
        }
//        this.compromise_lvl = numbeOfRules;
//        core(candidatesKB);
//        this.compromise_lvl=numbeOfRules;
        return candidatesKB;

    }

    /**
     * this is the core if the h1_TotaleRules is not the first for CMP to call
     *
     * @param candidatesKB
     */
    public void core(HashSet<Candidates> candidatesKB) {
        for (Candidates candidate :candidatesKB){
         candidate.setH1Score(countInActiveRules(candidate.getKB()));
        }
    }


//    public void increaseCompromiselvl() {
//        this.compromise_lvl++;
//    }

//    private Integer RulesNumber(HashMap<Integer, rule_engine.RuleEngine> candidatesKB) {
//        ArrayList<Integer> rules_counter = new ArrayList<>();
//        for (int i : candidatesKB.keySet()) {
//            rules_counter.add(countActiveRules(candidatesKB.get(i)));
//        }
//        if (rules_counter.isEmpty()) {
//            return 0;
//        }
//
//        return Collections.min(rules_counter);
//    }

    private int countInActiveRules(rule_engine.RuleEngine KB) {

        HashMap<Integer, HashMap<HashSet<String>, HashMap<String, Boolean>>> rules;
        HashMap<Integer, HashMap<String, HashMap<String, Boolean>>> preferences;
        HashMap<Integer, HashMap<String, Boolean>> facts;

        int NumberOfInActiveRules = 0;

        rules = KB.return_rules();
        preferences = KB.return_preference();
        facts = KB.return_facts();

        //count active rules 
        for (int i : rules.keySet()) {
            for (HashSet str : rules.get(i).keySet()) {
                for (String str1 : rules.get(i).get(str).keySet()) {
                    if (!rules.get(i).get(str).get(str1)) {
                        NumberOfInActiveRules++;
                    }
                }
            }
        }
        for (int i : preferences.keySet()) {
            for (String str : preferences.get(i).keySet()) {
                for (String str1 : preferences.get(i).get(str).keySet()) {
                    if (!preferences.get(i).get(str).get(str1)) {
                        NumberOfInActiveRules++;
                    }
                }
            }
        }
        for (int i : facts.keySet()) {
            for (String str : facts.get(i).keySet()) {
                if (!facts.get(i).get(str)) {
                    NumberOfInActiveRules++;
                }
            }
        }
        return NumberOfInActiveRules;
    }

    private static void getSubsets(List<Integer> superSet, int k, int idx, Set<Integer> current, List<Set<Integer>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unseccessful stop clause
        if (idx == superSet.size()) {
            return;
        }
        Integer x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx + 1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx + 1, current, solution);
    }

    private static List<Set<Integer>> getSubsets(List<Integer> superSet, int k) {
        List<Set<Integer>> res = new ArrayList<>();
        getSubsets(superSet, k, 0, new HashSet<Integer>(), res);
        return res;
    }

    private static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
