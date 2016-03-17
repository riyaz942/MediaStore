package UI.Video;

import Database.MediaBase;
import Default.SplashScreen;
import Holders.InfoHolder;
import Holders.VideoHolder;
import UI.Audio.AudioTab;
import UI.Library.StretchIcon;
import Util.MediaParser;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class VideosTabbed extends javax.swing.JFrame {

    public VideosTabbed() {
        initComponents();
        setAllVideosTab();
        setAllMoviesTab();
    }
    
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
                  System.out.println("Double-clicked on: " + o.toString());
                  
                   new MovieDetails((VideoHolder) holder.get(index)).setVisible(true);
                    
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
        jTabbedPane1.addTab("Movies",listScroller);  
            
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
        
                jTabbedPane1.addTab("Movies",panel);
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
                  System.out.println("Double-clicked on: " + o.toString());
                  
                    try {
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
        
       jTabbedPane1.addTab("All Images",listScroller);
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
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
        java.awt.EventQueue.invokeLater(new Runnable() {
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
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}