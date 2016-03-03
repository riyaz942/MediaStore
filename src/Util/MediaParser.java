package Util;

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
import Holders.InfoHolder;
import Holders.VideoHolder;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.tika.metadata.Metadata;

public class MediaParser {
    
    public static final int TYPE_IMAGE=0;
    public static final int TYPE_AUDIO=1;
    public static final int TYPE_VIDEO=2;
    
    
    public static final String IMAGE_OUTPUT_FOLDER="C:\\Users\\sasuke\\Documents\\NetBeansProjects\\MovieLibrary\\image\\";

    private static final String IMG="tiff:";
    private static final String AUD="xmpDM:";
    
    private static final String IMAGE_WIDTH=IMG+"ImageWidth";
    private static final String IMAGE_HEIGHT=IMG+"ImageLength";
   
    private static final String AUDIO_TITLE="dc:title";
    private static final String AUDIO_ALBUM=AUD+"album";
    private static final String AUDIO_ARTIST=AUD+"artist";
    private static final String AUDIO_GENRE=AUD+"genre";  
    private static final String AUDIO_RELEASE_DATE=AUD+"releaseDate";   
    private static final String AUDIO_DURATION=AUD+"duration";
    
    public static InfoHolder parse(Metadata metadata,File file,int type){
      InfoHolder holder=null;
    
      switch(type){     
          case TYPE_IMAGE :
                holder=parseImage(metadata,file);
              break;
          case TYPE_AUDIO :
                holder=parseAudio(metadata,file);
              break;
          case TYPE_VIDEO :
              
      }
      
      return holder;
    }
    
    
    public static ArrayList<InfoHolder> parse(ResultSet rs,int type,String[] basic,String[] specific) throws SQLException{
    ArrayList<InfoHolder> listHolder = new ArrayList<>();
    
        while(rs.next()){
            InfoHolder holder = getInfoHolderObj(type,rs,specific);
            
                for(String name : basic){
            
                    switch(name){
                        case QueryBuilder.COL_ID: holder.Id = rs.getInt(QueryBuilder.COL_ID); break;
                        case QueryBuilder.COL_FILE_NAME: holder.File_Name=rs.getString(QueryBuilder.COL_FILE_NAME); break;
                        case QueryBuilder.COL_FOLDER_NAME: holder.Folder_Name= rs.getString(QueryBuilder.COL_FOLDER_NAME);
                        case QueryBuilder.COL_CREATED_AT: holder.Created_At= rs.getInt(QueryBuilder.COL_CREATED_AT); break;
                        case QueryBuilder.COL_RECENTLY_VIEWED:  holder.Recently_Viewed=rs.getInt(QueryBuilder.COL_RECENTLY_VIEWED); break;
                        case QueryBuilder.COL_PATH: holder.Path = rs.getString(QueryBuilder.COL_PATH); break;
                    }               
                }
             
          listHolder.add(holder);  
        }
        rs.close();
        
        return listHolder;
    }
    
    
    private static InfoHolder getInfoHolderObj(int type,ResultSet rs,String[] specific) throws SQLException{
        InfoHolder holder=null;
        switch(type){     
          case TYPE_IMAGE :
                 ImageHolder iholder = new ImageHolder();
              
              for(String name: specific){             
                  switch(name){
                            case QueryBuilder.COL_HEIGHT: iholder.Height = rs.getInt(QueryBuilder.COL_HEIGHT); break;
                            case QueryBuilder.COL_WIDTH: iholder.Width = rs.getInt(QueryBuilder.COL_WIDTH); break;
                      }
                  }
              
                holder =iholder;
              break;
          case TYPE_AUDIO :
                AudioHolder aholder = new AudioHolder();
                
                for(String name :specific){
                
                    switch(name){
                        case QueryBuilder.COL_TITLE: aholder.Title = rs.getString(QueryBuilder.COL_TITLE); break;
                        case QueryBuilder.COL_ALBUM: aholder.Album = rs.getString(QueryBuilder.COL_ALBUM); break;
                        case QueryBuilder.COL_ARTIST: aholder.Artist = rs.getString(QueryBuilder.COL_ARTIST); break;
                        case QueryBuilder.COL_GENRE: aholder.Genre = rs.getString(QueryBuilder.COL_GENRE); break;
                        case QueryBuilder.COL_SONG_YEAR: aholder.Song_Year = rs.getInt(QueryBuilder.COL_SONG_YEAR); break;
                        case QueryBuilder.COL_LENGTH: aholder.Length = rs.getInt(QueryBuilder.COL_LENGTH); break;
                    }
                }
                
                holder =aholder;
              break;
          case TYPE_VIDEO :
                 holder = new VideoHolder();
      }
        return holder;
    }
    
    private static InfoHolder parseBasic(InfoHolder infoHolder,File file){
    
     infoHolder.File_Name = file.getName();
     infoHolder.Folder_Name = file.getParentFile().getName();
     infoHolder.Created_At = file.lastModified();
     infoHolder.Path=file.getPath();
     infoHolder.Size=file.length();
     
     return infoHolder;
    }
    
    /*
    private static InfoHolder getResolution(InfoHolder holder,Metadata data){
     String width,height;    
     height = data.get(IMAGE_HEIGHT);
     width = data.get(IMAGE_WIDTH);
    
     if (holder instanceof ImageHolder){
         if(height!=null){
             
         }
     }
     else{
     
     }
       
     return holder;
    }    
    */
    
    private static InfoHolder parseImage(Metadata metadata,File file){
     ImageHolder imageHolder = (ImageHolder) parseBasic(new ImageHolder(),file);     
     String height =metadata.get(IMAGE_HEIGHT);
     String width = metadata.get(IMAGE_WIDTH);
     
     if(height!=null&&!height.isEmpty())
        imageHolder.Height= Integer.parseInt(height);   
    
     if(width!=null&&!width.isEmpty())
         imageHolder.Width= Integer.parseInt(width);     
     
     return imageHolder;
    }
    
    private static InfoHolder parseAudio(Metadata data,File file){
       AudioHolder holder = (AudioHolder) parseBasic(new AudioHolder(),file);
       String year,length,album;
       
       holder.Title = data.get(AUDIO_TITLE);
       holder.Artist = data.get(AUDIO_ARTIST);
       album = data.get(AUDIO_ALBUM);
       holder.Genre = data.get(AUDIO_GENRE);
      
       year = data.get(AUDIO_RELEASE_DATE);
       length = data.get(AUDIO_DURATION);
       
       if(album!=null&&!album.isEmpty())
           holder.Album =album;
         else
           holder.Album=file.getParentFile().getName();
           
       if(year!=null&&!year.isEmpty())
           holder.Song_Year = Integer.parseInt(year);
           
       if(length!=null&&!length.isEmpty())    
           holder.Length = (int) Double.parseDouble(length);
       
       Mp3File song;
        try {
            song = new Mp3File(file.getPath());
   
        if (song.hasId3v2Tag()){
             ID3v2 id3v2tag = song.getId3v2Tag();
             byte[] imageData = id3v2tag.getAlbumImage();
             //converting the bytes to an image
             BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
        
             File outputfile = new File(IMAGE_OUTPUT_FOLDER+holder.Album+".jpg");
             ImageIO.write(img, "jpg", outputfile);
        }

        } catch (UnsupportedTagException ex) {
            Logger.getLogger(MediaParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDataException ex) {
            Logger.getLogger(MediaParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MediaParser.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       return holder;
    }
}