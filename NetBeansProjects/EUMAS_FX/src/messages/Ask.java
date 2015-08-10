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
public class Ask extends Messages{
    private final String literal;
    
    
    public Ask(String literal) {
        super("Ask");
        this.literal=literal;
    }
    
    public String getLiteral(){
        return this.literal;
    }
    
}
