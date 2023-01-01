package model.account;

import service.authority.AuthorityUtil;
import service.record.RecordUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class Root {
    public static final String USERNAME = "root";
    public static final String PASSWORD = "123123";

    public Root() {

    }

    public String getName()
    {
        return USERNAME;
    }

    public void routine()
    {
        // 进入账户操作界面
        while (true)
        {
            System.out.println("##【root账户权限操作】==========");
            System.out.println("##【root账户权限操作】root账户可以进行以下操作：");
            System.out.println("##【root账户权限操作】指令“list”：列出所有用户（不包括root）");
            System.out.println("##【root账户权限操作】指令“add”：添加新用户");
            System.out.println("##【root账户权限操作】指令“manageAdmission”：按校区更改所有学生进入的权限(在该校区除外)");
            System.out.println("##【root账户权限操作】指令“delete”：删除已有用户");
            System.out.println("##【root账户权限操作】指令“logout”：注销");
            System.out.println("##【root账户权限操作】指令“exit”：退出系统");
            System.out.println("##【root账户权限操作】==========");
            System.out.print(">");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.equals("list"))
            {
                //list();
            }
            else if (command.equals("add"))
            {
                //add();
            }
            else if (command.equals("manageAdmission"))
            {
                manageAdmission();
            }
            else if (command.equals("delete"))
            {
                //delete();
            }
            else if (command.equals("logout"))
            {
                break;
            }
            else if (command.equals("exit"))
            {
                System.exit(0);
            }
            else
            {
                System.out.println("##【root账户权限操作】无此指令，请重新输入。");
            }
        }
    }

    public void manageAdmission()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean invalid = true;
        do{
            System.out.println("##请问管控哪个校区？(H校区/J校区/F校区/Z校区)");
            System.out.print(">");
            input = scanner.nextLine();
            switch (input)
            {
                case "H校区":
                case "J校区":
                case "F校区":
                case "Z校区":
                    invalid = false;
                    AuthorityUtil.deleteAuthorityByCampusName(input);
                    System.out.printf("##已完成%s的管控\n",input);
                default:
                    System.out.println("##请输入正确的校区名！");
            }
        }while(invalid);
    }
}
