/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rule_engine;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author jmoschon
 */

public class RuleEngine {

    
    private final HashMap <Integer,HashMap<String,HashMap<String,Boolean>>>rules;
    private final HashMap <Integer,HashMap<String,HashMap<String,Boolean>>>preferences;
    private final HashMap <Integer,HashMap<String,Boolean>> facts;
    private final HashSet <String> inferted;
    
           
    public RuleEngine(){
        rules= new HashMap<>();
        facts= new HashMap<>();
        preferences= new HashMap<>();
        inferted= new HashSet<>();
    }
    
    public RuleEngine(HashMap facts, HashMap rules, HashMap preferences, HashSet inferted){

        this.rules=rules;
        this.facts= facts;
        this.preferences = preferences;
        this.inferted= inferted;
    }
    
    public RuleEngine(RuleEngine KB){
        this.facts=(HashMap<Integer, HashMap<String, Boolean>>) this.deepClone(KB.facts);
        this.rules=(HashMap<Integer, HashMap<String, HashMap<String, Boolean>>>) this.deepClone(KB.rules) ; 
        this.preferences=(HashMap<Integer, HashMap<String, HashMap<String, Boolean>>>) this.deepClone(KB.preferences); 
        this.inferted=(HashSet<String>) this.deepClone(KB.inferted);
    
    }
    public void print_rules(){
        System.out.println("rules are : ");
        System.out.println(rules);
    
    }
    
    public void print_preferences(){
        System.out.println("preferences are : ");
        System.out.println(preferences);
    }
    
    public void print_facts(){
        System.out.println("facts are : ");
        System.out.println(facts);
    
    }
    
    //TODO: error checking 
    public int add_rules(String rule,int count){
        StringTokenizer st = new StringTokenizer(rule);
        String a;
        String b;
        String c;
        // if st.countTokens ==2 we have a fact 
        if (st.countTokens()==2){
            
            st.nextToken();
            HashMap<String,Boolean> list=new  HashMap<>();
            list.put(st.nextToken(), true);
            facts.put(count, list);
            count++;
        }
        else{
            a=st.nextToken(); // first token
            b=st.nextToken(); // second token
            c=st.nextToken(); // third token
            if (b.equals("=>")){
              HashMap<String,Boolean> list=new  HashMap<>();
              HashMap<String,HashMap<String,Boolean>>list1=new  HashMap<>();
              list.put(c,true);
              list1.put(a, list);
              rules.put(count, list1);
              count++;
            }
            else{
              HashMap<String,Boolean> list=new  HashMap<>();
              HashMap<String,HashMap<String,Boolean>>list1=new  HashMap<>();
              list.put(c,true);
              list1.put(a, list);
              preferences.put(count, list1);
              count++;           
            
            }
        }
        return count;
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
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

    
    /**
     * 
     * @param path  the path of the file 
     */    
    public void readKB (String path) throws FileNotFoundException, IOException{
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            String line;
            int count_rules=0;
            while((line = in.readLine()) != null)
            {
                count_rules=this.add_rules(line,count_rules);
            }
        }
    
    }
    
    
    //this function will provide all the inferted states and rules
    public void reasoner(String str){
//            System.out.println(str);
            for (Integer count: rules.keySet())
            if (rules.get(count).containsKey(str)){
                for (String value : rules.get(count).get(str).keySet()){ //keySet->set with the keys
//                    System.out.println(value);
//                    System.out.println(rules.get(str).get(value));
                    if (rules.get(count).get(str).get(value)){
//                        System.out.println(rules.get(str));
                        if (!inferted.contains(value)){
                            this.inferted.add(value);
                        }
                        if(rules.get(count).containsKey(value)){
                            this.inferted.add(value);
                            this.reasoner(value);
                    
                        }
                    }
                }
            }
    }
    
  
    public void print_inferted(){
        System.out.println("inferted : ");
        System.out.println(inferted);
    }    

    

    
    public void init_reasoner(){
        inferted.removeAll(inferted);
        for (Integer str : facts.keySet()) {
            //vale mesa ola ta facts
//            System.out.println(facts.get(str));
            for (String str2 : facts.get(str).keySet()){
            if (facts.get(str).get(str2)){
                
                if (inferted.isEmpty() || !inferted.contains(str2)){
                    for (String str1 :facts.get(str).keySet()){
                        this.inferted.add(str1);
                        this.reasoner(str1);
                    }
                }
            }
            }
        }
    }
    
    
    public boolean isConsistentGeneral(){
       
       for(String question :inferted){
        StringTokenizer st2 = new StringTokenizer(question, "-");
        char c = question.charAt(0);
        
        if (c=='-'){
            String question1= st2.nextToken("-");
            if(this.inferted.contains(question) && 
                    this.inferted.contains(question1)){
                return false;
            }
        }
        else{
            if(this.inferted.contains(question) && 
                    this.inferted.contains("-"+question)){
                return false;
            }
            
        }
       }
       return true;
    }
    //check if the KB is consistent 
    public boolean isConsistent(String question){
        StringTokenizer st2 = new StringTokenizer(question, "-");
        char c = question.charAt(0);
        
        if (c=='-'){
            String question1= st2.nextToken("-");
            if(this.inferted.contains(question) && 
                    this.inferted.contains(question1)){
                return false;
            }
        }
        else{
            if(this.inferted.contains(question) && 
                    this.inferted.contains("-"+question)){
                return false;
            }
            
        }
        return true;
    }

