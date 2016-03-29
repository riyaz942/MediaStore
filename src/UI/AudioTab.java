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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author sasuke
 */
public class AudioTab extends javax.swing.JFrame {
    
    String totalColors[]={
            "#F44336","#E91E63","#9C27B0","#673AB7",
            "#3F51B5","#2196F3","#03A9F4","#00BCD4",
            "#009688","#8BC34A","#CDDC39","#FFEB3B",
            "#FFC107","#FF9800","#FF5722","#795548"};
    ArrayList<InfoHolder> searchInfo;
    DefaultTableModel searchTableModel;
    
    public AudioTab() {
        initComponents();
    
        searchTableModel = new DefaultTableModel(new String[]{"Track","Album","Artist","Path"},0);    
        setTableOnClick();
       
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        MediaBase base =new  MediaBase();      
        
         try{
              setUpGenre(base.queryGetAllGenre());
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
              setUpAlbum(base.queryGetAllAlbum());
        }
        catch(Exception e){
            Print.print(e.getMessage());
        }
       
       
         
        
        try {
            base.close();
        } catch (SQLException ex) {
            Logger.getLogger(AudioTab.class.getName()).log(Level.SEVERE, null, ex);
        }
        
               jTabbedPane1.setSelectedIndex(0);
 
    }
    
    
    private void setTableOnClick(){
    
    jTable1.setModel(searchTableModel);
    jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTable1.rowAtPoint(evt.getPoint());
                int col = jTable1.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                     new AudioDetail(searchInfo.get(row)).setVisible(true);
                }
            }
        });}
    
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
                
                label.setPreferredSize(new Dimension(250,250));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                
                AudioHolder h = (AudioHolder) holder;
                
                File file =new File(MediaParser.IMAGE_OUTPUT_FOLDER+h.Album+"-"+h.Artist+".jpg");              
                 
                if(file.exists())
                    icon = new StretchIcon(file.getPath());               
               else               
                  icon = new StretchIcon(MediaParser.DEFAULT_AUDIO_IMAGE);
                
                label.setBackground(color);
                label.setIcon(icon);        
                label.setText(h.Album);                      
             }
        };
           jTabbedPane1.insertTab("Albums", null,getJList(holder,mouseListener,dlist),null,0);
     
    //   jTabbedPane1.addTab("Albums",getJList(holder,mouseListener,dlist));
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
              
              StretchIcon icon= new StretchIcon(MediaParser.DEFAULT_ARTIST_IMAGE);
              
                 if(file.exists())
                    icon = new StretchIcon(file.getPath());
                
                        label.setBackground(color);
                 
              
                label.setIcon(icon);              
                label.setText(h.Artist);                      
             }
        };
     
           jTabbedPane1.insertTab("Artist", null,getJList(holder,mouseListener,dlist),null,0);
    
      //    jTabbedPane1.addTab("Artist",getJList(holder,mouseListener,dlist));
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
                
                label.setPreferredSize(new Dimension(250,250));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                
                AudioHolder h = (AudioHolder) holder;
                
                StretchIcon icon = new StretchIcon(MediaParser.DEFAULT_GENRE_IMAGE);
                
                label.setIcon(icon);
                label.setBackground(color);               
                label.setText(h.Genre);                      
             }
        };
        
        jTabbedPane1.insertTab("Genre", null,getJList(holder,mouseListener,dlist),null,0);
      //jTabbedPane1.addTab("Genre",getJList(holder,mouseListener,dlist));     
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        RTrack = new javax.swing.JRadioButton();
        RAlbum = new javax.swing.JRadioButton();
        RArtist = new javax.swing.JRadioButton();
        RGenre = new javax.swing.JRadioButton();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setState(1);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        buttonGroup1.add(RTrack);
        RTrack.setText("Track");

        buttonGroup1.add(RAlbum);
        RAlbum.setText("Album");

        buttonGroup1.add(RArtist);
        RArtist.setText("Artist");

        buttonGroup1.add(RGenre);
        RGenre.setText("Genre");

        jButton3.setText("search");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setText("Search name");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jButton3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(RTrack)
                        .addGap(18, 18, 18)
                        .addComponent(RAlbum)
                        .addGap(18, 18, 18)
                        .addComponent(RArtist)
                        .addGap(18, 18, 18)
                        .addComponent(RGenre)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RTrack)
                    .addComponent(RAlbum)
                    .addComponent(RArtist)
                    .addComponent(RGenre))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Search", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        String searchName = jTextField1.getText();
        if(buttonGroup1.getSelection()!=null){
            
            MediaBase base = new MediaBase();
            try {

                if(RTrack.isSelected()){
                    searchInfo= base.querySearchTrack(searchName);
                }else if(RAlbum.isSelected()){
                    searchInfo= base.querySearchAlbum(searchName);
                }else if(RGenre.isSelected()){
                    searchInfo= base.querySearchGenre(searchName);
                }else if(RArtist.isSelected()){
                    searchInfo= base.querySearchArtist(searchName);
                }

                base.close();
            }catch (SQLException ex) {
                Logger.getLogger(VideosTabbed.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else
        JOptionPane.showMessageDialog(rootPane, "Select a search Mode");

        if(!(searchInfo==null||searchInfo.isEmpty())){

            while(searchTableModel.getRowCount()!=0){
                searchTableModel.removeRow(0);
            }
            
            for(InfoHolder holder : searchInfo){
                AudioHolder aholder = (AudioHolder) holder;
                searchTableModel.addRow(new String[]{aholder.Title,aholder.Album,aholder.Artist,aholder.Genre});
            }
            
        }else
        JOptionPane.showMessageDialog(rootPane,"No Result Found");
    }//GEN-LAST:event_jButton3ActionPerformed

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
                label.setForeground(Color.WHITE);
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
    private javax.swing.JRadioButton RAlbum;
    private javax.swing.JRadioButton RArtist;
    private javax.swing.JRadioButton RGenre;
    private javax.swing.JRadioButton RTrack;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
