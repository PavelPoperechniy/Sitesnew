package controllers;

import informWindows.InformWindow;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import obgektDB.RolesDB;
import object.Roles;
import object.User;
import utilits.Util_Sites;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddUser_Controller {
    private User user;
    private User editUser;
    private boolean edit;
    private ArrayList<Roles> lisrRoles;
    private HashMap<String, User> listUser;
    private MyController parentController;
    private ArrayList<String> list_comboUser = new ArrayList<>();

    public void setParentController(MyController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private TextField text_fname;
    @FXML
    private TextField text_lastName;
    @FXML
    private ComboBox<Roles> combo_role;
    @FXML
    private ComboBox<String> combo_listUser;
    @FXML
    private Button btn_Act;
    @FXML
    private Button btn_ok;
    @FXML
    private Label labelСhoiceUser;

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
        if (edit) {
            editUser();
        } else {
            addUser();
        }
    }

    private void editUser() {

        if (Util_Sites.getInstance().checkEmptyTextField(text_fname) && Util_Sites.getInstance().checkEmptyTextField(text_lastName)) {
            if (combo_role.getValue() != null) {
                editUser.setRole_id(combo_role.getValue().getId());
            }
            editUser.setFerst_name(text_fname.getText());
            editUser.setLast_name(text_lastName.getText());
            try {
               // if (!Util_Sites.getInstance().checkListByKey(listUser, editUser)) {
                    if (Util_Sites.getInstance().updateObgectDB(editUser)) {
                        InformWindow informWindow = new InformWindow();
                        informWindow.informWindow("Статус операции", "Данные успешно изменены");
                        Util_Sites.getInstance().updateListUser(listUser, editUser);
                        Platform.runLater(() -> text_fname.clear());
                        Platform.runLater(() -> text_lastName.clear());
                        Platform.runLater(() -> combo_listUser.getSelectionModel().clearSelection());
                        Platform.runLater(()->combo_listUser.getItems().clear());
                        Platform.runLater(() -> combo_role.getSelectionModel().clearSelection());
                    } else {
                        InformWindow informWindow = new InformWindow();
                        informWindow.informWindow("Статус операции", "Данные не обновлены проверте соединение");

                    }
//                } else {
//                    InformWindow informWindow = new InformWindow();
//                    informWindow.informWindow("Ошибка данных", "Пользователь с такими данными уже существует \n Измените имя как в примере \n Ваня - Иван\n Витя - Виктор");
//                    Platform.runLater(() -> text_fname.clear());
//                    Platform.runLater(() -> text_lastName.clear());
//                }

            } catch (SQLException e) {
                InformWindow informWindow = new InformWindow();
                informWindow.errorWindow("Ошибка соединения", "Проверте соединение");
            }

        } else {
            InformWindow informWindow = new InformWindow();
            informWindow.informWindow("Ошибка данных", "Поля Имя и Фамилия не могут быть пустыми");
        }

    }

    private void addUser() {
        User newUser = new User();

        if (Util_Sites.getInstance().checkEmptyTextField(text_fname) && Util_Sites.getInstance().checkEmptyTextField(text_lastName)) {
            if (combo_role.getValue() != null) {
                newUser.setRole_id(combo_role.getValue().getId());
            }
            newUser.setFerst_name(text_fname.getText());
            newUser.setLast_name(text_lastName.getText());
            newUser.setCollection_id(user.getCollection_id());
            if (!Util_Sites.getInstance().checkListByKey(listUser, newUser)) {
                if (Util_Sites.getInstance().writeObgectFromDb(newUser)) {
                    InformWindow informWindow = new InformWindow();
                    informWindow.informWindow("Статус операции", "Пользователь " + newUser.getFerst_name() + " " + newUser.getLast_name() + " Успешно добавлен");
                    listUser.put(newUser.toString(),newUser);
                    Platform.runLater(() -> text_lastName.clear());
                    Platform.runLater(() -> text_fname.clear());

                }


            } else {
                InformWindow informWindow = new InformWindow();
                informWindow.informWindow("Ошибка данных", "Пользователь с такими данными уже существует \n Измените имя как в примере \n Ваня - Иван\n Витя - Виктор");
                Platform.runLater(() -> text_fname.clear());
                Platform.runLater(() -> text_lastName.clear());
            }
        } else {
            InformWindow informWindow = new InformWindow();
            informWindow.informWindow("Ошибка данных", "Поля Имя и Фамилия не могут быть пустыми");

        }

    }

    public void initWIndow() {
        listUser = parentController.getListUser();
        if (lisrRoles == null) {
            try {
                lisrRoles = RolesDB.getInstance().getAllRoles();
                combo_role.getItems().addAll(lisrRoles);
            } catch (SQLException e) {
                InformWindow informWindow = new InformWindow();
                informWindow.errorWindow("Ошибка соединения", "Проверте соединение");
            }


        }

        initEditWindowElement();
    }

    private void initEditWindowElement() {
        if (edit) {
            combo_listUser.setVisible(true);
            combo_listUser.setEditable(true);
            btn_ok.setVisible(true);
            labelСhoiceUser.setVisible(true);
        } else {
            combo_listUser.setVisible(false);
            btn_ok.setVisible(false);
            labelСhoiceUser.setVisible(false);
        }
    }

    public void okAktion(ActionEvent actionEvent) {
        list_comboUser.clear();

        String value = combo_listUser.getValue();
        if (value != null) {
            for (Map.Entry<String, User> item : listUser.entrySet()) {
                if (item.getKey().toLowerCase().contains(value.toLowerCase())) {
                    if (!list_comboUser.contains(item.getKey())) {
                        list_comboUser.add(item.getKey());

                    }
                }
            }
            combo_listUser.getItems().clear();
            combo_listUser.getItems().addAll(list_comboUser);
            combo_listUser.setValue(list_comboUser.get(0));
            editUser = listUser.get(combo_listUser.getValue());
            text_fname.setText(editUser.getFerst_name());
            text_lastName.setText(editUser.getLast_name());

        }
    }
}

