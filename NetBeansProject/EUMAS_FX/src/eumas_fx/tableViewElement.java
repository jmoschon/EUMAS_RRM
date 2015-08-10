/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eumas_fx;



/**
 *
 * @author jmoschon
 */
public class tableViewElement {
    
       
        private  String ruleID;
        private  String rule ;

        
        public tableViewElement(String ruleID, String rule){
            this.ruleID= ruleID;
            this.rule=rule;
        
        }
 
       public String getRuleID(){
           return this.ruleID;
    
       }
       public String getRule(){
           return this.rule;
       }

       public void setRuleID(String ruleID){
           this.ruleID=ruleID;
       }
       
       public void setRule(String rule){
           this.rule=rule;
       }
}
