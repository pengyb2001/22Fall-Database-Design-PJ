package service.record;

import model.account.Instructor;
import model.account.Student;
import model.record.AvgOutTime;
import model.record.PassRecord;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecordUtil {

    public static void addNewPassRecord(String student_ID, String campus_name, Integer type)
    {
        PassRecord.addRecord(student_ID,campus_name,type);
    }

    //查询学生最近一次出校记录
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

    //计算学生过去一年离校总时长
    public static String getOutSchoolTime(String studentID, String inSchool)
    {
        ArrayList<PassRecord> passRecords = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findPassRecordsByID;
            findPassRecordsByID = con.prepareStatement("select * from pass_record where " +
                    "student_ID=? and DATE_SUB(CURDATE(), INTERVAL 1 YEAR) <= date(timestamp) " +
                    "order by pass_num desc ");
            findPassRecordsByID.setString(1, studentID);
            try(ResultSet passRecordFound = findPassRecordsByID.executeQuery())
            {
                while (passRecordFound.next())
                {
                    Integer pass_num = passRecordFound.getInt("pass_num");
                    String student_ID = passRecordFound.getString("student_ID");
                    Date timestamp = passRecordFound.getTimestamp("timestamp");
                    String campus_name = passRecordFound.getString("campus_name");
                    Integer type = passRecordFound.getInt("type");

                    passRecords.add(new PassRecord(pass_num, student_ID, timestamp, campus_name, type));
                }
            }
            con.close();
//            java.text.Format formatter=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//            java.util.Date todayDate=new java.util.Date();
//
//            long beforeTime=(todayDate.getTime()/1000)-60*60*24*365;
//
//            Date yearbefore = todayDate;
//            yearbefore.setTime(beforeTime*1000);
//
//            String beforeDate=formatter.format(yearbefore);

            if (passRecords.isEmpty()) {
                if (inSchool.equals("不在校"))
                {
                    return "过去1年均不在校";
                }else
                {
                    return "过去1年均在校";
                }
            }else
            {
                if (inSchool.equals("不在校"))
                {
                    Date former_day = new Date();
                    long sum_time = 0;
                    long plus_time = 0;
                    while (!passRecords.isEmpty())
                    {
                        plus_time = former_day.getTime() - passRecords.get(0).getTimestamp().getTime();
//                        System.out.printf("formerDay:%d\t%s\npassRecord:%d\t%s\n",
//                                former_day.getTime(),secondToDate(former_day.getTime(),"yyyy-MM-dd HH:mm:ss"),
//                                passRecords.get(0).getTimestamp().getTime(),
//                                secondToDate(passRecords.get(0).getTimestamp().getTime(),"yyyy-MM-dd HH:mm:ss"));
//                        System.out.printf("plusTime:%d\n",plus_time);
                        sum_time += plus_time;
                        //break;
                        passRecords.remove(0);
                        if (passRecords.isEmpty())
                        {
                            break;
                        }else
                        {
                            former_day = passRecords.get(0).getTimestamp();
                            passRecords.remove(0);
//                            if (passRecords.isEmpty())
//                            {
//                                plus_time = former_day.getTime() - yearbefore.getTime();
//                                sum_time += plus_time;
//                            }
                        }
                    }
                    return secondToTime(sum_time);
                }else
                {
                    Date former_day = passRecords.get(0).getTimestamp();
                    long sum_time = 0;
                    long plus_time = 0;
                    passRecords.remove(0);
//                    if (passRecords.isEmpty())
//                    {
//                        sum_time = former_day.getTime() - yearbefore.getTime();
//                    }
                    while (!passRecords.isEmpty())
                    {
                        plus_time = former_day.getTime() - passRecords.get(0).getTimestamp().getTime();
                        sum_time += plus_time;
                        passRecords.remove(0);
                        if (passRecords.isEmpty())
                        {
                            break;
                        }else
                        {
                            former_day = passRecords.get(0).getTimestamp();
                            passRecords.remove(0);
//                            if (passRecords.isEmpty())
//                            {
//                                plus_time = former_day.getTime() - yearbefore.getTime();
//                                sum_time += plus_time;
//                            }
                        }
                    }
                    return secondToTime(sum_time);
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    //计算学生离校次数
    public static int getLeaveTimes(String id)
    {
        int times = 1;
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findOutByID;
            findOutByID = con.prepareStatement("select count(*) as cnt from pass_record where student_ID=? and type='0'");
            findOutByID.setString(1, id);
            ResultSet outTimesFound = findOutByID.executeQuery();
            if(outTimesFound.next()) times = outTimesFound.getInt("cnt");
            con.close();
            return times;
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return 1;
    }

    //计算学生平均离校时长
    public static AvgOutTime getOutSchoolTimeAvg(String studentID, String inSchool)
    {
        int outTimes = getLeaveTimes(studentID);

        ArrayList<PassRecord> passRecords = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findPassRecordsByID;
            findPassRecordsByID = con.prepareStatement("select * from pass_record where " +
                    "student_ID=? " +
                    "order by pass_num desc ");
            findPassRecordsByID.setString(1, studentID);
            try(ResultSet passRecordFound = findPassRecordsByID.executeQuery())
            {
                while (passRecordFound.next())
                {
                    Integer pass_num = passRecordFound.getInt("pass_num");
                    String student_ID = passRecordFound.getString("student_ID");
                    Date timestamp = passRecordFound.getTimestamp("timestamp");
                    String campus_name = passRecordFound.getString("campus_name");
                    Integer type = passRecordFound.getInt("type");

                    passRecords.add(new PassRecord(pass_num, student_ID, timestamp, campus_name, type));
                }
            }
            con.close();
            int leaveTimes = RecordUtil.getLeaveTimes(studentID);
            if (passRecords.isEmpty()) {
                    AvgOutTime a = new AvgOutTime(studentID, (long)0);
                    return (a);
            }else
            {
                if (inSchool.equals("不在校"))
                {
                    Date former_day = new Date();
                    long sum_time = 0;
                    long plus_time = 0;
                    while (!passRecords.isEmpty())
                    {
                        plus_time = former_day.getTime() - passRecords.get(0).getTimestamp().getTime();
                        sum_time += plus_time;
                        //break;
                        passRecords.remove(0);
                        if (passRecords.isEmpty())
                        {
                            break;
                        }else
                        {
                            former_day = passRecords.get(0).getTimestamp();
                            passRecords.remove(0);
                        }
                    }
                    AvgOutTime avgOutTime = new AvgOutTime(studentID, sum_time/outTimes);
                    return avgOutTime;
                }else
                {
                    Date former_day = passRecords.get(0).getTimestamp();
                    long sum_time = 0;
                    long plus_time = 0;
                    passRecords.remove(0);
                    while (!passRecords.isEmpty())
                    {
                        plus_time = former_day.getTime() - passRecords.get(0).getTimestamp().getTime();
                        sum_time += plus_time;
                        passRecords.remove(0);
                        if (passRecords.isEmpty())
                        {
                            break;
                        }else
                        {
                            former_day = passRecords.get(0).getTimestamp();
                            passRecords.remove(0);
                        }
                    }
                    AvgOutTime avgOutTime = new AvgOutTime(studentID, sum_time/outTimes);
                    return avgOutTime;
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    public static String secondToTime(long millisecond)
    {
        long second = millisecond/1000;
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数

        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数

        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数

        if (0 < days){
            return days + "天，"+hours+"小时，"+minutes+"分，"+second+"秒";
        }else {
            return hours+"小时，"+minutes+"分，"+second+"秒";
        }

    }
    public static String secondToDate(long millisecond,String patten) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);//转换为毫秒
        Date date = calendar.getTime();
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(patten);
        return format.format(date);
    }

}
