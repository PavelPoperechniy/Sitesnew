/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abstractObgect;

import obgektDB.UserDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import interfaceDb.ObgectDB;

/**
 *
 * @author lol74
 */
public abstract class AbstractObgectDb<T> implements ObgectDB<T>{
     public ArrayList<T> getAllObgect(PreparedStatement stat){
       ArrayList<T>list = new ArrayList<>();
       ResultSet res = null;
        try {
            res = stat.executeQuery();
            while (res.next()) {
                list.add(createObgect(res));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        }
       return list;
   }
     
     @Override
     public long insertNewObgect(PreparedStatement stm) throws SQLException{
       
       long id = -1;
       ResultSet res = null;
       id = stm.executeUpdate();
       res = stm.getGeneratedKeys();
       res.next();
       if(res.isFirst()){
           id = res.getLong(1);
       }
       return id;
   }
     
      public T getObgect(PreparedStatement stat) throws SQLException{
       T obgectDb  = null;
       ResultSet res = null;
       res = stat.executeQuery();

       res.next();
       if(res.isFirst()){
          obgectDb  = createObgect(res);
       }
       return obgectDb;
   }

   public boolean update_dataObgectDB(PreparedStatement stat) throws SQLException {
         boolean b = false;
         int rows = -1;
         rows = stat.executeUpdate();
         if(rows>0){
             b = true;
         }
         return b;
   }


    public boolean chekObgect(PreparedStatement stat) throws SQLException{
         boolean flag = false;
        int rows = -1;
        ResultSet res = null;
        res = stat.executeQuery();
        if(res.isBeforeFirst()){
            res.next();
            rows = res.getRow();
        }

        if(rows>0){
            flag = true;
        }
       return flag;
    }
     
    
}
