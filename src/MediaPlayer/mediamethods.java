package MediaPlayer;

//-----------------------------PACKAGES--------------------------------------
import UI.Library.StretchIcon;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.media.*;
import javax.media.Time;
import java.net.URL;
import java.net.MalformedURLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.media.ControllerListener;
import javax.media.ControllerEvent;
import javax.media.EndOfMediaEvent;
import javax.swing.table.DefaultTableModel;
//------------------------********************----------------------------------
public class mediamethods extends javax.swing.JFrame {

//----------------------------GLOBAL VARIABLE----------------------------------
String aurl=null;
boolean audiomode=false;
int back=1;
Player mplayer=null;
JFrame frame=null;
URL url=null;
int a;
double rate=1.0;
JFileChooser choose=new JFileChooser();
boolean mute=false;
String path;
int time=1;
DefaultTableModel ab=null;
DefaultTableModel ab1=null;
//------------------------****************--------------------------------------


//----------------------------CODE FOR TIMER -----------------------------------
ActionListener li=new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {   time=time+1;
        video.setValue((int)mplayer.getMediaTime().getSeconds());
        curmedia.setText((int)mplayer.getMediaTime().getSeconds()/60+":"+(int)mplayer.getMediaTime().getSeconds()%60);
    }};

Timer timer=new Timer(1000,li);
//----------------------------****************----------------------------------


