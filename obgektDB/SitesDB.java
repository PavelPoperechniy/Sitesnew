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
import object.Sites;

/**
 *
 * @author lol74
 */
public class SitesDB extends AbstractObgectDb<Sites>{
   
//     private long id;
//    private int number;
//    private int collection_id;
//    private String adress;
//    private byte[] img;
//    private Reservation resrvation;
//    private List<String> records  = new ArrayList<>();


    private static SitesDB instance;

    private SitesDB() {
    }

    public static SitesDB getInstance() {
        if(instance == null){
            instance = new SitesDB();
        }
        return instance;
    }

    private static final String TABLE_NAME = "sites.spr_sites";
    private Connection conn = null;

    @Override
    public Sites createObgect(ResultSet res) throws SQLException {
        Sites sites = new Sites();
        sites.setId(res.getLong("id"));
        sites.setNumber(res.getInt("number"));
        sites.setCollection_id(res.getInt("collection_id"));
        sites.setAdress(res.getString("adress"));
        sites.setReservation(ReservationDb.getInstance().createObgectbySites_id(sites.getId()));
        if(sites.getReservation()!=null) {
            if ((Long) (sites.getReservation().getDate_delivery()) == 0) {
                sites.setBusy(false);
            } else sites.setBusy(true);
        }
        return sites;
        
    }

    @Override
    public boolean insertObgect(Sites obgectDb) {
         long id = 0;
         boolean status = false;
        try {
            id = insertNewObgect(insertStm(obgectDb));
            if(id>0){
                status = true;
                obgectDb.setId(id);

            }
        } catch (SQLException ex) {
            Logger.getLogger(SitesDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            ConnectDb.getInstance().closeConnection(conn);
        }
        return status;
    }

    @Override
    public Sites createObgectbyId(long id) {
        Sites sites = null;
         try {
             sites = getObgect(getObgectStmbyId(id));
         } catch (SQLException ex) {
             Logger.getLogger(SitesDB.class.getName()).log(Level.SEVERE, null, ex);
         }
         finally{
             ConnectDb.getInstance().closeConnection(conn);
         }
         return sites;
    }
    

    @Override
    public ArrayList<Sites> getAllObgect(long id) {
        ArrayList<Sites> list = null;
         try {
             list = getAllObgect(getObgectStmByCollection_id(id));
         } catch (SQLException ex) {
             Logger.getLogger(SitesDB.class.getName()).log(Level.SEVERE, null, ex);
         }
         finally{
             ConnectDb.getInstance().closeConnection(conn);
         }
         return list;
    }

    @Override
    public PreparedStatement insertStm(Sites obgect) throws SQLException {
        String sql = "INSERT INTO "+TABLE_NAME+" (number, collection_id, adress) VALUES (?, ?, ?)";
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement stat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stat.setInt(1,obgect.getNumber());
        stat.setLong(2,obgect.getCollection_id());
        stat.setString(3,obgect.getAdress());
        return stat;
    }

    @Override
    public PreparedStatement updateObgectStm(Sites obgect) {
        return null;
    }

    public ArrayList<Sites> getAllObgectByBuff(long collection_id) {
           ArrayList<Sites> list = null;
        try {
            list =  getAllObgect(allObgectBuffer(getObgectStmByCollection_id(collection_id)));
        } catch (SQLException ex) {
            Logger.getLogger(SitesDB.class.getName()).log(Level.SEVERE, null, ex);
        }
         finally{
             ConnectDb.getInstance().closeConnection(conn);
         }
         return list;
     }

     public boolean chekSitesByNumber(long number, long collection_id) throws SQLException {
          return chekObgect(chekSitesByNumberStm(number,collection_id));
     }

     public Sites createObgect (long number,long collection_id){
         Sites sites = null;
         try {
             sites = getObgect(getObgectStm(number,collection_id));
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return sites;
     }
    
    private PreparedStatement getObgectStmbyId(long id) throws SQLException{
        String sql = "SELECT id FROM "+TABLE_NAME+" WHERE id = ?";
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setInt(1,(int)id);
        
        return stat;
    }

    private PreparedStatement getObgectStm (long number,long collection_id) throws SQLException {
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE number = ? and collection_id  = ?";
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setLong(1,number);
        stat.setLong(2,collection_id);
        return stat;
    }
    
    private PreparedStatement getObgectStmByCollection_id(long collection_id) throws SQLException{
        String sql = "SELECT * FROM sites.spr_sites WHERE collection_id  = ?";
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setLong(1,collection_id);
       // stat.setInt(1,(int)collection_id);
        return stat;
    }

    private PreparedStatement chekSitesByNumberStm(long number, long collection_id) throws SQLException {
        String sql = "SELECT id FROM "+TABLE_NAME+" WHERE number = ? and collection_id = ?";
        conn = ConnectDb.getInstance().getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setLong(1,number);
        stat.setLong(2, collection_id);
        return stat;
    }
    
//    private PreparedStatement getRecordsBySites_id(long sites_id)throws SQLException{
//         String sql = "SELECT * FROM sites.records_sites WHERE sites_id  = ? ";
//        conn = ConnectDb.getInstance().getConnection();
//        PreparedStatement stat = conn.prepareStatement(sql);
//        stat.setLong(1,sites_id);
//        return stat;
//    }
    
    private Sites createSitesByBuffer(Buffer buff){
        Sites sites = new Sites();
        sites.setAdress(buff.getAdress());
        sites.setCollection_id((int)buff.getCollection_id());
        sites.setId(buff.getId());
        sites.setNumber(buff.getNumber());
      //  sites.setRecords();
        sites.setReservation(ReservationDb.getInstance().createObgectbySites_id(sites.getId()));
      if(sites.getReservation()!=null ) {
          if ((sites.getReservation().getDate_delivery()) == 0) {
              sites.setBusy(false);
          } else sites.setBusy(true);
      } else sites.setBusy(true);
        return sites;
    }
    
    private ArrayList<Buffer> allObgectBuffer(PreparedStatement stat) throws SQLException{
        ArrayList<Buffer> list = new ArrayList<>();
        ResultSet res = null;
        res = stat.executeQuery();
        while(res.next()){
            Buffer buff = new Buffer();
            buff.setId(res.getLong("id"));
            buff.setNumber(res.getInt("number"));
            buff.setCollection_id(res.getInt("collection_id"));
            buff.setAdress(res.getString("adress"));
         //   buff.setReservation(ReservationDb.getInstance().createObgectbySites_id(buff.getId()));
            list.add(buff);
        }
        
        return list;
    }
    
    private ArrayList<Sites> getAllObgect(ArrayList<Buffer> buff){
         ArrayList<Sites> list = new ArrayList<>();
         for(Buffer buf :buff){
            Sites sites = createSitesByBuffer(buf);
               sites.setReservation(ReservationDb.getInstance().createObgectbySites_id(sites.getId()));
            list.add(sites);
        }
         return list;
    }
    
}
