package obgektDB;

import abstractObgect.AbstractObgectDb;
import object.Roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RolesDB extends AbstractObgectDb<Roles> {
    private static RolesDB instance;

    public static   RolesDB getInstance() {
        if(instance == null){
            instance = new RolesDB();
        }
        return instance;
    }

    private RolesDB() {
    }

    private Connection conn = null;
    private static String TABLE_NAME = "sites.spr_roles";
    @Override
    public Roles createObgect(ResultSet res) throws SQLException {
        Roles roles = new Roles();
        roles.setId(res.getLong("id"));
        roles.setName(res.getString("roles_name"));
        return roles;
    }

    @Override
    public boolean insertObgect(Roles obgectDb){
        long id = 0;
        boolean status = false;
        try {
            id = insertNewObgect(insertStm(obgectDb));
            if(id>0){
                status = true;
                obgectDb.setId(id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            ConnectDb.getInstance().closeConnection(conn);
        }
        return status;
    }

    @Override
    public Roles createObgectbyId(long id) {

        return null;
    }

    @Override
    public ArrayList<Roles> getAllObgect(long id) {
        return null;
    }

    public ArrayList<Roles>getAllRoles() throws SQLException {
        ArrayList<Roles> list = getAllObgect(getAllRolles());
        return list;
    }

    @Override
    public PreparedStatement insertStm(Roles obgect) throws SQLException {
        String sql = "INSERT INTO "+TABLE_NAME+" (roles_name) VALUES (?)";
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        return stat;
    }

    @Override
    public PreparedStatement updateObgectStm(Roles obgect) throws SQLException {
       return null;
    }

    public PreparedStatement getAllRolles() throws SQLException {
        String sql = "SELECT * FROM "+TABLE_NAME;
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        return stat;

    }
}