//-------------------------CODE FOR END OF MEDIA EVENT--------------------------
ControllerListener la=new ControllerListener(){
    public void controllerUpdate(ControllerEvent r)
    {
        if(r instanceof EndOfMediaEvent )
        {   if(table.getRowCount()-1==table.getSelectedRow())

            {
                mplayer.setMediaTime(new Time(0.0));
            video.setValue(0);
            time=0;
            curmedia.setText("0:0");
            timer.stop();
            play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/play_unpressed.gif")));
            }
            else
            { 
                  url=(URL)ab.getValueAt(table.getSelectedRow()+1,3);
                    playmedia();
     table.changeSelection(table.getSelectedRow()+1, table.getSelectedColumn(), false,false);
            
            }
        }
    }
  };
    
    //-------------------------*********************----------------------------


 public mediamethods(){
   initComponents();
   jLabel1.setBackground(new Color(55,00,00,00));
 }
 
 private void setBackgroundImage(String path,String imagePath){
 BufferedImage image = getImage(path);
 StretchIcon icon=null;
 
    if(image!=null)
        icon = new StretchIcon(image); 
    else if(imagePath!=null)
        if(new File(imagePath).exists())
          icon = new StretchIcon(imagePath);
    

    if(icon!=null)    
     background.setIcon(icon);
    else 
     background.setIcon(null);
 }
 
 public mediamethods(String path,String imagePath){    
     initComponents();
    jLabel1.setBackground(new Color(55,00,00,00));
     setBackgroundImage(path,imagePath);
     
    try {
        url = new URL("file","",path);
        this.path=path;
        setName();
        
            if(a==0){
                 playmedia();
                 video.setValue(0);
                }
    
    } catch (MalformedURLException ex) {
        Logger.getLogger(mediamethods.class.getName()).log(Level.SEVERE, null, ex);
    }        
 }
 
 public void geturl()
   {

    a=choose.showOpenDialog(null);
       if(a==0){
              try
              {
                  aurl = choose.getSelectedFile().toString();
                  url=new URL("file","",aurl);
                  path=choose.getSelectedFile().getName();
                  setName();
                  setBackgroundImage(aurl,null);
              }
              catch (MalformedURLException ex){
                  JOptionPane.showMessageDialog(null,"invalid url");
              }
       }

   }
 
 private void setName(){
 
     for(int r=0;r<=path.length()-1;r++)
                {
                    if(path.charAt(r)=='.')
                    {
                        path=path.substring(0,r);

                        break;
                    }
                }
           
 }
 
 
 private BufferedImage getImage(String path){
 File outputfile = new File(path);
 BufferedImage img=null;
       if(!outputfile.exists()){
           Mp3File song;
       
        try {
            song = new Mp3File(path);
   
        if (song.hasId3v2Tag()){
             ID3v2 id3v2tag = song.getId3v2Tag();
             byte[] imageData = id3v2tag.getAlbumImage();
             ByteArrayInputStream stream=null;
              
                if(imageData!=null)
                stream=new ByteArrayInputStream(imageData);
           
             if(stream!=null)
              img = ImageIO.read(stream);
          }
        } catch (UnsupportedTagException ex) {
            Logger.getLogger(mediamethods.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDataException ex) {
            Logger.getLogger(mediamethods.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(mediamethods.class.getName()).log(Level.SEVERE, null, ex);
        }
       }
       
     return img;
 }
    
  //-----------------METHOD TO GET URL WITH JFILECHOOSER------------------------
 

 //--------------------**************************-------------------------------


//---------------------***********************----------------------------------

//--------------------METHOD TO PLAY MEDIA--------------------------------------
   public void playmedia()
   {
          rate=1.0;
           if(frame!=null)
           {

               frame.dispose();
               mplayer.close();
           }

       try {mplayer = Manager.createRealizedPlayer(url);}
        catch (Exception ex){JOptionPane.showMessageDialog(null, "ERROR");}




           frame=new JFrame();
           Component vid=mplayer.getVisualComponent();

           if(vid!=null&&audiomode==false)
         {
           frame.setSize(400, 400);
             frame.setVisible(true);
             frame.add(vid);

         }

       video.setMaximum((int) mplayer.getDuration().getSeconds());
     video.setValue(0);
     mplayer.start();
     mplayer.getGainControl().setMute(mute);
     durmedia.setText("/"+(int)mplayer.getDuration().getSeconds()/60+":"+(int)mplayer.getDuration().getSeconds()%60);
       play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/pause_un.gif")));





   name.setText(path);
    srate.setText(""+rate);
     mplayer.getGainControl().setLevel((float)volume.getValue()/10);
     timer.start();
     time=0;
     mplayer.addControllerListener(la);



   }

//---------------------------*****************----------------------------------


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frame1 = new javax.swing.JFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        frame2 = new javax.swing.JFrame();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        open = new javax.swing.JLabel();
        play = new javax.swing.JLabel();
        mute_l = new javax.swing.JLabel();
        volume = new javax.swing.JSlider();
        slow = new javax.swing.JLabel();
        fast = new javax.swing.JLabel();
        video = new javax.swing.JSlider();
        stop = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        srate = new javax.swing.JLabel();
        curmedia = new javax.swing.JLabel();
        durmedia = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        background = new javax.swing.JLabel();

        frame1.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NAME", "DURATION", "FORMAT", "LOCATION"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        frame1.getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 400, 200));

        jButton1.setText("Open Playlist menu");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        frame1.getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 160, 40));

        jButton2.setText("create palylist");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        frame1.getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 160, 40));

        jButton4.setText("remove");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        frame1.getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 90, 40));

        jButton5.setText("add");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        frame1.getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 90, 40));

        frame2.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton3.setText("ok");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        frame2.getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 50, 30));

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "playlist"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(table1);

        frame2.getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 280, 200));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setBounds(new java.awt.Rectangle(0, 0, 441, 300));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/other/open_un.gif"))); // NOI18N
        open.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                openMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                openMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                openMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                openMouseReleased(evt);
            }
        });
        getContentPane().add(open, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, 40));

        play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/play_unpressed.gif"))); // NOI18N
        play.setToolTipText("play");
        play.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                playMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                playMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                playMouseReleased(evt);
            }
        });
        getContentPane().add(play, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 50, 50));

        mute_l.setIcon(new javax.swing.ImageIcon(getClass().getResource("/other/mute.gif"))); // NOI18N
        mute_l.setToolTipText("mute");
        mute_l.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mute_lMouseClicked(evt);
            }
        });
        getContentPane().add(mute_l, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 30, 23, 23));

        volume.setBackground(new java.awt.Color(153, 153, 153));
        volume.setMaximum(10);
        volume.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        volume.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                volumeMouseWheelMoved(evt);
            }
        });
        volume.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                volumeStateChanged(evt);
            }
        });
        getContentPane().add(volume, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, 90, 20));

        slow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/ss_un.gif"))); // NOI18N
        slow.setToolTipText("slow");
        slow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                slowMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                slowMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                slowMouseReleased(evt);
            }
        });
        getContentPane().add(slow, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 40, 40));

        fast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/ff_un.gif"))); // NOI18N
        fast.setToolTipText("fast");
        fast.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fastMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fastMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fastMouseReleased(evt);
            }
        });
        getContentPane().add(fast, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 310, 40, 40));

        video.setBackground(new java.awt.Color(153, 153, 153));
        video.setForeground(new java.awt.Color(51, 51, 51));
        video.setToolTipText("");
        video.setValue(0);
        video.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, new java.awt.Color(255, 255, 255), null, null));
        video.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                videoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                videoMouseReleased(evt);
            }
        });
        video.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                videoStateChanged(evt);
            }
        });
        getContentPane().add(video, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, 370, 20));

        stop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/stop_un.gif"))); // NOI18N
        stop.setToolTipText("stop");
        stop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stopMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                stopMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                stopMouseReleased(evt);
            }
        });
        getContentPane().add(stop, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 150, 50, 40));

        name.setFont(new java.awt.Font("Electrox ", 0, 24)); // NOI18N
        name.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, 290, 40));

        srate.setFont(new java.awt.Font("Matura MT Script Capitals", 1, 18)); // NOI18N
        srate.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(srate, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 280, 40, 30));

        curmedia.setBackground(new java.awt.Color(102, 102, 102));
        curmedia.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        curmedia.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(curmedia, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 330, 50, 30));

        durmedia.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        durmedia.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(durmedia, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 330, 50, 30));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 510, 380));

        background.setBackground(new java.awt.Color(0, 0, 0));
        background.setOpaque(true);
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 510, 380));

        pack();
    }// </editor-fold>//GEN-END:initComponents

