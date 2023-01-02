package service.record;

import model.account.Instructor;
import model.record.PassRecord;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
}
