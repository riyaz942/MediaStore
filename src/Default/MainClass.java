/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default;

import Database.MediaBase;
import Holders.InfoHolder;
import UI.Intro;
import Util.MediaParser;
import Util.QueryBuilder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sasuke
 */
public class MainClass {
    
    private static ArrayList<InfoHolder> getValues(){
    
        ArrayList<InfoHolder> holder=null;
        
        try {
            MediaBase base = new MediaBase();
            String sql = "Select * from Main";                
            String[] basicCol={QueryBuilder.COL_ID};
             
            String[] specificCol={};
             
             holder = base.getValues(sql,MediaParser.TYPE_IMAGE,basicCol,specificCol);           
            base.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return holder;
    } 
    
    public static void main(String argv[]){   
        
        ArrayList<InfoHolder> holder = getValues();
        
        if(holder!=null&&holder.size()>0){
            new SplashScreen().setVisible(true);
        }else{
            new Intro().setVisible(true);
        }
        
    }
    
}