//--------------------------CODE FOR OPEN BUTTON -------------------------------
    private void openMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openMouseClicked
geturl();
if(a==0)
{
 playmedia();
 video.setValue(0);
}
}//GEN-LAST:event_openMouseClicked


//-----------------------------CODE FOR PLAY BUTTON ----------------------------
    private void playMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playMouseClicked
int state=mplayer.getState();

        if(state==500)
        {     play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/pause_un.gif")));
            if(mplayer.getMediaTime()==mplayer.getDuration())
              mplayer.setMediaTime(new Time(0.0));
            mplayer.start();
            timer.start();
        }

         else if(state==600)
        {mplayer.stop();
         timer.stop();
           play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/play_unpressed.gif")));
         }
}//GEN-LAST:event_playMouseClicked


//--------------------------CODE FOR MUTE BUTTON--------------------------------
    private void mute_lMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mute_lMouseClicked
if(mplayer.getGainControl().getMute()==false)
{
    mplayer.getGainControl().setMute(true);
 mute_l.setIcon(new javax.swing.ImageIcon(getClass().getResource("/other/mute_m.gif")));
     mute=true;
}
else
{   mplayer.getGainControl().setMute(false);
     mute_l.setIcon(new javax.swing.ImageIcon(getClass().getResource("/other/mute.gif")));
mute=false;
}
}//GEN-LAST:event_mute_lMouseClicked


//--------------------------CODE FOR SLOW BUTTON--------------------------------
    private void slowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_slowMouseClicked
       rate=rate-0.5;
        mplayer.setRate((float)rate);
         srate.setText(""+rate);

}//GEN-LAST:event_slowMouseClicked


//--------------------------CODE FOR FASTFORWARD BUTTON--------------------------------
    private void fastMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fastMouseClicked
rate=rate+0.5;
        mplayer.setRate((float)rate);
        srate.setText(""+rate);

}//GEN-LAST:event_fastMouseClicked

//--------------------------CODE FOR AUDIO SLIDER--------------------------------
    private void volumeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_volumeStateChanged
double sound=volume.getValue();
sound=sound/10;
mplayer.getGainControl().setLevel((float)sound);

}//GEN-LAST:event_volumeStateChanged


//--------------------------CODE FOR VIDEO SLIDER--------------------------------
    private void videoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_videoStateChanged
if(mplayer.getDuration()==mplayer.getMediaTime())
{
    mplayer.setMediaTime(new Time(0.0));
    video.setValue(0);
    timer.stop();
}

    }//GEN-LAST:event_videoStateChanged

    private void volumeMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_volumeMouseWheelMoved

    }//GEN-LAST:event_volumeMouseWheelMoved


 //-----------------------------CODE FOR STOP BUTTON--------------------------------------
    private void stopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopMouseClicked
