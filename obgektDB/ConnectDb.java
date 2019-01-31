/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obgektDB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mysql.cj.jdbc.MysqlDataSource;

/**
 *
 * @author lol74
 */
public class ConnectDb {
    private static ConnectDb instance = null;
    private final String USER = "root";
    private final String PASSWORD = "root";
    private final String  URL = "jdbc:mysql://localhost:3306/sites?allowPublicKeyRetrieval = true & useSSL = false ";     
    private Connection conn = null;

    public static ConnectDb getInstance() {
        if(instance == null){
            return instance =  new ConnectDb();
        }
        return instance;
    }
    public Connection getConnection(){
        try {
            if(conn == null||conn.isClosed()){
                 MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser(USER);
            dataSource.setPassword(PASSWORD);
            dataSource.setURL(URL);
            conn = dataSource.getConnection();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
     
     public void closeConnection(Connection conn){
        try {
            if(conn!= null&&!conn.isClosed()){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConnectDb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDb.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

    private ConnectDb() {
    }
}
