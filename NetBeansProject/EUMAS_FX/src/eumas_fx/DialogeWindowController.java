/**
 * Sample Skeleton for 'DialogeWindow.fxml' Controller Class
 */

package eumas_fx;

////import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;
//import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;
import Agent.Agent;
import rule_engine.RuleEngine;
import conflict_resolution.conflict_resolution;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;


public class DialogeWindowController {

    
    private final ObservableList<tableViewElement> dataA=FXCollections.observableArrayList();
    private final ObservableList<tableViewElement> dataB=FXCollections.observableArrayList();
    private final ObservableList<tableViewJustElement>justA = FXCollections.observableArrayList();
    private final ObservableList<tableViewJustElement>justB = FXCollections.observableArrayList();
//    private RuleEngine agentA;
    /**
     * this is the list with the agent if the agents is more than 2
     */
    private ArrayList<Agent> AgentList = new ArrayList<>();
    private Agent agentA;
    private Agent agentB;
    
    
    @FXML // fx:id="button_browse_A"
    private Button button_browse_A; // Value injected by FXMLLoader

    @FXML // fx:id="button_browse_B"
    private Button button_browse_B; // Value injected by FXMLLoader

    @FXML // fx:id="MenuBar"
    private MenuBar MenuBar; // Value injected by FXMLLoader

    @FXML // fx:id="TextField_recievedB"
    private TextArea TextArea_recievedB; // Value injected by FXMLLoader

    @FXML // fx:id="AgentA1"
    private Label AgentA1; // Value injected by FXMLLoader

    @FXML // fx:id="TextArea_recievedA"
    private TextArea TextArea_recievedA; // Value injected by FXMLLoader

    @FXML // fx:id="Button_addJustB"
    private Button Button_addJustB; // Value injected by FXMLLoader

    @FXML // fx:id="RuleB"
    private TableColumn<?, ?> RuleB; // Value injected by FXMLLoader

    @FXML // fx:id="TableColumn_justA"
    private TableColumn<tableViewJustElement,String> TableColumn_justA; // Value injected by FXMLLoader

    @FXML // fx:id="RuleA"
    private TableColumn<tableViewElement, String> RuleA; // Value injected by FXMLLoader

    @FXML // fx:id="TableColumn_justB"
    private TableColumn<tableViewJustElement,String> TableColumn_justB; // Value injected by FXMLLoader

    @FXML // fx:id="combobox_message_A"
    private ComboBox<String> combobox_message_A; // Value injected by FXMLLoader

    @FXML // fx:id="combobox_literal_B"
    private ComboBox<String> combobox_literal_B; // Value injected by FXMLLoader

    @FXML // fx:id="tableView_B"
    private TableView<tableViewElement> tableView_B; // Value injected by FXMLLoader

    @FXML // fx:id="tableView_A"
    private TableView<tableViewElement> tableView_A; // Value injected by FXMLLoader

    @FXML // fx:id="button_send_B"
    private Button button_send_B; // Value injected by FXMLLoader

    @FXML // fx:id="button_send_A"
    private Button button_send_A; // Value injected by FXMLLoader

    @FXML // fx:id="RuleIDA"
    private TableColumn<?, ?> RuleIDA; // Value injected by FXMLLoader

    @FXML // fx:id="RuleIDB"
    private TableColumn<?, ?> RuleIDB; // Value injected by FXMLLoader

    @FXML // fx:id="combobox_message_B"
    private ComboBox<String> combobox_message_B; // Value injected by FXMLLoader

    @FXML // fx:id="TextFiled_justB"
    private TextField TextFiled_justB; // Value injected by FXMLLoader

    @FXML // fx:id="TextFiled_justA"
    private TextField TextFiled_justA; // Value injected by FXMLLoader

    @FXML // fx:id="TextField_path_A"
    private TextField TextField_path_A; // Value injected by FXMLLoader

    @FXML // fx:id="TableView_JustA"
    private TableView<tableViewJustElement> TableView_JustA; // Value injected by FXMLLoader

    @FXML // fx:id="TableView_JustB"
    private TableView<tableViewJustElement> TableView_JustB; // Value injected by FXMLLoader

    @FXML // fx:id="TextField_path_B"
    private TextField TextField_path_B; // Value injected by FXMLLoader

    @FXML // fx:id="AgentA"
    private Label AgentA; // Value injected by FXMLLoader

