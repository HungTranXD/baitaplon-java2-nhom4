package database;

import java.sql.*;
import java.util.ArrayList;

public class Connector {
    public final static String connectionString = "jdbc:mysql://localhost:3306/t2204m-java1?useUnicode=yes&characterEncoding=UTF-8";
    public final static String user = "root";
    public final static String pwd = "";

    private Connection conn;

    public Connector() throws Exception{
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection(connectionString, user, pwd);
    }

    private Statement getStatement() throws Exception {
        return this.conn.createStatement();
    }

    //Hàm cho câu lệnh SELECT
    public ResultSet query(String sql) throws Exception {
        return this.getStatement().executeQuery(sql);
    }

    //Hàm cho câu lệnh INSERT, UPDATE, DELETE (vì executeQuery không báo exeption cho các câu SQL trên nên dùng executeUpdate
    public boolean execute(String sql) throws Exception {
        return this.getStatement().executeUpdate(sql) > 0;
    }

    //Prepared statement
    private PreparedStatement getPreparedStatement (String sql) throws Exception{
        return this.conn.prepareStatement(sql);
    }

    public ResultSet query(String sql, ArrayList parameters) throws Exception{
        PreparedStatement stt = this.getPreparedStatement(sql);
        for(int i=0;i<parameters.size();i++){
            if(parameters.get(i) instanceof String){
                stt.setString(i+1,(String)parameters.get(i));
            }else if(parameters.get(i) instanceof Integer){
                stt.setInt(i+1,(int) parameters.get(i));
            }else if(parameters.get(i) instanceof Double){
                stt.setDouble(i+1,(double) parameters.get(i));
            }
        }
        return stt.executeQuery();
    }

    public boolean execute(String sql, ArrayList parameters) throws Exception{
        PreparedStatement stt = this.getPreparedStatement(sql);
        for(int i=0;i<parameters.size();i++){
            if(parameters.get(i) instanceof String){
                stt.setString(i+1,(String)parameters.get(i));
            }else if(parameters.get(i) instanceof Integer){
                stt.setInt(i+1,(int) parameters.get(i));
            }else if(parameters.get(i) instanceof Double){
                stt.setDouble(i+1,(double) parameters.get(i));
            }
        }
        return stt.executeUpdate() > 0;
    }
}
