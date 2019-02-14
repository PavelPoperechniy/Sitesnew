package controllers;

import informWindows.InformWindow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import obgektDB.SitesDB;
import object.Sites;
import object.User;
import sun.util.calendar.Gregorian;
import utilits.Util_Sites;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedactSitesController {

    private Sites sites;
    private static long MILLISSECOND = 1000;
    private Window thisWindow;
    private HashMap<String,User> listUser;
    private SitesDataController parentController;
    private String number;
    private String address;
    private ArrayList<String> listComboBox = new ArrayList<>() ;
    @FXML
    private TextField text_number;
    @FXML
    private TextField text_adress;
    @FXML
    private DatePicker pickerDate_issue;
    @FXML
    private DatePicker pickerDate_delivery;
    @FXML
    private ComboBox<String>comboUserList;


    public void okAction(ActionEvent actionEvent) {
        listComboBox.clear();
        String value = comboUserList.getValue();
        if (value != null) {
            for (Map.Entry<String, User> item : listUser.entrySet()) {
                if (item.getKey().toLowerCase().contains(value.toLowerCase())) {
                    if (!listComboBox.contains(item.getKey())) {
                        listComboBox.add(item.getKey());

                    }
                }
            }

        }
        comboUserList.getItems().clear();
        comboUserList.getItems().addAll(listComboBox);
        if(!comboUserList.getItems().isEmpty()) {
            comboUserList.setValue(comboUserList.getItems().get(0));
        }

    }

    public void initRedactWindow(){
        comboUserList.setEditable(true);
        number = String.valueOf(sites.getNumber());
        text_number.setText(number);
        address = sites.getAdress();
        text_adress.setText(address);
        listUser = parentController.getListUser();
        if(sites.getReservation().getUser()!=null) {
            comboUserList.setValue(sites.getReservation().getUser().toString());
        }
        else {
            comboUserList.setValue(null);
            comboUserList.setPromptText("Введите данные для поиска");
        }
        long date_delivery = sites.getReservation().getDate_delivery();
        long date_issue = sites.getReservation().getDate_issue();
        if(date_delivery>0){
            pickerDate_delivery.setValue(returnLocalDate(date_delivery));
        }
        if(date_issue>0){
            pickerDate_issue.setValue(returnLocalDate(date_issue));
        }


    }

    public void saveAction(ActionEvent actionEvent) {
        if(thisWindow == null) {
            thisWindow = ((Node) actionEvent.getSource()).getScene().getWindow();
        }
        if (!text_number.getText().equals(number)) {
            try {
                long number = Long.parseLong(text_number.getText());
                if (!SitesDB.getInstance().chekSitesByNumber(number, parentController.getUser().getCollection_id())) {
                    sites.setNumber((int) number);
                } else {
                    InformWindow informWindow = new InformWindow();
                    informWindow.informWindow("Ошибка данных", "Участок с таким номером существует");
                    text_number.setText(String.valueOf(sites.getNumber()));
                    return;
                }

            } catch (NumberFormatException ex) {
                InformWindow informWindow = new InformWindow();
                informWindow.errorWindow("Ошибка данных", "Введено неверное значение");
                return;
            } catch (SQLException ex) {
                InformWindow informWindow = new InformWindow();
                informWindow.errorWindow("Ошибка ", "Введено неверное значение");
                return;
            }

        }
        if (!text_adress.getText().equals(address)) {
            sites.setAdress(text_adress.getText());
        }
        if (pickerDate_issue.getValue() != null) {
            long time = getMillis(pickerDate_issue.getValue());
            sites.getReservation().setDate_issue(time);
        }
        if (pickerDate_delivery.getValue() != null) {
            long time = getMillis(pickerDate_delivery.getValue());
            sites.getReservation().setDate_delivery(time);
        }
        if(sites.getReservation().getDate_delivery()==0){
            sites.setBusy(false);
        }else {
            sites.setBusy(false);
        }
        User redactUser = getUserByListUser(listUser,comboUserList.getValue());
        if (redactUser!=null){
            sites.getReservation().setUser(redactUser);
        }
        else {
            InformWindow informWindow = new InformWindow();
            informWindow.errorWindow("Ошибка ", "Выберите возвещателя");
            return;
        }
        try {
            if ( Util_Sites.getInstance().updateObgectDB(sites)&&Util_Sites.getInstance().updateObgectDB(sites.getReservation())){
                InformWindow informWindow = new InformWindow();
                informWindow.informWindow("Статус операции ", "Данные успешно обновлены");
                MyController controller = parentController.getParentController();
                controller.initListSites();
                parentController.initLabel();
                thisWindow.hide();

            }else {
                InformWindow informWindow = new InformWindow();
                informWindow.errorWindow("Ошибка ", "Ошибка связи проверте соединение");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    private LocalDate returnLocalDate(long time){

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time*MILLISSECOND);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int days = calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate localDate = LocalDate.of(year,month+1,days);
        return localDate;
    }

    private long getMillis(LocalDate date){
        long value;
        int year = date.getYear();
        int month = date.getMonthValue()+1;
        int day = date.getDayOfMonth();
        GregorianCalendar calendar = new GregorianCalendar(year,month,day);
        value = calendar.getTimeInMillis()/MILLISSECOND;

        return value;
    }

    private User getUserByListUser(HashMap<String,User> listUser,String userName){
        User user = null;
        user = listUser.get(userName);
        return user;
    }

    private void initDatePicker(DatePicker datePicker,LocalDate time){
        datePicker.setValue(time);
    }
    public void setParentController(SitesDataController parentController) {
        this.parentController = parentController;
    }
    public void setSites(Sites sites) {
        this.sites = sites;
    }
    public void setListUser(HashMap<String, User> listUser) {
        this.listUser = listUser;
    }



}
