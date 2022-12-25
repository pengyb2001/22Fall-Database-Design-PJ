/**
 * 主治医生
 */

package model.user;

import model.patient.Patient;
import model.patient.TestSheet;
import service.id.IDGenerator;
import service.patient.NurseUtil;
import service.patient.PatientUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class Doctor extends User
{
    public static final String TYPE = "主治医生";
    
    Doctor(String id, String username, String password, String name, String area)
    {
        super(id, username, password, name, area);
    }
    
    @Override
    public String getType()
    {
        return TYPE;
    }
    
    @Override
    public void routine()
    {
        // 进入账户操作界面
        while (true)
        {
            System.out.println("##==========");
            System.out.println("##主治医生可以进行以下操作：");
            System.out.println("##指令“list -n”：查看当前区域护士长及病房护士信息");
            System.out.println("##指令“list -p”：查看当前区域患者信息");
            System.out.println("##指令“list -r”：查看当前区域病房护士负责的患者信息");
            System.out.println("##指令“add”：新增当前治疗区域的病人的核酸检测单");
            System.out.println("##指令“revise”：修改当前治疗区域的病人的病情评级和生命状态");
            System.out.println("##指令“logout”：注销");
            System.out.println("##指令“exit”：退出系统");
            System.out.println("##==========");
            System.out.print(">");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.equals("list -n"))
            {
                listN();
            }
            else if (command.equals("list -p"))
            {
                listP();
            }
            else if (command.equals("list -r"))
            {
                listR();
            }
            else if (command.equals("add"))
            {
                add();
            }
            else if (command.equals("revise"))
            {
                revise();
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
    
    private void listN()
    {
        User chiefNurse = NurseUtil.getChiefNurseByArea(getArea());
        ArrayList<User> wardNurses = NurseUtil.getWardNursesByArea(getArea());
        System.out.println("##护士长信息如下");
        System.out.println("##----------");
        System.out.println(String.format("##ID：%s，姓名：%s", chiefNurse.getId(), chiefNurse.getName()));
        System.out.println("##----------");
        System.out.println("##病房护士信息如下");
        System.out.println("##----------");
        for (User wardNurse: wardNurses)
        {
            System.out.println(String.format("##ID：%s，姓名：%s", wardNurse.getId(), wardNurse.getName()));
        }
        System.out.println("##----------");
    }
    
    private void listP()
    {
        while(true)
        {
            // 筛选条件：是否可以出院，是否待转移，生命状态，病情评级
            Scanner scanner = new Scanner(System.in);
            System.out.println("##请输入筛选条件，或输入“cancel”取消：");
            System.out.println("##（是否可以出院(全部/是/否) 是否待转移(全部/是/否) " +
                    "生命状态(全部/康复出院/在院治疗/病亡) 病情评级(全部/轻症/重症/危重症)）");
            System.out.print(">");
        
            String optionCanLeave = scanner.next();
            if (optionCanLeave.equals("cancel"))
            {
                break;
            }
            else if (!(optionCanLeave.equals("全部") || optionCanLeave.equals("是") || optionCanLeave.equals("否")))
            {
                System.out.println("##是否可以出院:无此筛选条件，请重新输入。");
                continue;
            }
        
            String optionShouldTransfer = scanner.next();
            if (optionShouldTransfer.equals("cancel"))
            {
                break;
            }
            else if (!(optionShouldTransfer.equals("全部") || optionShouldTransfer.equals("是") ||
                    optionShouldTransfer.equals("否")))
            {
                System.out.println("##是否待转移:无此筛选条件，请重新输入。");
                continue;
            }
        
            String optionLifeState = scanner.next();
            if (optionLifeState.equals("cancel"))
            {
                break;
            }
            else if (!(optionLifeState.equals("全部") || optionLifeState.equals("康复出院") ||
                    optionLifeState.equals("在院治疗") || optionLifeState.equals("病亡")))
            {
                System.out.println("##生命状态:无此筛选条件，请重新输入。");
                continue;
            }
        
            String optionIllState = scanner.next();
            if (optionIllState.equals("cancel"))
            {
                break;
            }
            else if (!(optionIllState.equals("全部") || optionIllState.equals("轻症") ||
                    optionIllState.equals("重症") || optionIllState.equals("危重症")))
            {
                System.out.println("##病情评级:无此筛选条件，请重新输入。");
                continue;
            }
        
            // 查询特定患者信息
            ArrayList<Patient> patients = PatientUtil.getPatients(optionCanLeave, optionShouldTransfer,
                    getArea(), "全部", "全部", optionLifeState, optionIllState);
        
            PatientUtil.printPatients(patients);
            break;
        }
    }
    
    private void listR()
    {
        // 选择特定的护士号
        Scanner scanner = new Scanner(System.in);
        System.out.println("##请输入需要查询的护士号，或输入“cancel”取消：");
        System.out.print(">");
    
        String optionUID = scanner.next();
        if (!optionUID.equals("cancel"))
        {
            // 查询特定患者信息
            ArrayList<Patient> patients = PatientUtil.getPatients("全部",
                    "全部", getArea(),
                    optionUID, "全部", "全部", "全部");
        
            PatientUtil.printPatients(patients);
        }
    }
    
    private void add()
    {
        // 为指定患者添加核酸检测单
        
        Scanner scanner = new Scanner(System.in);
    
        String pID;
        while (true)
        {
            System.out.println("##请输入需要添加核酸检测单的患者ID，或输入“cancel”取消：");
            System.out.print(">");
            pID = scanner.nextLine();
            if (pID.equals("cancel"))
            {
                return;
            }
            else if (!PatientUtil.checkArea(getArea(), pID))
            {
                System.out.println("##该患者并不属于您管理，请重试。");
            }
            else
            {
                break;
            }
        }
    
        String date;
        while (true)
        {
            System.out.println("##请输入填写的日期(YYYY-MM-DD)，或输入“cancel”取消：");
            System.out.print(">");
            date = scanner.nextLine();
            if (date.equals("cancel"))
            {
                return;
            }
            else if (!date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}"))
            {
                System.out.println("##日期格式错误，请重试。");
            }
            else
            {
                break;
            }
        }
    
        String result;
        while (true)
        {
            System.out.println("##请输入患者的核酸检测结果(阳性/阴性)，或输入“cancel”取消：");
            System.out.print(">");
            result = scanner.nextLine();
            if (result.equals("cancel"))
            {
                return;
            }
            else if (!(result.equals("阳性") || result.equals("阴性")))
            {
                System.out.println("##无此检测结果选项，请重试。");
            }
            else
            {
                break;
            }
        }
    
        String illState;
        while (true)
        {
            System.out.println("##请输入患者的病情评级(轻症/重症/危重症)，或输入“cancel”取消：");
            System.out.print(">");
            illState = scanner.nextLine();
            if (illState.equals("cancel"))
            {
                return;
            }
            else if (!Patient.isValidIllState(illState))
            {
                System.out.println("##无此病情评级选项，请重试。");
            }
            else
            {
                break;
            }
        }
    
        TestSheet testSheet = new TestSheet(IDGenerator.generateID(), pID, date, result, illState);
    
        PatientUtil.addTestSheet(testSheet);
        System.out.println("##添加核酸检测单成功。");
    }
    
    private void revise()
    {
        // 修改病人的病情评级和生命状态
    
        Scanner scanner = new Scanner(System.in);
    
        String pID;
        Patient patient;
        while (true)
        {
            System.out.println("##请输入修改信息的患者ID，或输入“cancel”取消：");
            System.out.print(">");
            pID = scanner.nextLine();
            if (pID.equals("cancel"))
            {
                return;
            }
            else if (!PatientUtil.checkArea(getArea(), pID))
            {
                System.out.println("##该患者并不属于您管理，请重试。");
            }
            else
            {
                patient = PatientUtil.getPatientByID(pID);
                if (patient == null)
                {
                    System.out.println("##无法找到该患者，请重试。");
                }
                else
                {
                    break;
                }
            }
        }
    
        String lifeState;
        while (true)
        {
            System.out.println("##请输入患者的生命状态(康复出院/在院治疗/病亡)，或输入“cancel”取消：");
            System.out.print(">");
            lifeState = scanner.nextLine();
            if (lifeState.equals("cancel"))
            {
                return;
            }
            else if (!Patient.isValidLifeState(lifeState))
            {
                System.out.println("##无此检测结果选项，请重试。");
            }
            else if (lifeState.equals(Patient.LIFE_RECOVERED) && !patient.isCanLeave())
            {
                System.out.println("##病人未满足条件，不能允许出院，请重试。");
            }
            else
            {
                break;
            }
        }
    
        String illState;
        while (true)
        {
            System.out.println("##请输入患者的病情评级(轻症/重症/危重症)，或输入“cancel”取消：");
            System.out.print(">");
            illState = scanner.nextLine();
            if (illState.equals("cancel"))
            {
                return;
            }
            else if (!Patient.isValidIllState(illState))
            {
                System.out.println("##无此病情评级选项，请重试。");
            }
            else
            {
                break;
            }
        }
        
        PatientUtil.revisePatientState(pID, lifeState, illState);
        
        if (lifeState.equals(Patient.LIFE_RECOVERED) || lifeState.equals(Patient.LIFE_DEAD))
        {
            // 若患者出院或病亡，则删除患者与护士和病床的从属关系
            PatientUtil.deleteOwnership(pID);
            PatientUtil.deleteResponsibility(pID);
        }
    
        PatientUtil.transferArea(); // 病情评级可能改变，自动尝试转移
        
        System.out.println("##修改患者信息成功。");
    }
}
