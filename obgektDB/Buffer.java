package obgektDB;

import obgect.Reservation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lol74
 */
public class Buffer {
   


    private String adress;
    private byte[] img;
  
    private long id;
    private String last_name;
    private String ferst_name;
    private String login;
    private String password;
    private long role_id;
    private long collection_id;
    
    private int sites_id;
    private int user_id;
    private long data_issue;
    private long data_delivery;

    public int getSites_id() {
        return sites_id;
    }

    public void setSites_id(int sites_id) {
        this.sites_id = sites_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getData_issue() {
        return data_issue;
    }

    public void setData_issue(long data_issue) {
        this.data_issue = data_issue;
    }

    public long getData_delivery() {
        return data_delivery;
    }

    public void setData_delivery(long data_delivery) {
        this.data_delivery = data_delivery;
    }
    
   

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFerst_name() {
        return ferst_name;
    }

    public void setFerst_name(String ferst_name) {
        this.ferst_name = ferst_name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long role_id) {
        this.role_id = role_id;
    }

    public long getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(long collection_id) {
        this.collection_id = collection_id;
    }

    public Buffer() {
    }
     
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public byte[] getImg() {
        return img;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
     public void setImg(byte[] img) {
        this.img = img;
    }
       private Reservation reservation;

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
