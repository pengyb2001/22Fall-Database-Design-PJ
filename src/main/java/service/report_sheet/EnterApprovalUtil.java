package service.report_sheet;

import model.account.Student;
import model.report_sheet.EnterApproval;
import model.report_sheet.LeaveApproval;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class EnterApprovalUtil {
    //查询是否存在未处理完的入校申请
    public static boolean enterApprovalExists(String id) {
        return getEnterApproval(id, 4) != null;//4代表查找状态为0、1、2的离校申请
    }

    //根据学生ID和审批状态查找入校申请
    public static EnterApproval getEnterApproval(String id, Integer sta) {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findEnterApprovalByIdandStatus;
            if(sta != 4){
                findEnterApprovalByIdandStatus = con.prepareStatement(
                        "select * from enter_approval " +
                                "where student_ID=? " +
                                "and status=? " +
                                "order by form_num DESC;"
                );
                findEnterApprovalByIdandStatus.setString(1, id);
                findEnterApprovalByIdandStatus.setInt(2, sta);
            }
            else
            {
                findEnterApprovalByIdandStatus = con.prepareStatement(
                        "select * from enter_approval " +
                                "where student_ID=? " +
                                "and (status=0 or status=1 or status=2) " +
                                "order by form_num DESC;"
                );
                findEnterApprovalByIdandStatus.setString(1, id);
            }
            try (ResultSet enterApprovalFond = findEnterApprovalByIdandStatus.executeQuery())
            {
                if(enterApprovalFond.next())
                {
//                    System.out.println(leaveApprovalFound.getString("student_ID"));//debug
                    Integer form_num = enterApprovalFond.getInt("form_num");
                    String student_ID = enterApprovalFond.getString("student_ID");
                    Date timestamp = enterApprovalFond.getDate("timestamp");
                    String reason = enterApprovalFond.getString("reason");
                    String lived_area = enterApprovalFond.getString("lived_area");
                    Date entry_date = enterApprovalFond.getDate("entry_date");
                    Integer status = enterApprovalFond.getInt("status");
                    String refuse_reason = enterApprovalFond.getString("refuse_reason");
                    con.close();
                    return new EnterApproval(form_num, student_ID, timestamp, reason, lived_area, entry_date,
                            status, refuse_reason);
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    public static void addEnterApproval(String student_ID, String reason, String lived_area,
                                        String entry_date)
    {
        try
        {
            //增加入校申请
            Connection con = SQLUtil.getConnection();
            PreparedStatement addNewEnterApproval = con.prepareStatement("insert into " +
                    "enter_approval(student_ID, timestamp, reason, lived_area, entry_date, status)" +
                    "values (?, now(), ?, ?, ?, 0)");

            //把时间类型转换为sql Date
            java.sql.Date entryDate = new java.sql.Date(LeaveApprovalUtil.dateChange(entry_date).getTime());

            addNewEnterApproval.setString(1, student_ID);
            addNewEnterApproval.setString(2, reason);
            addNewEnterApproval.setString(3, lived_area);
            addNewEnterApproval.setDate(4, entryDate);

            addNewEnterApproval.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }

    //根据班级和院系和审批状态查找入校申请
    public static ArrayList<EnterApproval> getEnterApprovals(String class_name, String faculty_name, Integer sta) {
        ArrayList<EnterApproval> enterApprovals = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findEnterApprovalByClassFacultyandStatus;
            if(sta != 4){
                findEnterApprovalByClassFacultyandStatus = con.prepareStatement(
                        "select * from enter_approval, student " +
                                "where student_ID=ID " +
                                "and class_name = ? and faculty_name = ? and status = ?;"
                );
                findEnterApprovalByClassFacultyandStatus.setString(1, class_name);
                findEnterApprovalByClassFacultyandStatus.setString(2, faculty_name);
                findEnterApprovalByClassFacultyandStatus.setInt(3, sta);
            }
            else
            {
                findEnterApprovalByClassFacultyandStatus = con.prepareStatement(
                        "select * from enter_approval, student " +
                                "where student_ID=ID " +
                                "and class_name = ? and faculty_name = ? and (status=0 or status=1 or status=2);"
                );
                findEnterApprovalByClassFacultyandStatus.setString(1, class_name);
                findEnterApprovalByClassFacultyandStatus.setString(2, faculty_name);
            }
            try (ResultSet enterApprovalFound = findEnterApprovalByClassFacultyandStatus.executeQuery())
            {
                while (enterApprovalFound.next())
                {
                    Integer form_num = enterApprovalFound.getInt("form_num");
                    String student_ID = enterApprovalFound.getString("student_ID");
                    Date timestamp = enterApprovalFound.getDate("timestamp");
                    String reason = enterApprovalFound.getString("reason");
                    String lived_area = enterApprovalFound.getString("lived_area");
                    Date entry_date = enterApprovalFound.getDate("entry_date");
                    Integer status = enterApprovalFound.getInt("status");
                    String refuse_reason = enterApprovalFound.getString("refuse_reason");
                    enterApprovals.add(new EnterApproval(form_num, student_ID, timestamp, reason, lived_area, entry_date,
                            status, refuse_reason));
                }
                con.close();
                return enterApprovals;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    //根据班级和院系和审批状态查找过去n天的入校申请
    public static ArrayList<EnterApproval> getEnterApprovals(String class_name, String faculty_name, Integer sta, int n) {
        ArrayList<EnterApproval> enterApprovals = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findEnterApprovalByClassFacultyandStatus;
            if(sta != 4){
                findEnterApprovalByClassFacultyandStatus = con.prepareStatement(
                        "select * from enter_approval, student " +
                                "where student_ID=ID " +
                                "and class_name = ? and faculty_name = ? and status = ? " +
                                "and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp);"
                );
                findEnterApprovalByClassFacultyandStatus.setString(1, class_name);
                findEnterApprovalByClassFacultyandStatus.setString(2, faculty_name);
                findEnterApprovalByClassFacultyandStatus.setInt(3, sta);
                findEnterApprovalByClassFacultyandStatus.setInt(4, n-1);
            }
            else
            {
                findEnterApprovalByClassFacultyandStatus = con.prepareStatement(
                        "select * from enter_approval, student " +
                                "where student_ID=ID " +
                                "and class_name = ? and faculty_name = ? and (status=0 or status=1 or status=2) " +
                                "and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp);"
                );
                findEnterApprovalByClassFacultyandStatus.setString(1, class_name);
                findEnterApprovalByClassFacultyandStatus.setString(2, faculty_name);
                findEnterApprovalByClassFacultyandStatus.setInt(3, n-1);
            }
            try (ResultSet enterApprovalFound = findEnterApprovalByClassFacultyandStatus.executeQuery())
            {
                while (enterApprovalFound.next())
                {
                    Integer form_num = enterApprovalFound.getInt("form_num");
                    String student_ID = enterApprovalFound.getString("student_ID");
                    Date timestamp = enterApprovalFound.getDate("timestamp");
                    String reason = enterApprovalFound.getString("reason");
                    String lived_area = enterApprovalFound.getString("lived_area");
                    Date entry_date = enterApprovalFound.getDate("entry_date");
                    Integer status = enterApprovalFound.getInt("status");
                    String refuse_reason = enterApprovalFound.getString("refuse_reason");
                    enterApprovals.add(new EnterApproval(form_num, student_ID, timestamp, reason, lived_area, entry_date,
                            status, refuse_reason));
                }
                con.close();
                return enterApprovals;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    //根据院系和审批状态查找入校申请
    public static ArrayList<EnterApproval> getEnterApprovals_1(String faculty_name, Integer sta) {
        ArrayList<EnterApproval> enterApprovals = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findEnterApprovalByClassFacultyandStatus;
            if(sta != 4){
                findEnterApprovalByClassFacultyandStatus = con.prepareStatement(
                        "select * from enter_approval, student " +
                                "where student_ID=ID " +
                                "and faculty_name = ? and status = ?;"
                );
                findEnterApprovalByClassFacultyandStatus.setString(1, faculty_name);
                findEnterApprovalByClassFacultyandStatus.setInt(2, sta);
            }
            else
            {
                findEnterApprovalByClassFacultyandStatus = con.prepareStatement(
                        "select * from enter_approval, student " +
                                "where student_ID=ID " +
                                "and faculty_name = ? and (status=0 or status=1 or status=2);"
                );
                findEnterApprovalByClassFacultyandStatus.setString(1, faculty_name);
            }
            try (ResultSet enterApprovalFound = findEnterApprovalByClassFacultyandStatus.executeQuery())
            {
                while (enterApprovalFound.next())
                {
                    Integer form_num = enterApprovalFound.getInt("form_num");
                    String student_ID = enterApprovalFound.getString("student_ID");
                    Date timestamp = enterApprovalFound.getDate("timestamp");
                    String reason = enterApprovalFound.getString("reason");
                    String lived_area = enterApprovalFound.getString("lived_area");
                    Date entry_date = enterApprovalFound.getDate("entry_date");
                    Integer status = enterApprovalFound.getInt("status");
                    String refuse_reason = enterApprovalFound.getString("refuse_reason");
                    enterApprovals.add(new EnterApproval(form_num, student_ID, timestamp, reason, lived_area, entry_date,
                            status, refuse_reason));
                }
                con.close();
                return enterApprovals;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    //变更入校申请
    public static void updateEnterApproval(EnterApproval enterApproval)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement updateEnterApprovalByFormNum = con.prepareStatement("update enter_approval set " +
                    "student_ID = ?,timestamp = ?,reason = ?,lived_area = ?,entry_date = ?," +
                    "status = ?,refuse_reason = ? where form_num = ?");
            updateEnterApprovalByFormNum.setInt(8, enterApproval.getForm_num());
            updateEnterApprovalByFormNum.setString(1, enterApproval.getStudent_ID());
            updateEnterApprovalByFormNum.setDate(2, (java.sql.Date) enterApproval.getTimestamp());
            updateEnterApprovalByFormNum.setString(3, enterApproval.getReason());
            updateEnterApprovalByFormNum.setString(4, enterApproval.getLived_area());
            updateEnterApprovalByFormNum.setDate(5, (java.sql.Date) enterApproval.getEntry_date());
            updateEnterApprovalByFormNum.setInt(6, enterApproval.getStatus());
            updateEnterApprovalByFormNum.setString(7, enterApproval.getRefuse_reason());
            updateEnterApprovalByFormNum.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }

    //撤销入校申请
    public static void deleteEnterApproval(Integer form_num)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement deleteEnterApprovalByFormNum = con.prepareStatement("delete from enter_approval " +
                    "where form_num = ?");
            deleteEnterApprovalByFormNum.setInt(1, form_num);
            deleteEnterApprovalByFormNum.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }

    //根据学号查询入校申请次数
    public static int getCount(String id)
    {
        int count = 0;
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findCount = con.prepareStatement("select count(*) as cnt from enter_approval " +
                    "where student_ID=?");
            findCount.setString(1, id);
            try(ResultSet countFound = findCount.executeQuery()){
                while (countFound.next())
                {
                    count = countFound.getInt("cnt");
                }
                con.close();
                return count;
            }

        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return 0;
    }
    //查询前n个提交入校申请最多的学生
    public static ArrayList<Student> getMaxEnterApproval()
    {
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findMaxEnterApproval = con.prepareStatement("select student.*, count(*) as cnt " +
                    "from student right join enter_approval ea on student.ID = ea.student_ID " +
                    "group by student.ID " +
                    "order by cnt desc");
            try(ResultSet usersFound = findMaxEnterApproval.executeQuery())
            {
                while(usersFound.next())
                {
                    String ID = usersFound.getString("ID");
                    String name = usersFound.getString("name");
                    String phone = usersFound.getString("phone");
                    String email = usersFound.getString("email");
                    String personal_address = usersFound.getString("personal_address");
                    String home_address = usersFound.getString("home_address");
                    String identity_type = usersFound.getString("identity_type");
                    String id_num = usersFound.getString("id_num");
                    String in_school = usersFound.getString("in_school");
                    String class_name = usersFound.getString("class_name");
                    String faculty_name = usersFound.getString("faculty_name");
                    students.add(new Student(ID, name, phone, email, personal_address, home_address, identity_type,
                            id_num, in_school, class_name, faculty_name));
                }
                con.close();
                return students;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    //按院系查询前n个提交入校申请最多的学生
    public static ArrayList<Student> getMaxEnterApproval(String faculty)
    {
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findMaxEnterApproval = con.prepareStatement("select student.*, count(*) as cnt " +
                    "from student right join enter_approval ea on student.ID = ea.student_ID " +
                    "where faculty_name=?" +
                    "group by student.ID " +
                    "order by cnt desc");
            findMaxEnterApproval.setString(1, faculty);
            try(ResultSet usersFound = findMaxEnterApproval.executeQuery())
            {
                while(usersFound.next())
                {
                    String ID = usersFound.getString("ID");
                    String name = usersFound.getString("name");
                    String phone = usersFound.getString("phone");
                    String email = usersFound.getString("email");
                    String personal_address = usersFound.getString("personal_address");
                    String home_address = usersFound.getString("home_address");
                    String identity_type = usersFound.getString("identity_type");
                    String id_num = usersFound.getString("id_num");
                    String in_school = usersFound.getString("in_school");
                    String class_name = usersFound.getString("class_name");
                    String faculty_name = usersFound.getString("faculty_name");
                    students.add(new Student(ID, name, phone, email, personal_address, home_address, identity_type,
                            id_num, in_school, class_name, faculty_name));
                }
                con.close();
                return students;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    //按班级查询前n个提交入校申请最多的学生
    public static ArrayList<Student> getMaxEnterApproval(String classname, String faculty)
    {
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findMaxEnterApproval = con.prepareStatement("select student.*, count(*) as cnt " +
                    "from student right join enter_approval ea on student.ID = ea.student_ID " +
                    "where faculty_name=? and class_name=?" +
                    "group by student.ID " +
                    "order by cnt desc");
            findMaxEnterApproval.setString(1, faculty);
            findMaxEnterApproval.setString(2, classname);
            try(ResultSet usersFound = findMaxEnterApproval.executeQuery())
            {
                while(usersFound.next())
                {
                    String ID = usersFound.getString("ID");
                    String name = usersFound.getString("name");
                    String phone = usersFound.getString("phone");
                    String email = usersFound.getString("email");
                    String personal_address = usersFound.getString("personal_address");
                    String home_address = usersFound.getString("home_address");
                    String identity_type = usersFound.getString("identity_type");
                    String id_num = usersFound.getString("id_num");
                    String in_school = usersFound.getString("in_school");
                    String class_name = usersFound.getString("class_name");
                    String faculty_name = usersFound.getString("faculty_name");
                    students.add(new Student(ID, name, phone, email, personal_address, home_address, identity_type,
                            id_num, in_school, class_name, faculty_name));
                }
                con.close();
                return students;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }
}
