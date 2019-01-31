/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obgect;

import java.util.Objects;

/**
 *
 * @author lol74
 */
public class User {
    private long id;
    private String last_name;
    private String ferst_name;
    private String login;
    private String password;
    private long role_id;
    private long collection_id;

    @Override
    public String toString() {
        return last_name +" "+ferst_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                collection_id == user.collection_id &&
                Objects.equals(last_name, user.last_name) &&
                Objects.equals(ferst_name, user.ferst_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, last_name, ferst_name, collection_id);
    }

    public User(long collection_id) {
        this.collection_id = collection_id;
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

    public User() {
    }
}
