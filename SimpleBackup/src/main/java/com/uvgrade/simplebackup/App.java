package com.uvgrade.simplebackup;

import org.json.simple.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.parser.JSONParser;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        
        ConfigWindow page= new ConfigWindow();

    }

    public static void main(String[] args) {
        launch();
    }

}

class ConfigWindow{
    final int btnSize=60;
    private File jsonDir;
    private ObservableList<String> srcList;
    private Stage stage;
    
    ConfigWindow(){
        //inits
        stage= new Stage();
        VBox app = new VBox();
        app.setPadding(new Insets(5,25,5,25));
        
        GridPane grid= new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);

        grid.setHgap(10);
        grid.setVgap(10);
        
        Text scenetitle = new Text("Simple Backup Config Editor");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        VBox titleRow= new VBox(scenetitle);
        titleRow.setPadding(new Insets(10,20,0,15));
        

        Label destTitle = new Label("Destination");
        grid.add(destTitle,0,1);

        TextField destField = new TextField();
        grid.add(destField, 0, 2);
        
        Button browseDest = new Button("Browse");
        browseDest.setPrefWidth(btnSize);
        grid.add(browseDest, 1, 2);
        browseDest.setOnAction(value -> {
            DirectoryChooser destChooser = new DirectoryChooser();
            File destDir = destChooser.showDialog(stage);
            if(destDir!=null){
                destField.setText(destDir.getAbsoluteFile().getAbsolutePath());
            }
            });

        Label pw = new Label("Sources:");
        grid.add(pw, 0, 3);

        ListView srcView = new ListView();
        grid.add(srcView, 0, 4);
        
        VBox listCtrl= new VBox();
        Button srcAdd = new Button("Browse");
        srcAdd.setPrefWidth(btnSize);
        Button srcDel = new Button("Delete");
        srcDel.setPrefWidth(btnSize);
        //Button srcUp = new Button("Up");
        //srcUp.setPrefWidth(btnSize);
        //Button srcDn = new Button("Down");
        //srcDn.setPrefWidth(btnSize);
        listCtrl.getChildren().addAll(srcAdd,srcDel);
        listCtrl.setSpacing(10);
        grid.add(listCtrl,1,4);
        
        srcAdd.setOnAction(value -> { 
            DirectoryChooser srcChooser = new DirectoryChooser();
            File srcDir = srcChooser.showDialog(stage);
            if(srcDir!=null){
                addSrc(srcView,srcDir);
            }
            
        });
        srcDel.setOnAction(value -> { 
            srcView.getItems().remove(srcView.getSelectionModel().getSelectedIndex());
        });
        
        
        TextField srcField = new TextField();
        grid.add(srcField, 0, 5);
        Button addSrc = new Button("Add");
        addSrc.setPrefWidth(btnSize);
        grid.add(addSrc, 1, 5);
        addSrc.setOnAction(value -> { 
            addSrc(srcView,new File(srcField.getText()));
        });
        
        HBox menu = new HBox();
        menu.setPadding(new Insets(10,20,10,20));
        Button newC = new Button("New");
        Button openC= new Button("Open");
        Button saveC= new Button("Save");
        menu.getChildren().addAll(newC,openC,saveC);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(30);
        grid.add(menu,0,6);
        
        newC.setOnAction(value ->  {
            ConfigWindow test= new ConfigWindow();});
        saveC.setOnAction(value -> { saveJson(destField,srcView);});
        openC.setOnAction(value -> { loadJson(destField,srcView);});
        
        Label info= new Label("v1.0 github.com");
        app.getChildren().addAll(titleRow,grid,info);
        
        var scene = new Scene(app, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Backup Config Editor");
        stage.show();
    }
    void loadJson(TextField dest, ListView srcList){
        FileChooser openJ = new FileChooser();
        openJ.setTitle("Open Config File");
        File f = openJ.showOpenDialog(stage);
        if(f!=null){
            JSONParser jsonParser= new JSONParser();
            try (FileReader reader = new FileReader(f))
            {
                dest.setText("");
                srcList.getItems().clear();
                //Read JSON file
                JSONObject obj = (JSONObject) jsonParser.parse(reader);
                dest.setText((String)obj.get("destination"));
                JSONArray list= (JSONArray) obj.get("sources");
                for(var src : list){
                    srcList.getItems().add(src);
                }
                jsonDir=f;
            } catch(Exception e){
                msg("We had trouble reading this config file");
            }
        }
        
    }
    void saveJson(TextField dest, ListView srcList){
        if(jsonDir==null){
            FileChooser saveWin = new FileChooser();
            saveWin.setTitle("Save Config File");
            saveWin.getExtensionFilters().add(new FileChooser.ExtensionFilter("json","*.json"));
            jsonDir= saveWin.showSaveDialog(stage);
        }
        if(jsonDir!=null){
            JSONObject obj= new JSONObject();
            obj.put("destination",dest.getText());
            JSONArray sources= new JSONArray();
            List<String> tempSrcs = srcList.getItems(); // Only here to clarify the code
            for(var item : tempSrcs){
                System.out.println(tempSrcs);
                sources.add(item);
            }
            obj.put("sources",sources);
            try(FileWriter f= new FileWriter(jsonDir)){
                f.write(obj.toJSONString());
            }catch(Exception e){
                msg("Ran into an issue saving, check data");
            }
        }
    }
    void addSrc(ListView srcView,File name){
        
            ArrayList<String> sampleSrc= new ArrayList<String>();
            List<String> tempSrcs = srcView.getItems(); // Only here to clarify the code
            
            //check for duplicate directory names
            if(tempSrcs.contains(name.getAbsoluteFile())){
                msg("Directory already exists in sources");
            }else{
                Boolean duplicate=false;
                for(var dir: tempSrcs){
                    File temp = new File(dir);
                    if(temp.getName().equals(name.getName())){
                        duplicate=true;
                    }
                }
                if(duplicate){
                    msg("Please choose a folder with a different name (duplicate name)"); 
                }else{
                    srcView.getItems().add(name.getAbsoluteFile().toString());
                }
                
            }
    }
    void msg(String msg){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}