/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sasuke
 */

import java.io.File;
import org.apache.tika.metadata.Metadata;

public class MediaParser {
    
    public static final int TYPE_IMAGE=0;
    public static final int TYPE_AUDIO=1;
    public static final int TYPE_VIDEO=2;
    
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
       String year,length;
       
       holder.Title = data.get(AUDIO_TITLE);
       holder.Artist = data.get(AUDIO_ARTIST);
       holder.Album = data.get(AUDIO_ALBUM);
       holder.Genre = data.get(AUDIO_GENRE);
      
       year = data.get(AUDIO_RELEASE_DATE);
       length = data.get(AUDIO_DURATION);
       
       if(year!=null&&!year.isEmpty())
           holder.Song_Year = Integer.parseInt(year);
           
       if(length!=null&&!length.isEmpty())    
           holder.Length = (int) Double.parseDouble(length);
       
       return holder;
    }
}