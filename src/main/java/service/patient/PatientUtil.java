/**
 * 实现患者信息管理功能
 */

package service.patient;

import model.patient.DailyState;
import model.patient.Patient;
import model.patient.TestSheet;
import model.user.User;
import service.sql.SQLUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PatientUtil
{
    public static void printPatients(ArrayList<Patient> patients)
    {
        // 列出患者信息
        System.out.println("##查询患者信息如下");
        System.out.println("##----------");
        int count = 1;
        for (Patient patient: patients)
        {
            System.out.println(String.format("##[%d]患者ID：%s, 姓名：%s, 身份证号：%s, 家庭住址：%s, 生命状态：%s",
                    count, patient.getId(), patient.getName(),
                    patient.getResidentID(), patient.getAddress(), patient.getLifeState()));
            count++;
            if (patient.getLifeState().equals(Patient.LIFE_ILL))
            {
                String canLeave = (patient.isCanLeave()) ? "是" : "否";
                String shouldTransfer = (patient.shouldTransfer()) ? "是" : "否";
                System.out.println(String.format("##......病情评级：%s, 是否可以出院：%s, 是否待转移：%s",
                        patient.getIllState(), canLeave, shouldTransfer));
                if (patient.getArea() == null)
                {
                    System.out.println("##......所在区域：隔离区域");
                }
                else
                {
                    System.out.println(String.format("##......所在区域：%s, 房间号：%s, 床号：%s, 护士号：%s, 护士名：%s",
                            patient.getArea(), patient.getrID(), patient.getbID(),
                            patient.getuID(), patient.getuName()));
                }
            }
        }
        System.out.println("##----------");
    
        // 列出可以出院和待转移人数
        int countCanLeave = 0;
        int countShouldTransfer = 0;
    
        for (Patient patient: patients)
        {
            if (patient.isCanLeave())
            {
                countCanLeave++;
            }
            if (patient.shouldTransfer())
            {
                countShouldTransfer++;
            }
        }
    
        if (countCanLeave > 0 || countShouldTransfer > 0)
        {
            System.out.println(String.format("##【提醒】列表中有%d名患者可以出院，有%d名患者等待转移",
                    countCanLeave, countShouldTransfer));
            System.out.println("##----------");
        }
    }
    
    public static ArrayList<Patient> getAllPatients()
    {
    
        ArrayList<Patient> patients = new ArrayList<>();
        try
        {
            // 获取全部患者
            Connection con = SQLUtil.getConnection();
            String sql = "select patient.p_ID as p_ID, patient.name as name, patient.resident_ID as resident_ID, patient.address as address, \n" +
                    "patient.life_state as life_state, patient.ill_state as ill_state, \n" +
                    "user.u_ID as u_ID, user.name as u_name, bed.b_ID as b_ID, room.r_ID as r_ID, room.area as area, \n" +
                    "(select max(temp.temperature) as temperature\n" +
                    "from (select * from daily_state where p_ID=patient.p_ID order by date desc limit 3) as temp) as temperature,\n" +
                    "(select result from test_sheet where p_ID=patient.p_ID order by date desc limit 1) as result1,\n" +
                    "(select result from test_sheet where p_ID=patient.p_ID order by date desc limit 1, 1) as result2\n" +
                    "\tfrom patient left join (nurse_for_patient natural join user) \n" +
                    "    on patient.p_ID = nurse_for_patient.p_ID \n" +
                    "    left join (bed_for_patient natural join bed natural join room_in_bed natural join room) \n" +
                    "    on patient.p_ID = bed_for_patient.p_ID";
            PreparedStatement findAllPatients = con.prepareStatement(sql);
            try (ResultSet patientsFound = findAllPatients.executeQuery())
            {
                while (patientsFound.next())
                {
                    String id = patientsFound.getString("p_id");
                    String name = patientsFound.getString("name");
                    String residentID = patientsFound.getString("resident_ID");
                    String address = patientsFound.getString("address");
                    String lifeState = patientsFound.getString("life_state");
                    String illState = patientsFound.getString("ill_state");
                    String uID = patientsFound.getString("u_ID");
                    String uName = patientsFound.getString("u_name");
                    String bID = patientsFound.getString("b_ID");
                    String rID = patientsFound.getString("r_ID");
                    String area = patientsFound.getString("area");
                    String temperature = patientsFound.getString("temperature");
                    String result1 = patientsFound.getString("result1");
                    String result2 = patientsFound.getString("result2");
                    
                    boolean canLeave = Patient.checkCanLeave(lifeState, illState, temperature, result1, result2);
                    
                    Patient patient = new Patient(id, name, residentID, address, lifeState, illState,
                            canLeave, bID, rID, area, uID, uName);
                    patients.add(patient);
                }
            }
        
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return patients;
    }
    
    public static Patient getPatientByID(String id)
    {
        try
        {
            // 获取指定患者
            Connection con = SQLUtil.getConnection();
            String sql = "select * from (select patient.p_ID as p_ID, patient.name as name, patient.resident_ID as resident_ID, patient.address as address, \n" +
                    "patient.life_state as life_state, patient.ill_state as ill_state, \n" +
                    "user.u_ID as u_ID, user.name as u_name, bed.b_ID as b_ID, room.r_ID as r_ID, room.area as area, \n" +
                    "(select max(temp.temperature) as temperature\n" +
                    "from (select * from daily_state where p_ID=patient.p_ID order by date desc limit 3) as temp) as temperature,\n" +
                    "(select result from test_sheet where p_ID=patient.p_ID order by date desc limit 1) as result1,\n" +
                    "(select result from test_sheet where p_ID=patient.p_ID order by date desc limit 1, 1) as result2\n" +
                    "\tfrom patient left join (nurse_for_patient natural join user) \n" +
                    "    on patient.p_ID = nurse_for_patient.p_ID \n" +
                    "    left join (bed_for_patient natural join bed natural join room_in_bed natural join room) \n" +
                    "    on patient.p_ID = bed_for_patient.p_ID) as patient_info where p_ID=?";
            PreparedStatement findPatientByID = con.prepareStatement(sql);
            findPatientByID.setString(1, id);
            try (ResultSet patientFound = findPatientByID.executeQuery())
            {
                if (patientFound.next())
                {
                    String name = patientFound.getString("name");
                    String residentID = patientFound.getString("resident_ID");
                    String address = patientFound.getString("address");
                    String lifeState = patientFound.getString("life_state");
                    String illState = patientFound.getString("ill_state");
                    String uID = patientFound.getString("u_ID");
                    String uName = patientFound.getString("u_name");
                    String bID = patientFound.getString("b_ID");
                    String rID = patientFound.getString("r_ID");
                    String area = patientFound.getString("area");
                    String temperature = patientFound.getString("temperature");
                    String result1 = patientFound.getString("result1");
                    String result2 = patientFound.getString("result2");
            
                    boolean canLeave = Patient.checkCanLeave(lifeState, illState, temperature, result1, result2);
            
                    Patient patient = new Patient(id, name, residentID, address, lifeState, illState,
                            canLeave, bID, rID, area, uID, uName);
                    return patient;
                }
            }
    
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    
        return null;
    }
    
    public static ArrayList<Patient> getPatients(String optionCanLeave, String optionShouldTransfer, String optionArea,
                                                 String optionUID, String optionBID, String optionLifeState, String optionIllState)
    {
        ArrayList<Patient> patients = new ArrayList<>();
        final String ALL = "全部";
        boolean canLeaveIsAll = optionCanLeave.equals(ALL);
        boolean shouldTransferIsAll = optionShouldTransfer.equals(ALL);
        boolean areaIsAll = optionArea.equals(ALL);
        boolean uIDIsAll = optionUID.equals(ALL);
        boolean bIDIsAll = optionBID.equals(ALL);
        boolean lifeStateIsAll = optionLifeState.equals(ALL);
        boolean illStateIsAll = optionIllState.equals(ALL);
        if (canLeaveIsAll && shouldTransferIsAll && areaIsAll && uIDIsAll && bIDIsAll && lifeStateIsAll && illStateIsAll)
        {
            return getAllPatients();
        }
        
        try
        {
            // 获取指定患者
            Connection con = SQLUtil.getConnection();
            String basicSelect = "select * from (select patient.p_ID as p_ID, patient.name as name, patient.resident_ID as resident_ID, patient.address as address, \n" +
                    "patient.life_state as life_state, patient.ill_state as ill_state, \n" +
                    "user.u_ID as u_ID, user.name as u_name, bed.b_ID as b_ID, room.r_ID as r_ID, room.area as area, \n" +
                    "(select max(temp.temperature) as temperature\n" +
                    "from (select * from daily_state where p_ID=patient.p_ID order by date desc limit 3) as temp) as temperature,\n" +
                    "(select result from test_sheet where p_ID=patient.p_ID order by date desc limit 1) as result1,\n" +
                    "(select result from test_sheet where p_ID=patient.p_ID order by date desc limit 1, 1) as result2\n" +
                    "\tfrom patient left join (nurse_for_patient natural join user) \n" +
                    "    on patient.p_ID = nurse_for_patient.p_ID \n" +
                    "    left join (bed_for_patient natural join bed natural join room_in_bed natural join room) \n" +
                    "    on patient.p_ID = bed_for_patient.p_ID) as patient_info where p_ID is not null";
            String sql = basicSelect;
            
            // 添加筛选项
            if (!canLeaveIsAll)
            {
                // 可以出院的标准是：在院治疗，病情评级为轻症，连续3天体温低于37.3摄氏度，连续两次核酸检测结果为阴性
                String restrictCanLeave = "(life_state='在院治疗' and ill_state='轻症' and temperature < 37.3 and " +
                        "result1='阴性' and result2='阴性')";
                if (optionCanLeave.equals("是"))
                {
                    sql += " and " + restrictCanLeave + " is true";
                }
                else if (optionCanLeave.equals("否"))
                {
                    sql += " and " + restrictCanLeave + " is not true";
                }
            }
            
            if (!shouldTransferIsAll)
            {
                //待转移的标准是：在院治疗，在隔离区（所在区域为null），或所在区域与病情评级不符合
                String restrictShouldTransfer = "(life_state='在院治疗' and (area is null or not" +
                        "((ill_state='轻症' and area='轻症区域') or " +
                        "(ill_state='重症' and area='重症区域') or " +
                        "(ill_state='危重症' and area='危重症区域'))))";
                if (optionShouldTransfer.equals("是"))
                {
                    sql += " and " + restrictShouldTransfer + " is true";
                }
                else if (optionShouldTransfer.equals("否"))
                {
                    sql += " and " + restrictShouldTransfer + " is not true";
                }
            }
            
            ArrayList<String> preparedArgs = new ArrayList<>();
            
            if (!areaIsAll)
            {
                if (optionArea.equals("隔离区域"))
                {
                    sql += " and area is null";
                }
                else
                {
                    sql += " and area=?";
                    preparedArgs.add(optionArea);
                }
            }
    
            if (!uIDIsAll)
            {
                sql += " and u_id=?";
                preparedArgs.add(optionUID);
            }
    
            if (!bIDIsAll)
            {
                sql += " and b_id=?";
                preparedArgs.add(optionBID);
            }
    
            if (!lifeStateIsAll)
            {
                sql += " and life_state=?";
                preparedArgs.add(optionLifeState);
            }
    
            if (!illStateIsAll)
            {
                sql += " and ill_state=?";
                preparedArgs.add(optionIllState);
            }
            
            PreparedStatement findCertainPatients = con.prepareStatement(sql);
            int count = 1;
            for (String arg : preparedArgs)
            {
                findCertainPatients.setString(count, arg);
                count++;
            }
            
            try (ResultSet patientsFound = findCertainPatients.executeQuery())
            {
                while (patientsFound.next())
                {
                    String id = patientsFound.getString("p_id");
                    String name = patientsFound.getString("name");
                    String residentID = patientsFound.getString("resident_ID");
                    String address = patientsFound.getString("address");
                    String lifeState = patientsFound.getString("life_state");
                    String illState = patientsFound.getString("ill_state");
                    String uID = patientsFound.getString("u_ID");
                    String uName = patientsFound.getString("u_name");
                    String bID = patientsFound.getString("b_ID");
                    String rID = patientsFound.getString("r_ID");
                    String area = patientsFound.getString("area");
                    String temperature = patientsFound.getString("temperature");
                    String result1 = patientsFound.getString("result1");
                    String result2 = patientsFound.getString("result2");
                
                    boolean canLeave = Patient.checkCanLeave(lifeState, illState, temperature, result1, result2);
                
                    Patient patient = new Patient(id, name, residentID, address, lifeState, illState,
                            canLeave, bID, rID, area, uID, uName);
                    patients.add(patient);
                }
            }
        
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        
        return patients;
    }
    
    // 检查患者是否属于病房护士管理
    public static boolean checkResponsibility(String uID, String pID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findResponsibility = con.prepareStatement("select * from nurse_for_patient where " +
                    "u_ID=? and p_ID=?");
            findResponsibility.setString(1, uID);
            findResponsibility.setString(2, pID);
            try (ResultSet responsibilityFound = findResponsibility.executeQuery())
            {
                if (responsibilityFound.next())
                {
                    con.close();
                    return true;
                }
                else
                {
                    con.close();
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return false;
    }
    
    public static void addDailyState(DailyState dailyState)
    {
        try
        {
            // 新增每日状态
            Connection con = SQLUtil.getConnection();
            PreparedStatement addNewDailyState = con.prepareStatement("insert into daily_state " +
                    "values (?,?,?,?,?,?,?)");
            addNewDailyState.setString(1, dailyState.getId());
            addNewDailyState.setString(2, dailyState.getpID());
            addNewDailyState.setDate(3, Date.valueOf(dailyState.getDate()));
            addNewDailyState.setBigDecimal(4, BigDecimal.valueOf(
                    Double.parseDouble(dailyState.getTemperature())));
            addNewDailyState.setString(5, dailyState.getSymptom());
            addNewDailyState.setString(6, dailyState.getResult());
            addNewDailyState.setString(7, dailyState.getLifeState());
            addNewDailyState.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
    
    // 检查患者是否在某一区域
    public static boolean checkArea(String area, String pID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findInArea = con.prepareStatement("select * from bed_for_patient " +
                    "natural join bed natural join room_in_bed natural join room " +
                    "where area=? and p_ID=?");
            findInArea.setString(1, area);
            findInArea.setString(2, pID);
            try (ResultSet inAreaFound = findInArea.executeQuery())
            {
                if (inAreaFound.next())
                {
                    con.close();
                    return true;
                }
                else
                {
                    con.close();
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return false;
    }
    
    public static void addTestSheet(TestSheet testSheet)
    {
        try
        {
            // 新增核酸检测单
            Connection con = SQLUtil.getConnection();
            PreparedStatement addNewTestSheet = con.prepareStatement("insert into test_sheet " +
                    "values (?,?,?,?,?)");
            addNewTestSheet.setString(1, testSheet.getId());
            addNewTestSheet.setString(2, testSheet.getpID());
            addNewTestSheet.setDate(3, Date.valueOf(testSheet.getDate()));
            addNewTestSheet.setString(4, testSheet.getResult());
            addNewTestSheet.setString(5, testSheet.getIllState());
            addNewTestSheet.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
    
    // 为患者找到空闲的病房护士号
    public static String getFreeWardNurseID(String area)
    {
        try
        {
            TreeMap<String, Integer> uIDCountMap = new TreeMap<>();
            // 获取当前区域全部病房护士
            Connection con = SQLUtil.getConnection();
            PreparedStatement findWardNursesByArea = con.prepareStatement("select * from user " +
                    "left join nurse_for_patient on user.u_ID=nurse_for_patient.u_ID " +
                    "where user.post='病房护士' and user.area=?");
            findWardNursesByArea.setString(1, area);
            try (ResultSet wardNursesFound = findWardNursesByArea.executeQuery())
            {
                while (wardNursesFound.next())
                {
                    String id = wardNursesFound.getString("u_id");
                    String pID = wardNursesFound.getString("p_ID");
                    if (!uIDCountMap.containsKey(id))
                    {
                        uIDCountMap.put(id, 0);
                    }
                    if (pID != null)
                    {
                        int oldCount = uIDCountMap.get(id);
                        uIDCountMap.put(id, oldCount + 1);
                    }
                }
            }
            
            //轻症区域的一位病房护士最多照顾3位病人，重症区域的一位病房护士最多照顾2位病人，危重症区域的一位病房护士最多照顾1位病人
            int maxResponsibility = 0;
            if (area.equals("轻症区域"))
            {
                maxResponsibility = 3;
            }
            else if (area.equals("重症区域"))
            {
                maxResponsibility = 2;
            }
            else if (area.equals("危重症区域"))
            {
                maxResponsibility = 1;
            }
    
            String freeUID = null;
            for (Map.Entry<String, Integer> entry : uIDCountMap.entrySet())
            {
                if (entry.getValue() < maxResponsibility)
                {
                    freeUID = entry.getKey();
                    break;
                }
            }
        
            con.close();
            return freeUID;
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }
    
    // 为患者找到空闲的病床号
    public static String getFreeBedID(String area)
    {
        try
        {
            TreeMap<String, Integer> uIDCountMap = new TreeMap<>();
            Connection con = SQLUtil.getConnection();
            PreparedStatement findBedsByArea = con.prepareStatement("select * from " +
                    "(bed natural join room_in_bed natural join room) " +
                    "left join bed_for_patient on bed.b_ID=bed_for_patient.b_ID " +
                    "where area=? and p_ID is null");
            findBedsByArea.setString(1, area);
            try (ResultSet bedsFound = findBedsByArea.executeQuery())
            {
                if (bedsFound.next())
                {
                    String id = bedsFound.getString("b_id");
                    con.close();
                    return id;
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }
    
    // 添加患者与护士的从属关系
    public static void addResponsibility(String pID, String uID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement addNewResponsibility = con.prepareStatement("insert into nurse_for_patient " +
                    "values (?,?)");
            addNewResponsibility.setString(1, uID);
            addNewResponsibility.setString(2, pID);
            addNewResponsibility.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
    
    // 添加患者与病床的从属关系
    public static void addOwnership(String pID, String bID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement addNewOwnership = con.prepareStatement("insert into bed_for_patient " +
                    "values (?,?)");
            addNewOwnership.setString(1, bID);
            addNewOwnership.setString(2, pID);
            addNewOwnership.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
    
    // 删除患者与护士的从属关系
    public static void deleteResponsibility(String pID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement removeResponsibility = con.prepareStatement("delete from nurse_for_patient " +
                    "where p_ID=?");
            removeResponsibility.setString(1, pID);
            removeResponsibility.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
    
    // 删除患者与病床的从属关系
    public static void deleteOwnership(String pID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement removeOwnership = con.prepareStatement("delete from bed_for_patient " +
                    "where p_ID=?");
            removeOwnership.setString(1, pID);
            removeOwnership.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
    
    // 尝试将所有待转移的患者转移
    public static void transferArea()
    {
        ArrayList<Patient> patients = getAllPatients();
        for (Patient patient : patients)
        {
            if (patient.shouldTransfer())
            {
                // 尝试转移
                String area = null;
                if (patient.getIllState().equals(Patient.ILL_MODERATE))
                {
                    area = "轻症区域";
                }
                else if (patient.getIllState().equals(Patient.ILL_SERIOUS))
                {
                    area = "重症区域";
                }
                else if (patient.getIllState().equals(Patient.ILL_SEVERE))
                {
                    area = "危重症区域";
                }
                
                String uID = getFreeWardNurseID(area);
                String bID = getFreeBedID(area);
                if (uID != null && bID != null)
                {
                    // 清除原有的从属关系
                    deleteResponsibility(patient.getId());
                    deleteOwnership(patient.getId());
                    
                    // 新增从属关系
                    addResponsibility(patient.getId(), uID);
                    addOwnership(patient.getId(), bID);
                }
            }
        }
    }
    
    // 添加新患者
    public static void addPatient(Patient patient)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement addNewPatient = con.prepareStatement("insert into patient " +
                    "values (?,?,?,?,?,?)");
            addNewPatient.setString(1, patient.getId());
            addNewPatient.setString(2, patient.getName());
            addNewPatient.setString(3, patient.getResidentID());
            addNewPatient.setString(4, patient.getAddress());
            addNewPatient.setString(5, patient.getLifeState());
            addNewPatient.setString(6, patient.getIllState());
            addNewPatient.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
    
    // 修改患者病情信息
    public static void revisePatientState(String pID, String lifeState, String illState)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement updatePatientState = con.prepareStatement("update patient " +
                    "set life_state=?, ill_state=? where p_ID=?");
            updatePatientState.setString(1, lifeState);
            updatePatientState.setString(2, illState);
            updatePatientState.setString(3, pID);
            updatePatientState.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
}
