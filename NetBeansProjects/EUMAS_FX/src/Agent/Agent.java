/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agent;

import conflict_resolution.conflict_resolution;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import messages.*;

import rule_engine.RuleEngine;

/**
 *
 * @author jmoschon
 */
public class Agent {

    private  String recieved_message_log="empty" ;
    private final ArrayList <Integer> rules_log;
    private final RuleEngine KB;
    /*each aagent has his own CRP*/
//    private final conflict_resolution crp;
    private final ArrayList<String> nextMouves;
    private String literal_toDiscuss;
    private int compromiselvl;
    private String preferedliteral;
    private HashSet<Integer> mouvesLeft; 
    private boolean propose_recieved= false;
    private String last_proposed_literal="non yet";
    private String BelieveLiteral;
    private HashSet<Integer> mouvesDone;
    
    public Agent(String path) throws IOException{

        this.rules_log= new ArrayList<>();
        this.KB= new RuleEngine();
        KB.readKB(path);
        KB.init_reasoner();
        this.nextMouves = new ArrayList<>();
        this.nextMouves.add("Ask");
        this.nextMouves.add("Propose");
        this.literal_toDiscuss=null;
        this.mouvesLeft= new HashSet<>();
        this.mouvesDone = new HashSet<>();
        this.addMouves();
        
    }
    private void addMouves(){
        for(int i : this.KB.returnTotalIDset()){
            if (!mouvesDone.contains(i)){
                this.mouvesLeft.add(i);
            }
            //System.out.println("oi kanones einai : "+i);
        }
        //System.out.println("oi kanones einai : "+mouvesLeft);
    }
    
    private void findBelievLiteral(String question){

        HashSet<String> inferted=this.KB.return_inferted();
            System.out.println("inferted: "+inferted);
            StringTokenizer st2 = new StringTokenizer(question, "-");
            char c = question.charAt(0);

            if (c=='-'){
                String question1= st2.nextToken("-");
                if(inferted.contains(question) && 
                        inferted.contains(question1)){
                    this.BelieveLiteral="+"+question1;
                }
                else if (inferted.contains(question)){
                    this.BelieveLiteral=question;
                }
                else if (inferted.contains(question1)){
                    this.BelieveLiteral=question1;
                }
                else {
                    this.BelieveLiteral="~"+question1;
                }
            }
            else{
                if(inferted.contains(question) && 
                        inferted.contains("-"+question)){
                    this.BelieveLiteral="+"+question;
                }
                else if (inferted.contains(question)){
                    this.BelieveLiteral=question;
                }
                else if (inferted.contains("-"+question)){
                    this.BelieveLiteral="-"+question;
                }
                else{
                    this.BelieveLiteral="~"+question;
                }
            }

            
               
    
    }
    public void recieveMessage(messages.Messages msg){
        
        this.recieved_message_log=msg.getTypeOfMessage();
        System.out.println("a message is just recieved");
        switch(msg.getTypeOfMessage()){
            case "Ask":
                
                if (msg instanceof Ask){
                  Ask msg1 = (Ask) msg;
                  this.literal_toDiscuss= msg1.getLiteral();
                    findBelievLiteral(msg1.getLiteral());
                  
//                  conflict_resolution crp = new conflict_resolution(KB);
//                  crp.setQuestion(literal_toDiscuss);
//                  crp.CRP(this.KB);
//                  this.compromiselvl=crp.getCompromiselvl();
//                  this.preferedliteral=crp.getPreferedliteral();
                  
                }
                AskMessageRecieved();
                break;
            case "Agree":
                AgreeMessageRecieved();
                if (msg instanceof Agree){
                  Agree msg1 = (Agree) msg;
                  this.literal_toDiscuss= msg1.getLiteral();
//                  conflict_resolution crp = new conflict_resolution(KB);
//                  crp.setQuestion(literal_toDiscuss);
//                  crp.CRP(this.KB);
//                  this.compromiselvl=crp.getCompromiselvl();
//                  this.preferedliteral=crp.getPreferedliteral();
                  
                }
                break;
            case "Believe":

                if (msg instanceof Believe){
                    Believe msg1= (Believe) msg;
                    addJustToKB(msg1.getJust());
                    this.KB.print_facts();

                    this.KB.print_preferences();
                    this.KB.print_rules();
                    this.KB.print_inferted();
                    this.literal_toDiscuss=msg1.getLiteral();
                  
//                    if (this.literal_toDiscuss.charAt(0)=='+' && this.literal_toDiscuss.charAt(0)=='~'){
                        
                        if (this.literal_toDiscuss.contains("+")){
                            this.literal_toDiscuss=this.literal_toDiscuss.replace('+','-');
                        }
                        else if (this.literal_toDiscuss.contains("~")){
                            this.literal_toDiscuss=this.literal_toDiscuss.replace('~','-');
                        }
                        conflict_resolution crp = new conflict_resolution(KB);
                        if (propose_recieved){
                        crp.setQuestion(last_proposed_literal);
//                        crp.setQuestion(literal_toDiscuss.replace('~', '-'));
                        
                        crp.CRP(this.KB);
                        this.compromiselvl=crp.getCompromiselvl();
                        this.preferedliteral=crp.getPreferedliteral();
                        
                    }
                }
                BelieveMessageRecieved();
                break;
            case "Propose":
                
                if (msg instanceof Propose){
                    Propose msg1= (Propose) msg;
                    addJustToKB(msg1.getJust());
                    this.literal_toDiscuss=msg1.getLiteral();
                    this.last_proposed_literal= msg1.getLiteral();
                    
                    conflict_resolution crp = new conflict_resolution(KB);

                    crp.setQuestion(last_proposed_literal);
                    crp.CRP(this.KB);
                    this.compromiselvl=crp.getCompromiselvl();
                    this.preferedliteral=crp.getPreferedliteral();
                    

                }
                ProposeMessageRecieved();
                break;
            case "Pass":
                PassMessageRecieved();
                break;
            default:
                System.err.println("Error! Wrong type of message.");
        }
    }
    
