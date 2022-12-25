/**
 * 病房护士
 */

package model.user;

import model.patient.DailyState;
import model.patient.Patient;
import service.id.IDGenerator;
import service.patient.PatientUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class WardNurse extends User
{
    public static final String TYPE = "病房护士";
    
    WardNurse(String id, String username, String password, String name, String area)
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
            System.out.println("##病房护士可以进行以下操作：");
            System.out.println("##指令“list”：查看自己负责的患者信息");
            System.out.println("##指令“add”：新增自己负责的患者的每日状态");
            System.out.println("##指令“logout”：注销");
            System.out.println("##指令“exit”：退出系统");
            System.out.println("##==========");
            System.out.print(">");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.equals("list"))
            {
                list();
            }
            else if (command.equals("add"))
            {
                add();
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
    
    private void list()
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
                    getArea(), getId(), "全部", optionLifeState, optionIllState);
        
            PatientUtil.printPatients(patients);
            break;
        }
    }
    
    private void add()
    {
        // 为指定患者添加每日状态
        Scanner scanner = new Scanner(System.in);
    
        String pID;
        while (true)
        {
            System.out.println("##请输入需要添加每日状态的患者ID，或输入“cancel”取消：");
            System.out.print(">");
            pID = scanner.nextLine();
            if (pID.equals("cancel"))
            {
                return;
            }
            else if (!PatientUtil.checkResponsibility(getId(), pID))
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
    
        String temperature;
        while (true)
        {
            System.out.println("##请输入患者的体温(XX.X)(摄氏度)，或输入“cancel”取消：");
            System.out.print(">");
            temperature = scanner.nextLine();
            if (temperature.equals("cancel"))
            {
                return;
            }
            else if (!temperature.matches("[0-9]{2}.[0-9]"))
            {
                System.out.println("##体温格式错误，请重试。");
            }
            else
            {
                break;
            }
        }
    
        System.out.println("##请输入患者的症状，或输入“cancel”取消：");
        System.out.print(">");
        String symptom = scanner.nextLine();
        if (symptom.equals("cancel"))
        {
            return;
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
                System.out.println("##无此生命状态选项，请重试。");
            }
            else
            {
                break;
            }
        }
    
        DailyState dailyState = new DailyState(IDGenerator.generateID(), pID, date,
                temperature, symptom, result, lifeState);
    
        PatientUtil.addDailyState(dailyState);
        System.out.println("##添加每日状态成功。");
    }
}
