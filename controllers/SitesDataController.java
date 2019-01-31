package controllers;

import informWindows.InformWindow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;
import obgect.Reservation;
import obgect.Sites;
import obgect.User;
import obgektDB.ReservationDb;
import utilits.Util_Sites;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class SitesDataController {


   private Sites sites;
    private Window parentWindow;
    private User user;
    private boolean mode;
    private boolean statusCombo;
    private boolean init;
    private HashMap<String, User> listUser;
    private ObservableList<String> listComboBox;
    private MyController parentController;
    private long MICROSECOND = 1000;
    private int count = 0;

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
    private ImageView image;
    @FXML
    private ComboBox<String> seachByLastname;


    public void initDataWindow(Sites sites) {
        listUser = parentController.getListUser();
        if (sites.isBusy()) {
            mode = true;
        } else mode = false;
        initComboBox();
        init_btn_Act();
        initLabel();
        init = true;
        listComboBox = FXCollections.observableArrayList();
        user = parentController.getUser();

    }

    public void okAction(ActionEvent actionEvent) {
        listComboBox.clear();
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
        if(sites.isBusy()) {
           giveSites();
        }
        else returnSites();
        Node node = (Node)actionEvent.getSource();
        Window window = (Window)node.getScene().getWindow();
        listComboBox.clear();
        seachByLastname.getItems().clear();
        window.hide();


    }

    public void comboAction(ActionEvent actionEvent) {

/*           String value = seachByLastname.getValue();
            if(init) {
                if (value != null) {
                    for (Map.Entry<String, User> item : listUser.entrySet()) {
                        if (item.getKey().toLowerCase().contains(value.toLowerCase())) {
                            if (!listComboBox.contains(item.getKey())) {
                                listComboBox.add(item.getKey());

                            }
                        }
                    }
                    seachByLastname.getItems().clear();
                }

                seachByLastname.getItems().addAll(listComboBox);
            }
            else  {
                 count++;
                if(count == 1){
                    init = true;
                }
            }
*/


    }

    private void initComboBox() {
        if (mode) {
            seachByLastname.setEditable(true);
            seachByLastname.setVisible(true);
            seachByLastname.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                  //  seachByLastname.setValue(oldValue);
                    System.out.println(observable);
                    System.out.println(oldValue);
                    System.out.println(newValue);
                }
            });
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

    private void initLabel() {
        labelNymber.setText(sites.toString());
        labeltimeStatus.setText(sites.getTimeStatus());


        String str = "";
        if (sites.getReservation() != null) {
            if (!sites.isBusy()) {
                str = "Обрабатывает " + sites.getReservation().getUser().getLast_name() + " " + sites.getReservation().getUser().getFerst_name();
            } else
                str = "Сдал участок " + sites.getReservation().getUser().getLast_name() + " " + sites.getReservation().getUser().getFerst_name();
        } else {
            str = "Данные о возвещателе отсутствуют";
        }
        labelnameUser.setText(str);

        try {
            FileInputStream stream = new FileInputStream("F://Foto_sites//foto1.png");
            image.setImage(new Image(stream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void giveSites(){
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

        } else {
            InformWindow informWindow = new InformWindow();
            informWindow.informWindow("Ошибка ввода данных", "Выберете имя из списка");
        }
    }

    private void returnSites(){
        if(!sites.isBusy()) {
            long thisData = new GregorianCalendar().getTimeInMillis()/MICROSECOND;
            sites.getReservation().setDate_delivery(thisData);
            if (Util_Sites.getInstance().returnSites(sites.getReservation())) {
                parentController.getListBusySites().removeAll(sites);
                sites.setBusy(true);
                Util_Sites.getInstance().chekDate(sites, true);
                parentController.getListFreeSites().addAll(sites);


            } else {
                InformWindow informWindow = new InformWindow();
                informWindow.informWindow("", "Операция не успешна");
            }
        }

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

}
