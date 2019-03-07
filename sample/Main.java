package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception{
      //  Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("windowStart.fxml"));
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(new Scene(root, 650, 400));
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(650);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
