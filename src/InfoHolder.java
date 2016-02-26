
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sasuke
 */
public class InfoHolder {
        int Id;
        String File_Name;
        String Folder_Name;
        long Created_At;
        long Recently_Viewed;
        String Path;
        long Size;

        public InfoHolder getSubclass(int type){
          InfoHolder holder=null;
          
          switch(type){
              case MediaParser.TYPE_AUDIO :
                  holder = new AudioHolder();
                  break;
              case MediaParser.TYPE_VIDEO :
                   holder = new VideoHolder();
                  break;
              case MediaParser.TYPE_IMAGE :                 
                   holder = new ImageHolder();
          }
          
          holder = copyHolder(this,holder);
          
          return holder;
        }
        
        private InfoHolder copyHolder(InfoHolder from,InfoHolder to){
            
            to.Id = from.Id;
            to.File_Name= from.File_Name;
            to.Folder_Name=from.Folder_Name;
            to.Created_At = from.Created_At;
            to.Path = from.Path;
            to.Recently_Viewed = from.Recently_Viewed;
            to.Size=from.Size;
            
            return to;
        }
}

   class AudioHolder extends InfoHolder{
        String Title;
        String Artist;
        String Album;
        String Genre;
        int Song_Year;
        int Length;       
    }
    
  class VideoHolder extends InfoHolder{
        String Title;
        int Length;
        int Height;
        int Width;
    }
    
   class ImageHolder extends InfoHolder{
        int Height;
        int Width;
    } 