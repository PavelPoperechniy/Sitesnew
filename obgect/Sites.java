/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obgect;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author lol74
 */
public class Sites {
    private boolean busy;

   
    private long id;
    private int number;
    private long collection_id;
    private String adress;
    private byte[] img;
    private Reservation reservation;
    private List<String> records  = new ArrayList<>();
    private String timeStatus;

    public String getTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(String timeStatus) {
        this.timeStatus = timeStatus;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public List<String> getRecords() {
        return records;
    }

    public void setRecords(List<String> records) {
        this.records = records;
    }
    

    public Sites() {
       
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(long collection_id) {
        this.collection_id = collection_id;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
     
     public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    @Override
    public String toString() {
        return "Участок №"+number + "  "+ adress;
    }
}
