/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sasuke
 */

import java.sql.*;

public class MediaBase {
    private static final String DRIVER="sun.jdbc.odbc.JdbcOdbcDriver";
    private static final String CONNECTION = "jdbc:odbc:"+"MediaBase";      
    
    public static Connection init() throws ClassNotFoundException, SQLException{
     
        Class.forName(DRIVER);
	Connection con=DriverManager.getConnection(CONNECTION);
    
        return con;
    }
    
    public static void insert(InfoHolder holder) {  
        
        try{
        Connection con = init();
        Statement st=con.createStatement();
        
        String sql = "insert into Main (File_Name,Folder_Name,Created_At,Path) values(?,?,?,?)";
        PreparedStatement pst=con.prepareStatement(sql);
       
        pst.setString(1,holder.File_Name);
        pst.setString(2,holder.Folder_Name);
        pst.setInt(3,(int)holder.Created_At);
        pst.setString(4,holder.Path);
        
        int result=pst.executeUpdate();
       
        if(result==1){       
        sql = "select LAST(ID) from Main";
        ResultSet rs = st.executeQuery(sql);
          rs.next();
        int id = rs.getInt(1);
        
        Print.print("ID:"+id);
        }
        else{
          Print.print("error in sql");
        
        }
        
        st.close();
        pst.close();
        }
        catch(Exception e){
            Print.print(e.getLocalizedMessage());
        }     
    }
}
