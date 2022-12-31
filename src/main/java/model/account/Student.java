package model.account;

import model.user.User;

import java.util.Scanner;

public class Student {
    private String ID;
    private String name;
    private String phone;
    private String email;
    private String personal_address;
    private String home_address;
    private String identity_type;
    private String id_num;
    private Integer in_school;
    private String class_name;
    private String faculty_name;

    public Student(String ID, String name, String phone, String email, String personal_address, String home_address,
                   String identity_type, String id_num, Integer in_school, String class_name, String faculty_name)
    {
        this.ID = ID;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.personal_address = personal_address;
        this.home_address = home_address;
        this.identity_type = identity_type;
        this.id_num = id_num;
        this.in_school = in_school;
        this.class_name = class_name;
        this.faculty_name = faculty_name;
    }
    public String getID()
    {
        return this.ID;
    }
    public String getName()
    {
        return this.name;
    }
    public String getPhone()
    {
        return this.phone;
    }
    public Integer getInSchool()
    {
        return this.in_school;
    }
    public String getClassName()
    {
        return this.class_name;
    }
    public String getFacultyName()
    {
        return this.faculty_name;
    }

    /**
     *1.查询：
     * 	学生：
     * 		1.查询自己的个人信息
     * 		2.查询自己的出校申请表
     * 		3.查询自己的入校申请表
     * 		4.查询本班的当日填报健康日报人数
     * 		5.查询自己的入校权限
     * 		6.查询过去一年的离校总时长
     * 	2.增加：
     * 	学生：
     * 		1.打卡进入校区增加打卡记录
     * 		2.打卡离开校区增加离开记录
     * 		3.每日健康填报增加填报记录
     * 		4.提交出校申请
     * 		5.提交入校申请
     */
    public void routine()
    {
        // 进入账户操作界面
        while (true)
        {
            System.out.println("##==========");
            System.out.println("##学生可以进行以下操作：");
            System.out.println("##指令“getInfo”：查看当前区域病房护士信息");
            System.out.println("##指令“list -p”：查看当前区域患者信息");
            System.out.println("##指令“list -r”：查看当前区域病房护士负责的患者信息");
            System.out.println("##指令“list -b”：查看当前区域病床信息");
            System.out.println("##指令“list -o”：查看当前区域病床对应的患者信息");
            System.out.println("##指令“add”：新增当前治疗区域的病房护士");
            System.out.println("##指令“delete”：删除当前治疗区域的病房护士");
            System.out.println("##指令“logout”：注销");
            System.out.println("##指令“exit”：退出系统");
            System.out.println("##==========");
            System.out.print(">");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.equals("getInfo"))
            {
                //listN();
                getInfo();
            }
            else if (command.equals("list -p"))
            {
                //listP();
            }
            else if (command.equals("list -r"))
            {
                //listR();
            }
            else if (command.equals("list -b"))
            {
                //listB();
            }
            else if (command.equals("list -o"))
            {
                //listO();
            }
            else if (command.equals("add"))
            {
                //add();
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
                System.out.println("##无此指令，请重新输入。");
            }
        }
    }


    private void getInfo()
    {
        System.out.println("##个人信息如下");
        System.out.println("##----------");
        System.out.println(String.format("##ID：%s\n姓名：%s\n手机号：%s\n电子邮箱：%s\n宿舍/住址：%s\n" +
                        "家庭住址：%s\n证件类型：%s\n证件号：%s\n在校状态：%d\n班级：%s\n学院：%s\n",
                this.ID, this.name, this.phone, this.email, this.personal_address, this.home_address,
                this.identity_type, this.id_num, this.in_school, this.class_name, this.faculty_name));
        System.out.println("##----------");
    }


//    public static Student getInstance(String ID,String name,String phone,String email,String personal_address,String home_address,String identity_type,
//                                     String id_num,Integer in_school,String class_name,String faculty_name)
//    {
//        return new Student(ID, name, phone, email, personal_address, home_address,identity_type,id_num,in_school, class_name, faculty_name);
//    }
}
