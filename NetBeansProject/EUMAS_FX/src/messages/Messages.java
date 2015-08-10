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
public  abstract class Messages {
    
    private String type;
    
    public Messages(String type){
    
        this.type= type;
    }
    
    public String getTypeOfMessage(){
        return type;
    }
    
    
}
