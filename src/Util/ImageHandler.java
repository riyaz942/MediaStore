/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Holders.AudioHolder;
import Holders.VideoHolder;
import static Util.MediaParser.IMAGE_OUTPUT_FOLDER;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;

/**
 *
 * @author sasuke
 */
public class ImageHandler {
    
    public static void AudioImage(AudioHolder holder){
        
        File outputfile = new File(IMAGE_OUTPUT_FOLDER+holder.Album+"-"+holder.Artist+".jpg");
       
       if(!outputfile.exists())
       {
           Mp3File song;
       
        try {
            song = new Mp3File(holder.Path);
   
        if (song.hasId3v2Tag()){
             ID3v2 id3v2tag = song.getId3v2Tag();
             byte[] imageData = id3v2tag.getAlbumImage();
             ByteArrayInputStream stream=null;
                //converting the bytes to an image
             
                if(imageData!=null)
                stream=new ByteArrayInputStream(imageData);
           
             if(stream!=null){
              BufferedImage img = ImageIO.read(stream);
        
             if(img!=null)
             ImageIO.write(img, "jpg", outputfile);
             }
          }
        } catch (UnsupportedTagException ex) {
            Logger.getLogger(MediaParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDataException ex) {
            Logger.getLogger(MediaParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MediaParser.class.getName()).log(Level.SEVERE, null, ex);
        }
       }
    }
    
    
    public static void VideoImage(VideoHolder holder){
    final File f = new File(MediaParser.VIDEO_OUTPUT_FOLDER+holder.File_Name+".jpg");
    
         if(!f.exists()){
             
             int frameNumber = 150;
                    try {
                           File file = new File(holder.Path);
                            BufferedImage frame = FrameGrab.getFrame(file, frameNumber);
                            
                            if(frame!=null){
                           frame =Thumbnails.of(frame).size(500, 500).asBufferedImage();

                            ImageIO.write(frame, "jpg", f);
                            }
                            
                  } catch (IOException ex) {
                      Print.print("Error Extracting image");
                  } catch (JCodecException ex) {
                      Print.print("Error Extracting image :"+ex.getLocalizedMessage());
                  } 
                    
        }
    }
   
    
}
