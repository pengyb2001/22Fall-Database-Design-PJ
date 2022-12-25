/**
 * 实现护士信息管理功能
 */

package service.patient;

import model.user.ChiefNurse;
import model.user.User;
import model.user.WardNurse;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class NurseUtil
{
    // 查看某一区域的护士长
    public static User getChiefNurseByArea(String area)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findChiefNurseByArea = con.prepareStatement("select * from user " +
                    "where post='护士长' and area=?");
            findChiefNurseByArea.setString(1, area);
            try (ResultSet chiefNurseFound = findChiefNurseByArea.executeQuery())
            {
                if (chiefNurseFound.next())
                {
                    String id = chiefNurseFound.getString("u_id");
                    String username = chiefNurseFound.getString("username");
                    String password = chiefNurseFound.getString("password");
                    String name = chiefNurseFound.getString("name");
                    con.close();
                    return User.getInstance(id, username, password, name, area, ChiefNurse.TYPE);
                }
                else
                {
                    con.close();
                    return null;
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }
    
    // 查看某一区域的病房护士
    public static ArrayList<User> getWardNursesByArea(String area)
    {
        ArrayList<User> users = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findWardNursesByArea = con.prepareStatement("select * from user " +
                    "where post='病房护士' and area=?");
            findWardNursesByArea.setString(1, area);
            try (ResultSet wardNursesFound = findWardNursesByArea.executeQuery())
            {
                while (wardNursesFound.next())
                {
                    String id = wardNursesFound.getString("u_id");
                    String username = wardNursesFound.getString("username");
                    String password = wardNursesFound.getString("password");
                    String name = wardNursesFound.getString("name");
                    User user = User.getInstance(id, username, password, name, area, WardNurse.TYPE);
                    if (user != null)
                    {
                        users.add(user);
                    }
                }
            }
        
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return users;
    }
}
