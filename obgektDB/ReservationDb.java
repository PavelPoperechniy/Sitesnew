/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obgektDB;

import abstractObgect.AbstractObgectDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import object.Reservation;
import object.User;

/**
 * @author lol74
 */
public class ReservationDb extends AbstractObgectDb<Reservation> {
    private static Connection conn;
    private static final String TABLE_NAME = "sites.reservation";
    private static ReservationDb instance;

    public static ReservationDb getInstance() {
        if (instance == null) {
            instance = new ReservationDb();
        }
        return instance;
    }

    private ReservationDb() {
    }


    @Override
    public Reservation createObgect(ResultSet res) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setDate_delivery(res.getLong("date_delivery"));
        reservation.setDate_issue(res.getLong("date_issue"));
        reservation.setId(res.getLong("id"));
        reservation.setSites_id(res.getLong("sites_id"));
        reservation.setUser_id(res.getLong("user_id"));
        User user = UserDB.getInstance().createObgectbyId(res.getInt("user_id"));
        reservation.setUser(user);


        return reservation;
    }


    private PreparedStatement getObgectStmById(long id) throws SQLException {
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement statement = conn.prepareStatement("Select * from " + TABLE_NAME + " where id = ?");
        statement.setLong(1, id);
        return statement;
    }

    private PreparedStatement getObgectStmBySites_id(long sites_id) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " where sites_id = ? order by id desc limit 1";
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1, sites_id);
        return statement;
    }

    @Override
    public boolean insertObgect(Reservation obgectDb) {
        long id = 0;
        boolean status = false;
        try {

            id = insertNewObgect(insertStm(obgectDb));
            if (id > 0) {
                status = true;
                obgectDb.setId(id);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ReservationDb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectDb.getInstance().closeConnection(conn);
        }
        return status;
    }

    @Override
    public Reservation createObgectbyId(long id) {
        Reservation res = null;
        try {
            res = getObgect(getObgectStmById(id));
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectDb.getInstance().closeConnection(conn);
        }
        return res;
    }


    public Reservation createObgectbySites_id(long id) {
        Reservation res = null;
        try {
            res = getObgect(getObgectStmBySites_id(id));
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectDb.getInstance().closeConnection(conn);
        }
        return res;

    }




    @Override
    public ArrayList<Reservation> getAllObgect(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Template
    }

    @Override
    public PreparedStatement insertStm(Reservation obgect) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (sites_id, date_issue, date_delivery, user_id) VALUES (?,?,?,?)";
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, obgect.getSites_id());
        statement.setLong(2, obgect.getDate_issue());
        statement.setLong(3, obgect.getDate_delivery());
        statement.setLong(4, obgect.getUser().getId());
        return statement;
    }

    @Override
    public PreparedStatement updateObgectStm(Reservation obgect)throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " set  date_delivery = ? where id = ?";
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, obgect.getDate_delivery());
        statement.setLong(2, obgect.getId());
        return statement;

    }


}
