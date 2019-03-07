package controllers;

import informWindows.InformWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import object.User;
import utilits.Util_Sites;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WindowStartController {


@FXML
    private TextField textLogin;
@FXML
    private TextField textPassword;
    private User user = null;
    private Window thisWindow;
    private MyController myController;
    private   FXMLLoader loaderSample = new FXMLLoader();
    private  FXMLLoader loaderRegistr = new FXMLLoader();
    private Stage stageRegistr;
    private Parent parentRegist;
    private   Stage stageSample;
    private Parent parentSample;
    private WindowRegistrController registrController;
    private int ADMIN = 1;

    public void comeAction(ActionEvent actionEvent) {
        initWindow(actionEvent);
        if(Util_Sites.getInstance().checkEmptyTextField(textLogin)&&Util_Sites.getInstance().checkEmptyTextField(textPassword)){

            String login = Util_Sites.getInstance().stringInMD5(textLogin.getText());
            String password =  Util_Sites.getInstance().stringInMD5(textPassword.getText());
            try {
                user = Util_Sites.getInstance().checkUser(login,password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(user != null){
                startChildWindow(user);
            }else{
                InformWindow informWindow = new InformWindow();
                informWindow.informWindow("","Пользователь с такими данными не существует");
            }
        }

    }



    @FXML
    private void initialize(){
       initChildWindow();
    }
    private void startChildWindow(User user){
        if(user.getRole_id() == ADMIN){
              startAdminWindow();
        }
    }

    private void initChildWindow(){
        try {
            loaderSample.setLocation(getClass().getResource("../sample/sample.fxml"));
            parentSample = loaderSample.load();
            myController = loaderSample.getController();
            loaderRegistr.setLocation(getClass().getResource("../sample/windowRegistr.fxml"));
            parentRegist = loaderRegistr.load();
            registrController = loaderRegistr.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAdminWindow(){
        myController.setUser(user);
        if(stageSample == null){
            stageSample = new Stage();
            stageSample.setTitle("Окно администрирования");
            stageSample.setMinHeight(400);
            stageSample.setMinWidth(650);
            stageSample.setScene(new Scene(parentSample));

        }
        myController.initWindow();
        thisWindow.hide();
        stageSample.show();
    }

    private void initWindow(ActionEvent event){
        if(thisWindow == null){
            thisWindow = ((Node)event.getSource()).getScene().getWindow();
        }
    }

    private void startRegistrWindow(){
        registrController.setUser(user);
        if(stageRegistr == null){
            stageRegistr = new Stage();
            stageRegistr.setTitle("Окно администрирования");
            stageRegistr.setMinHeight(400);
            stageRegistr.setMinWidth(650);
            stageRegistr.setScene(new Scene(parentRegist));

        }
        registrController.initWindow();
      //  thisWindow.hide();
        stageRegistr.show();
    }


    public void registrAction(ActionEvent actionEvent) {
        initWindow(actionEvent);
        startRegistrWindow();
    }
}
