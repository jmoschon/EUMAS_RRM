/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conflict_resolution;

import rule_engine.RuleEngine;

/**
 *
 * @author jmoschon
 */
public class Candidates {

    private int h1_score;
    private int h2_score;
    private int h3_score;
    private int h4_score;
    private int h5_score;
    private int h6_score;

    private int compromise_lvl;

    private final RuleEngine KB;

    public Candidates(RuleEngine KB) {

        this.h1_score = 0;
        this.h2_score = 0;
        this.h3_score = 0;
        this.h4_score = 0;
        this.h5_score = 0;
        this.h6_score = 0;
        this.KB = KB;

    }

    public void setH1Score(int score) {
        this.h1_score = score;
    }

    public void setH2Score(int score) {
        this.h2_score = score;
    }

    public void setH3Score(int score) {
        this.h3_score = score;
    }

    public void setH4Score(int score) {
        this.h4_score = score;
    }

    public void setH5Score(int score) {
        this.h5_score = score;
    }

    public void setH6Score(int score) {
        this.h6_score = score;
    }

    public int getH1Score() {
        return this.h1_score;
    }

    public int getH2Score() {
        return this.h2_score;
    }

    public int getH3Score() {
        return this.h3_score;
    }

    public int getH4Score() {
        return this.h4_score;
    }

    public int getH5Score() {
        return this.h5_score;
    }

    public int getH6Score() {
        return this.h6_score;
    }
    
   public RuleEngine getKB(){
       return this.KB;
   }
}
