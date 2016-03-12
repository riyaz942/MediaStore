package Default;

import Database.MediaBase;
import Holders.InfoHolder;
import Util.FileUtil;
import Util.MediaParser;
import Util.Print;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.parser.mp3.Mp3Parser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sasuke
 */

//this class scans a directory and returns only the paths of all the images,audios and videos
public class Scan {
    
    private final ArrayList<String> images;
    private final ArrayList<String> videos;
    private final ArrayList<String> audios;
    private final ExecutorService service;
    private final ProgressStage stage;
    private final Tika tika;
    private static int threadTotal;
    private static int threadCount;
    private int progressCount;
    
    public Scan(ProgressStage stage){
        this.stage = stage;
        images = new ArrayList<>();
        audios = new ArrayList<>();
        videos = new ArrayList<>();
        tika = new Tika();
        service= Executors.newFixedThreadPool(30);
    
        threadTotal=0;
        threadCount=0;
        progressCount=0;
    }
    
    
    public void initiateScan(File file){
        stage.progressStarted();
        scanFolder(file);
    }
    
    public void initiateScan(ArrayList<String> files){
        stage.progressStarted();
        scanFolder(files);
    }
    
    private void checkThreadCount(){  
        threadCount++;
        
        if(threadTotal==threadCount){
            stage.progressFounds(images.size()+audios.size()+videos.size()); 
            scanAndExtractData(); 
        }      
    }
    
    private void scanAndExtractData(){
                
                if(audios.size()>0){ 
                            Mp3Parser parser = new Mp3Parser();
                            insertToDatabase(audios,parser,MediaParser.TYPE_AUDIO);
                        }
                        
                        if(images.size()>0){
                            JpegParser imageParser = new JpegParser();
                            insertToDatabase(images,imageParser,MediaParser.TYPE_IMAGE);
                        }
                        
                        stage.progressCompleted();
    }
    
    private void scanFolder(final File file){
        
   threadTotal++;
       Thread thread = new Thread(){
         
          @Override
          public void run(){
              
              
                  
        if(file.isDirectory())
        {
            File[] files = file.listFiles();
        
        int max=files.length;
        for(int i=0;i<max;i++){
        
            if(files[i].isDirectory()){
                scanFolder(files[i]);
            }
            else{
                 String type = FileUtil.detectFile(tika,files[i]);
                 
                 if(type!=null)
                 {
                 type = FileUtil.getFileType(type);
                 
                 switch(type){
                     case "image" :
                            images.add(files[i].getPath());
                         break;
                     case "video" :
                            videos.add(files[i].getPath());
                         break;
                     case "audio" :
                            audios.add(files[i].getPath());
                 }
              }
            }
          }
          checkThreadCount();
                  
              }
              
        }};   
       
       service.execute(thread);
    }
    
    private void scanFolder(final ArrayList<String> paths){
              for(String file: paths){
                scanFolder(new File(file));
              }
    }
    
    
    private void insertToDatabase(ArrayList<String> files,Parser parser,int type){
       ExtractMetadata extractMetadata = new ExtractMetadata(parser);
       MediaBase mediaBase = new MediaBase();       
       
       for(String file : files){        
           try{
               
            File path = new File(file);
            Metadata data =extractMetadata.extractData(path);
           
            InfoHolder holder = MediaParser.parse(data, path, type);
           
            mediaBase.insert(holder);
            
         stage.progressCurrent(progressCount++, file);
       }
        catch(Exception e){
            Print.print("Error :"+e.getMessage());
        
            stage.progressCurrent(progressCount++,"Error :"+e.getMessage()+"\n"+file);
        }
       }
       
        try {
            mediaBase.close();
        } catch (SQLException ex) {
            Logger.getLogger(Scan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}