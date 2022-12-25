import model.user.Root;
import model.user.User;
import service.account.AccountUtil;

import java.util.Scanner;

public class Main
{
    private static User user;
    
    public static void main(String[] args)
    {
        // 欢迎界面
        System.out.println("##欢迎进入复旦医院管理系统。");
        system:
        while (true)
        {
            // 用户登录界面，如果用户已登录则允许进行权限操作。
            if (user == null)
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
                                user = new Root();
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
                        while (true)
                        {
                            System.out.println("##请输入密码，或输入“cancel”取消登录：");
                            System.out.print(">");
                            String password = scanner.nextLine();
                            if (password.equals("cancel"))
                            {
                                break;
                            }
                            else
                            {
                                user = AccountUtil.getUser(username, password);
                                if (user == null)
                                {
                                    System.out.println("##密码错误，请重试。");
                                }
                                else
                                {
                                    System.out.println("##登录成功！");
                                    break login;
                                }
                            }
                        }
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
                System.out.println(String.format("##欢迎，用户%s！", user.getUsername()));
                user.routine();
                
                // 用户注销
                System.out.println(String.format("##用户%s成功退出登录。", user.getUsername()));
                user = null;
            }
        }
    }
}
