
import java.util.Date;

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
        int Size;
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