
import java.io.File;
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
public class FileUtil {
    
    public static String getFileType(String file){
    
        int max=file.length();
        
        for(int i=0;i<max;i++){   
            if(file.charAt(i)=='/'){
                file=file.substring(0,i);
                break;
            }         
        }
        return file;
    }
    
    public static String detectFile(Tika tika,File file){
    
            String fileType=null;
            
            try {
                fileType = tika.detect(file);
            }
            catch(Exception e){
                
        }
           return fileType; 
    }
    
}