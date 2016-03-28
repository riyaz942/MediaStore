package UI;

import Database.MediaBase;
import Default.SplashScreen;
import UI.Library.StretchIcon;
import Holders.ImageHolder;
import Holders.InfoHolder;
import UI.AudioTab.DisplayList;
import Util.MediaParser;
import Util.QueryBuilder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
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

public class ImagesTabbed extends javax.swing.JFrame {
    
    public ImagesTabbed() {
        initComponents();
        setAllImagesTab();
        setFolderTab();
    }
   
    
    private ArrayList<InfoHolder> getAllImageHolder(){
    ArrayList<InfoHolder> holder = null;

        try {
            MediaBase base = new MediaBase();
            String sql = "Select * from Images,Main where Images.Main_Id=Main.ID";                
            String[] basicCol={QueryBuilder.COL_ID,
                 QueryBuilder.COL_FILE_NAME,
                 QueryBuilder.COL_FOLDER_NAME,
                 QueryBuilder.COL_RECENTLY_VIEWED,
                 QueryBuilder.COL_PATH};
             
             String[] specificCol={
                 QueryBuilder.COL_HEIGHT,
                 QueryBuilder.COL_WIDTH};
             
            holder = base.getValues(sql,MediaParser.TYPE_IMAGE,basicCol,specificCol);           
            base.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return holder;
    };
    
    private void setFolderTab(){
    
        DefaultListModel model = new DefaultListModel();  
       
         ArrayList<InfoHolder> holder=null;
           
        try {
            
            MediaBase base = new MediaBase();        
            holder=base.getImageFolderValues();          
            
            base.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ImagesTabbed.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        for(InfoHolder h : holder){
            model.addElement(h.File_Name);
        }
        
        JList list = new JList(model);
        final ArrayList<InfoHolder> finalHolder = holder;
        
        DisplayList display = new DisplayList() {
            @Override
            public void display(JLabel label, InfoHolder holder, Color color) {            
                ImageHolder h = (ImageHolder)holder;
            
                StretchIcon icon = new StretchIcon(h.Path);
                label.setIcon(icon);
                
                Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
                Border border = BorderFactory.createLineBorder(Color.BLUE);            
                label.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
                
                //label.setBackground(Color.red);
                label.setPreferredSize(new Dimension(200,200));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                label.setText(h.Folder_Name);          
            }
        };
        
      final ListRenderer renderer=new ListRenderer(holder,display);
        
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
              JList theList = (JList) mouseEvent.getSource();
              if (mouseEvent.getClickCount() == 2) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                  
                    new ImageList(finalHolder.get(index).Folder_Name).setVisible(true);

                    //Object o = theList.getModel().getElementAt(index);
                  //System.out.println("Double-clicked on: " + o.toString());
                
                //JLabel label = (JLabel)renderer.getListCellRendererComponent(theList, o, index, true, true);
                
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
        
       jTabbedPane1.addTab("Folders",listScroller);
        
    }
    
    private void setAllImagesTab(){
    
        DefaultListModel model = new DefaultListModel();    
        ArrayList<InfoHolder> holder = getAllImageHolder();
        
        for(InfoHolder h : holder){
            model.addElement(h.File_Name);
        }
        
        JList list = new JList(model);
        
        DisplayList display = new DisplayList() {
            @Override
            public void display(JLabel label, InfoHolder holder, Color color) {            
                ImageHolder h = (ImageHolder)holder;
            
                StretchIcon icon = new StretchIcon(h.Path);
                label.setIcon(icon);
                
                Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
                Border border = BorderFactory.createLineBorder(Color.BLUE);            
                label.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
                
                //label.setBackground(Color.red);
                label.setPreferredSize(new Dimension(300,300));
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
                  
                
                //JLabel label = (JLabel)renderer.getListCellRendererComponent(theList, o, index, true, true);
                
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(ImagesTabbed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImagesTabbed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImagesTabbed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImagesTabbed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ImagesTabbed().setVisible(true);
            }
        });
    }
    
    public class ListRenderer extends DefaultListCellRenderer {

        Font font = new Font("helvitica", Font.BOLD, 24);
        ArrayList<InfoHolder> holder;
        DisplayList display;
        
        public ListRenderer(ArrayList<InfoHolder> holder,DisplayList display){
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
