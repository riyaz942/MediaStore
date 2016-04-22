package UI;

import Database.MediaBase;
import Default.SplashScreen;
import Holders.InfoHolder;
import Holders.VideoHolder;
import UI.Library.StretchIcon;
import Util.MediaParser;
import Util.Print;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class VideosTabbed extends javax.swing.JFrame {
    
    DefaultTableModel searchTableModel;
    String[] columnHeaders;
    ArrayList<InfoHolder> searchInfo;
    
    public VideosTabbed() {
        columnHeaders = new String[]{
            "Sno",
            "File Name",
            "FolderName",
            "Path"};
        
        searchTableModel = new DefaultTableModel(columnHeaders,0);
        initComponents();
        
        setTableOnClick();
       
        jTable1.setModel(searchTableModel);
        
        setAllWatchedTab();
        setAllMoviesTab();     
        setAllVideosTab(); 
        
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
                    if(Rvideos.isSelected()){
                        try {
                            Desktop.getDesktop().open(new File(searchInfo.get(row).Path));
                        } catch (IOException ex) {
                            Logger.getLogger(VideosTabbed.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                        else
                     new MovieDetails(((VideoHolder)searchInfo.get(row)).Id).setVisible(true);
                }
            }
        });}
    
    private ArrayList<InfoHolder> getAllVideoHolder(){
    ArrayList<InfoHolder> holder = null;
    
        try {
            MediaBase base = new MediaBase();
            holder=base.queryGetAllVideos();
            base.close();            
        } catch (SQLException ex) {
            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return holder;
    }
    
    private ArrayList<InfoHolder> getAllMoviesHolder(){
    ArrayList<InfoHolder> holder = null;
        try {          
            MediaBase base = new MediaBase();
            
            holder = base.queryGetAllMovies();
            base.close();        
            
        } catch (SQLException ex) {
            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return holder;
    }
    
    private ArrayList<InfoHolder> getAllWatchedMoviesHolder(){
    
        ArrayList<InfoHolder> holder = null;
        try {
            MediaBase base = new MediaBase();          
            holder = base.queryGetAllWatchedMovies();
            base.close();                 
        } catch (SQLException ex) {
            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return holder;
    }
    
    private void setAllWatchedTab(){
        
    DefaultListModel model = new DefaultListModel();    
        final ArrayList<InfoHolder> holder = getAllWatchedMoviesHolder();
        
        if(holder!=null&&holder.size()>0){        
            for(InfoHolder h : holder){
            model.addElement(h.File_Name);
        }
        
        JList list = new JList(model);
        
        AudioTab.DisplayList display = new AudioTab.DisplayList() {
            @Override
            public void display(JLabel label, InfoHolder holder, Color color) {            
                VideoHolder h = (VideoHolder)holder;
            
                File f =new File(MediaParser.VIDEO_OUTPUT_FOLDER+h.File_Name+".jpg");
                StretchIcon icon;
                
                if(f.exists())
                    icon = new StretchIcon(f.getPath());
                else
                    icon = new StretchIcon(MediaParser.DEFAULT_MOVIE_IMAGE);
                
                label.setIcon(icon);
                
                Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
                Border border = BorderFactory.createLineBorder(Color.BLUE);            
                label.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
                
                //label.setBackground(Color.red);
                label.setPreferredSize(new Dimension(200,200));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                label.setText(h.File_Name);          
            }
        };
        
        final ListRenderer renderer=new ListRenderer(holder,display);
        
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
              JList theList = (JList) mouseEvent.getSource();
              if (mouseEvent.getClickCount() == 2) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                 // Object o = theList.getModel().getElementAt(index);
                 // System.out.println("Double-clicked on: " + o.toString());
                  
                   new MovieDetails(((VideoHolder) holder.get(index)).Id).setVisible(true);
                    
                }
              }
            }
          };
        
        list.addMouseListener(mouseListener);
        
        list.setCellRenderer(renderer);     
        list.setModel(model);
        
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(list);
        jTabbedPane1.insertTab("Watched", null, listScroller,"", 0);
        //jTabbedPane1.addTab("Watched",listScroller);  
            
        }else{
            JPanel panel = new JPanel();
            
            panel.setLayout(new FlowLayout());
            panel.add(new JLabel("No Videos Watched"));
          
            jTabbedPane1.insertTab("Watched", null,panel,"", 0);
            
            //jTabbedPane1.addTab("Watched",panel);
        }
    }
    
    private void setAllMoviesTab(){
    DefaultListModel model = new DefaultListModel();    
        final ArrayList<InfoHolder> holder = getAllMoviesHolder();
        
        if(holder!=null&&holder.size()>0){
        
            for(InfoHolder h : holder){
            model.addElement(h.File_Name);
        }
        
        JList list = new JList(model);
        
        AudioTab.DisplayList display = new AudioTab.DisplayList() {
            @Override
            public void display(JLabel label, InfoHolder holder, Color color) {            
                VideoHolder h = (VideoHolder)holder;
            
                File f =new File(MediaParser.VIDEO_OUTPUT_FOLDER+h.File_Name+".jpg");
                StretchIcon icon;
                
                if(f.exists())
                    icon = new StretchIcon(f.getPath());
                else
                    icon = new StretchIcon(MediaParser.DEFAULT_MOVIE_IMAGE);
                
                label.setIcon(icon);
                
                Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
                Border border = BorderFactory.createLineBorder(Color.BLUE);            
                label.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
                
                //label.setBackground(Color.red);
                label.setPreferredSize(new Dimension(200,200));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                label.setText(h.File_Name);          
            }
        };
        
        final ListRenderer renderer=new ListRenderer(holder,display);
        
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
              JList theList = (JList) mouseEvent.getSource();
              if (mouseEvent.getClickCount() == 2) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                  Object o = theList.getModel().getElementAt(index);
                //  System.out.println("Double-clicked on: " + o.toString());
                    
                    new MovieDetails(((VideoHolder) holder.get(index)).Id).setVisible(true);
                }
              }
            }
          };
        
        list.addMouseListener(mouseListener);
        
        list.setCellRenderer(renderer);     
        list.setModel(model);
        
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(list);
        jTabbedPane1.insertTab("Movies", null, listScroller,"", 0);
        
       // jTabbedPane1.addTab("Movies",listScroller);  
            
        }else{
            JPanel panel = new JPanel();
            JButton button= new JButton("Add");
            
            panel.setLayout(new FlowLayout());
            panel.add(new JLabel("No Movies found! click on the button to Add movies"));
            button.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {                  
                    dispose();
                    new AddMovies().setVisible(true);
                }
            });
        
            panel.add(button);
        
            jTabbedPane1.insertTab("Movies", null, panel,"", 0);
        
         //       jTabbedPane1.addTab("Movies",panel);
        }                
    }
    
    
    
     private void setAllVideosTab(){
    
        DefaultListModel model = new DefaultListModel();    
        final ArrayList<InfoHolder> holder = getAllVideoHolder();
        
        for(InfoHolder h : holder){
            model.addElement(h.File_Name);
        }
        
        JList list = new JList(model);
        
        AudioTab.DisplayList display = new AudioTab.DisplayList() {
            @Override
            public void display(JLabel label, InfoHolder holder, Color color) {            
                VideoHolder h = (VideoHolder)holder;
            
                 File f =new File(MediaParser.VIDEO_OUTPUT_FOLDER+h.File_Name+".jpg");
                StretchIcon icon;
                
                if(f.exists())
                    icon = new StretchIcon(f.getPath());
                else
                    icon = new StretchIcon(MediaParser.DEFAULT_VIDEO_IMAGE);
                
                label.setIcon(icon);
                
                Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
                Border border = BorderFactory.createLineBorder(Color.BLUE);            
                label.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
                
                //label.setBackground(Color.red);
                label.setPreferredSize(new Dimension(200,200));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                label.setText(h.File_Name);          
            }
        };
        
        final ListRenderer renderer=new ListRenderer(holder,display);
        
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
              JList theList = (JList) mouseEvent.getSource();
              if (mouseEvent.getClickCount() == 2) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                  Object o = theList.getModel().getElementAt(index);
                  System.out.println("Double-clicked on: " + o.toString());
                  
                    try {
                        //MediaBase base = new MediaBase();
                       //Print.print(((VideoHolder)holder.get(index)).Id+"");
                       
                       Desktop.getDesktop().open(new File(holder.get(index).Path));
                       //JLabel label = (JLabel)renderer.getListCellRendererComponent(theList, o, index, true, true);
                    } catch (IOException ex) {
                        Logger.getLogger(VideosTabbed.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                                   
                }
              }
            }
          };
        
        list.addMouseListener(mouseListener);
        
        list.setCellRenderer(renderer);     
        list.setModel(model);
        
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(list);
        jTabbedPane1.insertTab("All Videos", null, listScroller,"", 0);
        
       //jTabbedPane1.addTab("All Videos",listScroller);
    }
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        Rvideos = new javax.swing.JRadioButton();
        Rmovies = new javax.swing.JRadioButton();
        Rdirector = new javax.swing.JRadioButton();
        Rproducer = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Search name");

        buttonGroup1.add(Rvideos);
        Rvideos.setText("All Video");

        buttonGroup1.add(Rmovies);
        Rmovies.setText("Movies");

        buttonGroup1.add(Rdirector);
        Rdirector.setText("Director");

        buttonGroup1.add(Rproducer);
        Rproducer.setText("Producer");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(111, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jButton1)
                        .addGap(100, 100, 100))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(Rvideos)
                        .addGap(18, 18, 18)
                        .addComponent(Rmovies)
                        .addGap(18, 18, 18)
                        .addComponent(Rdirector)
                        .addGap(18, 18, 18)
                        .addComponent(Rproducer)
                        .addGap(193, 193, 193))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Rvideos)
                    .addComponent(Rmovies)
                    .addComponent(Rdirector)
                    .addComponent(Rproducer))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Search", jPanel1);

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       
        String searchName = jTextField1.getText();
        if(buttonGroup1.getSelection()!=null)
        {
            MediaBase base = new MediaBase();
            try {
            
                if(Rvideos.isSelected()){
                    searchInfo= base.querySearchVideo(searchName);
                }else if(Rmovies.isSelected()){
                    searchInfo= base.querySearchMovies(searchName);
                }else if(Rproducer.isSelected()){
                    searchInfo= base.querySearchProducer(searchName);
                }else if(Rdirector.isSelected()){
                    searchInfo= base.querySearchDirector(searchName);
                }
                
                base.close();
             }catch (SQLException ex) {
                    Logger.getLogger(VideosTabbed.class.getName()).log(Level.SEVERE, null, ex);
                }
        }else
            JOptionPane.showMessageDialog(rootPane, "Select a search");
        
        if(!(searchInfo==null||searchInfo.isEmpty())){
        
            while(searchTableModel.getRowCount()!=0){       
                searchTableModel.removeRow(0);
            }

            int index=0;      
            for(InfoHolder holder : searchInfo){      
                searchTableModel.addRow(new String[]{""+(++index),holder.File_Name,holder.Folder_Name,holder.Path});           
            }
        }else
            JOptionPane.showMessageDialog(rootPane,"No Result Found");
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(VideosTabbed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VideosTabbed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VideosTabbed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VideosTabbed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable(){
            public void run() {
                new VideosTabbed().setVisible(true);
            }
        });
    }
    
    
     public class ListRenderer extends DefaultListCellRenderer {

        Font font = new Font("helvitica", Font.BOLD, 24);
        ArrayList<InfoHolder> holder;
        AudioTab.DisplayList display;
        
        public ListRenderer(ArrayList<InfoHolder> holder,AudioTab.DisplayList display){
        this.holder = holder;
        this.display = display;
        }
        
        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            final JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            
                 display.display(label, holder.get(index), Color.yellow);
            
            return label;
        }
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Rdirector;
    private javax.swing.JRadioButton Rmovies;
    private javax.swing.JRadioButton Rproducer;
    private javax.swing.JRadioButton Rvideos;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}