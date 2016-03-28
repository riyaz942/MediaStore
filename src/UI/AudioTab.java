/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import UI.Library.StretchIcon;
import Database.MediaBase;
import Util.MediaParser;
import Holders.AudioHolder;
import Holders.InfoHolder;
import Util.Print;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;


/**
 *
 * @author sasuke
 */
public class AudioTab extends javax.swing.JFrame {

    /**
     * Creates new form AudioTab
     */
    
    String totalColors[]={"#F44336","#E91E63","#9C27B0","#673AB7",
            "#3F51B5","#2196F3","#03A9F4","#00BCD4",
            "#009688","#8BC34A","#CDDC39","#FFEB3B",
            "#FFC107","#FF9800","#FF5722","#795548","#000000"};
    
    public AudioTab() {
        initComponents();
    
        
        MediaBase base =new  MediaBase();      
        
        try{
              setUpAlbum(base.queryGetAllAlbum());
        }
        catch(Exception e){
            Print.print(e.getMessage());
        }
       
        
         try{
              setUpArtist(base.queryGetAllArtist());
        }
        catch(Exception e){
            Print.print(e.getMessage());
        }
       
         
         
         try{
              setUpGenre(base.queryGetAllGenre());
        }
        catch(Exception e){
            Print.print(e.getMessage());
        }
        
        
        try {
            base.close();
        } catch (SQLException ex) {
            Logger.getLogger(AudioTab.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    private JScrollPane getJList(ArrayList<InfoHolder> holder,MouseListener listener,DisplayList dlist){
    
        
        DefaultListModel model = new DefaultListModel();     
        int max = holder.size();
        String[] colors= new String[max];
        int totalColor = totalColors.length;
        Random rand =new Random();
        
        for(int i=0;i<max;i++){          
            InfoHolder h = holder.get(i);
            model.addElement(h);         
            colors[i]= totalColors[rand.nextInt(totalColor)];
        }
        
        JList list = new JList(model);
        final ListRenderer renderer=new ListRenderer(colors,dlist);
        list.addMouseListener(listener);
        
        list.setCellRenderer(renderer);     
        list.setModel(model);
        
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);

       JScrollPane listScroller = new JScrollPane(list);
        
       return listScroller;
    }
    
    
    private void setUpAlbum(final ArrayList<InfoHolder> holder){ 
        
        MouseListener mouseListener = new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
       
           JList theList = (JList) mouseEvent.getSource();
              
            if (mouseEvent.getClickCount() == 2) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {                  
                    ArrayList<InfoHolder> h = null;
                        try {
                            MediaBase base = new MediaBase();         
                            h = base.queryGetAlbum(((AudioHolder)holder.get(index)).Album);
                            base.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(AudioTab.class.getName()).log(Level.SEVERE, null, ex);
                        }

                      new AudioList(h).setVisible(true);
                    }
                  }
                }
              };
        
        DisplayList dlist = new DisplayList(){
        
            @Override
            public void display(JLabel label,InfoHolder holder,Color color){
             
                StretchIcon icon;
                
                label.setPreferredSize(new Dimension(300,300));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                
                AudioHolder h = (AudioHolder) holder;
                
                 File file =new File(MediaParser.IMAGE_OUTPUT_FOLDER+h.Album+"-"+h.Artist+".jpg");
                 
                 
                if(file.exists()){
                    icon = new StretchIcon(file.getPath());
                }
               else{               
                  label.setBackground(color);
                  icon = new StretchIcon(MediaParser.DEFAULT_AUDIO_IMAGE);
                }
                
                     label.setIcon(icon);        
                label.setText(h.Album);                      
             }
        };
        
       jTabbedPane1.addTab("Albums",getJList(holder,mouseListener,dlist));
    }
    
    private void setUpArtist(final ArrayList<InfoHolder> holder){
    
        MouseListener mouseListener = new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
       
           JList theList = (JList) mouseEvent.getSource();
              
            if (mouseEvent.getClickCount() == 2) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {                  
                    ArrayList<InfoHolder> h = null;
                        try {
                            MediaBase base = new MediaBase();         
                            h = base.queryGetArtist(((AudioHolder)holder.get(index)).Artist);
                            base.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(AudioTab.class.getName()).log(Level.SEVERE, null, ex);
                        }

                      new AudioList(h).setVisible(true);
                    }
                  }
                }
              };
        
        
        DisplayList dlist = new DisplayList(){
        
            @Override
            public void display(JLabel label,InfoHolder holder,Color color){
    
                
                label.setPreferredSize(new Dimension(200,200));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                
                AudioHolder h = (AudioHolder) holder;
                
              File file =new File(MediaParser.IMAGE_OUTPUT_FOLDER+h.Album+"-"+h.Artist+".jpg");
                
                if(file.exists()){
                    StretchIcon icon = new StretchIcon(file.getPath());
                    label.setIcon(icon);
                }
               else{               
                  label.setBackground(color);
                }
                label.setText(h.Artist);                      
             }
        };
        
        jTabbedPane1.addTab("Artist",getJList(holder,mouseListener,dlist));
    }
    
    private void setUpGenre(final ArrayList<InfoHolder> holder){
        
        MouseListener mouseListener = new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
       
           JList theList = (JList) mouseEvent.getSource();
              
            if (mouseEvent.getClickCount() == 2) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {                  
                    ArrayList<InfoHolder> h = null;
                        try {
                            MediaBase base = new MediaBase();         
                            h = base.queryGetGenre(((AudioHolder)holder.get(index)).Genre);
                            base.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(AudioTab.class.getName()).log(Level.SEVERE, null, ex);
                        }

                      new AudioList(h).setVisible(true);
                    }
                  }
                }
              };        
        
        DisplayList dlist = new DisplayList(){
        
            @Override
            public void display(JLabel label,InfoHolder holder,Color color){   
                
                label.setPreferredSize(new Dimension(200,200));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                
                AudioHolder h = (AudioHolder) holder;
                 
                label.setBackground(color);
                
                label.setText(h.Genre);                      
             }
        };
        
        jTabbedPane1.addTab("Genre",getJList(holder,mouseListener,dlist));     
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AudioTab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AudioTab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AudioTab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AudioTab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AudioTab().setVisible(true);
            }
        });
    }
    
    
    public class ListRenderer extends DefaultListCellRenderer {

        Font font = new Font("helvitica", Font.BOLD, 24);
        String colors[];
        DisplayList dlist;
        
        public ListRenderer(String colors[],DisplayList dlist){
            this.colors=colors;
            this.dlist=dlist;
        }
        

        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            final JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                
                AudioHolder h = (AudioHolder)value;
                
                Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
                Border border = BorderFactory.createLineBorder(Color.BLUE);
            
                label.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
                
                //label.setBackground(Color.red);
                label.setFont(font);
            
                dlist.display(label,h,Color.decode(colors[index]));              
               
            return label;
        }
    }
    
    public interface DisplayList{       
        void display(JLabel label,InfoHolder holder,Color color);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
