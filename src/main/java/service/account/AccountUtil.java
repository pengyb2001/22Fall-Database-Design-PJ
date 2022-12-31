/**
 * 实现账户管理功能
 */

package service.account;

import model.account.FacultyAdmin;
import model.account.Instructor;
import model.account.Student;
import model.user.User;
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
    
    public static User getUserById(String id)
    {
        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findUserByUsername = con.prepareStatement("select * from user where u_id=?");
            findUserByUsername.setString(1, id);
            try (ResultSet usersFound = findUserByUsername.executeQuery())
            {
                if (usersFound.next())
                {
                    String username = usersFound.getString("username");
                    String password = usersFound.getString("password");
                    String name = usersFound.getString("name");
                    String type = usersFound.getString("post");
                    String area = usersFound.getString("area");
                    con.close();
                    return User.getInstance(id, username, password, name, area, type);
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
    
    public static ArrayList<User> getAllUsers()
    {
        ArrayList<User> users = new ArrayList<>();
        try
        {
            // 获取全部用户
            Connection con = SQLUtil.getConnection();
            PreparedStatement findAllUsers = con.prepareStatement("select * from user");
            try (ResultSet usersFound = findAllUsers.executeQuery())
            {
                while (usersFound.next())
                {
                    String id = usersFound.getString("u_id");
                    String username = usersFound.getString("username");
                    String password = usersFound.getString("password");
                    String name = usersFound.getString("name");
                    String type = usersFound.getString("post");
                    String area = usersFound.getString("area");
                    User user = User.getInstance(id, username, password, name, area, type);
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
    
    public static void addUser(User user)
    {
        try
        {
            // 添加新用户
            Connection con = SQLUtil.getConnection();
            PreparedStatement addNewUser = con.prepareStatement("insert into user values (?,?,?,?,?,?)");
            addNewUser.setString(1, user.getId());
            addNewUser.setString(2, user.getUsername());
            addNewUser.setString(3, user.getPassword());
            addNewUser.setString(4, user.getName());
            addNewUser.setString(5, user.getType());
            addNewUser.setString(6, user.getArea());
            addNewUser.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
    
    public static void updateUser(User user)
    {
        try
        {
            // 修改用户信息
            Connection con = SQLUtil.getConnection();
            PreparedStatement reviseUser = con.prepareStatement("update user set password=?, name=?, " +
                    "post=?, area=? where username=?");
            reviseUser.setString(1, user.getPassword());
            reviseUser.setString(2, user.getName());
            reviseUser.setString(3, user.getType());
            reviseUser.setString(4, user.getArea());
            reviseUser.setString(5, user.getUsername());
            reviseUser.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
    
    public static void removeUser(User user)
    {
        try
        {
            // 删除用户
            Connection con = SQLUtil.getConnection();
            PreparedStatement deleteUser = con.prepareStatement("delete from user where username=?");
            deleteUser.setString(1, user.getUsername());
            deleteUser.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
}
