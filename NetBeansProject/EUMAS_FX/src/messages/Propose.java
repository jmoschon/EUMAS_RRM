/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

/**
 *
 * @author jmoschon
 */
public class Propose extends Messages{

    private final String literal;
    private final rule_engine.RuleEngine Just;
    public Propose(String literal,rule_engine.RuleEngine Just) {
        super("Propose");
        this.literal=literal;
        this.Just=Just;
    }
    
    public String getLiteral(){
        return this.literal;
    }
    
    public rule_engine.RuleEngine getJust(){
        return this.Just;
    }
}
