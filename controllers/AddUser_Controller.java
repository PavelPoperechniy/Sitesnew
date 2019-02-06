package controllers;

import informWindows.InformWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import object.Roles;
import object.User;
import utilits.Util_Sites;

public class AddUser_Controller {
    private User user;
    boolean edit;

    @FXML
    private TextField text_fname;
    @FXML
    private TextField text_lastName;
    @FXML
    private ComboBox<Roles> combo_role;
    @FXML
    private Button btn_Act;

    public AddUser_Controller(User user, boolean edit) {
        this.user = user;
        this.edit = edit;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public AddUser_Controller() {
    }

    public void btnAction(ActionEvent actionEvent) {
    }

    private void editUser(User user){
        if(user != null){
            if(Util_Sites.getInstance().chekTextFaild(text_fname)&&Util_Sites.getInstance().chekTextFaild(text_lastName)){
                if(combo_role.getValue() != null){
                    user.setRole_id(combo_role.getValue().getId());
                }
                user.setFerst_name(text_fname.getText());
                user.setLast_name(text_lastName.getText());

            }
            else {
                InformWindow informWindow = new InformWindow();
                informWindow.informWindow("Ошибка данных","Все поля Имя и Фамилия не могут быть пустыми");
            }
        }
    }
}

