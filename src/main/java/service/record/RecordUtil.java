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
}
