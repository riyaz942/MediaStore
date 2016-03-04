package Database;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sasuke
 */

import Util.MediaParser;
import Holders.AudioHolder;
import Holders.ImageHolder;
import Util.Print;
import Holders.InfoHolder;
import Holders.VideoHolder;
import Util.QueryBuilder;
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
    
   
    /*public ResultSet getRowFromId(int id) throws SQLException{
        String sql = "select * from Main where ID = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
     return rs;
    }  
    */
    public PreparedStatement getPreparedStatement(String sql) throws SQLException{
        return con.prepareStatement(sql);
    }

    public ArrayList<InfoHolder> getValues(PreparedStatement st,int type,String[] basic,String[] specific) throws SQLException{
      ResultSet rs = st.executeQuery();    
      ArrayList<InfoHolder> holder=MediaParser.parse(rs, type,basic,specific);     
      return holder; 
    }
    
    public ArrayList<InfoHolder> getValues(String sql,int type,String[] basic,String[] specific) throws SQLException{
      Statement st = con.createStatement();
      ResultSet rs = st.executeQuery(sql);    
      ArrayList<InfoHolder> holder=MediaParser.parse(rs, type,basic,specific);     
      return holder;
    }   
    
    private int getGeneratedId(Statement st) throws SQLException{ 
        int Id;
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
        Statement st = con.createStatement();
        
        pst.setString(1,holder.File_Name);
        pst.setString(2,holder.Folder_Name);
        pst.setInt(3,(int)holder.Created_At);
        pst.setString(4,holder.Path);
        
        int result=pst.executeUpdate();       
        pst.close();
        
        if(result==1)       
            insertToSubTable(holder,getGeneratedId(st));
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
    
  
    public ArrayList<InfoHolder> queryGetAllArtist() throws SQLException{
    ArrayList<InfoHolder> holder = null;

            String sql = "Select Artist, First(Audios.Album) As Album from Audios group by Artist";
           
            String[] basicCol={};
             
             String[] specificCol={   
             QueryBuilder.COL_ALBUM,
             QueryBuilder.COL_ARTIST
             };
            
            holder = getValues(sql,MediaParser.TYPE_AUDIO,basicCol,specificCol);
        
        return holder;
    }
    
    
    
    public ArrayList<InfoHolder> queryGetAllAlbum() throws SQLException{
    ArrayList<InfoHolder> holder = null;

            String sql = "Select Album, First(Audios.Artist) As Artist from Audios group by Album";
           
            String[] basicCol={};
             
             String[] specificCol={   
             QueryBuilder.COL_ALBUM,
             QueryBuilder.COL_ARTIST
             };
            
            holder = getValues(sql,MediaParser.TYPE_AUDIO,basicCol,specificCol);
        
        return holder;
    }
    
    public ArrayList<InfoHolder> queryGetAlbum(String albumName) throws SQLException{   
   String sql = "Select * from Audios,Main where Album=? And Audios.Main_Id=Main.Id";
            PreparedStatement st = getPreparedStatement(sql);
            st.setString(1, albumName);
            String[] basicCol={QueryBuilder.COL_PATH};
             
             String[] specificCol={ 
             QueryBuilder.COL_TITLE,    
             QueryBuilder.COL_ALBUM,
             QueryBuilder.COL_ARTIST
             };
            
            return getValues(st,MediaParser.TYPE_AUDIO,basicCol,specificCol);  
    }

  
}