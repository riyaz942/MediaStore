
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
    
            Tika tika = new Tika();
            String fileType=null;
            try {
                fileType = tika.detect(file);
            }
            catch(Exception e){
                
        }
           return fileType; 
    }
}
