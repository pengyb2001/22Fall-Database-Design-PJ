package service.authority;

import model.account.Student;
import model.admission_authority.AdmissionAuthority;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AuthorityUtil {
    //根据学生ID返回能有权限进入的校区列表
    public static ArrayList<String> getAuthorityByID(String ID)
    {
        ArrayList<String> authorities = new ArrayList<>();

        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findAuthoritiesByID = con.prepareStatement("select * from admission_authority where student_ID=?");
            findAuthoritiesByID.setString(1, ID);
            try (ResultSet authoritiesFound = findAuthoritiesByID.executeQuery())
            {
                while (authoritiesFound.next())
                {
                    //String student_ID = authoritiesFound.getString("student_ID");
                    String campus_name = authoritiesFound.getString("campus_name");
                    //AdmissionAuthority admissionAuthority = new AdmissionAuthority(student_ID, campus_name);
                    authorities.add(campus_name);
                }
            }

            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }

        return authorities;
    }

    public static void deleteAuthorityByID(String ID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement deleteAuthorityByID = con.prepareStatement("delete from admission_authority where student_ID=?");
            deleteAuthorityByID.setString(1, ID);
            deleteAuthorityByID.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }

    public static void addAuthorityByID(String ID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement addAuthorityByID = con.prepareStatement("insert into admission_authority(student_ID, campus_name) " +
                    "values " +
                    "(?, 'H校区'), (?, 'J校区'), (?,'F校区'), (?, 'Z校区');");
            addAuthorityByID.setString(1, ID);
            addAuthorityByID.setString(2, ID);
            addAuthorityByID.setString(3, ID);
            addAuthorityByID.setString(4, ID);
            addAuthorityByID.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
    //按校区删除所有学生进入的权限（在校除外）
    public static void deleteAuthorityByCampusName(String campus_name)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement deleteAuthorityByCampusName = con.prepareStatement("delete from admission_authority " +
                    "where campus_name = ? " +
                    "and student_ID not in " +
                    "(select a.student_ID " +
                    "from (select a.student_ID from admission_authority a left join student on a.student_ID=student.ID where student.in_school = ?)a)");
            deleteAuthorityByCampusName.setString(1, campus_name);
            deleteAuthorityByCampusName.setString(2, campus_name);
            deleteAuthorityByCampusName.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }

    //根据校区返回能有权限进入的学生列表
    public static ArrayList<Student> getStudentsByCampusName(String campusName)
    {
        ArrayList<Student> students = new ArrayList<>();

        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findStudentsByCampusName = con.prepareStatement("select * from student, admission_authority where student.ID = admission_authority.student_ID and campus_name=?");
            findStudentsByCampusName.setString(1, campusName);
            try (ResultSet studentsFound = findStudentsByCampusName.executeQuery())
            {
                while (studentsFound.next())
                {
                    String ID = studentsFound.getString("ID");
                    String name = studentsFound.getString("name");
                    String phone = studentsFound.getString("phone");
                    String email = studentsFound.getString("email");
                    String personal_address = studentsFound.getString("personal_address");
                    String home_address = studentsFound.getString("home_address");
                    String identity_type = studentsFound.getString("identity_type");
                    String id_num = studentsFound.getString("id_num");
                    String in_school = studentsFound.getString("in_school");
                    String class_name = studentsFound.getString("class_name");
                    String faculty_name = studentsFound.getString("faculty_name");
                    students.add(new Student(ID, name, phone, email, personal_address, home_address, identity_type,
                            id_num, in_school, class_name, faculty_name));
                }
            }
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }

        return students;
    }
}
