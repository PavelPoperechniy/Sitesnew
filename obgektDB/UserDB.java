/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obgektDB;

import abstractObgect.AbstractObgectDb;
import java.nio.Buffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import object.User;

/**
 *
 * @author lol74
 */
public class UserDB extends AbstractObgectDb<User>{
    private static UserDB instance ;
    private  Connection conn =  null;
    private final String TABLE_NAME = "sites.user";

    private UserDB() {
    }
    
    public static UserDB getInstance() {
        if(instance == null){
            instance = new UserDB();
        }
        return instance;
    }
   
   private PreparedStatement getObgectStmById(long id) throws SQLException{
        conn = ConnectDb.getInstance().getConnection();
       PreparedStatement statement = conn.prepareStatement("Select * from "+TABLE_NAME+" where id = ?");
       statement.setLong(1, id);
       return statement;
   }
   private PreparedStatement allUsers(long collection_id ) throws SQLException{
         conn = ConnectDb.getInstance().getConnection();
       PreparedStatement statement = conn.prepareStatement("Select * from "+TABLE_NAME+" where collection_id = ?");
       statement.setLong(1, collection_id);
       return statement;
   }
   
  
   private User createUserOfBuffer(Buffer buff){
       return null;
   }
   
   
//   private User getObgect(PreparedStatement stat) throws SQLException{
//       User user  = null;
//       ResultSet res = null;
//       res = stat.executeQuery();
//       res.next();
//       if(res.isFirst()){
//           user = createObgect(res);
//       }
//       return user;
//   }
//   
//  
   
//   private ArrayList<User> getAllObgect(PreparedStatement stat){
//       ArrayList<User>list = new ArrayList<>();
//       ResultSet res = null;
//        try {
//            res = stat.executeQuery();
//            while (res.next()) {
//                list.add(createObgect(res));
//                
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
//       return list;
//   }
   
    @Override
   public User createObgectbyId(long id){
       User user = null;
        try {
             user = getObgect(getObgectStmById(id));
        } catch (SQLException ex) {
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
          ConnectDb.getInstance().closeConnection(conn);
        }
        return  user;
   }

    @Override
    public ArrayList<User> getAllObgect(long id) {
        ArrayList<User>list = null;
        try {
            list = getAllObgect(allUsers(id));
        } catch (SQLException ex) {
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            ConnectDb.getInstance().closeConnection(conn);
        }
        return list;
    }


    public ArrayList<User> getAllObgect(String value){
       ArrayList<User>list = null;
       try {
           list = getAllObgect(searchBy_partNameStm(value));
       } catch (SQLException ex) {
           Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
       }
       finally{
           ConnectDb.getInstance().closeConnection(conn);
       }
       return list;
   }

   public HashMap<String,User> getAllUser(long id){
        HashMap<String,User> list = new HashMap<>();
       try {
           ArrayList<User> listUser =  getAllObgect(allUsers(id));
           for(User user:listUser){
               list.put(user.toString(),user);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return list;
   }

    @Override
    public User createObgect(ResultSet res) throws SQLException {
           User user  = new User();
       user.setCollection_id(res.getLong("collection_id"));
       user.setId(res.getLong("id"));
       user.setFerst_name(res.getString("ferst_name"));
       user.setLast_name(res.getString("last_name"));
       user.setRole_id(res.getLong("role_id"));
       user.setCollection_id(res.getLong("collection_id"));
       return user;
    }

    @Override
    public boolean insertObgect(User obgectDb) {
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
    public PreparedStatement insertStm(User obgect) throws SQLException {
         conn = ConnectDb.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("INSERT INTO "+TABLE_NAME+" (last_name, ferst_name,  role_id, collection_id) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
        stm.setString(1, obgect.getLast_name());
        stm.setString(2, obgect.getFerst_name());
       // stm.setString(3, obgect.getLogin());
      //  stm.setString(4, obgect.getPassword());
        stm.setLong(3, obgect.getRole_id());
        stm.setLong(4, obgect.getCollection_id());
       return stm;
    }

    @Override
    public PreparedStatement updateObgectStm(User obgect) throws SQLException {
        String sql  = "UPDATE "+TABLE_NAME+" SET  last_name  = ? , ferst_name = ? , role_id = ? where id = ?";
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1,obgect.getLast_name());
        stat.setString(2,obgect.getFerst_name());
        stat.setLong(3,obgect.getRole_id());
        stat.setLong(4,obgect.getId());
        return stat;
    }

    private PreparedStatement searchBy_partNameStm(String text) throws SQLException {
        String sql = "SELECT * FROM sites.user where last_name like  ?%  or ferst_name like ?%";
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1,text);
        stm.setString(2,text);
        return stm;
    }

}
