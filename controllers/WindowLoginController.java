package controllers;

import informWindows.InformWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import object.User;
import utilits.Util_Sites;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WindowLoginController {
    @FXML
    private TextField textLogin;
    @FXML
    private TextField textPassword;
    @FXML
    private TextField textSecondPassword;
    private User user;
    private Window thisWindow;

    public void setUser(User user) {
        this.user = user;
    }

    public void checkAction(ActionEvent actionEvent) {
        initWindow(actionEvent);
       boolean login = Util_Sites.getInstance().checkEmptyTextField(textLogin);
       boolean password = Util_Sites.getInstance().checkEmptyTextField(textPassword);
       boolean secondPassword = Util_Sites.getInstance().checkEmptyTextField(textSecondPassword);

       if(login&&password&&secondPassword){                                             // проверка на пустые поля
           if(textPassword.getText().equals(textSecondPassword.getText())){
               if(checkStringLogin(textLogin.getText())) {                              //  проверка на совпадение паролей
                   String loginUser = Util_Sites.getInstance().stringInMD5(textLogin.getText());
                   try {
                       if(!Util_Sites.getInstance().checkRepeatLogin(loginUser)){         // проверка н ауникальность логина
                           String passwordUser = Util_Sites.getInstance().stringInMD5(textPassword.getText());
                           user.setLogin(loginUser);
                           user.setPassword(passwordUser);

                           if(updateUser(user)){                                            // проверка на успех операции
                               InformWindow informWindow = new InformWindow();
                               informWindow.informWindow("","Новый пользователь зарегистрирован");
                               thisWindow.hide();
                           }else {
                               InformWindow informWindow = new InformWindow();
                               informWindow.informWindow("","Что то пошло не так");
                           }

                       }else {
                           InformWindow informWindow = new InformWindow();
                           informWindow.informWindow("","Login занят");
                       }
                   } catch (SQLException e) {
                       InformWindow informWindow = new InformWindow();
                       informWindow.errorWindow("Ошибка соединения","Проверте соединение1");
                   }

               }else{
                   InformWindow informWindow = new InformWindow();
                   informWindow.informWindow("","Логин должен задаваться в формате email адресса /n И содержать знак @");
               }

           }else {
               InformWindow informWindow = new InformWindow();
               informWindow.informWindow("","Пароли не совпадать");
           }
       }
       else{
           InformWindow informWindow = new InformWindow();
           informWindow.informWindow("","Все поля должны быть заполнены");
       }

    }

    private boolean checkStringLogin(String login){
        boolean flag = false;
        Pattern pattern = Pattern.compile("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$");
        Matcher matcher = pattern.matcher(login);
        flag = matcher.matches();
        return flag;
    }

    private boolean updateUser(User user){
        boolean update = false;
        try {
                update = Util_Sites.getInstance().updateLoginAndPassword(user);
        } catch (SQLException e) {
            e.printStackTrace();
            //нет связи
            InformWindow informWindow = new InformWindow();
            informWindow.informWindow("","Ошибка соединения. Проверте соединение");
        }
        return update;
    }
    private void initWindow(ActionEvent actionEvent) {
        if (thisWindow == null) {
            thisWindow = ((Node) actionEvent.getSource()).getScene().getWindow();
        }

    }
}
