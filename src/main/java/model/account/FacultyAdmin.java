package model.account;

import model.report_sheet.LeaveApproval;
import service.authority.AuthorityUtil;
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
//                System.out.println("##指请输入用于筛选的状态\n待审批请输入2 已同意请输入3 已拒绝请输入1");
//                System.out.print(">");
//                Scanner scan = new Scanner(System.in);
//                String sta = scan.nextLine();
                getMyLeaveApprovals();
            }
            else if (command.equals("editLeaveApproval"))
            {
                editLeaveApproval();
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
                    status = 2;
                    break;
                case "已同意":
                    invalid = false;
                    status = 3;
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
        leaveApprovals = LeaveApprovalUtil.getLeaveApprovals(getFacultyName(),status);
        if (leaveApprovals.isEmpty())
        {
            System.out.printf("##无%s下的离校申请！\n", input);
        }
        else
        {

            System.out.printf("##%s的离校申请如下：", input);

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
        ArrayList<LeaveApproval> leaveApprovals;
        leaveApprovals = LeaveApprovalUtil.getLeaveApprovals(getFacultyName(),2);
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
            LeaveApproval leaveApproval = LeaveApprovalUtil.getLeaveApproval(input, 2);
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
                    leaveApproval.setStatus(3);
                    LeaveApprovalUtil.updateLeaveApproval(leaveApproval);
                    System.out.println("##已通过审批！");
                    AuthorityUtil.deleteAuthorityByID(leaveApproval.getStudent_ID());
                    System.out.println("##已关闭该学生所有入校权限！");
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
}
