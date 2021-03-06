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
     // TempCode.PrintColumnTypes.printCol(rs);
      ArrayList<InfoHolder> holder=MediaParser.parse(rs, type,basic,specific);   
      st.close();
      return holder; 
    }
    
    public ArrayList<InfoHolder> getValues(String sql,int type,String[] basic,String[] specific) throws SQLException{
      Statement st = con.createStatement();
      
      //TempCode.PrintColumnTypes.printCol(st.executeQuery(sql));
                 
      ResultSet rs = st.executeQuery(sql);    
      ArrayList<InfoHolder> holder=MediaParser.parse(rs, type,basic,specific);   
      st.close();
      return holder;
    }   
    
    private int getGeneratedId(Statement st) throws SQLException{ 
        int Id;
        String sql = "select MAX(ID) from Main";
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
    
    public void insertMovies(ArrayList<VideoHolder> holders) throws SQLException{
    
        for(VideoHolder holder:holders){
            String query = "insert into Movies(Videos_Id) values(?)";
            
            PreparedStatement st= con.prepareStatement(query);
            st.setInt(1,holder.Id);
            st.executeUpdate();
            st.close();          
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
        pst.setInt(4,videoHolder.Height);
        pst.setInt(5,videoHolder.Width);
         
        pst.executeUpdate();      
        pst.close();    
        }
    }
  
    public ArrayList<InfoHolder> queryGetAllArtist() throws SQLException{
            String sql = "Select Artist, First(Audios.ID) as ID,First(Audios.Album) As Album from Audios group by Artist";
        String[] basicCol={};

                  String[] specificCol={   
                  QueryBuilder.COL_ID,
                  QueryBuilder.COL_ALBUM,      
                  QueryBuilder.COL_ARTIST
                  };

             return getValues(sql,MediaParser.TYPE_AUDIO,basicCol,specificCol);     
        }
    
    public ArrayList<InfoHolder> queryGetAllAlbum() throws SQLException{
    
           String sql = "Select Album, First(Audios.ID) as ID,First(Audios.Artist) As Artist from Audios group by Album";
           
           String[] basicCol={};
             
             String[] specificCol={
                 QueryBuilder.COL_ID,
                 QueryBuilder.COL_ARTIST,
                 QueryBuilder.COL_ALBUM            
                };
            
        return getValues(sql,MediaParser.TYPE_AUDIO,basicCol,specificCol);     
    }
    
    public ArrayList<InfoHolder> queryGetAllGenre()throws SQLException{
  
            String sql = "Select Genre, First(Audios.ID) as ID,First(Audios.Album) As Album from Audios group by Genre";
           
            String[] basicCol={};
             
             String[] specificCol={   
             QueryBuilder.COL_ID,
             QueryBuilder.COL_ALBUM,
             QueryBuilder.COL_GENRE
             };
            
        return getValues(sql,MediaParser.TYPE_AUDIO,basicCol,specificCol);     
    }
    
    public ArrayList<InfoHolder> queryGetAlbum(String albumName) throws SQLException{
   
        String sql = "Select Audios.ID,Title,Album,Artist,Genre,Path from Audios,Main where Album=? And Audios.Main_Id=Main.Id";
            PreparedStatement st = getPreparedStatement(sql);
            st.setString(1, albumName);
             
            return getAudio(st);  
    }
    
   
     public ArrayList<InfoHolder> queryGetArtist(String artistName) throws SQLException{
         
         String sql = "Select Audios.ID,Title,Album,Artist,Genre,Path from Audios,Main where Artist=? And Audios.Main_Id=Main.Id";
            PreparedStatement st = getPreparedStatement(sql);
            st.setString(1, artistName);
            
            return getAudio(st);  
     }
     
     public ArrayList<InfoHolder> queryGetGenre(String genre) throws SQLException{
         
         String sql = "Select Audios.ID,Title,Album,Artist,Genre,Path from Audios,Main where Genre=? And Audios.Main_Id=Main.Id";
            PreparedStatement st = getPreparedStatement(sql);
            st.setString(1, genre);
            
            return getAudio(st);  
    }  
     
     
      private ArrayList<InfoHolder> getAudio(PreparedStatement st) throws SQLException{
            String[] basicCol={QueryBuilder.COL_PATH};
             
             String[] specificCol={ 
             QueryBuilder.COL_ID,    
             QueryBuilder.COL_TITLE,    
             QueryBuilder.COL_ALBUM,
             QueryBuilder.COL_ARTIST,
             QueryBuilder.COL_GENRE
             };
            
            return getValues(st,MediaParser.TYPE_AUDIO,basicCol,specificCol);    
      }
     
      public  ArrayList<InfoHolder> getImageFolderValues() throws SQLException{ 
            ArrayList<InfoHolder> holder ;

            String sql = "Select Folder_Name,First(Main.Path) As Path from Images,Main where Images.Main_Id=Main.ID group by Folder_Name";                
            String[] basicCol={
                 QueryBuilder.COL_FOLDER_NAME,
                 QueryBuilder.COL_PATH};
             
             String[] specificCol={};
             
            holder = getValues(sql,MediaParser.TYPE_IMAGE,basicCol,specificCol);           
            
        return holder;       
    }
      
    public ArrayList<InfoHolder> queryGetAllMovies() throws SQLException{
        ArrayList<InfoHolder> holder;

            String sql = "Select Movies.ID,File_Name,Folder_Name,Path,Producer,Director from Videos,Movies,Main where Videos.Main_Id=Main.ID AND Videos.ID=Movies.Videos_Id";                
            String[] basicCol={
                 QueryBuilder.COL_FILE_NAME,
                 QueryBuilder.COL_FOLDER_NAME,
                 QueryBuilder.COL_PATH
            };
             
            String[] specificCol={
                QueryBuilder.COL_ID,
                QueryBuilder.COL_PRODUCER,
                QueryBuilder.COL_DIRECTOR
                };
             
            holder = getValues(sql,MediaParser.TYPE_VIDEO,basicCol,specificCol);           
        
        return holder;
    }
    
    public ArrayList<InfoHolder> queryGetAllVideos() throws SQLException{
    ArrayList<InfoHolder> holder ;

            String sql = "Select Videos.ID,File_Name,Path from Videos,Main where Videos.Main_Id=Main.ID";                
            String[] basicCol={
                 QueryBuilder.COL_FILE_NAME,
                 QueryBuilder.COL_PATH};
             
            String[] specificCol={
               QueryBuilder.COL_ID};
             
            holder = getValues(sql,MediaParser.TYPE_VIDEO,basicCol,specificCol);           
        
        return holder;
    }
    
    
    public ArrayList<InfoHolder> queryGetAllWatchedMovies() throws SQLException{
    ArrayList<InfoHolder> holder ;

           String sql = "Select Movies.ID,File_Name,Folder_Name,Path,Producer,Director from Videos,Movies,Main where Videos.Main_Id=Main.ID AND Videos.ID=Movies.Videos_Id And Watched=1";                
             String[] basicCol={
                 QueryBuilder.COL_FILE_NAME,
                 QueryBuilder.COL_PATH,
                 QueryBuilder.COL_FOLDER_NAME,
             };
             
            String[] specificCol={
               QueryBuilder.COL_ID,
               QueryBuilder.COL_PRODUCER,
               QueryBuilder.COL_DIRECTOR
            };
             
            holder = getValues(sql,MediaParser.TYPE_VIDEO,basicCol,specificCol);           
       
        return holder;
    }
    
    
    public VideoHolder queryGetMovieDetail(int Id) throws SQLException{
       String sql = "Select Movies.ID,Path,Producer,Director from Videos,Movies,Main where Videos.Main_Id=Main.ID AND Videos.ID=Movies.Videos_Id AND Movies.ID=?";                
            PreparedStatement st =getPreparedStatement(sql);
       st.setInt(1, Id);
       
       String[] basicCol={
           QueryBuilder.COL_PATH};
       
       String[] specificCol={
           QueryBuilder.COL_ID,
           QueryBuilder.COL_PRODUCER,
           QueryBuilder.COL_DIRECTOR};
       
       ArrayList<InfoHolder> holders = getValues(st,MediaParser.TYPE_VIDEO, basicCol, specificCol);
       
       if(holders!=null&&holders.size()>0)
           return (VideoHolder)holders.get(0);
       
        return null;
    }   
    
    public void updateWatchedAndRecentlyViewed(int Id) throws SQLException{
        String sql= "update Movies set Watched=1 where Id=?";               
        
        PreparedStatement st = getPreparedStatement(sql);
        st.setInt(1, Id);
        
        st.executeUpdate();
    }
   
    
    public ArrayList<InfoHolder> queryGetImageList(String folderName) throws SQLException{
       String sql = "select Images.ID,File_Name,Folder_Name,Path from Images,Main where Images.Main_Id=Main.ID And Folder_Name=?";
       PreparedStatement st =getPreparedStatement(sql);
       st.setString(1, folderName);
       
       String[] basicCol={
           QueryBuilder.COL_FILE_NAME,
           QueryBuilder.COL_FOLDER_NAME,
           QueryBuilder.COL_PATH};
       
       String[] specificCol={
           QueryBuilder.COL_ID};
       
       ArrayList<InfoHolder> holders = getValues(st,MediaParser.TYPE_IMAGE, basicCol, specificCol);
    
       return holders;
    }  
    
    public ArrayList<InfoHolder> querySearchVideo(String searchName) throws SQLException{
    String sql = "select Videos.ID,File_Name,Folder_Name,Path from Videos,Main where Videos.Main_Id=Main.ID AND File_Name like '%"+searchName+"%'";
      
      return getVideos(sql);
    }
    
    public ArrayList<InfoHolder> querySearchMovies(String searchName) throws SQLException{  
     String sql = "select Movies.ID,File_Name,Folder_Name,Path from Videos,Main,Movies where Videos.Main_Id=Main.ID AND Movies.Videos_Id=Videos.ID AND File_Name like '%"+searchName+"%'";
      return getVideos(sql);
    }
    
    public ArrayList<InfoHolder> querySearchProducer(String searchName) throws SQLException{  
     String sql = "select Movies.ID,File_Name,Folder_Name,Path from Videos,Main,Movies where Videos.Main_Id=Main.ID AND Movies.Videos_Id=Videos.ID AND Producer like '%"+searchName+"%'"; 
       return getVideos(sql);
    }
    
    
    public ArrayList<InfoHolder> querySearchDirector(String searchName) throws SQLException{  
     String sql = "select Movies.ID,File_Name,Folder_Name,Path from Videos,Main,Movies where Videos.Main_Id=Main.ID AND Movies.Videos_Id=Videos.ID AND Director like '%"+searchName+"%'";  
       return getVideos(sql);
    }
    
     private ArrayList<InfoHolder> getVideos(String sql) throws SQLException{
           
       String[] basicCol={
           QueryBuilder.COL_FILE_NAME,
           QueryBuilder.COL_FOLDER_NAME,
           QueryBuilder.COL_PATH};
       
       String[] specificCol={
           QueryBuilder.COL_ID};
       
       ArrayList<InfoHolder> holders = getValues(sql,MediaParser.TYPE_VIDEO, basicCol, specificCol);
     
       return holders;
     }
     
     
     
     public ArrayList<InfoHolder> querySearchTrack(String searchName) throws SQLException{
     String sql = "select Audios.ID,Title,Artist,Album,Genre,Path from Main,Audios where Main.ID=Audios.Main_Id And Title like '%"+searchName+"%'";
     return getAudios(sql);
     }
     
     public ArrayList<InfoHolder> querySearchAlbum(String searchName) throws SQLException{
     String sql = "select Audios.ID,Title,Artist,Album,Genre,Path from Main,Audios where Main.ID=Audios.Main_Id And Album like '%"+searchName+"%'";
     return getAudios(sql);
     }
     
     public ArrayList<InfoHolder> querySearchArtist(String searchName) throws SQLException{
     String sql = "select Audios.ID,Title,Artist,Album,Genre,Path from Main,Audios where Main.ID=Audios.Main_Id And Artist like '%"+searchName+"%'";
     return getAudios(sql);
     }
     
     public ArrayList<InfoHolder> querySearchGenre(String searchName) throws SQLException{
     String sql = "select Audios.ID,Title,Artist,Album,Genre,Path from Main,Audios where Main.ID=Audios.Main_Id And Genre like '%"+searchName+"%'";
     return getAudios(sql);
     }
     
     private ArrayList<InfoHolder> getAudios(String sql) throws SQLException{
     
       String[] basicCol={
           QueryBuilder.COL_PATH};
       
       String[] specificCol={
           QueryBuilder.COL_ID,
           QueryBuilder.COL_TITLE,
           QueryBuilder.COL_ARTIST,
           QueryBuilder.COL_ALBUM,
           QueryBuilder.COL_GENRE          
       };
       
       ArrayList<InfoHolder> holders = getValues(sql,MediaParser.TYPE_AUDIO, basicCol, specificCol);
     
       return holders;
     }
     
}