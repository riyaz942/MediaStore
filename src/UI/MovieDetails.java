/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Database.MediaBase;
import Holders.InfoHolder;
import Holders.VideoHolder;
import UI.Library.StretchIcon;
import Util.FileUtil;
import Util.MediaParser;
import Util.Print;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author sasuke
 */
public class MovieDetails extends javax.swing.JFrame {

    /**
     * Creates new form MovieDetails
     */
    VideoHolder holder;
    
    public MovieDetails(int Id){
     
        
        
      MediaBase base =new MediaBase();
        try {
            holder =base.queryGetMovieDetail(Id);
            
        } catch (SQLException ex) {
            Logger.getLogger(MovieDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
     
      initComponents();
      displayInfo();
      
    }
    
    
    
    public MovieDetails() {
        initComponents();
    }
    
    private void checkDirectors(){
    if(holder.Producer==null||holder.Producer.isEmpty())
            producer.setText("Empty");
         else
            producer.setText(holder.Producer);
        
       if(holder.Director==null||holder.Director.isEmpty())
            director.setText("Empty");
        else
            director.setText(holder.Director);      
    }
    
    private void displayInfo(){
        
        checkDirectors();
        
        path.setText(holder.Path);
        save.setVisible(false);
    
        File file = new File(MediaParser.VIDEO_OUTPUT_FOLDER+holder.File_Name+".jpg");
        StretchIcon icon;
        
        if(file.exists())
             icon = new StretchIcon(file.getPath());
        else
            icon = new StretchIcon(MediaParser.DEFAULT_MOVIE_IMAGE);
        
        videoImage.setIcon(icon);          
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        videoImage = new javax.swing.JLabel();
        director = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        producer = new javax.swing.JTextField();
        edit = new javax.swing.JButton();
        save = new javax.swing.JButton();
        path = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        director.setEditable(false);

        jLabel2.setText("Director");

        jLabel3.setText("Producer");

        producer.setEditable(false);

        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        jLabel4.setText("Path");

        jButton1.setText("open");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Titillium Web", 0, 18)); // NOI18N
        jLabel1.setText("Movie Detail");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(videoImage, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                                .addComponent(edit)
                                .addGap(43, 43, 43)
                                .addComponent(save))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(director)
                                    .addComponent(producer, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                        .addComponent(path, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(250, 250, 250))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(path, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(9, 9, 9)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(videoImage, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(director, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(producer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edit)
                            .addComponent(save)
                            .addComponent(jButton1))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        
        producer.setEditable(true);
        director.setEditable(true);
        save.setVisible(true);
        
    }//GEN-LAST:event_editActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        
        producer.setEditable(false);
        director.setEditable(false);
        save.setVisible(false);
        
        String pro = producer.getText();
        String dir = director.getText();
        
        if(FileUtil.containsNumeric(pro)){
            JOptionPane.showMessageDialog(rootPane, "Producer shoudn't contain any number");
            checkDirectors();
          return;
        }
          if(FileUtil.containsNumeric(dir)){
            JOptionPane.showMessageDialog(rootPane, "Director shoudn't contain any number"); 
            checkDirectors();
           return;
          }
          
        MediaBase base = new MediaBase();
        try {
            String sql = "update Movies set Director=?,Producer=? where ID=?";
            PreparedStatement st= base.getPreparedStatement(sql);
            
            st.setString(1, dir);
            st.setString(2, pro);
            st.setInt(3, holder.Id);
            
            int result =st.executeUpdate();
            
            if(result>0)
                Print.print("Row updated");
            else
                Print.print("Row not updated");
            
            st.close();
            
            base.close();
        } catch (SQLException ex) {
            Logger.getLogger(MovieDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_saveActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        MediaBase base = new MediaBase();
                   
        try { 
                        int id=holder.Id;
                        base.updateWatchedAndRecentlyViewed(id);                    
                        base.close();
                       
            Desktop.getDesktop().open(new File(holder.Path));
        } catch (IOException ex) {
            Logger.getLogger(MovieDetails.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MovieDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField director;
    private javax.swing.JButton edit;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel path;
    private javax.swing.JTextField producer;
    private javax.swing.JButton save;
    private javax.swing.JLabel videoImage;
    // End of variables declaration//GEN-END:variables
}
