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
public class Agree extends Messages{
    private final String literal;
    
    
    public Agree(String literal) {
        super("Agree");
        this.literal=literal;
    }
    
    public String getLiteral(){
        return this.literal;
    }
}
