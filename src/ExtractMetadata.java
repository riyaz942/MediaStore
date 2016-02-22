
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.parser.mp3.Mp3Parser;
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
    
    ArrayList<String> path;
    
    public ExtractMetadata(ArrayList<String> path) throws Exception{
       this.path=path;
       
       
       for(String paths : this.path){
           
               Print.print(paths);
               extractData(new File(paths));
           
           
       }
    }
    
    private void extractData(File path)throws Exception{
    
      BodyContentHandler handler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      
      FileInputStream inputstream = new FileInputStream(path);
      ParseContext pcontext = new ParseContext();
      
      Mp3Parser  audioParser = new  Mp3Parser();
      audioParser.parse(inputstream, handler, metadata, pcontext);
      
      /*
      LyricsHandler lyrics = new LyricsHandler(inputstream,handler);
      
      while(lyrics.hasLyrics()) {
    	  System.out.println(lyrics.toString());
      }
      */
      
      String[] metadataNames = metadata.names();
      for(String name : metadataNames) {		        
    	  System.out.println(name + ": " + metadata.get(name));
      }
    }
}