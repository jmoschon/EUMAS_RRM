/**
 * Sample Skeleton for 'DialogeWindow.fxml' Controller Class
 */

package eumas_fx;

////import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;
//import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;
import rule_engine.RuleEngine;
import conflict_resolution.conflict_resolution;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;


public class DialogeWindowController {

    
    private final ObservableList<tableViewElement> dataA=FXCollections.observableArrayList();
    private RuleEngine agentA;
    
    
    
    
    @FXML // fx:id="combobox_message_A"
    private ComboBox<?> combobox_message_A; // Value injected by FXMLLoader

    @FXML // fx:id="button_browse_A"
    private Button button_browse_A; // Value injected by FXMLLoader

    @FXML // fx:id="combobox_literal_B"
    private ComboBox<?> combobox_literal_B; // Value injected by FXMLLoader

    @FXML // fx:id="tableView_B"
    private TableView<?> tableView_B; // Value injected by FXMLLoader

    @FXML // fx:id="tableView_A"
    private TableView<tableViewElement> tableView_A; // Value injected by FXMLLoader

    @FXML // fx:id="button_send_B"
    private Button button_send_B; // Value injected by FXMLLoader

    @FXML // fx:id="button_browse_B"
    private Button button_browse_B; // Value injected by FXMLLoader

    @FXML // fx:id="button_send_A"
    private Button button_send_A; // Value injected by FXMLLoader

    @FXML // fx:id="MenuBar"
    private MenuBar MenuBar; // Value injected by FXMLLoader

    @FXML // fx:id="RuleIDA"
    private TableColumn<tableViewElement, String> RuleIDA; // Value injected by FXMLLoader

    @FXML // fx:id="RuleIDB"
    private TableColumn<tableViewElement, String> RuleIDB; // Value injected by FXMLLoader

    @FXML // fx:id="combobox_message_B"
    private ComboBox<?> combobox_message_B; // Value injected by FXMLLoader

    @FXML // fx:id="AgentA1"
    private Label AgentA1; // Value injected by FXMLLoader

    @FXML // fx:id="TextField_path_A"
    private TextField TextField_path_A; // Value injected by FXMLLoader

    @FXML // fx:id="TextField_path_B"
    private TextField TextField_path_B; // Value injected by FXMLLoader

    @FXML // fx:id="AgentA"
    private Label AgentA; // Value injected by FXMLLoader

    @FXML // fx:id="combobox_literal_A"
    private ComboBox<?> combobox_literal_A; // Value injected by FXMLLoader


    @FXML // fx:id="RuleB"
    private TableColumn<?, ?> RuleB; // Value injected by FXMLLoader

    @FXML // fx:id="RuleA"
    private TableColumn<?, ?> RuleA; // Value injected by FXMLLoader


    
    @FXML      
    void peos(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(new Stage());
        
    }
//    @FXML
//    private void handleButtonAction(ActionEvent e) {
//
//        tableView_A entry = new tableViewElement();
//        entry.str1=t1.getText();
//        entry.str2=t2.getText();
//        data.add(entry);
//    }
    
    void fillTableViewA(){
        fillTableData(agentA, dataA);
//        tableViewElement entry = new tableViewElement("peow", "dasdsa");
//        dataA.add(entry);
        Callback<TableColumn, TableCell> editableFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                System.out.println("fdfdfd");
                return new EditableTableCell();
            }
        };
        
       
        tableView_A.setEditable(true);
        tableView_A.setItems(dataA);
        RuleIDA.setCellValueFactory(new PropertyValueFactory<>("ruleID"));
        RuleA.setCellValueFactory(new PropertyValueFactory<>("rule"));

    
    }
    void fillTableData(RuleEngine KB,ObservableList<tableViewElement> data){
        
        HashMap <Integer,HashMap<String,HashMap<String,Boolean>>>rules;
        HashMap <Integer,HashMap<String,HashMap<String,Boolean>>>preferences;
        HashMap <Integer,HashMap<String,Boolean>> facts;
        
        int NumberOfInActiveRules=0;
        
        rules= KB.return_rules();
        preferences= KB.return_preference();
        facts= KB.return_facts();
        
        //count active rules 
        for (int i: rules.keySet()){
            for (String str :rules.get(i).keySet()){
                data.add(new tableViewElement(Integer.toString(i), str));
            }
        }
        for (int i: preferences.keySet()){
            for (String str :preferences.get(i).keySet()){
                data.add(new tableViewElement(Integer.toString(i), str));
            }
        }        
        for (int i : facts.keySet()){
            for (String str : facts.get(i).keySet()){
                data.add(new tableViewElement(Integer.toString(i), str));
            }
        }
    
    }
    void initiAgentA(String Path) throws IOException{
        agentA= new RuleEngine();
        agentA.readKB(Path);
        conflict_resolution CR = new conflict_resolution(agentA);
    
    }
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException {
        
        String PathKBa = "/home/jmoschon/Desktop/praktikh/test";
        initiAgentA(PathKBa);
        fillTableViewA();
//        fillTableViewA();
        
    }


   
}
