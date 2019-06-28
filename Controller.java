package sample;

import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import javafx.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.ProcessBuilder;

public class Controller {
    @FXML
    private TextArea areaText;
    private EditorModel model;
    private TextFile currentTextFile;

    public Controller(EditorModel model){
        this.model = model;
    }

    @FXML
    private void onSave(){
        TextFile textFile = new TextFile(currentTextFile.getFile(), Arrays.asList(areaText.getText().split("\n")) );
        model.save(textFile);
    }

    @FXML
    private void onOpen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
             IOResult<TextFile> io = model.load(file.toPath());
            if(io.isOk() && io.hasData()){
                currentTextFile = io.getData();
                areaText.clear();
                currentTextFile.getContent().forEach(line -> areaText.appendText(line + "\n"));
            }else {
                System.out.println("Failed!");
            }
        }
    }

    @FXML
    private void onExit(){
            model.close();
    }


    @FXML
    private void onAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("About");
        alert.setContentText("Cqq interpreter v1.0 , a C minus minus interpreter written in C plus plus!");
        alert.show();
    }

    @FXML
    public void onPressBuildAndRun(ActionEvent e){
        TextFile textFile = new TextFile(currentTextFile.getFile(), Arrays.asList(areaText.getText().split("\n")) );
        model.save(textFile);
        ArrayList<String> toks;
        Lexer lexer = new Lexer();
        toks =  lexer.lex(areaText.getText());
        Parser parser = new Parser(toks);
        parser.parse();


    }


}