    @FXML // fx:id="combobox_literal_A"
    private ComboBox<String> combobox_literal_A; // Value injected by FXMLLoader




    
    @FXML      
    /**
     * choose file with the KB for each agent
     */
    void filechooserA(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File...");
        File file = fileChooser.showOpenDialog(new Stage());
        this.TextField_path_A.setText(file.getAbsolutePath());  
        agentA= new Agent(file.getAbsolutePath());
        initialize_TableView_JustA();
        fillTableViewA(agentA);
        fillCombobox_messageA(agentA);
        fillCombobox_literalA(agentA);
        TextArea_recievedA.appendText("Mouves left : ");
        for (Integer i : agentA.getMouvesLeft()){
            TextArea_recievedA.appendText(i.toString()+" ");
        }
        TextArea_recievedA.appendText("\n");
    }
    
    @FXML
    void filechooserB(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File...");
        File file = fileChooser.showOpenDialog(new Stage());
        this.TextField_path_B.setText(file.getAbsolutePath());  
        agentB= new Agent(file.getAbsolutePath());
        initialize_TableView_JustB();
        fillTableViewB(agentB);
        fillCombobox_messageB(agentB);
        fillCombobox_literalB(agentB);
        TextArea_recievedB.appendText("Mouves left : ");
        for (Integer i : agentB.getMouvesLeft()){
            TextArea_recievedB.appendText(i.toString()+" ");
        }
        TextArea_recievedB.appendText("\n");
    }
//    @FXML
//    private void handleButtonAction(ActionEvent e) {
//
//        tableView_A entry = new tableViewElement();
//        entry.str1=t1.getText();
//        entry.str2=t2.getText();
//        data.add(entry);
//    }
    
    private void fillTableViewA(Agent agentA){
        dataA.removeAll(dataA);
        fillTableData(agentA.getKB(), dataA);
//        tableViewElement entry = new tableViewElement("peow", "dasdsa");
//        dataA.add(entry);
        Callback<TableColumn, TableCell> editableFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
//                System.out.println("fdfdfd");
                return new EditableTableCell();
            }
        };
        
       
//        tableView_A.setEditable(true);
        tableView_A.setItems(dataA);
        RuleIDA.setCellValueFactory(new PropertyValueFactory<>("ruleID"));
        RuleA.setCellValueFactory(new PropertyValueFactory<>("rule"));

    
    }
    private void fillTableViewB(Agent agentB){
        dataB.removeAll(dataB);
        fillTableData(agentB.getKB(), dataB);
//        tableViewElement entry = new tableViewElement("peow", "dasdsa");
//        dataA.add(entry);
        Callback<TableColumn, TableCell> editableFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
//                System.out.println("fdfdfd");
                return new EditableTableCell();
            }
        };
        
       
//        tableView_B.setEditable(true);
        tableView_B.setItems(dataB);
        RuleIDB.setCellValueFactory(new PropertyValueFactory<>("ruleID"));
        RuleB.setCellValueFactory(new PropertyValueFactory<>("rule"));

    
    }
    private void fillTableData(RuleEngine KB,ObservableList<tableViewElement> data){
        
        HashMap <Integer,HashMap<HashSet<String>,HashMap<String,Boolean>>>rules;
        HashMap <Integer,HashMap<String,HashMap<String,Boolean>>>preferences;
        HashMap <Integer,HashMap<String,Boolean>> facts;
        
        int NumberOfInActiveRules=0;
        
        rules= KB.return_rules();
        preferences= KB.return_preference();
        facts= KB.return_facts();
        
        //count active rules
        rules.keySet().stream().forEach((i) -> {
            rules.get(i).keySet().stream().forEach((str) -> {
                rules.get(i).get(str).keySet().stream().forEach((str1) -> {
                    data.add(new tableViewElement(Integer.toString(i), str+" => "+str1));
                });
            });
        });
        preferences.keySet().stream().forEach((i) -> {
            preferences.get(i).keySet().stream().forEach((str) -> {
                preferences.get(i).get(str).keySet().stream().forEach((_item) -> {
                    data.add(new tableViewElement(Integer.toString(i), str+" > "+_item));
                });
            });
        });        
        facts.keySet().stream().forEach((Integer i) -> {
            facts.get(i).keySet().stream().forEach((str) -> {
                data.add(new tableViewElement(Integer.toString(i), "=> "+str));
            });
        });
    
    }
    
    
    private void initialize_TableView_JustA(){

        justA.removeAll(justA);
//        fillTableData(agentA.getKB(), dataA);
//        tableViewElement entry = new tableViewElement("peow", "dasdsa");
//        dataA.add(entry);
        Callback<TableColumn, TableCell> editableFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                System.out.println("fdfdfd");
                return new EditableTableCell();
            }
        };
        
       
//        tableView_A.setEditable(true);
        TableView_JustA.setItems(justA);
        TableColumn_justA.setCellValueFactory(new PropertyValueFactory<>("str1"));
