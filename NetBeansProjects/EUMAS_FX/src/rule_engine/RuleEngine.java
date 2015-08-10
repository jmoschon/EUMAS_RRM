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

    
    private final HashMap <Integer,HashMap<HashSet<String>,HashMap<String,Boolean>>>rules;
//    private final HashMap <Integer,HashMap<HashSet<String>,HashMap<String,Boolean>>> complex_rules;
    private final HashMap <Integer,HashMap<String,HashMap<String,Boolean>>>preferences;
    private final HashMap <Integer,HashMap<String,Boolean>> facts;
    private final HashSet <Integer> OwnPreferences;
    private final HashSet <String> inferted;
    private int totalRules;
           
    public RuleEngine(){
        this.rules= new HashMap<>();
        this.facts= new HashMap<>();
        this.preferences= new HashMap<>();
//        this.complex_rules= new HashMap<>();
        this.inferted= new HashSet<>();
        this.totalRules=0;
        
        this.OwnPreferences=new HashSet<>();
       
    }
    
//    public RuleEngine(HashMap facts, HashMap rules, HashMap preferences, HashSet inferted){
//
//        this.rules=rules;
//        this.facts= facts;
//        this.preferences = preferences;
//        this.inferted= inferted;
//   
//    }
    
    public RuleEngine(RuleEngine KB){
        this.facts=(HashMap<Integer, HashMap<String, Boolean>>) this.deepClone(KB.facts);
        this.rules=(HashMap<Integer,HashMap<HashSet<String>,HashMap<String,Boolean>>>) this.deepClone(KB.rules) ; 
        this.preferences=(HashMap<Integer, HashMap<String, HashMap<String, Boolean>>>) this.deepClone(KB.preferences); 
//        this.complex_rules= (HashMap<Integer,HashMap<HashSet<String>,HashMap<String,Boolean>>>)this.deepClone(KB.complex_rules);
        this.inferted=(HashSet<String>) this.deepClone(KB.inferted);
        this.totalRules= (Integer) this.deepClone(KB.totalRules);
        this.OwnPreferences= (HashSet<Integer>) this.deepClone(KB.OwnPreferences);
    
    }
    
    /** return the number of rules that inside in the KB
     * @return totalRules
     */
    public int getTotalRules(){
        return this.totalRules;
    }
    /**increase totalRules when a rules is added*/
    private void increaseTotalRules(){
        this.totalRules++;
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
    private void readRules(String rule){
        StringTokenizer st = new StringTokenizer(rule);
        String a;
        String b;
        String c;
        // if st.countTokens ==2 we have a fact 
        if (st.countTokens()==2){
            
            st.nextToken();
            HashMap<String,Boolean> list=new  HashMap<>();
            list.put(st.nextToken(), true);
            facts.put(this.getTotalRules(), list);
            this.increaseTotalRules();
        }
        else {
            a=st.nextToken(); // first token
            b=st.nextToken(); // second token
            c=st.nextToken(); // third token
            
            if (b.equals("=>")){
              StringTokenizer st1 = new StringTokenizer(a,",");
              HashSet<String> set= new HashSet<>();
              while (st1.hasMoreTokens()){
                  set.add(st1.nextToken().replace(" ",""));
              }
              HashMap<String,Boolean> list=new  HashMap<>();
              HashMap<HashSet<String>,HashMap<String,Boolean>>list1=new  HashMap<>();
              list.put(c,true);
              list1.put(set, list);
              rules.put(this.getTotalRules(), list1);
              this.increaseTotalRules();
            }
            else{
              HashMap<String,Boolean> list=new  HashMap<>();
              HashMap<String,HashMap<String,Boolean>>list1=new  HashMap<>();
              list.put(c,true);
              list1.put(a, list);
              preferences.put(getTotalRules(), list1);
              this.OwnPreferences.add(getTotalRules());
              this.increaseTotalRules();
            
            }
        }
//        return count;
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
//            int count_rules=0;
            while((line = in.readLine()) != null)
            {
                this.readRules(line);
            }
        }
    
    }
    
    
    //this function will provide all the inferted states and rules
    public void reasoner(){
        
        for (Integer count : rules.keySet()){
            for (HashSet s : rules.get(count).keySet()){
                if (inferted.containsAll(s)){
                    
                    for (String value : rules.get(count).get(s).keySet()){
                        if (!inferted.contains(value)){
                            inferted.add(value);
                            this.reasoner();
                        }
//                        this.reasoner();
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
//                        HashSet<String> set= new HashSet<>();
//                        set.add(str1);
                        this.reasoner();
                    }
                    
                }
            }
            }
        }
    }
