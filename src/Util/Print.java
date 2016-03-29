package Util;

import Holders.AudioHolder;
import Holders.ImageHolder;
import Holders.InfoHolder;
import Holders.VideoHolder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sasuke
 */
public class Print {
    
    public static void print(String string){
     System.out.println(string);
    }
    
    public static void printMedia(InfoHolder infoHolder){
    
        print("infoHolder Id:"+infoHolder.Id);
        print("File name:"+infoHolder.File_Name);
        print("Folder Name:"+infoHolder.Folder_Name);
        print("Path :"+infoHolder.Path);
        print("Created at:"+infoHolder.Created_At);
        print("Recently Viewed: "+infoHolder.Recently_Viewed);
        print("Size :"+infoHolder.Size);
      
        if(infoHolder instanceof AudioHolder){      
            AudioHolder holder = (AudioHolder) infoHolder; 
            print("\n Id: "+holder.Id);          
            print("Title :"+holder.Title);
            print("Album:"+holder.Album);
            print("Artist:"+holder.Artist);
            print("Genre:"+holder.Genre);
            print("Song year:"+holder.Song_Year);
            print("Length:"+holder.Length);
        }
        else if(infoHolder instanceof ImageHolder){      
          ImageHolder holder = (ImageHolder) infoHolder;
           print("\nId: "+holder.Id);
            print("width: "+holder.Width);
            print("Height: "+holder.Height);
        }else if(infoHolder instanceof VideoHolder){
            VideoHolder holder = (VideoHolder) infoHolder; 
            
            print("\n VideoHolder ID"+holder.Id);
            print("Title:"+holder.Title);
            print("Producer :"+holder.Producer);
            print("Director :"+holder.Director);
      
        }       
    }
}
