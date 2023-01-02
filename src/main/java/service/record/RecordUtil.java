package service.record;

import model.account.Instructor;
import model.account.Student;
import model.record.PassRecord;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class RecordUtil {

    public static void addNewPassRecord(String student_ID, String campus_name, Integer type)
    {
        PassRecord.addRecord(student_ID,campus_name,type);
    }

    public static PassRecord getNearestOutPassRecordByID(String studentID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findNearestOutPassRecordByID;
            findNearestOutPassRecordByID = con.prepareStatement("select * from pass_record where " +
                    "student_ID=? and type = 0 " +
                    "order by pass_num desc ");
            findNearestOutPassRecordByID.setString(1, studentID);
            try(ResultSet passRecordFound = findNearestOutPassRecordByID.executeQuery())
            {
                if(passRecordFound.next())
                {
                    Integer pass_num = passRecordFound.getInt("pass_num");
                    String student_ID = passRecordFound.getString("student_ID");
                    Date timestamp = passRecordFound.getTimestamp("timestamp");
                    String campus_name = passRecordFound.getString("campus_name");
                    con.close();
                    return new PassRecord(pass_num, student_ID, timestamp, campus_name, 0);
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    //根据院系获得过去n天该院系学生进出最多的校区
    public static String getCampusMaxVisitByFaculty(String faculty, int n)
    {
        String campusMaxVisit = "该段时间没有进出校记录";
        try
        {
            Connection con =SQLUtil.getConnection();
            PreparedStatement getCampusMaxVisitByFaculty = con.prepareStatement(
                    "select campus_name, count(*) as cnt " +
                            "from student join pass_record on (student.ID = pass_record.student_ID) " +
                            "where faculty_name = ? and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp) " +
                            "group by campus_name " +
                            "order by cnt desc"
            );
            getCampusMaxVisitByFaculty.setString(1, faculty);
            getCampusMaxVisitByFaculty.setInt(2, n);
            try(ResultSet maxFound = getCampusMaxVisitByFaculty.executeQuery())
            {
                //记录出入校记录最多的校区
                if(maxFound.next())
                {
                    campusMaxVisit = maxFound.getString("campus_name");
                }
                con.close();
                return campusMaxVisit;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    //查询全校过去n天一直在校未曾出校的学生
    public static ArrayList<Student> getInSchoolStudent(int n)
    {
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findStudents = con.prepareStatement("select * from student " +
                    "where (in_school!='不在校') " +
                    "and not exists (select * from pass_record where student_ID=student.ID and type='0' " +
                    "and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp))");
            findStudents.setInt(1, n);
            try(ResultSet usersFound = findStudents.executeQuery())
            {
                while (usersFound.next())
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

    //按院系查询过去n天一直在校未曾出校的学生
    public static ArrayList<Student> getInSchoolStudent(String faculty, int n)
    {
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findStudents = con.prepareStatement("select * from student " +
                    "where (in_school!='不在校') " +
                    "and not exists (select * from pass_record where student_ID=student.ID and type='0' " +
                    "and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp)) " +
                    "and faculty_name=?");
            findStudents.setInt(1, n);
            findStudents.setString(2, faculty);
            try(ResultSet usersFound = findStudents.executeQuery())
            {
                while (usersFound.next())
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

    //按班级查询过去n天一直在校未曾出校的学生
    public static ArrayList<Student> getInSchoolStudent(String classname, String faculty, int n)
    {
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findStudents = con.prepareStatement("select * from student " +
                    "where (in_school!='不在校') " +
                    "and not exists (select * from pass_record where student_ID=student.ID and type='0' " +
                    "and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp)) " +
                    "and faculty_name=? and class_name=?");
            findStudents.setInt(1, n);
            findStudents.setString(2, faculty);
            findStudents.setString(3, classname);
            try(ResultSet usersFound = findStudents.executeQuery())
            {
                while (usersFound.next())
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
