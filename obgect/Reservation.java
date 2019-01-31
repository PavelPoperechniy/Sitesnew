/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obgect;

/**
 *
 * @author lol74
 */
public class Reservation {
    private boolean longInwork;

    public boolean isLongInwork() {
        return longInwork;
    }

    public void setLongInwork(boolean longInwork) {
        this.longInwork = longInwork;
    }

    private long sites_id;
    private long user_id;
    private long date_issue;
    private long date_delivery;
    private long id;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reservation() {
    }

    public long getSites_id() {
        return sites_id;
    }

    public void setSites_id(long sites_id) {
        this.sites_id = sites_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getDate_issue() {
        return date_issue;
    }

    public void setDate_issue(long date_issue) {
        this.date_issue = date_issue;
    }

    public long getDate_delivery() {
        return date_delivery;
    }

    public void setDate_delivery(long date_delivery) {
        this.date_delivery = date_delivery;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    
           
}
