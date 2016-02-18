/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sasuke
 */
public class ScanThread extends Thread{
    ProgressStage stages;
    
    public ScanThread(ProgressStage stages){
      this.stages = stages;
    }
    
    @Override
     public void run(){
         stages.progressStarted();
    }
    
}
