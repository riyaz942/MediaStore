package Default;


import java.util.ArrayList;

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
    public void progressFounds(int total);
    public void progressCurrent(int progress,String message);
    public void progressCompleted();    
}