//        c.setCellValueFactory(new PropertyValueFactory<>("rudsa"));

    }
    private void initialize_TableView_JustB(){
         justB.removeAll(justB);
        Callback<TableColumn, TableCell> editableFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                System.out.println("fdfdfd");
                return new EditableTableCell();
            }
        };
        
       
//        tableView_A.setEditable(true);
        TableView_JustB.setItems(justB);
        TableColumn_justB.setCellValueFactory(new PropertyValueFactory<>("str1"));
//        c.setCellValueFactory(new PropertyValueFactory<>("rudsa"));

    }

    @FXML
    private void Button_JustA(ActionEvent e) {
//        initialize_TableView_JustA();
        String str1;
        str1=TextFiled_justA.getText();
        System.out.println(str1);
        tableViewJustElement entry = new tableViewJustElement(str1);
//        entry.str2=t2.getText();
        if (agentA.getKB().containsFacts(Integer.parseInt(str1)) || agentA.getKB().containsPreference(Integer.parseInt(str1))
            || agentA.getKB().containsRule(Integer.parseInt(str1))){
//            if (agentA.getMouvesLeft().contains(Integer.parseInt(str1))){
                justA.add(entry);
//            }
        }
        TextFiled_justA.clear();
//        System.out.println(justA);
        
    }

    @FXML
    private void Button_JustB(ActionEvent e) {
//        initialize_TableView_JustA();
        String str1;
        str1=TextFiled_justB.getText();
        System.out.println(str1);
        tableViewJustElement entry = new tableViewJustElement(str1);
//        entry.str2=t2.getText();
        if (agentB.getKB().containsFacts(Integer.parseInt(str1)) || agentB.getKB().containsPreference(Integer.parseInt(str1))
            || agentB.getKB().containsRule(Integer.parseInt(str1))){
//            if (agentB.getMouvesLeft().contains(Integer.parseInt(str1))){
                justB.add(entry);
//            }
        }
        TextFiled_justB.clear();
//        System.out.println(justA);
        
    } 
    
    
    private void fillCombobox_messageA(Agent agentA){
//    combo.getItems().addAll("Upload to Database","Export to Excel","Import Excel");
      combobox_message_A.getItems().clear();
      
      combobox_message_A.getItems().addAll(agentA.getNextMouve());
    
    }
    private void fillCombobox_messageB(Agent agentB){
//    combo.getItems().addAll("Upload to Database","Export to Excel","Import Excel");
      combobox_message_B.getItems().clear();
      
      combobox_message_B.getItems().addAll(agentB.getNextMouve());
    
    }
    
    private void fillCombobox_literalA(Agent agentA){
        
        combobox_literal_A.getItems().clear();
        combobox_literal_A.getItems().addAll(agentA.getKB().return_inferted());
    
    }
    private void fillCombobox_literalB(Agent agentB){
        
        combobox_literal_B.getItems().clear();
        combobox_literal_B.getItems().addAll(agentB.getKB().return_inferted());
    
    }
    @FXML 
    private void AskMessageA(ActionEvent event){
        try {
        
        
        if (combobox_message_A.getValue().equals("Ask")){
            combobox_literal_A.getItems().clear();
            combobox_literal_A.getItems().addAll(agentA.getKB().getAskLiterals());
        }
        else if (combobox_message_A.getValue().equals("Agree")){
            combobox_literal_A.getItems().clear();
            combobox_literal_A.getItems().addAll(agentA.getLastProposeLiteral());
        }
        else if (combobox_message_A.getValue().equals("Believe")){
            combobox_literal_A.getItems().clear();
            combobox_literal_A.getItems().addAll(agentA.getBelieveLiteral());
        
        }
        else {
            combobox_literal_A.getItems().clear();
            combobox_literal_A.getItems().addAll(agentA.getKB().return_inferted());
        }
        }
        catch (NullPointerException e){System.out.println("to piasaaa");}
        
    }
  
     @FXML 
    private void AskMessageB(ActionEvent event){
        try{
        if (combobox_message_B.getValue().equals("Ask")){
            combobox_literal_B.getItems().clear();
            combobox_literal_B.getItems().addAll(agentB.getKB().getAskLiterals());
        }
        else if (combobox_message_B.getValue().equals("Agree")){
            combobox_literal_B.getItems().clear();
            combobox_literal_B.getItems().addAll(agentB.getLastProposeLiteral());
        }
        else if (combobox_message_B.getValue().equals("Believe")){
            combobox_literal_B.getItems().clear();
            combobox_literal_B.getItems().addAll(agentB.getBelieveLiteral());
        
        }
        else {
            combobox_literal_B.getItems().clear();
            combobox_literal_B.getItems().addAll(agentB.getKB().return_inferted());
    
        }
        }
        catch (NullPointerException e){System.out.println(" to piasaaaa");}
    }
    @FXML
    private void sentA(ActionEvent event){
//        System.out.println("adsdadsadsa");
    String message=combobox_message_A.getValue();
    if (message.equals("Believe") || message.equals("Propose")){
        ArrayList<Integer> just= new ArrayList<>();
        for (tableViewJustElement element : TableView_JustA.getItems()){
            just.add(Integer.parseInt(element.getStr1()));
        }
        
        agentB.recieveMessage(agentA.sentMessage(message, combobox_literal_A.getValue(), just));
        updateB();
        updateA();
    }
    else if (!message.equals("Pass")){
        agentB.recieveMessage(agentA.sentMessages(message, combobox_literal_A.getValue()));
                updateB();
        updateA();
    }
    else
        agentB.recieveMessage(agentA.sentMessages(message));
            updateB();
        updateA();
        
    
    }
        @FXML
    private void sentB(ActionEvent event){
//        System.out.println("adsdadsadsa");
    String message=combobox_message_B.getValue();
    if (message.equals("Believe") || message.equals("Propose")){
        ArrayList<Integer> just= new ArrayList<>();
        for (tableViewJustElement element : TableView_JustB.getItems()){
            just.add(Integer.parseInt(element.getStr1()));
        }
        
        agentA.recieveMessage(agentB.sentMessage(message, combobox_literal_B.getValue(), just));
        updateA();
        updateB();
    }
        else if (!message.equals("Pass")){
        agentA.recieveMessage(agentB.sentMessages(message, combobox_literal_B.getValue()));
        updateB();
        updateA();
    }
    else
        agentA.recieveMessage(agentB.sentMessages(message));
        updateB();
        updateA();
        
    
    
    }
    private void updateB(){
        System.out.println("updateeee");
        initialize_TableView_JustB();
        fillTableViewB(agentB);
        fillCombobox_messageB(agentB);
        fillCombobox_literalB(agentB);
        fillTextArea_recievedB();
    
    }    
    private void updateA(){
        System.out.println("updateeee");
        initialize_TableView_JustA();
        fillTableViewA(agentA);
        fillCombobox_messageA(agentA);
        fillCombobox_literalA(agentA);
        fillTextArea_recievedA();
    
    }
    
    private void fillTextArea_recievedA(){
        TextArea_recievedA.clear();
        TextArea_recievedA.appendText("Mouves left : ");
        for (Integer i : agentA.getMouvesLeft()){
            TextArea_recievedA.appendText(i.toString()+" ");
        }
        TextArea_recievedA.appendText("\n");
        TextArea_recievedA.appendText("recieved message : "+agentA.getLastMessage());
        if (!agentA.getLastMessage().equals("Pass")){
            TextArea_recievedA.appendText(" with literal : "+ agentA.getLiteralToDiscuss()+'\n');
            if(agentA.getLastMessage().equals("Propose") || agentA.getLastMessage().equals("Believe")){
                TextArea_recievedA.appendText("preferd literal: "+agentA.getPreferedLiteral()+'\n');
                TextArea_recievedA.appendText("Optimal choise found at "+agentA.getCompromiselvl()+"lvl of compromise \n");
            }
        }
        TextArea_recievedA.appendText("Last Proposed Literal: "+ agentA.getLastProposeLiteral());
    }
    private void fillTextArea_recievedB(){
        
        TextArea_recievedB.clear();
        TextArea_recievedB.appendText("Mouves left : ");
        for (Integer i : agentB.getMouvesLeft()){
            TextArea_recievedB.appendText(i.toString()+" ");
        }
        TextArea_recievedB.appendText("\n");
        TextArea_recievedB.appendText("recieved message : "+agentB.getLastMessage());
        if (!agentB.getLastMessage().equals("Pass")){
            TextArea_recievedB.appendText(" with literal : "+ agentB.getLiteralToDiscuss()+'\n');
            if(agentB.getLastMessage().equals("Propose") || agentB.getLastMessage().equals("Believe")){
                TextArea_recievedB.appendText("preferd literal: "+agentB.getPreferedLiteral()+'\n');
                TextArea_recievedB.appendText("Optimal choise found at "+agentB.getCompromiselvl()+"lvl of compromise \n");
            }
        }
        TextArea_recievedB.appendText("Last Proposed Literal: "+ agentB.getLastProposeLiteral());
    }
//    private
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException {
//        justA.add("dasadsads");
//        justA.add(new tableViewElement("dasdas", "dasdasdsa"));
//        dataA.add(new tableViewElement("dasda", "adsdasasd"));
//        fillTableViewA(null);
        
//        String PathKBa = "/home/jmoschon/Desktop/praktikh/test";
//        initiAgentA(PathKBa);
//        fillTableViewA();
//        fillTableViewA();
        
    }


   
}