mplayer.setMediaTime(new Time(0.0));
mplayer.stop();
  play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/play_unpressed.gif")));
 
}//GEN-LAST:event_stopMouseClicked

    private void playMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playMousePressed
       if(mplayer==null||mplayer.getState()==500) 
             play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/play_pressed.gif")));

       else if(mplayer.getState()==600)
        play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/pause_p.gif")));

    }//GEN-LAST:event_playMousePressed

    private void playMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playMouseReleased

         if(mplayer==null||mplayer.getState()==500)
 play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/play_unpressed.gif")));
         else if(mplayer.getState()==600)
        play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/pause_un.gif")));
      
    }//GEN-LAST:event_playMouseReleased

    private void stopMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopMousePressed
stop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/stop_p.gif")));
      
    }//GEN-LAST:event_stopMousePressed

    private void stopMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopMouseReleased
stop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/stop_un.gif")));
      
    }//GEN-LAST:event_stopMouseReleased

    private void slowMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_slowMousePressed
  slow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/ss_p.gif")));        // TODO add your handling code here:
}//GEN-LAST:event_slowMousePressed

    private void slowMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_slowMouseReleased
  slow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/ss_un.gif")));
      
    }//GEN-LAST:event_slowMouseReleased

    private void fastMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fastMousePressed
  fast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/ff_p.gif")));        // TODO add your handling code here:
    }//GEN-LAST:event_fastMousePressed

    private void fastMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fastMouseReleased
      fast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newpackage/ff_un.gif")));
      
    }//GEN-LAST:event_fastMouseReleased

    private void openMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openMousePressed
       open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/other/open_p.gif")));

    }//GEN-LAST:event_openMousePressed

    private void openMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openMouseReleased
        open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/other/open_un.gif")));

    }//GEN-LAST:event_openMouseReleased

    private void openMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openMouseEntered
 open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/other/open_ungl.gif")));        // TODO add your handling code here:
    }//GEN-LAST:event_openMouseEntered

    private void openMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openMouseExited
 open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/other/open_un.gif")));        // TODO add your handling code here:
    }//GEN-LAST:event_openMouseExited

    private void videoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_videoMousePressed

        // TODO add your handling code here:
    }//GEN-LAST:event_videoMousePressed

    private void videoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_videoMouseReleased
      mplayer.setMediaTime(new Time((float)video.getValue()));
      time=video.getValue();
      
    }//GEN-LAST:event_videoMouseReleased


    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
aurl=ab.getValueAt(table.getSelectedRow(),3).toString();
        try {
            url = new URL("file", "", aurl);
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        }
playmedia();
 name.setText(""+ab.getValueAt(table.getSelectedRow(),0));

    }//GEN-LAST:event_tableMouseClicked

//--------------------------CODE TO OPEN PLAYLIST MENU--------------------------------
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

//--------------------------CODE FOR CREATE PLAYLIST BUTTON --------------------
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
     
    }//GEN-LAST:event_jButton2ActionPerformed


//--------------------------CODE TO SELECT PLAYLIST ITEM--------------------------------
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

    }//GEN-LAST:event_jButton3ActionPerformed

//--------------------------CODE FOR REMOVE BUTTON--------------------------------
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
ab.removeRow(table.getSelectedRow());  
    }//GEN-LAST:event_jButton4ActionPerformed


//--------------------------CODE FOR ADD URL TO TABLE --------------------------------
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       geturl();
       playmedia();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        mplayer.stop();
        mplayer.close();
        mplayer.deallocate();
    }//GEN-LAST:event_formWindowClosing


   
 
    /**
    * @param args the command line arguments
    */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {   new mediamethods().setBounds(0, 0, 440, 300);
                new mediamethods().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JLabel curmedia;
    private javax.swing.JLabel durmedia;
    private javax.swing.JLabel fast;
    private javax.swing.JFrame frame1;
    private javax.swing.JFrame frame2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel mute_l;
    private javax.swing.JLabel name;
    private javax.swing.JLabel open;
    private javax.swing.JLabel play;
    private javax.swing.JLabel slow;
    private javax.swing.JLabel srate;
    private javax.swing.JLabel stop;
    private javax.swing.JTable table;
    private javax.swing.JTable table1;
    private javax.swing.JSlider video;
    private javax.swing.JSlider volume;
    // End of variables declaration//GEN-END:variables

}
