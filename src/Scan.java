import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.tika.Tika;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sasuke
 */
public class Scan {
    
    private ArrayList<String> images;
    private ArrayList<String> videos;
    private ArrayList<String> audios;
    private ExecutorService service;
    private ProgressStage stage;
    private final Tika tika;
    private static int threadTotal;
    private static int threadCount;
    
    public Scan(ProgressStage stage){
        this.stage = stage;
        images = new ArrayList<>();
        audios = new ArrayList<>();
        videos = new ArrayList<>();
        tika = new Tika();
        service= Executors.newFixedThreadPool(30);
    
        threadTotal=0;
        threadCount=0;
    }
    
    
    public void initiateScan(File file){
        stage.progressStarted();
        scanFolder(file);
    }
    
    private void checkThreadCount(){  
        threadCount++;
        
        if(threadTotal==threadCount){
            stage.progressCompleted(images.size()+videos.size()+audios.size());            
        }      
    }
    
    private void scanFolder(File file){
       
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
          }
          stage.progressCurrent(threadCount);
          checkThreadCount();
        }};   
       
       service.execute(thread);
    }
}