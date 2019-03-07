package controllers;

import informWindows.InformWindow;
import javafx.application.Platform;
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


public class WindowRegistrController {
    @FXML
    private TextField textNumber;
    @FXML
    private TextField textName;
    @FXML
    private TextField textLastName;
    private static final int FORMATERROR = -1;
    private User user;
    private WindowLoginController windowLoginController ;
    private FXMLLoader loaderWindowLogin = new FXMLLoader();
    private Parent parentWindowLogin;
    private Stage stageWindowLogin;
    private Window thisWindow;

   public void initWindow(){
       initChildWindow();
   }


    public void setUser(User user) {
        this.user = user;
    }

    public void continueAction(ActionEvent actionEvent) {
        initWindow(actionEvent);
        boolean number = Util_Sites.getInstance().checkEmptyTextField(textNumber);
        boolean name = Util_Sites.getInstance().checkEmptyTextField(textName);
        boolean lastname = Util_Sites.getInstance().checkEmptyTextField(textLastName);
        if(number&&name&&lastname){
           String nameUser = textName.getText();
           String lastNameUser = textLastName.getText();
           int numberCollection = transformStringByInt(textNumber.getText());
           if(numberCollection>FORMATERROR){
               try {
                   user = Util_Sites.getInstance().createUser(nameUser,lastNameUser,numberCollection);
                   if (user!=null){
                       showChildWindow();
                      // thisWindow.hide();
                   }else {
                       InformWindow informWindow = new InformWindow();
                       informWindow.informWindow("","Вас нет в списке вероятных пользователей");
                       Platform.runLater(()->textNumber.clear());
                       Platform.runLater(()->textName.clear());
                       Platform.runLater(()->textLastName.clear());
                   }
               } catch (SQLException e) {
                   // проверить соединение
                   InformWindow informWindow = new InformWindow();
                   informWindow.errorWindow("","Проверить соединение");
               }
           }else {
               // номер не должен содержать букв
               InformWindow informWindow = new InformWindow();
               informWindow.errorWindow("","Номер не должен содержать букв");
           }
        }else{
            InformWindow informWindow = new InformWindow();
            informWindow.errorWindow("","Заполните все поля");
        }
    }
    private int transformStringByInt(String text){
        int result = 0;
        try{
            result = Integer.parseInt(text);
        }catch (NumberFormatException ex){
            result = FORMATERROR;
        }

       return result;
    }
    private void initChildWindow(){

        try {
            loaderWindowLogin.setLocation(getClass().getResource("../sample/windowLogin.fxml"));
            parentWindowLogin = loaderWindowLogin.load();
            windowLoginController = loaderWindowLogin.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void showChildWindow(){
        if(stageWindowLogin ==null){
            stageWindowLogin = new Stage();
            stageWindowLogin.setMinWidth(600);
            stageWindowLogin.setMinHeight(400);
            stageWindowLogin.setScene(new Scene(parentWindowLogin));
            stageWindowLogin.initOwner(thisWindow);
        }
        windowLoginController.setUser(user);
        stageWindowLogin.show();

    }
    private void initWindow(ActionEvent actionEvent) {
        if (thisWindow == null) {
            thisWindow = ((Node) actionEvent.getSource()).getScene().getWindow();
        }

    }
}
