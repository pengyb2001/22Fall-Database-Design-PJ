package service.report_sheet;

import model.report_sheet.DailyReport;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class DailyReportUtil {
    public static boolean dailyReportExists(String id)
    {
        return  getDailyReport(id) != null;
    }

    public static DailyReport getDailyReport(String id)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findDailyReportById = con.prepareStatement(
                    "select * from daily_report " +
                            "where student_ID=? " +
                            "and to_days(timestamp) = to_days(now());"
            );
            findDailyReportById.setString(1, id);
            try (ResultSet dailyReportFound = findDailyReportById.executeQuery())
            {
                if(dailyReportFound.next())
                {
                    Integer report_num = dailyReportFound.getInt("report_num");
                    String student_ID = dailyReportFound.getString("student_ID");
                    Date timestamp = dailyReportFound.getDate("timestamp");
                    String location = dailyReportFound.getString("location");
                    Integer is_healthy = dailyReportFound.getInt("is_healthy");
                    con.close();
                    return new DailyReport(report_num, student_ID, timestamp, location, is_healthy);
                }
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    public static void addDailyReport(String student_ID, String location, Integer is_healthy)
    {
        try
        {
            //增加健康日报记录
            Connection con = SQLUtil.getConnection();
            PreparedStatement addNewDailyReport = con.prepareStatement("insert into daily_report(student_ID, timestamp, location, is_healthy)" +
                    "values (?, now(), ?, ?)" );
            addNewDailyReport.setString(1, student_ID);
//            addNewDailyReport.setDate(2, (java.sql.Date) timestamp);
            addNewDailyReport.setString(2, location);
            addNewDailyReport.setInt(3, is_healthy);
            addNewDailyReport.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }

}
