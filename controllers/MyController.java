package controllers;

import informWindows.InformWindow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import object.Sites;
import object.User;
import obgektDB.SitesDB;
import obgektDB.UserDB;
import utilits.Util_Sites;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MyController {


    private FXMLLoader fxmlLoader = new FXMLLoader();
    private FXMLLoader loaderSitesDataAdmin = new FXMLLoader();
    private FXMLLoader loaderAddUser = new FXMLLoader();


    private Parent parentDataAdmin;
    private Stage stageDataAdmin;

    private Parent parentAddUser;
    private Stage  stageAddUser;


    private Parent parentAddSites;
    private Stage stageAddSites;


    private SitesDataController sitesDataController;
    private AddSites_Controller controllerAddSites;
    private AddUser_Controller controllerAddUser;


    private ObservableList<Sites> listFreeSites;
    private ObservableList<Sites> listBusySites;
    private HashMap<String,User> listUser;



    private User user;//TODO передавать user из окна входа после проверки логина и пароля
    private Window thisWindow;
    private Stage thisStage;
// ---------------------------------------------------------


    @FXML
    private   ComboBox<Sites> freeSites;
    @FXML
    private   ComboBox<Sites> busySites;
    @FXML
    private   ComboBox<String> operations;
    @FXML
    private TextField searchSites;
    @FXML
    private   Button btnSearch;



    //------------------------------------------------------------
    @FXML
    private void initialize() {}


    public void  initWindow(){
        operationsIni();
        initListSites();
        initChildWindow();
        listUser = UserDB.getInstance().getAllUser(user.getCollection_id());
    }


    public void freeSitesAction(ActionEvent actionEvent) {
        initWindow(actionEvent);
        if(freeSites.getValue() != null) {
            Sites sites = freeSites.getValue();
            showWindowDataAdmin(sites, true);

        }
        Platform.runLater(() -> freeSites.getSelectionModel().clearSelection());
    }


    public void busySitesAction(ActionEvent actionEvent) {
        initWindow(actionEvent);
        if(busySites.getValue()!= null) {
            Sites sites = busySites.getValue();
            showWindowDataAdmin(sites, false);

        }
        Platform.runLater(() -> busySites.getSelectionModel().clearSelection());// очищаем выбор ComboBox

    }


    public void searchAction(ActionEvent actionEvent) {

        initWindow(actionEvent);
        if(Util_Sites.getInstance().checkEmptyTextField(searchSites)){

            try {
                long nomer = Integer.parseInt(searchSites.getText());
                if(SitesDB.getInstance().chekSitesByNumber(nomer , user.getCollection_id())){
                    Sites sites = SitesDB.getInstance().createObgect(nomer,user.getCollection_id());
                    searchSites.clear();
                    if(sites.isBusy()) {
                        showWindowDataAdmin(sites,true);
                    }
                    else { showWindowDataAdmin(sites,false);}
                }else {
                    InformWindow informWindow = new InformWindow();
                    informWindow.informWindow("Ошибка данных","Участка с таким номером не существует");
                }
            }
            catch (NumberFormatException ex){
                InformWindow informWindow = new InformWindow();
                informWindow.informWindow("Ошибка данных","Введено не корректное значение");
            }
            catch (SQLException ex){
                InformWindow informWindow = new InformWindow();
                informWindow.errorWindow("Ошибка данных","Проверте соединение");
            }

        }
        else {
            InformWindow informWindow = new InformWindow();
            informWindow.informWindow("Ошибка данных","Введите данные");
        }

    }

    public void operationsAction(ActionEvent actionEvent) {
        initWindow(actionEvent);
        String val = operations.getValue();
        if(val!=null) {
            switch (val) {
                case "Добавить участок": {
                    controllerAddSites.setList(listFreeSites);
                    showWindowAddSites();
                    break;
                }
                case "Редактировать участок": {

                    break;
                }
                case "Добавить пользователя": {
                    controllerAddUser.setEdit(false);
                    controllerAddUser.setParentController(this);
                    controllerAddUser.initWIndow();
                    showWindowAddUser();

                    break;
                }
                case "Удалить пользователя": {

                    break;
                }
                case "Редактировать пользователя": {
                    controllerAddUser.setEdit(true);
                    controllerAddUser.setParentController(this);
                    controllerAddUser.initWIndow();
                    showWindowAddUser();
                }
            }
        }

        Platform.runLater(() -> operations.getSelectionModel().clearSelection());
    }

    private void operationsIni() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Добавить участок");
        list.add("Редактировать участок");
        list.add("Добавить пользователя");
        list.add("Удалить пользователя");
        list.add("Редактировать пользователя");
        ObservableList<String> stringObservableList = FXCollections.observableList(list);
        operations.setItems(stringObservableList);
    }

    private void showWindowAddSites() {
        if (stageAddSites == null) {
            stageAddSites = new Stage();
            stageAddSites.setMinWidth(600);
            stageAddSites.setMinHeight(400);
            stageAddSites.setTitle("Добавление Участка");
           // stageAddSites.setResizable(false);
            stageAddSites.initModality(Modality.WINDOW_MODAL);
            stageAddSites.setScene(new Scene(parentAddSites));
            stageAddSites.initOwner(thisWindow);

        }
        controllerAddSites.setUser(user);
        stageAddSites.show();

    }

    private void showWindowAddUser(boolean flag){
        if(stageAddUser == null){
            stageAddUser = new Stage();
            stageAddUser.setMinWidth(600);
            stageAddUser.setMinHeight(400);
            if(flag){
                stageAddUser.setTitle("Добавление пользователя");
            }
            else {
                stageAddUser.setTitle("Добавление пользователя");
            }


        }
    }



    private void showWindowDataAdmin(Sites sites, boolean status){
        if(stageDataAdmin == null) {
            stageDataAdmin = new Stage();
            stageDataAdmin.setMinWidth(600);
            stageDataAdmin.setMinHeight(400);
            stageDataAdmin.setTitle("Данные карточки");
          //  stageDataAdmin.setResizable(false);
            stageDataAdmin.initModality(Modality.WINDOW_MODAL);
            stageDataAdmin.setScene(new Scene(parentDataAdmin));
            stageDataAdmin.initOwner(thisWindow);
        }
        try {
            if(sites.getReservation()!= null) {
                Util_Sites.getInstance().chekDate(sites, status);
            }
            else {sites.setTimeStatus("Данных о обработке участка нет");}
            sitesDataController.setSites(sites);
            sitesDataController.setParentController(this);


        }catch (NullPointerException ex){

        }


          sitesDataController.setListUser(listUser);


          stageDataAdmin.show();
          sitesDataController.initDataWindow(sites);
    }

    private void showWindowAddUser(){
        if(stageAddUser == null){
            stageAddUser = new Stage();
            stageAddUser.setMinWidth(600);
            stageAddUser.setMinHeight(400);
            stageAddUser.setTitle("Добавление пользователя");
            stageAddUser.initModality(Modality.WINDOW_MODAL);
            stageAddUser.setScene(new Scene(parentAddUser));
            stageAddUser.initOwner(thisWindow);
        }

        stageAddUser.show();


    }

    public void initListSites() {
        ArrayList<Sites> list = new ArrayList<>();
        ArrayList<Sites> listFree = new ArrayList<>();
        ArrayList<Sites> listBusy = new ArrayList<>();
        list = SitesDB.getInstance().getAllObgectByBuff(user.getCollection_id());
        for (Sites sites:list){
            if(!sites.isBusy()){
                listBusy.add(sites);
            }
            else {listFree.add(sites);}
        }

        listFreeSites = FXCollections.observableArrayList(listFree);
        listBusySites = FXCollections.observableList(listBusy);
        freeSites.setItems(listFreeSites);
        busySites.setItems(listBusySites);
    }

    private void initChildWindow() {
        try {
            fxmlLoader.setLocation(getClass().getResource("../sample/addSites.fxml"));
            parentAddSites = fxmlLoader.load();
            controllerAddSites = fxmlLoader.getController();
            controllerAddSites.setUser(user);
            controllerAddSites.setParentWindow(thisWindow);

            loaderSitesDataAdmin.setLocation(getClass().getResource("../sample/sitesData.fxml"));
            parentDataAdmin = loaderSitesDataAdmin.load();
            sitesDataController = loaderSitesDataAdmin.getController();
            sitesDataController.setParentWindow(thisWindow);

            loaderAddUser.setLocation(getClass().getResource("../sample/addUser.fxml"));
            parentAddUser = loaderAddUser.load();
            controllerAddUser = loaderAddUser.getController();
            controllerAddUser.setUser(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initWindow(ActionEvent actionEvent) {
        if (thisWindow == null) {
            thisWindow = ((Node) actionEvent.getSource()).getScene().getWindow();
        }
        if (thisStage == null) {
            thisStage = (Stage) thisWindow;
        }
    }

    //--------------------------------------------------------------------------------------
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ObservableList<Sites> getList() {
        return listFreeSites;
    }

    public HashMap<String, User> getListUser() {
        return listUser;
    }

    public void setListFreeSites(ObservableList<Sites> listFreeSites) {
        this.listFreeSites = listFreeSites;
    }

    public void setListBusySites(ObservableList<Sites> listBusySites) {
        this.listBusySites = listBusySites;
    }

    public ObservableList<Sites> getListFreeSites() {
        return listFreeSites;
    }

    public ObservableList<Sites> getListBusySites() {
        return listBusySites;
    }
}


