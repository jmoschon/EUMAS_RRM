/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eumas_fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author jmoschon
 */
public class tableViewElement {
    
       
        private  String ruleID;
        private  String rule ;
        private  CheckBox cbox;
//        private final ObjectProperty<CheckBox> cbox;
        
        public tableViewElement(String ruleID, String rule){
            this.ruleID= ruleID;
            this.rule=rule;
            this.cbox= new CheckBox();
        
        }
 
       public String getRuleID(){
           return this.ruleID;
    
       }
       public String getRule(){
           return this.rule;
       }
       
       public CheckBox getCbox(){
           return this.cbox;
       }
       
       public void setRuleID(String ruleID){
           this.ruleID=ruleID;
       }
       
       public void setRule(String rule){
           this.rule=rule;
       }
}
