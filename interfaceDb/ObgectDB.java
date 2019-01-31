/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceDb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author lol74
 */
public interface ObgectDB<T> {
    ArrayList<T> getAllObgect(PreparedStatement stat);
    T createObgect(ResultSet res) throws SQLException;
    boolean insertObgect(T obgectDb);
    long insertNewObgect(PreparedStatement stm) throws SQLException;
     T createObgectbyId(long id);
     ArrayList<T> getAllObgect(long id);
     PreparedStatement insertStm (T obgect) throws SQLException;
}