    /**
     * this function returns how many rules are into the KB
     * @return size of KB
     */
    public int size(){
       return this.facts.size()+this.preferences.size()+this.rules.size();
        
    }
    
  // for the CR
    public HashMap <Integer,HashMap<String,HashMap<String,Boolean>>> return_rules(){
        return this.rules;  
    }
    public HashSet<String> return_inferted(){
        return this.inferted;
    }
    public HashMap<Integer,HashMap<String, HashMap<String, Boolean>>> return_preference(){
        return this.preferences;
    }
    
    public HashMap <Integer,HashMap<String,Boolean>>  return_facts(){
        return this.facts;
    }
    
    /**
     * return the IDs of active rules in order to find the powerset 
     *@return IDset
     */
    public List<Integer> returnIDset(){
        List<Integer> IDset = new ArrayList<>();
        for (Integer rule : rules.keySet()){
            for (String str: rules.get(rule).keySet() ){
                for (String str1: rules.get(rule).get(str).keySet()){
                    if (rules.get(rule).get(str).get(str1))
                        IDset.add(rule);
//                        System.out.println(rule);
                }
            }
        }
        for (Integer fact : facts.keySet()){
            for (String str: facts.get(fact).keySet()){
                if (facts.get(fact).get(str)){
                    IDset.add(fact);
//                    System.out.println(fact);
                }
            }
        
        }
        return IDset;
    }
     /**
     * set a rule as Active
     * @param ID
     */
    public void setActive (int ID){
        if(rules.containsKey(ID)){
            for (String str : rules.get(ID).keySet()){
                for (String str1 : rules.get(ID).get(str).keySet()){
                    rules.get(ID).get(str).replace(str1, true);
                }
            }
        }
        
        if (facts.containsKey(ID)){
            for (String str : facts.get(ID).keySet()){
                facts.get(ID).replace(str, true );
            }
        }
        if (preferences.containsKey(ID)){
            for (String str : preferences.get(ID).keySet()){
                for (String str1 : preferences.get(ID).get(str).keySet()){
                    preferences.get(ID).get(str).replace(str1, true);
                }
            }
        }
    }
     /**
     * set a rule as InActivre
     * @param ID
     */    
    public void setInActive (int ID){
        if(rules.containsKey(ID)){
//            System.out.println("poes");
            for (String str : rules.get(ID).keySet()){
                for (String str1 : rules.get(ID).get(str).keySet()){
                    rules.get(ID).get(str).replace(str1, false);
                    
                }
            }
        }
        
        if (facts.containsKey(ID)){
            for (String str : facts.get(ID).keySet()){
                facts.get(ID).replace(str, false );
            }
        }
        if (preferences.containsKey(ID)){
            for (String str : preferences.get(ID).keySet()){
                for (String str1 : preferences.get(ID).get(str).keySet()){
                    preferences.get(ID).get(str).replace(str1, false);
                }
            }
        }
    }

    /**
    *if a>b and b is inferted and a is not inferted make a>b inactive
    */   
    public void CheckPreferences(){
        
        String key = null;
        String value = null;
        boolean check= false;
        for (int i : this.preferences.keySet()){
            for (String str: this.preferences.get(i).keySet()){
                key=str;
                for (String str1: this.preferences.get(i).get(str).keySet()){
                    value=str1;
                }
            }
            if(!this.inferted.contains(key) && this.inferted.contains(value)){
                this.setInActive(i);
                check= true;
                
            }
        }
//        return check;
    }

}
