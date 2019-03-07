package controllers;

import informWindows.InformWindow;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import object.Reservation;
import object.Sites;
import object.User;
import obgektDB.ReservationDb;
import utilits.Util_Sites;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class SitesDataController {


   private Sites sites;
    private Window parentWindow;
    private User user;
    private boolean mode;
    private boolean statusCombo;
    private boolean init;
    private HashMap<String, User> listUser;
    private ObservableList<String> listComboBox = FXCollections.observableArrayList();
    private MyController parentController;
    private long MICROSECOND = 1000;
    private int count = 0;
    private FXMLLoader loaderRedact = new FXMLLoader();
    private Parent parentRedact;
    private Stage  stageRedact;
    private RedactSitesController controllerRedact;
    private Window thisWindow;

    @FXML
    private Label labelNymber;
    @FXML
    private Label labeltimeStatus;
    @FXML
    private Label labelnameUser;
    @FXML
    private Button btn_Act;
    @FXML
    private Button btn_OK;
    @FXML
    private Button btn_Redact;
    @FXML
    private ImageView image;
    @FXML
    private ComboBox<String> seachByLastname;




    public void initDataWindow(Sites sites) {
      //  listUser = parentController.getListUser();
        if (sites.isBusy()) {
            mode = true;
        } else mode = false;
        initComboBox();
        init_btn_Act();
        initLabel();
      //  initImages();
        init = true;
        user = parentController.getUser();

    }

    public void okAction(ActionEvent actionEvent) {
        if (!listComboBox.isEmpty()) {
            listComboBox.clear();
        }
        String value = seachByLastname.getValue();
            if (value != null) {
                for (Map.Entry<String, User> item : listUser.entrySet()) {
                    if (item.getKey().toLowerCase().contains(value.toLowerCase())) {
                        if (!listComboBox.contains(item.getKey())) {
                            listComboBox.add(item.getKey());

                        }
                    }
                }

            }
            seachByLastname.getItems().clear();
            seachByLastname.getItems().addAll(listComboBox);
            seachByLastname.setValue(seachByLastname.getItems().get(0));



    }



    public void actAction(ActionEvent actionEvent) {
        boolean flag = false;
        if(sites.isBusy()) {
         flag = giveSites();
        }
        else returnSites();
        Node node = (Node)actionEvent.getSource();
        Window window = (Window)node.getScene().getWindow();
        listComboBox.clear();
        seachByLastname.getItems().clear();
        if(flag) {
            window.hide();
        }


    }


    public void redactAction(ActionEvent actionEvent) {
        if(thisWindow == null) {
            thisWindow = ((Node) actionEvent.getSource()).getScene().getWindow();
        }
        if(parentRedact == null){
            initChildWindow();
        }

        showRedactSitesWindow();
        controllerRedact.initRedactWindow();
        controllerRedact.setListUser(listUser);
    }

    public void comboAction(ActionEvent actionEvent) {


    }

    private void initComboBox() {
        if (mode) {
            seachByLastname.setEditable(true);
            seachByLastname.setVisible(true);

        } else seachByLastname.setVisible(false);
    }

    private void init_btn_Act() {
        if (mode) {
            btn_Act.setText("Выдать");
            btn_OK.setVisible(true);
        } else {
            btn_Act.setText("Сдать");
            btn_OK.setVisible(false);
        }
    }

    public User getUser() {
        return user;
    }

    public void initLabel() {
        labelNymber.setText(sites.toString());
        labeltimeStatus.setText(sites.getTimeStatus());
        String str = "";
        if (sites.getReservation() != null) {
            str = chekUserbyNull(sites);
        } else {
            str = "Данные о обработке участка отсутствуют";
        }
        labelnameUser.setText(str);
    }


    private void initImages(){
        try {
            FileInputStream stream = new FileInputStream("F://Foto_sites//foto1.png");
            image.setImage(new Image(stream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private String chekUserbyNull(Sites sites){
        String text;
        Reservation reservation = sites.getReservation();
        if(reservation.getUser()!=null){
            User user = reservation.getUser();
            if(sites.isBusy()) {
                text = "Участок сдал " + user.getLast_name() + " " + user.getFerst_name();
            }
            else text = "Участок обрабатывает " + user.getLast_name() + " " + user.getFerst_name();
        }else text = "Данных о возвещателе нет";
        return text;
    }

    private boolean giveSites(){
        boolean flag = false;
        if (listUser.containsKey(seachByLastname.getValue())) {
            Reservation reservation = new Reservation();
            reservation.setUser(listUser.get(seachByLastname.getValue()));
            reservation.setSites_id(sites.getId());
            long thisdate = new GregorianCalendar().getTimeInMillis() / MICROSECOND;
            reservation.setDate_issue(thisdate);
            reservation.setDate_delivery(0);
            ReservationDb.getInstance().insertObgect(reservation);
            sites.setReservation(reservation);
            sites.setBusy(false);
            parentController.getListFreeSites().removeAll(sites);
            Util_Sites.getInstance().chekDate(sites,false);
            parentController.getListBusySites().addAll(sites);
            flag = true;

        } else {
            InformWindow informWindow = new InformWindow();
            informWindow.informWindow("Ошибка ввода данных", "Выберете имя из списка");
        }
        return flag;
    }

    private void returnSites(){
        if(!sites.isBusy()) {
            long thisData = new GregorianCalendar().getTimeInMillis()/MICROSECOND;
            sites.getReservation().setDate_delivery(thisData);
            try {
                if (Util_Sites.getInstance().updateObgectDB(sites.getReservation())) {
                    parentController.getListBusySites().removeAll(sites);
                    sites.setBusy(true);
                    Util_Sites.getInstance().chekDate(sites, true);
                    parentController.getListFreeSites().addAll(sites);


                } else {
                    InformWindow informWindow = new InformWindow();
                    informWindow.informWindow("", "Операция не успешна");
                }
            } catch (SQLException e) {
                InformWindow informWindow = new InformWindow();
                informWindow.errorWindow("Ошибка соединения", "Проверте настройки сети");
            }
        }

    }
    private void initChildWindow()  {

        try {
            loaderRedact.setLocation(getClass().getResource("../sample/redactSites.fxml"));
            parentRedact = loaderRedact.load();
            controllerRedact = loaderRedact.getController();
            controllerRedact.setParentController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showRedactSitesWindow(){
        if (stageRedact == null){
            stageRedact = new Stage();
            stageRedact.setMinWidth(680);
            stageRedact.setMinHeight(570);
            stageRedact.setTitle("Редактирование участка");
            stageRedact.setScene(new Scene(parentRedact));
            stageRedact.initOwner(thisWindow);

        }
        controllerRedact.setSites(sites);
        stageRedact.show();

    }

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    public void setSites(Sites sites) {
        this.sites = sites;
    }

    public Label getLabelNymber() {
        return labelNymber;
    }

    public void setLabelNymber(Label labelNymber) {
        this.labelNymber = labelNymber;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public void setListUser(HashMap<String, User> listUser) {
        this.listUser = listUser;
    }


    public void setParentController(MyController parentController) {
        this.parentController = parentController;
    }


    public MyController getParentController() {
        return parentController;
    }

    public HashMap<String, User> getListUser() {
        return listUser;
    }
}
