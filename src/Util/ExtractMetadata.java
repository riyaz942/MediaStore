package Util;


import Util.Print;
import java.io.File;
import java.io.FileInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author sasuke
 */

public class ExtractMetadata {
    
    Parser parser;
    
    public ExtractMetadata(Parser parser) {
       this.parser=parser;  
    }
    
    public Metadata extractData(File path){
      Metadata metadata = new Metadata();
    
        try{
        BodyContentHandler handler = new BodyContentHandler();
      
      FileInputStream inputstream = new FileInputStream(path);
      ParseContext pcontext = new ParseContext();
      
      parser.parse(inputstream, handler, metadata, pcontext);           
      
      /*
      LyricsHandler lyrics = new LyricsHandler(inputstream,handler);
      
      while(lyrics.hasLyrics()) {
    	  System.out.println(lyrics.toString());
      }
      */
      
     /* String[] metadataNames = metadata.names();
      for(String name : metadataNames) {		        
    	  System.out.println(name + ": " + metadata.get(name));
      }
      */
     
        }catch(Exception e){
            Print.print(path+" metadataError :"+e.getMessage());
        }
      
     return metadata;
    }
    
    
  /*  private void parseVideo(File file){
    
MediaInfo info    = new MediaInfo();
info.open(file);

String format     = info.get(MediaInfo.StreamKind.Video, i, "Format", 
                        MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
int bitRate       = info.get(MediaInfo.StreamKind.Video, i, "BitRate", 
                        MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
float frameRate   = info.get(MediaInfo.StreamKind.Video, i, "FrameRate", 
                        MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
short width       = info.get(MediaInfo.StreamKind.Video, i, "Width", 
                        MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);

int audioBitrate  = info.get(MediaInfo.StreamKind.Audio, i, "BitRate", 
                        MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
int audioChannels = info.get(MediaInfo.StreamKind.Audio, i, "Channels", 
                        MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
    }
*/
    
}