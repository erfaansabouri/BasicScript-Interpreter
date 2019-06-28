package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        loader.setControllerFactory(t -> new Controller(new EditorModel()));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {

        launch(args);
        //BasicInterpreter basicInterpreter = new BasicInterpreter(args[0]);
        //basicInterpreter.interpret(args[0]);
    }
}
