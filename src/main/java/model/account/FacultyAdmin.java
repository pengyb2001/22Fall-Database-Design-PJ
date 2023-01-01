package model.account;

import model.report_sheet.LeaveApproval;
import service.report_sheet.LeaveApprovalUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class FacultyAdmin {
    private String ID;
    private String name;
    private String faculty_name;

    public FacultyAdmin(String ID, String name, String faculty_name)
    {
        this.ID = ID;
        this.name = name;
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
    public String getFacultyName()
    {
        return this.faculty_name;
    }

    public static FacultyAdmin getInstance(String ID,String name,String faculty_name)
    {
        return new FacultyAdmin(ID, name, faculty_name);
    }

    public void routine()
    {
        // 进入账户操作界面
        while (true)
        {
            System.out.println("##==========");
            System.out.println("##院系管理员可以进行以下操作：");
            System.out.println("##指令“getMyLeaveApprovals”：按状态查看本院系的离校申请");
            System.out.println("##指令“editLeaveApproval”：审批本院系离校申请");

            System.out.println("##指令“getMyDailyReport”：查看过去14天日报");
            System.out.println("##指令“list -o”：查看当前区域病床对应的患者信息");
            System.out.println("##指令“passGate”：进出校");
            System.out.println("##指令“addDailyReport”：新增每日健康填报记录");
            System.out.println("##指令“addLeaveApproval”：新增离校申请");
            System.out.println("##指令“delete”：删除当前治疗区域的病房护士");
            System.out.println("##指令“logout”：注销");
            System.out.println("##指令“exit”：退出系统");
            System.out.println("##==========");
            System.out.print(">");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.equals("getMyLeaveApprovals"))
            {
                System.out.println("##指请输入用于筛选的状态\n待审批请输入2 已同意请输入3 已拒绝请输入1");
                System.out.print(">");
                Scanner scan = new Scanner(System.in);
                String sta = scan.nextLine();
                getMyLeaveApprovals(sta);
            }
            else if (command.equals("editLeaveApproval"))
            {
//                editLeaveApproval();
            }
            else if (command.equals("list -r"))
            {
                //listR();
            }
            else if (command.equals("getMyDailyReport"))
            {
                //getMyDailyReport();
            }
            else if (command.equals("list -o"))
            {
                //listO();
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

    public void getMyLeaveApprovals(String sta)
    {
        Integer status = Integer.parseInt(sta);
        ArrayList<LeaveApproval> leaveApprovals = new ArrayList<>();
        leaveApprovals = LeaveApprovalUtil.getLeaveApprovals(getFacultyName(),status);
        if (leaveApprovals.isEmpty())
        {
            System.out.println("##无该状态下的离校申请！");
        }
        else
        {

            System.out.println("##状态"+ sta +"的离校申请如下：");
            if(status == 2) System.out.println("##要进行审批，请记下对应学号");
            for (LeaveApproval leaveApproval: leaveApprovals)
            {
                System.out.println("##----------");
                System.out.printf("表单号：%d\n学号：%s\n申请时间：%s\n申请理由：%s\n目的地：%s\n离校日期：%s\n返校日期：%s\n",
                        leaveApproval.getForm_num(), leaveApproval.getStudent_ID(), leaveApproval.getTimestamp().toString(), leaveApproval.getReason(),
                        leaveApproval.getDestination(), leaveApproval.getLeave_date().toString(), leaveApproval.getEntry_date().toString());
            }
            System.out.println("##----------");
        }
    }
}