//    
    private void makeAlltrue(){
        for (Integer rule : rules.keySet()){
            for(HashSet<String> s : rules.get(rule).keySet()){
                
                    for (String str1: rules.get(rule).get(s).keySet()){
                        this.rules.get(rule).get(s).put(str1, true);
                    }
                
            }
        }
        for (Integer preference : preferences.keySet()){
            for (String str: preferences.get(preference).keySet() ){
                for (String str1: preferences.get(preference).get(str).keySet()){
                    this.preferences.get(preference).get(str).put(str1, true);
                }
            }
        }
        for (Integer fact : facts.keySet()){
            for (String str: facts.get(fact).keySet()){
                this.facts.get(fact).put(str, true);
            }
        
        }
    
    }
        
    public boolean isConsistentGeneral(){
       this.makeAlltrue();
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
    public HashMap <Integer,HashMap<HashSet<String>, HashMap<String, Boolean>>> return_rules(){
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
            for (HashSet str: rules.get(rule).keySet() ){
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
    public List<Integer> returnTotalIDset(){
        List<Integer> IDset = new ArrayList<>();
        for (Integer rule : rules.keySet()){
            for (HashSet str: rules.get(rule).keySet() ){
                for (String str1: rules.get(rule).get(str).keySet()){
                    if (rules.get(rule).get(str).get(str1))
                        IDset.add(rule);
//                        System.out.println(rule);
                }
            }
        }
        for (Integer preference : preferences.keySet()){
            for (String str: preferences.get(preference).keySet() ){
                for (String str1: preferences.get(preference).get(str).keySet()){
                    if (preferences.get(preference).get(str).get(str1))
                        IDset.add(preference);
//                        System.out.println(rule);
                }
            }
        }
        for (Integer fact : facts.keySet()){
            for (String str: facts.get(fact).keySet()){
                if (facts.get(fact).get(str)){
                    IDset.add(fact);
//                    System.out.println("to fact poy mpike :"+fact);
                }
            }
        
        }
        return IDset;
    }
    /**
     * @param ID
     * @return true if the key is  Rules
     */
    public boolean containsRule(int ID){
        return rules.containsKey(ID);
    }
    /**
     * @param ID
     * @return true if the key is  Preference
     */
    public boolean containsPreference(int ID){
        return preferences.containsKey(ID);
    }
    /**
     * @param ID
     * @return true if the key is  Facts
     */
    public boolean containsFacts(int ID){
        return facts.containsKey(ID);
    }
    /**
     * returns a rule with ID if the ID is not in the Rules returns NULL
     * @param ID
     * @return 
     */
    public HashMap<HashSet<String>, HashMap<String, Boolean>> getRule(int ID){
        if (rules.containsKey(ID)){
            return rules.get(ID);
        }
        else{
            return null;
        }
    
    }

    /**
     * 
     * @return the set with the Own Preference IDs 
     */
    public HashSet<Integer> getOwnPreferenceIDs(){
        return this.OwnPreferences;
    }
    /**
     * returns a preference with ID if the ID is not in the Preferences returns NULL
     * @param ID
     * @return 
     */
    public HashMap<String, HashMap<String, Boolean>> getPreference(int ID){
        if (preferences.containsKey(ID)){
            return preferences.get(ID);
        }
        else{
            return null;
        }
    
    }

    public List<String> getAskLiterals(){
        List<String> IDset = new ArrayList<>();
        for (Integer rule : rules.keySet()){
                for (HashSet<String> str: rules.get(rule).keySet() ){
                        if (!inferted.containsAll(str)&&!IDset.containsAll(str)){
                            for (String str1 : str){
                                if (!IDset.contains(str1)){
                                IDset.add(str1);
                                }
                            }
                        }
    //       System.out.println(rule);
                    }
            }

            for (Integer preference : preferences.keySet()){
                for (String str: preferences.get(preference).keySet() ){
                        if (!inferted.contains(str)&&!IDset.contains(str)){
                            IDset.add(str);
    //                        System.out.println(rule);
                    }
                }
            }
            for (Integer fact : facts.keySet()){
                for (String str: facts.get(fact).keySet()){
                        if (!inferted.contains(str)&&!IDset.contains(str)){
                            IDset.add(str);
    //                    System.out.println("to fact poy mpike :"+fact);
                    }
                }

            }
        return IDset;
    }
    /**
     * returns a fact with ID if the ID is not in the Facts returns NULL
     * @param ID
     * @return 
     */
    public HashMap<String, Boolean> getFacts(int ID){
        if (facts.containsKey(ID)){
            return facts.get(ID);
        }
        else{
            return null;
        }
    
    }    
    
    /** put a fact into the Facts
     * @param fact
     */
    public void putFact(HashMap<String, Boolean> fact){
        boolean contains_already=false;
        for (Integer i : this.facts.keySet()){
            for(String str : this.facts.get(i).keySet()){
                for (String str2 : fact.keySet()){
                    if (str.equals(str2)){
                        contains_already=true;
                    }
                }
            }
        }
        if (!contains_already){
            facts.put(getTotalRules(), fact);
            increaseTotalRules();
        }
        
    
    }
  
    /** put a fact into the Facts
     * @param rule
     */
    public void putRule(HashMap<HashSet<String>, HashMap<String, Boolean>> rule){
       
        boolean contains_already = false;
        
        for( Integer i: this.rules.keySet()){
            for (HashSet str : this.rules.get(i).keySet()){
                for(HashSet _str: rule.keySet()){
                    if (str.equals(_str)){
                        for (String str1 : this.rules.get(i).get(str).keySet()){
                            for (String _str1: rule.get(_str).keySet()){
                                if (_str1.equals(str1)){
                                    contains_already=true;
                                }
                            }           
                        }          
                    }            
                }
            }
            
        }
        if (!contains_already){
            rules.put(getTotalRules(), rule);
            increaseTotalRules();
        }
        
    
    }    
    
   
    
    /** put a fact into the Facts
     * @param preference
     */
    public void putPreference(HashMap<String, HashMap<String, Boolean>> preference){
            boolean contains_already = false;
        
        for( Integer i: this.preferences.keySet()){
            for (String str : this.preferences.get(i).keySet()){
                for(String _str: preference.keySet()){
                    if (str.equals(_str)){
                        for (String str1 : this.preferences.get(i).get(str).keySet()){
                            for (String _str1: preference.get(_str).keySet()){
                                if (_str1.equals(str1)){
                                    contains_already=true;
                                }
                            }           
                        }          
                    }            
                }
            }
        }       
        
        
        if (!contains_already){
            preferences.put(getTotalRules(), preference);
            increaseTotalRules();
        }
        
    }    
        
     /**
     * set a rule as Active
     * @param ID
     */
    public void setActive (int ID){
        if(rules.containsKey(ID)){
            for (HashSet str : rules.get(ID).keySet()){
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
            for (HashSet str : rules.get(ID).keySet()){
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
