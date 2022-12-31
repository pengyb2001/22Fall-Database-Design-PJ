/**
 * 实现SQL连接
 * 可修改URL，USERNAME和PASSWORD以连接到相应的数据库
 */

package service.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLUtil
{
    private static final String URL = "jdbc:mysql://localhost:3306/admission_authority_management?serverTimezone=UTC&useSSL=false&autoReconnect=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "skybase911";
    
    public static Connection getConnection() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        if (con.isClosed())
        {
            throw new SQLException("Connection not available.");
        }
        return con;
    }
    
    public static void handleExceptions(Exception e)
    {
        System.out.println("##【SQL错误】请检查数据库并重新登录系统。");
        e.printStackTrace();
        System.exit(-1);
    }
}
