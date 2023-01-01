/**
 * 实现账户管理功能
 */

package service.account;

import model.account.FacultyAdmin;
import model.account.Instructor;
import model.account.Student;

import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccountUtil
{
    public static boolean usernameExists(String username)
    {
        int len = username.length();
        String type;
        switch (len) {
            case 4 -> {type = "faculty_administrator";
            return getFacultyAdministrator(username) != null;}
            case 5 -> {type = "instructor";
            return getInstructor(username) != null;}
            case 11 -> {type = "student";
            return getStudent(username) != null;}
            default -> {
                type = "default";
                return false;
            }
        }
    }


    
//    public static User getUser(String username, String password)
//    {
//        User user = getUser(username);
//        // 检查密码是否正确
//        if (user != null && user.getPassword().equals(password))
//        {
//            return user;
//        }
//        else
//        {
//            return null;
//        }
//    }
    
    public static Student getStudent(String username)
    {

        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findUserByUsername = con.prepareStatement("select * from student where ID=?");
            findUserByUsername.setString(1, username);
            try (ResultSet usersFound = findUserByUsername.executeQuery())
            {
                if (usersFound.next())
                {
                    String ID = usersFound.getString("ID");
                    String name = usersFound.getString("name");

                    String phone = usersFound.getString("phone");
                    String email = usersFound.getString("email");
                    String personal_address = usersFound.getString("personal_address");
                    String home_address = usersFound.getString("home_address");
                    String identity_type = usersFound.getString("identity_type");
                    String id_num = usersFound.getString("id_num");
                    String in_school = usersFound.getString("in_school");
                    String class_name = usersFound.getString("class_name");
                    String faculty_name = usersFound.getString("faculty_name");
                    con.close();
                    return new Student(ID, name, phone, email, personal_address, home_address, identity_type,
                            id_num, in_school, class_name, faculty_name);
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

    public static int getCountByClass(String classname, String facultyname)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement getCountByClass = con.prepareStatement("select count(*) from student where class_name=? " +
                    "and faculty_name=?");
            getCountByClass.setString(1, classname);
            getCountByClass.setString(2, facultyname);
            ResultSet numOfClass = getCountByClass.executeQuery();
            int num = 0;
            while (numOfClass.next()){
                num = numOfClass.getInt(1);
            }
            return num;
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return 0;
    }

    public static FacultyAdmin getFacultyAdministrator(String username)
    {

        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findUserByUsername = con.prepareStatement("select * from faculty_administrator where ID=?");
            findUserByUsername.setString(1, username);
            try (ResultSet usersFound = findUserByUsername.executeQuery())
            {
                if (usersFound.next())
                {
                    String ID = usersFound.getString("ID");
                    String name = usersFound.getString("name");
                    String faculty_name = usersFound.getString("faculty_name");
                    con.close();
                    return new FacultyAdmin(ID, name, faculty_name);
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

    public static Instructor getInstructor(String username)
    {

        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findUserByUsername = con.prepareStatement("select * from instructor where ID=?");
            findUserByUsername.setString(1, username);
            try (ResultSet usersFound = findUserByUsername.executeQuery())
            {
                if (usersFound.next())
                {
                    String ID = usersFound.getString("ID");
                    String name = usersFound.getString("name");
                    String class_name = usersFound.getString("class_name");
                    String faculty_name = usersFound.getString("faculty_name");
                    con.close();
                    return new Instructor(ID, name, class_name, faculty_name);
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
    

    

    

    

    

}
