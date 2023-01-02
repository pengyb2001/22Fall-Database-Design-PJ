/**
 * 实现账户管理功能
 */

package service.account;

import model.account.FacultyAdmin;
import model.account.Instructor;
import model.account.Student;

import model.record.PassRecord;
import service.record.RecordUtil;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class AccountUtil
{
    public static boolean usernameExists(String username)
    {
        int len = username.length();
        String type;
        switch (len) {
            case 4 -> {type = "faculty_administrator";
            return getFacultyAdministrator(username) != null;}
            case 5 -> {type = "instructor";
            return getInstructor(username) != null;}
            case 11 -> {type = "student";
            return getStudent(username) != null;}
            default -> {
                type = "default";
                return false;
            }
        }
    }


    public static ArrayList<Student> getClassList(String className, String facultyName)
    {
    ArrayList<Student> students = new ArrayList<>();
    try
    {
        // 查找用户名
        Connection con = SQLUtil.getConnection();
        PreparedStatement findStudentByClassFaculty = con.prepareStatement("select * from student where class_name = ? and faculty_name = ?");
        findStudentByClassFaculty.setString(1, className);
        findStudentByClassFaculty.setString(2, facultyName);
        try (ResultSet usersFound = findStudentByClassFaculty.executeQuery())
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

    public static ArrayList<Student> getFacultyList(String facultyName)
    {
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findStudentByClassFaculty = con.prepareStatement("select * from student where faculty_name = ?");
            findStudentByClassFaculty.setString(1, facultyName);
            try (ResultSet usersFound = findStudentByClassFaculty.executeQuery())
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
    
    public static Student getStudent(String username)
    {

        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findUserByUsername = con.prepareStatement("select * from student where ID=?");
            findUserByUsername.setString(1, username);
            try (ResultSet usersFound = findUserByUsername.executeQuery())
            {
                if (usersFound.next())
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
                    con.close();
                    return new Student(ID, name, phone, email, personal_address, home_address, identity_type,
                            id_num, in_school, class_name, faculty_name);
                }
                else
                {
                    con.close();
                    return null;
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    public static int getCountByClass(String classname, String facultyname)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement getCountByClass = con.prepareStatement("select count(*) from student where class_name=? " +
                    "and faculty_name=?");
            getCountByClass.setString(1, classname);
            getCountByClass.setString(2, facultyname);
            ResultSet numOfClass = getCountByClass.executeQuery();
            int num = 0;
            while (numOfClass.next()){
                num = numOfClass.getInt(1);
            }
            return num;
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return 0;
    }

    public static FacultyAdmin getFacultyAdministrator(String username)
    {

        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findUserByUsername = con.prepareStatement("select * from faculty_administrator where ID=?");
            findUserByUsername.setString(1, username);
            try (ResultSet usersFound = findUserByUsername.executeQuery())
            {
                if (usersFound.next())
                {
                    String ID = usersFound.getString("ID");
                    String name = usersFound.getString("name");
                    String faculty_name = usersFound.getString("faculty_name");
                    con.close();
                    return new FacultyAdmin(ID, name, faculty_name);
                }
                else
                {
                    con.close();
                    return null;
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    public static Instructor getInstructor(String username)
    {

        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findUserByUsername = con.prepareStatement("select * from instructor where ID=?");
            findUserByUsername.setString(1, username);
            try (ResultSet usersFound = findUserByUsername.executeQuery())
            {
                if (usersFound.next())
                {
                    String ID = usersFound.getString("ID");
                    String name = usersFound.getString("name");
                    String class_name = usersFound.getString("class_name");
                    String faculty_name = usersFound.getString("faculty_name");
                    con.close();
                    return new Instructor(ID, name, class_name, faculty_name);
                }
                else
                {
                    con.close();
                    return null;
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    public static boolean instructorExists(String classname, String faculty) {
        return getInstructorByClassAndFaculty(classname, faculty) != null;
    }

    public static Instructor getInstructorByClassAndFaculty(String classname, String faculty) {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findInstructorByClassAndFaculty;
            findInstructorByClassAndFaculty = con.prepareStatement("select * from instructor where " +
                    "class_name=? and faculty_name=?");
            findInstructorByClassAndFaculty.setString(1, classname);
            findInstructorByClassAndFaculty.setString(2, faculty);
            try(ResultSet instructorFond = findInstructorByClassAndFaculty.executeQuery())
            {
                if(instructorFond.next())
                {
                    String ID = instructorFond.getString("ID");
                    String name = instructorFond.getString("name");
                    String class_name = instructorFond.getString("class_name");
                    String faculty_name = instructorFond.getString("faculty_name");
                    con.close();
                    return new Instructor(ID, name, class_name, faculty_name);
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    //获取已提交出校申请但未离校的学生
    public static ArrayList<Student> getInSchoolLeaveStudents(String classname, String faculty)
    {
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findStudent = con.prepareStatement(
                    "(select student.* " +
                            "from student, leave_approval " +
                            "where ID=student_ID and (in_school != '不在校') and (status = 0 or status = 1 or status = 2)" +
                            "and class_name=? and faculty_name=?) " +
                            "union " +
                            "(select * " +
                            "from student " +
                            "where (in_school != '不在校') and class_name=? and faculty_name=? " +
                            "and not exists(select * from admission_authority " +
                            "where admission_authority.student_ID=student.ID))"
            );
            findStudent.setString(1, classname);
            findStudent.setString(2, faculty);
            findStudent.setString(3, classname);
            findStudent.setString(4, faculty);

            try (ResultSet usersFound = findStudent.executeQuery())
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

    //获取全校已提交出校申请但未离校的学生
    public static ArrayList<Student> getInSchoolLeaveStudents()
    {
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findStudent = con.prepareStatement(
                    "(select student.* " +
                            "from student, leave_approval " +
                            "where ID=student_ID and (in_school != '不在校') and (status = 0 or status = 1 or status = 2)) " +
                            "union " +
                            "(select * " +
                            "from student " +
                            "where (in_school != '不在校') " +
                            "and not exists(select * from admission_authority " +
                            "where admission_authority.student_ID=student.ID))"
            );

            try (ResultSet usersFound = findStudent.executeQuery())
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

    //根据班级和院系获取未提交出校申请但离校超过24H的学生
    //提交离校申请的，离校申请状态为0、1、2；对于离校刚审批通过的 通过有无入校权限来判断
    public  static ArrayList<Student> getLeaveStudents(String classname, String faculty)
    {
        long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findStudent = con.prepareStatement(
                    "select * " +
                            "from student where (in_school = '不在校') and class_name=? and faculty_name=? " +
                            "and not exists(select * from leave_approval where student_ID=student.ID " +
                                            "and (status = 0 or status = 1 or status = 2)) " +
                            "and ID in(select student_ID from admission_authority where student_ID=student.ID)"
            );
            findStudent.setString(1, classname);
            findStudent.setString(2, faculty);

            try (ResultSet usersFound = findStudent.executeQuery())
            {
                while (usersFound.next())
                {
                    PassRecord passRecord = RecordUtil.getNearestOutPassRecordByID(usersFound.getString("ID"));
                    Date nowDate = new Date();
                    boolean moreThanDay = Math.abs(nowDate.getTime() - passRecord.getTimestamp().getTime()) > MILLIS_PER_DAY;
                    //离校超过24H才存入
                    if(moreThanDay)
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
                    else break;

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
    //获取所有学生
    public static ArrayList<Student> listStudent()
    {
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findStudent = con.prepareStatement("select * from student");
            try (ResultSet usersFound = findStudent.executeQuery())
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
    //获取所有辅导员
    public static ArrayList<Instructor> listInstructor()
    {
        ArrayList<Instructor> instructors = new ArrayList<>();
        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findInstructor = con.prepareStatement("select * from instructor");
            try (ResultSet usersFound = findInstructor.executeQuery())
            {
                while (usersFound.next())
                {
                    String ID = usersFound.getString("ID");
                    String name = usersFound.getString("name");
                    String class_name = usersFound.getString("class_name");
                    String faculty_name = usersFound.getString("faculty_name");
                    instructors.add(new Instructor(ID, name, class_name, faculty_name));
                }
                con.close();
                return instructors;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }
    //获取所有院系管理员
    public static ArrayList<FacultyAdmin> listFacultyAdmin()
    {
        ArrayList<FacultyAdmin> facultyAdmins = new ArrayList<>();
        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findFacultyAdmin = con.prepareStatement("select * from faculty_administrator");
            try (ResultSet usersFound = findFacultyAdmin.executeQuery())
            {
                while (usersFound.next())
                {
                    String ID = usersFound.getString("ID");
                    String name = usersFound.getString("name");
                    String faculty_name = usersFound.getString("faculty_name");
                    facultyAdmins.add(new FacultyAdmin(ID, name, faculty_name));
                }
                con.close();
                return facultyAdmins;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    //获取本班所有不在校但具有进校权限的学生
    public static ArrayList<Student> outSchoolInAuthorityStudents(String className, String facultyName)
    {
        ArrayList<Student> students = new ArrayList<>();
        try
        {
            // 查找用户名
            Connection con = SQLUtil.getConnection();
            PreparedStatement findStudent = con.prepareStatement("select distinct ID, name, phone, email, " +
                    "personal_address, home_address, identity_type, id_num, in_school, class_name, faculty_name from " +
                    "(select * from student where in_school = '不在校' and class_name = ? and faculty_name = ? ) as o " +
                    "left join admission_authority aa on aa.student_ID = o.ID " +
                    "where aa.campus_name is not null");
            findStudent.setString(1, className);
            findStudent.setString(2, facultyName);
            try (ResultSet usersFound = findStudent.executeQuery())
            {
                while (usersFound.next())
                {
                    String ID = usersFound.getString("ID");
                    String name = usersFound.getString("name");
                    //以下部分变量可能为NULL
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
