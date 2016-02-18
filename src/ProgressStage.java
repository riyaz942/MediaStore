/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sasuke
 */
public interface ProgressStage {
   
    public void progressStarted();
   // public void objectsFound(int total);
    public void progressCurrent(int progress);
    public void progressCompleted(int total); // the total will return the total objects found
    
}
