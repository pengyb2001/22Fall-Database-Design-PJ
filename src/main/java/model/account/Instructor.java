package model.account;
import model.record.PassRecord;
import model.report_sheet.DailyReport;
import model.report_sheet.EnterApproval;
import service.account.AccountUtil;
import service.record.RecordUtil;
import service.report_sheet.DailyReportUtil;
import service.report_sheet.EnterApprovalUtil;
import service.report_sheet.LeaveApprovalUtil;
import model.report_sheet.LeaveApproval;
import model.account.Student;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static service.report_sheet.LeaveApprovalUtil.dateChange;

public class Instructor {
    private String ID;
    private String name;
    private String class_name;
    private String faculty_name;

    public Instructor(String ID, String name, String class_name, String faculty_name)
    {
        this.ID = ID;
        this.name = name;
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
    public String getClassName()
    {
        return this.class_name;
    }
    public String getFacultyName()
    {
        return this.faculty_name;
    }

    public void routine()
    {
        // 进入账户操作界面
        while (true)
        {
            System.out.println("##==========");
            System.out.println("##辅导员可以进行以下操作：");
            System.out.println("##指令“getClassList”：查询班级名单");
            System.out.println("##指令“getMyLeaveApprovals”：查询离校申请");
            System.out.println("##指令“editLeaveApproval”：审批离校申请");
            System.out.println("##指令“getMyEnterApprovals”：查询入校申请");
            System.out.println("##指令“editEnterApproval”：审批入校申请");
            System.out.println("##指令“getDailyReportCount”：查询所在院系指定班级当日的健康日报填报人数");
            System.out.println("##指令“getEntryApprovalsToDo”：查询本班过去n天尚未批准的入校申请数量及详细信息");
            System.out.println("##指令“getLeaveApprovalsToDo”：查询本班过去n天尚未批准的离校申请数量及详细信息");
            System.out.println("##指令“getInSchoolLeaveStudents”：查询本班已提交出校申请但未离校的学生数量、个人信息；");
            System.out.println("##指令“getOutSchoolStudents”：查询本班已出校但尚未返回校园（即离校状态）的学生数量、个人信息及各自的离校时间(学生状态不在校但具有进校权限)");
            System.out.println("##指令“getOutSchoolTime”：查询本班学生（从当天算起）过去一年的离校总时长");
            System.out.println("##指令“getLeaveStudents”：查询本班未提交出校申请但离校状态超过 24h 的学生数量、个人信息");
            System.out.println("##指令“logout”：注销");
            System.out.println("##指令“exit”：退出系统");
            System.out.println("##==========");
            System.out.print(">");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.equals("getClassList"))
            {
                getClassList();
            }
            else if (command.equals("getMyLeaveApprovals"))
            {
                getMyLeaveApprovals();
            }
            else if (command.equals("editLeaveApproval"))
            {
                editLeaveApproval();
            }
            else if (command.equals("getMyEnterApprovals"))
            {
                getMyEnterApprovals();
            }
            else if (command.equals("editEnterApproval"))
            {
                editEnterApproval();
            }
            else if (command.equals("getDailyReportCount"))
            {
                getDailyReportCount();
            }
            else if (command.equals("getEntryApprovalsToDo"))
            {
                getEntryApprovalsToDo();
            }
            else if (command.equals("getLeaveApprovalsToDo"))
            {
                getLeaveApprovalsToDo();
            }
            else if (command.equals("getOutSchoolStudents"))
            {
                getOutSchoolStudents();
            }
            else if (command.equals("getOutSchoolTime"))
            {
                getOutSchoolTime();
            }
            else if (command.equals("getLeaveStudents")){
                getLeaveStudents();
            }
            else if (command.equals("getInSchoolLeaveStudents"))
            {
                getInSchoolLeaveStudents();
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

    public void getClassList()
    {
        ArrayList<Student> students = new ArrayList<>();
        students = AccountUtil.getClassList(getClassName(), getFacultyName());
        System.out.println("##学生名单如下：");

        for (Student student: students)
        {
            System.out.println("##----------");
            System.out.printf("学号：%s\n姓名：%s\n",
                    student.getID(), student.getName());
        }
        System.out.println("##----------");
    }

    public void getMyLeaveApprovals()
    {
        int status = 0;
        boolean invalid = true;
        String input;
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.println("##输入查询条件(待审批/已同意/已拒绝)：");
            System.out.print(">");
            input = scanner.nextLine();
            switch (input)
            {
                case "待审批":
                    invalid = false;
                    status = 0;
                    break;
                case "已同意":
                    invalid = false;
                    status = 2;
                    break;
                case "已拒绝":
                    invalid = false;
                    status = 1;
                    break;
                default:
                    System.out.println("##请输入正确的指令！");
            }
        }while(invalid);
        ArrayList<LeaveApproval> leaveApprovals = new ArrayList<>();
        leaveApprovals = LeaveApprovalUtil.getLeaveApprovals(getClassName(),getFacultyName(),status);
        if (leaveApprovals.isEmpty())
        {
            System.out.printf("##无%s的离校申请！\n",input);
        }
        else
        {
            System.out.printf("##%s的离校申请如下：",input);

            for (LeaveApproval leaveApproval: leaveApprovals)
            {
                String sta = switch (leaveApproval.getStatus()) {
                    case 0 -> "待辅导员审核";
                    case 1 -> "待学生修改";
                    case 2 -> "待院系管理员审核";
                    case 3 -> "已结束";
                    default -> "未知错误";
                };
                System.out.println("##----------");
                System.out.printf("表单号：%d\n学号：%s\n申请时间：%s\n申请理由：%s\n目的地：%s\n离校日期：%s\n返校日期：%s\n状态：%s\n拒绝理由：%s\n",
                        leaveApproval.getForm_num(), leaveApproval.getStudent_ID(), leaveApproval.getTimestamp().toString(), leaveApproval.getReason(),
                        leaveApproval.getDestination(), leaveApproval.getLeave_date().toString(), leaveApproval.getEntry_date().toString(), sta, leaveApproval.getRefuse_reason());
            }
            System.out.println("##----------");
        }
    }

    public void editLeaveApproval()
    {
        ArrayList<LeaveApproval> leaveApprovals = new ArrayList<>();
        leaveApprovals = LeaveApprovalUtil.getLeaveApprovals(getClassName(),getFacultyName(),0);
        if (leaveApprovals.isEmpty())
        {
            System.out.println("##无待审批的离校申请！");
            return;
        }
        else
        {
            System.out.println("##待审批的离校申请如下：");
            System.out.println("##要进行审批，请记下对应学号");
            for (LeaveApproval leaveApproval: leaveApprovals)
            {
                System.out.println("##----------");
                System.out.printf("表单号：%d\n学号：%s\n申请时间：%s\n申请理由：%s\n目的地：%s\n离校日期：%s\n返校日期：%s\n",
                        leaveApproval.getForm_num(), leaveApproval.getStudent_ID(), leaveApproval.getTimestamp().toString(), leaveApproval.getReason(),
                        leaveApproval.getDestination(), leaveApproval.getLeave_date().toString(), leaveApproval.getEntry_date().toString());
            }
            System.out.println("##----------");
        }
        Scanner scanner = new Scanner(System.in);
        String input;
        int n = 0;
        do{
            System.out.println("##请输入要审批的学生学号");
            System.out.print(">");
            input = scanner.nextLine();
            if(Student.isNumeric(input))
            {
                n = 1;
            }
            else
            {
                System.out.println("##请输入正确的学号！");
            }
        }while(n == 0);
        ArrayList<String> ids = new ArrayList<>();
        for (LeaveApproval leaveApproval: leaveApprovals)
        {
            ids.add(leaveApproval.getStudent_ID());
        }
        if (ids.contains(input))
        {
            LeaveApproval leaveApproval = LeaveApprovalUtil.getLeaveApproval(input, 0);
            System.out.println("##----------");
            assert leaveApproval != null;
            System.out.printf("表单号：%d\n学号：%s\n申请时间：%s\n申请理由：%s\n目的地：%s\n离校日期：%s\n返校日期：%s\n",
                    leaveApproval.getForm_num(), leaveApproval.getStudent_ID(), leaveApproval.getTimestamp().toString(), leaveApproval.getReason(),
                    leaveApproval.getDestination(), leaveApproval.getLeave_date().toString(), leaveApproval.getEntry_date().toString());
            System.out.println("##----------");
            do {
                System.out.println("##通过或拒绝(Y/N)：");
                System.out.print(">");
                input = scanner.nextLine();
                if (input.equals("Y"))
                {
                    leaveApproval.setStatus(2);
                    leaveApproval.setRefuse_reason(null);
                    LeaveApprovalUtil.updateLeaveApproval(leaveApproval);
                    System.out.println("##已通过审批！");
                    break;
                } else if (input.equals("N"))
                {
                    leaveApproval.setStatus(1);
                    System.out.println("##请输入拒绝原因：");
                    System.out.print(">");
                    input = scanner.nextLine();
                    leaveApproval.setRefuse_reason(input);
                    LeaveApprovalUtil.updateLeaveApproval(leaveApproval);
                    System.out.println("##已拒绝申请！");
                    break;
                } else
                {
                    System.out.println("请输入正确的指令！");
                }
            }while (true);

        }else
        {
            System.out.println("该学生没有待审批的离校申请！");
        }
    }
    //按条件查询入校申请
    public void getMyEnterApprovals()
    {
        int status = 4;
        boolean invalid = true;
        String input;
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.println("##输入查询条件(待审批/已同意/已拒绝)：");
            System.out.print(">");
            input = scanner.nextLine();
            switch (input)
            {
                case "待审批":
                    invalid = false;
                    status = 0;
                    break;
                case "已同意":
                    invalid = false;
                    status = 2;
                    break;
                case "已拒绝":
                    invalid = false;
                    status = 1;
                    break;
                default:
                    System.out.println("##请输入正确的指令！");
            }
        }while(invalid);
        ArrayList<EnterApproval> enterApprovals = new ArrayList<>();
        enterApprovals = EnterApprovalUtil.getEnterApprovals(getClassName(),getFacultyName(),status);
        if (enterApprovals.isEmpty())
        {
            System.out.printf("##无%s的入校申请！\n",input);
        }
        else
        {
            System.out.printf("##%s的入校申请如下：",input);

            for (EnterApproval enterApproval: enterApprovals)
            {
                String sta = switch (enterApproval.getStatus()) {
                    case 0 -> "待辅导员审核";
                    case 1 -> "待学生修改";
                    case 2 -> "待院系管理员审核";
                    case 3 -> "已结束";
                    default -> "未知错误";
                };
                System.out.println("##----------");
                System.out.printf("表单号：%d\n学号：%s\n申请时间：%s\n申请理由：%s\n七天内经过地区：%s\n返校日期：%s\n状态：%s\n拒绝理由：%s\n",
                        enterApproval.getForm_num(), enterApproval.getStudent_ID(), enterApproval.getTimestamp().toString(), enterApproval.getReason(),
                        enterApproval.getLived_area(), enterApproval.getEntry_date().toString(), sta, enterApproval.getRefuse_reason());
            }
            System.out.println("##----------");
        }
    }

    public void editEnterApproval()
    {
        ArrayList<EnterApproval> enterApprovals = new ArrayList<>();
        enterApprovals = EnterApprovalUtil.getEnterApprovals(getClassName(),getFacultyName(),0);
        if (enterApprovals.isEmpty())
        {
            System.out.println("##无待审批的入校申请！");
            return;
        }
        else
        {
            System.out.println("##待审批的入校申请如下：");
            System.out.println("##要进行审批，请记下对应学号");
            for (EnterApproval enterApproval: enterApprovals)
            {
                System.out.println("##----------");
                System.out.printf("表单号：%d\n学号：%s\n申请时间：%s\n申请理由：%s\n七天内经过地区：%s\n返校日期：%s\n",
                        enterApproval.getForm_num(), enterApproval.getStudent_ID(), enterApproval.getTimestamp().toString(), enterApproval.getReason(),
                        enterApproval.getLived_area(), enterApproval.getEntry_date().toString());
            }
            System.out.println("##----------");
        }
        Scanner scanner = new Scanner(System.in);
        String input;
        int n = 0;
        do{
            System.out.println("##请输入要审批的学生学号");
            System.out.print(">");
            input = scanner.nextLine();
            if(Student.isNumeric(input))
            {
                n = 1;
            }
            else
            {
                System.out.println("##请输入正确的学号！");
            }
        }while(n == 0);
        ArrayList<String> ids = new ArrayList<>();
        for (EnterApproval enterApproval: enterApprovals)
        {
            ids.add(enterApproval.getStudent_ID());
        }
        if (ids.contains(input))
        {
            EnterApproval enterApproval = EnterApprovalUtil.getEnterApproval(input, 0);
            assert enterApproval != null;
            System.out.println("##----------");
            System.out.printf("表单号：%d\n学号：%s\n申请时间：%s\n申请理由：%s\n七天内经过地区：%s\n返校日期：%s\n",
                    enterApproval.getForm_num(), enterApproval.getStudent_ID(), enterApproval.getTimestamp().toString(), enterApproval.getReason(),
                    enterApproval.getLived_area(), enterApproval.getEntry_date().toString());
            System.out.println("##----------");
            do {
                System.out.println("##通过或拒绝(Y/N)：");
                System.out.print(">");
                input = scanner.nextLine();
                if (input.equals("Y"))
                {
                    enterApproval.setStatus(2);
                    enterApproval.setRefuse_reason(null);
                    EnterApprovalUtil.updateEnterApproval(enterApproval);
                    System.out.println("##已通过审批！");
                    break;
                } else if (input.equals("N"))
                {
                    enterApproval.setStatus(1);
                    System.out.println("##请输入拒绝原因：");
                    System.out.print(">");
                    input = scanner.nextLine();
                    enterApproval.setRefuse_reason(input);
                    EnterApprovalUtil.updateEnterApproval(enterApproval);
                    System.out.println("##已拒绝申请！");
                    break;
                } else
                {
                    System.out.println("请输入正确的指令！");
                }
            }while (true);

        }else
        {
            System.out.println("该学生没有待审批的入校申请！");
        }
    }

    public void getDailyReportCount()
    {
        int isClassExists = 0;
        String classname = getClassName();
        do
        {
            System.out.print("##请输入你所在的院系要查询的班级：\n");
            System.out.print(">");
            Scanner scanner = new Scanner(System.in);
            classname = scanner.nextLine();
            if(AccountUtil.instructorExists(classname, getFacultyName())){
                isClassExists = 1;
            }
            else
            {
                System.out.println("##您所在的院系没有该班级");
            }
        }while (isClassExists == 0);
        int cnt = 0;
        int total = 0;
        total = AccountUtil.getCountByClass(classname, getFacultyName());
        System.out.println("##----------");
        System.out.printf("##%s%s人数为：%d\n",getFacultyName(), classname, total);
        cnt = DailyReportUtil.getTodayCountByClass(classname, getFacultyName());
        System.out.printf("##%s%s今天填写了健康日报的人数为：%d\n",getFacultyName(), classname, cnt);
        System.out.println("##----------");

    }

    public void getEntryApprovalsToDo()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        int n = 0;
        do{
            System.out.println("##请输入要查询过去几天的未审批入校申请");
            System.out.print(">");
            input = scanner.nextLine();
            if(Student.isNumeric(input))
            {
                n = Integer.parseInt(input);
            }
            else
            {
                System.out.println("##请输入数字！");
            }
        }while(n == 0);
        ArrayList<EnterApproval> enterApprovals = new ArrayList<>();
        enterApprovals = EnterApprovalUtil.getEnterApprovals(getClassName(),getFacultyName(),0, n);
        if (enterApprovals.isEmpty())
        {
            System.out.println("##无待审批的入校申请！");
            return;
        }
        else
        {
            System.out.printf("##过去%d天待审批的入校申请数量为：%d\n", n, enterApprovals.size());
            System.out.printf("##过去%d天待审批的入校申请如下：\n", n);
            for (EnterApproval enterApproval: enterApprovals)
            {
                System.out.println("##----------");
                System.out.printf("表单号：%d\n学号：%s\n申请时间：%s\n申请理由：%s\n七天内经过地区：%s\n返校日期：%s\n",
                        enterApproval.getForm_num(), enterApproval.getStudent_ID(), enterApproval.getTimestamp().toString(), enterApproval.getReason(),
                        enterApproval.getLived_area(), enterApproval.getEntry_date().toString());
            }
            System.out.println("##----------");
        }
    }

