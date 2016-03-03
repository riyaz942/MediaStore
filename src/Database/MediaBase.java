/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sasuke
 */

import Holders.AudioHolder;
import Holders.ImageHolder;
import Util.Print;
import Holders.InfoHolder;
import Holders.VideoHolder;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MediaBase {
    private static final String DRIVER="sun.jdbc.odbc.JdbcOdbcDriver";
    private static final String CONNECTION = "jdbc:odbc:"+"MediaBase";      
    private Connection con;
    
    public MediaBase(){
        try {
            init();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MediaBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MediaBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void init() throws ClassNotFoundException, SQLException{
        Class.forName(DRIVER);
	con=DriverManager.getConnection(CONNECTION);
    }
    
    public void close() throws SQLException {
        con.close();
    }
    
    public ArrayList<InfoHolder> getAllImages() throws SQLException{
     String sql = "Select * from Images,Main where Images.Main_Id=Main.ID";    
     return getInfoHolder(sql,MediaParser.TYPE_IMAGE);
    }
    
    public ArrayList<InfoHolder> getAllAudios() throws SQLException{
     String sql = "Select * from Audios,Main where Audios.Main_Id=Main.ID";    
     return getInfoHolder(sql,MediaParser.TYPE_AUDIO); 
    }
    
    /*public ResultSet getRowFromId(int id) throws SQLException{
        String sql = "select * from Main where ID = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
     return rs;
    }  
    */
    
    
    private ArrayList<InfoHolder> getInfoHolder(String sql,int type) throws SQLException{
      Statement st = con.createStatement();
      ResultSet rs = st.executeQuery(sql);    
      ArrayList<InfoHolder> holder=MediaParser.parse(rs, type);     
      return holder;
    }   
    
    private int getGeneratedId() throws SQLException{ 
        int Id;
        Statement st=con.createStatement(); 
        String sql = "select LAST(ID) from Main";
        ResultSet rs = st.executeQuery(sql);
          if(rs.next());
        Id = rs.getInt(1);
        
        st.close();
        return Id;
    }
    
    public void insert(InfoHolder holder) {  
        
        try{
       
        String sql = "insert into Main (File_Name,Folder_Name,Created_At,Path) values(?,?,?,?)";
        PreparedStatement pst=con.prepareStatement(sql);
       
        pst.setString(1,holder.File_Name);
        pst.setString(2,holder.Folder_Name);
        pst.setInt(3,(int)holder.Created_At);
        pst.setString(4,holder.Path);
        
        int result=pst.executeUpdate();       
        pst.close();
        
        if(result==1)       
            insertToSubTable(holder,getGeneratedId());
        else
          Print.print("error in sql");

        }
        catch(Exception e){
            Print.print(e.getLocalizedMessage());
        }
    }
    
    private void insertToSubTable(InfoHolder holder,int Id) throws SQLException {
    
        if(holder instanceof AudioHolder){
        
        String sql = "insert into Audios (Main_Id,Title,Album,Artist,Genre,Song_Year,Length) values(?,?,?,?,?,?,?)";
        PreparedStatement pst=con.prepareStatement(sql);
        AudioHolder audioHolder = (AudioHolder) holder;
        
        pst.setInt(1,Id);
        pst.setString(2,audioHolder.Title);
        pst.setString(3,audioHolder.Album);
        pst.setString(4,audioHolder.Artist);
        pst.setString(5,audioHolder.Genre);
        pst.setInt(6,audioHolder.Song_Year);
        pst.setInt(7,audioHolder.Length);
        
        pst.executeUpdate();
        pst.close();
        
        }else if(holder instanceof ImageHolder){
        
        String sql = "insert into Images (Main_Id,Height,Width) values(?,?,?)";
        PreparedStatement pst=con.prepareStatement(sql);
        ImageHolder imageHolder = (ImageHolder) holder;
        
        pst.setInt(1,Id);
        pst.setInt(2,imageHolder.Height);
        pst.setInt(3,imageHolder.Width);
        
        pst.executeUpdate();
        pst.close();
        }else if(holder instanceof VideoHolder){
        
        String sql = "insert into Videos (Main_Id,Title,Length,Height,Width) values(?,?,?,?,?)";
        PreparedStatement pst=con.prepareStatement(sql);
        VideoHolder videoHolder = (VideoHolder) holder;
        
        pst.setInt(1,Id);
        pst.setString(2,videoHolder.Title);
        pst.setInt(3,videoHolder.Length);
        pst.setInt(2,videoHolder.Height);
        pst.setInt(3,videoHolder.Width);
         
        pst.executeUpdate();      
        pst.close();    
        }       
    }
}