    public void addJustToKB(RuleEngine Just){
//        System.out.println("dassdasda");
//        System.out.println("me kaleses");
        HashMap <Integer,HashMap<HashSet<String>,HashMap<String,Boolean>>>rules=Just.return_rules();
        HashMap <Integer,HashMap<String,HashMap<String,Boolean>>>preferences=Just.return_preference();
        HashMap <Integer,HashMap<String,Boolean>> facts = Just.return_facts();
        for (Integer ID : rules.keySet()){
            this.KB.putRule((HashMap<HashSet<String>, HashMap<String, Boolean>>) this.deepClone(rules.get(ID)));
            
            
        }
        for (Integer ID : preferences.keySet()){
            this.KB.putPreference((HashMap<String, HashMap<String, Boolean>>) this.deepClone(preferences.get(ID)));
        }
        for (Integer ID : facts.keySet()){
            this.KB.putFact((HashMap<String, Boolean>) this.deepClone(facts.get(ID)));
        }
        this.KB.init_reasoner();
//        this.addMouves();
//        this.KB.print_rules();
    }
    
    public Messages sentMessage(String messageType,String literal, ArrayList<Integer> IDs){
        Messages msg = null;
        RuleEngine just = new RuleEngine();
        System.out.println("sent message");
        for (int i : IDs){
            if (this.KB.containsFacts(i)){
                just.putFact((HashMap<String, Boolean>) this.deepClone(KB.getFacts(i)));
                
                this.mouvesLeft.remove(i);
                this.mouvesDone.add(i);
            }
            else if (this.KB.containsPreference(i)){
                just.putPreference((HashMap<String, HashMap<String, Boolean>>) this.deepClone(KB.getPreference(i)));
                this.mouvesLeft.remove(i);
                this.mouvesDone.add(i);
            }
            else if (this.KB.containsRule(i)){
                just.putRule((HashMap<HashSet<String>, HashMap<String, Boolean>>) this.deepClone(KB.getRule(i)));
                this.mouvesLeft.remove(i);
                this.mouvesDone.add(i);
            }
//            this.mouvesLeft.remove(i); <====
        }
        switch (messageType) {
            case "Believe":
                msg = new Believe(literal, just);
                break;
            case "Propose":
                msg = new Propose(literal, just);
                System.out.println("a propose message created");
                break;
            default:
                System.out.println("Error! Message Type :"+messageType);
                break;
        }
    
        return msg;
    }
    public Messages sentMessages(String messageType, String literal){
        Messages msg = null;
        switch (messageType) {
            case "Agree":
                msg= new Agree(literal);
                break;
            case "Ask":
                msg = new Ask(literal);
                break;
            default:
                System.out.println("Error! Message Type :"+messageType);
                break;
        }
        return msg;
    }
    
    public Messages sentMessages(String messageType){
        
        Messages msg = new Pass();
        return msg;
    }
    
    public void AskMessageRecieved(){

        if (!this.nextMouves.isEmpty()){
            this.nextMouves.removeAll(nextMouves);
        }
        this.nextMouves.add("Believe");
    } 
    
    public void AgreeMessageRecieved(){

        if (!this.nextMouves.isEmpty()){
            this.nextMouves.removeAll(nextMouves);
        }
        this.nextMouves.add("Argreement!!!!");
    } 
    
    public void BelieveMessageRecieved(){

        if (!this.nextMouves.isEmpty()){
            this.nextMouves.removeAll(nextMouves);
        }
        if (this.propose_recieved && this.preferedliteral.equals(last_proposed_literal)){
            this.nextMouves.add("Agree");
        }
        this.nextMouves.add("Propose");
        this.nextMouves.add("Pass");
        this.nextMouves.add("Ask");
    }
    public void ProposeMessageRecieved(){

        if (!this.nextMouves.isEmpty()){
            this.nextMouves.removeAll(nextMouves);
        }
        this.nextMouves.add("Ask");
        this.nextMouves.add("Propose");
        if (this.preferedliteral.equals(last_proposed_literal)){
            this.nextMouves.add("Agree");
            }
        this.propose_recieved= true;
    }    
    public void PassMessageRecieved(){

        if (!this.nextMouves.isEmpty()){
            this.nextMouves.removeAll(nextMouves);
        }
        this.nextMouves.add("Ask");
        this.nextMouves.add("Propose");
    }
    
    
    public ArrayList<String> getNextMouve(){
        return this.nextMouves;
    }
    
    public rule_engine.RuleEngine getKB(){
        return this.KB;
    }
    
    public String getLastProposeLiteral(){
        return this.last_proposed_literal;
    }
    
    public String getLastMessage(){   
        return this.recieved_message_log;
    }
    public String getBelieveLiteral(){
        return this.BelieveLiteral;
    }
    public String getLiteralToDiscuss(){
        return this.literal_toDiscuss;
    }
    public String getPreferedLiteral(){
        return this.preferedliteral;
    }
    public String getCompromiselvl(){
        return Integer.toString(this.compromiselvl);
    }
    public HashSet<Integer> getMouvesLeft(){
        return this.mouvesLeft;
    }
    /**
    * This method makes a "deep clone" of any object it is given.
     * @param object
     * @return 
    */
  private static Object deepClone(Object object) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);
      return ois.readObject();
    }
    catch (IOException | ClassNotFoundException e) {
      return null;
    }
  }
}

