package model.account;

import model.admission_authority.AdmissionAuthority;
import model.report_sheet.DailyReport;
import model.user.User;
import service.authority.AuthorityUtil;
import service.id.IDGenerator;
import service.report_sheet.DailyReportUtil;
import service.report_sheet.LeaveApprovalUtil;

import java.util.ArrayList;
import java.util.Date;
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
    private String in_school;
    private String class_name;
    private String faculty_name;

    public Student(String ID, String name, String phone, String email, String personal_address, String home_address,
                   String identity_type, String id_num, String in_school, String class_name, String faculty_name)
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
    public String getInSchool()
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
            System.out.println("##指令“getInfo”：查看自己的个人信息");
            System.out.println("##指令“getAuthority”：查看自己的入校权限");
            System.out.println("##指令“getDailyReportCount”：查询当日班级填报人数");//TODO
            System.out.println("##指令“getMyDailyReport”：查看过去14天日报");
            System.out.println("##指令“list -o”：查看当前区域病床对应的患者信息");
            System.out.println("##指令“addDailyReport”：新增每日健康填报记录");
            System.out.println("##指令“addLeaveApproval”：新增离校申请");
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
            else if (command.equals("getAuthority"))
            {
                //listP();
                getAuthority();
            }
            else if (command.equals("list -r"))
            {
                //listR();
            }
            else if (command.equals("getMyDailyReport"))
            {
                getMyDailyReport();
            }
            else if (command.equals("list -o"))
            {
                //listO();
            }
            else if (command.equals("addDailyReport"))
            {
                //add();
                addDailyReport();
            }
            else if (command.equals("addLeaveApproval"))
            {
                addLeaveApproval();
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
                        "家庭住址：%s\n证件类型：%s\n证件号：%s\n在校状态：%s\n班级：%s\n学院：%s\n",
                this.ID, this.name, this.phone, this.email, this.personal_address, this.home_address,
                this.identity_type, this.id_num, this.in_school, this.class_name, this.faculty_name));
        System.out.println("##----------");
    }

    private void getAuthority()
    {
        ArrayList<AdmissionAuthority> authorities = AuthorityUtil.getAuthorityByID(getID());
        System.out.println("##可进校区如下");
        System.out.println("##----------");
        for (AdmissionAuthority authority: authorities)
        {
            System.out.println(String.format("%s", authority.getCampus_name()));
        }
        System.out.println("##----------");
    }

    private void addDailyReport()
    {
        if(DailyReportUtil.dailyReportExists(this.ID))
        {
            System.out.println("##今日已填写过健康日报");
        }
        else
        {
            Scanner scanner = new Scanner(System.in);
            String student_id = this.ID;
//                Date timestamp = new Date();
            System.out.println("##请输入地点：");
            System.out.print(">");
            String location = scanner.nextLine();
            String healthy;
            int is_healthy = -1;
            do{
                System.out.println("##请输入健康状况（Y 或 N）：");
                System.out.print(">");
                healthy = scanner.nextLine();
                if(healthy.equals("Y"))
                {
                    is_healthy = 1;
                }
                else if(healthy.equals("N"))
                {
                    is_healthy = 0;
                }
            }while(is_healthy == -1);
            DailyReportUtil.addDailyReport(student_id, location, is_healthy);
            System.out.println("##填写健康日报成功");

        }
    }

    private void addLeaveApproval()
    {
        if(LeaveApprovalUtil.leaveApprovalExists(this.ID))
        {
            System.out.println("##还有未处理完的离校申请");
        }
        else
        {
            Scanner scanner = new Scanner(System.in);
            String student_id = this.ID;
            System.out.println("##请输入离校原因：");
            System.out.print(">");
            String reason = scanner.nextLine();
            System.out.println("##请输入目的地：");
            System.out.print(">");
            String destination = scanner.nextLine();
            int compare = 0;
            String leave_date;
            String entry_date;
            do
            {
                //TODO 日期正则匹配
                System.out.println("##请输入预计离校日期（形如yyyy-MM-dd）：");
                System.out.print(">");
                leave_date = scanner.nextLine();
                System.out.println("##请输入预计进校日期（形如yyyy-MM-dd）：");
                System.out.print(">");
                entry_date = scanner.nextLine();
                compare = entry_date.compareTo(leave_date);
                if(compare <= 0) System.out.println("##进校日期应该晚于离校日期，请重新填写");
            }while(compare <= 0);

            LeaveApprovalUtil.addLeaveApproval(student_id, reason, destination, leave_date, entry_date);
            System.out.println("##填写离校申请成功");
        }
    }

    private void getMyDailyReport()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        int n = 0;
        do{
            System.out.println("##请输入要查询过去几天的填报记录");
            System.out.print(">");
            input = scanner.nextLine();
            if(isNumeric(input))
            {
                n = Integer.parseInt(input);
            }
            else
            {
                System.out.println("##请输入数字！");
            }
        }while(n == 0);
        ArrayList<DailyReport> dailyReports = DailyReportUtil.getMyDailyReport(getID(), n);
        System.out.printf("##过去%d天填报记录如下\n",n);
        if (dailyReports == null){
            System.out.println("##无记录！");
            return;
        }
        for (DailyReport dailyReport: dailyReports)
        {
            System.out.println("##----------");
            System.out.printf("日期：%s\n地点：%s\n健康状况：%d\n",
                    dailyReport.getTimestamp().toString(), dailyReport.getLocation(), dailyReport.getIs_healthy());
        }
        System.out.println("##----------");
    }
    public static boolean isNumeric(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            //System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

//    public static Student getInstance(String ID,String name,String phone,String email,String personal_address,String home_address,String identity_type,
//                                     String id_num,Integer in_school,String class_name,String faculty_name)
//    {
//        return new Student(ID, name, phone, email, personal_address, home_address,identity_type,id_num,in_school, class_name, faculty_name);
//    }
}