    public void getLeaveApprovalsToDo()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        int n = 0;
        do{
            System.out.println("##请输入要查询过去几天的未审批离校申请");
            System.out.print(">");
            input = scanner.nextLine();
            if(Student.isNumeric(input))
            {
                n = Integer.parseInt(input);
            }
            else
            {
                System.out.println("##请输入数字！");
            }
        }while(n == 0);
        ArrayList<LeaveApproval> leaveApprovals = new ArrayList<>();
        leaveApprovals = LeaveApprovalUtil.getLeaveApprovals_1(getClassName(),getFacultyName(),0, n);
        if (leaveApprovals.isEmpty())
        {
            System.out.println("##无待审批的离校申请！");
            return;
        }
        else
        {
            System.out.printf("##过去%d天待审批的入校申请数量为：%d\n", n, leaveApprovals.size());
            System.out.printf("##过去%d天待审批的离校申请如下：\n", n);
            for (LeaveApproval leaveApproval: leaveApprovals)
            {
                System.out.println("##----------");
                System.out.printf("表单号：%d\n学号：%s\n申请时间：%s\n申请理由：%s\n目的地：%s\n离校日期：%s\n返校日期：%s\n",
                        leaveApproval.getForm_num(), leaveApproval.getStudent_ID(), leaveApproval.getTimestamp().toString(), leaveApproval.getReason(),
                        leaveApproval.getDestination(),leaveApproval.getLeave_date().toString(), leaveApproval.getEntry_date().toString());
            }
            System.out.println("##----------");
        }
    }

    //getOutSchoolStudents查询本班已出校但尚未返回校园（即离校状态）的学生数量、个人信息及各自的离校时间(学生状态不在校但具有进校权限)
    public void getOutSchoolStudents()
    {
        ArrayList<Student> students = new ArrayList<>();
        students = AccountUtil.outSchoolInAuthorityStudents(getClassName(), getFacultyName());
        if (students.isEmpty())
        {
            System.out.println("没有具有进校权限但不在校的学生！");
            return;
        }
        System.out.printf("##共%d名学生，学生名单如下：\n",students.size());
        for (Student student: students)
        {
            PassRecord passRecord = RecordUtil.getNearestOutPassRecordByID(student.getID());
            System.out.println("##----------");
            if (passRecord == null)
            {
                System.out.printf("学号：%s\n姓名：%s\n离校时间：无，可能是尚未报道的新生\n",
                        student.getID(), student.getName());
            }
            else
            {
                System.out.printf("学号：%s\n姓名：%s\n离校时间：%s\n",
                        student.getID(), student.getName(), passRecord.getTimestamp().toString());
            }
        }
        System.out.println("##----------");
    }

    //查询本班已提交出校申请但未离校的学生数量、个人信息
    public void getInSchoolLeaveStudents()
    {
        ArrayList<Student> students = new ArrayList<>();
        students = AccountUtil.getInSchoolLeaveStudents(getClassName(), getFacultyName());
        if (students.isEmpty())
        {
            System.out.println("本班没有已提交出校申请但未离校的学生！");
            return;
        }
        System.out.printf("##共%d名学生，学生名单如下：\n",students.size());
        for (Student student: students)
        {
            System.out.println("##----------");
            System.out.printf("学号：%s\n姓名：%s\n电话：%s\n",
                    student.getID(), student.getName(), student.getPhone());
        }
        System.out.println("##----------");
    }

    //查询本班未提交出校申请但离校状态超过 24h 的学生数量、个人信息
    public void getLeaveStudents()
    {
        ArrayList<Student> students = new ArrayList<>();
        students = AccountUtil.getLeaveStudents(getClassName(), getFacultyName());
        if (students.isEmpty())
        {
            System.out.println("本班没有未提交出校申请但离校状态超过 24h 的学生！");
            return;
        }
        System.out.printf("##共%d名学生，学生名单如下：\n",students.size());
        for (Student student: students)
        {
            System.out.println("##----------");
            System.out.printf("学号：%s\n姓名：%s\n电话：%s\n",
                    student.getID(), student.getName(), student.getPhone());
        }
        System.out.println("##----------");
    }

    //查询本班学生过去一年的离校总时长
    public void getOutSchoolTime()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        int n = 0;
        do{
            System.out.println("##请输入要查询的学生学号");
            System.out.print(">");
            input = scanner.nextLine();
            if(Student.isNumeric(input))
            {
                n = 1;
            }
            else
            {
                System.out.println("##请输入正确的学号！");
            }
        }while(n == 0);
        Student student = AccountUtil.getMyStudent(input, getClassName(),getFacultyName());
        if (student == null)
        {
            System.out.println("本班没有该学号的学生！");
            return;
        }
        String date = RecordUtil.getOutSchoolTime(input, student.getInSchool());
        System.out.printf("##该学生过去一年离校时长：%s\n",date);
        System.out.println("##----------");
    }

}
