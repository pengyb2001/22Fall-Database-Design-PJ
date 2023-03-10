import model.account.FacultyAdmin;
import model.account.Instructor;
import model.account.Student;
import model.account.Root;
//import model.user.User;
import service.account.AccountUtil;

import java.util.Scanner;

public class Main
{
    //private static User user;
    private static Student student;
    private static Instructor instructor;
    private static FacultyAdmin facultyAdmin;
    private static Root root;

    static int len;
    
    public static void main(String[] args)
    {
        // 欢迎界面
        System.out.println("##进出校管理系统。");
        system:
        while (true)
        {
            // 用户登录界面，如果用户已登录则允许进行权限操作。
            if ((student == null)&&(instructor == null)&&(facultyAdmin == null)&&(root == null))
            {
                login:
                while (true)
                {
                    // 提示用户登录
                    System.out.println("##请输入用户名登录，或输入“exit”退出系统：");
                    System.out.print(">");
                    Scanner scanner = new Scanner(System.in);
                    String username = scanner.nextLine();
                    if (username.equals("exit"))
                    {
                        break system;
                    }
                    else if (username.equals(Root.USERNAME))
                    {
                        // root账户登录
                        while (true)
                        {
                            System.out.println("##【root账户登录】请输入密码，或输入“cancel”取消登录：");
                            System.out.print(">");
                            String password = scanner.nextLine();
                            if (password.equals("cancel"))
                            {
                                break;
                            }
                            else if (password.equals(Root.PASSWORD))
                            {
                                System.out.println("##【root账户登录】登录成功！");
                                root = new Root();
                                len = 999;
                                break login;
                            }
                            else
                            {
                                System.out.println("##【root账户登录】密码错误，请重试。");
                            }
                        }
                    }
                    else if (AccountUtil.usernameExists(username))
                    {
                        // 普通账户登录
                        len = username.length();
                        switch (len) {
                            case 4 -> {facultyAdmin = AccountUtil.getFacultyAdministrator(username);}
                            case 5 -> {instructor = AccountUtil.getInstructor(username);}
                            case 11 -> {student = AccountUtil.getStudent(username);}
                        }
                        System.out.println("##登录成功！");
                        break login;
                    }
                    else
                    {
                        // 无此用户
                        System.out.println("##该用户不存在，请重试。");
                    }
                }
            }
            else
            {
                // 进入用户交互界面
                switch (len) {
                    case 4 -> {System.out.printf("##欢迎，院系管理员%s！%n", facultyAdmin.getName());
                        facultyAdmin.routine();
                        // 用户注销
                        System.out.printf("##院系管理员%s成功退出登录。%n", facultyAdmin.getName());
                        facultyAdmin = null;}
                    case 5 -> {System.out.printf("##欢迎，辅导员%s！%n", instructor.getName());
                        instructor.routine();
                        // 用户注销
                        System.out.printf("##辅导员%s成功退出登录。%n", instructor.getName());
                        instructor = null;}
                    case 11 -> {System.out.printf("##欢迎，学生%s！%n", student.getName());
                        student.routine();
                        // 用户注销
                        System.out.printf("##学生%s成功退出登录。%n", student.getName());
                        student = null;}
                    case 999 -> {System.out.printf("##欢迎，超级管理员%s！%n", root.getName());
                        root.routine();
                        // 用户注销
                        System.out.printf("##超级管理员%s成功退出登录。%n", root.getName());
                        root = null;}
                }
            }
        }
    }
}
