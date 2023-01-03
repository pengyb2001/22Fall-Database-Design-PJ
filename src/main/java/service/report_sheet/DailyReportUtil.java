package service.report_sheet;

import model.account.ContinuousStudent;
import model.report_sheet.DailyReport;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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

    public static int getTodayCountByClass(String classname, String facultyname)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement getTodayCountByClass = con.prepareStatement("select count(*) " +
                    "from student join daily_report on student.ID=daily_report.student_ID " +
                    "where class_name=? and faculty_name=? and to_days(timestamp) = to_days(now())");
            getTodayCountByClass.setString(1, classname);
            getTodayCountByClass.setString(2, facultyname);
            ResultSet count = getTodayCountByClass.executeQuery();
            int num = 0;
            while (count.next()){
                num = count.getInt(1);
            }
            return num;
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return 0;
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

    public static ArrayList<DailyReport> getMyDailyReport(String id, Integer n)
    {
        ArrayList<DailyReport> dailyReports = new ArrayList<>();
        try
        {
            //System.out.println("Search");
            Connection con = SQLUtil.getConnection();
            PreparedStatement findDailyReportById = con.prepareStatement(
                    "select * from daily_report " +
                            "where student_ID=? " +
                            "and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp);"
            );
            findDailyReportById.setString(1, id);
            findDailyReportById.setInt(2, n-1);
            try (ResultSet dailyReportFound = findDailyReportById.executeQuery())
            {
                while (dailyReportFound.next())
                {
                    Integer report_num = dailyReportFound.getInt("report_num");
                    String student_ID = dailyReportFound.getString("student_ID");
                    Date timestamp = dailyReportFound.getDate("timestamp");
                    String location = dailyReportFound.getString("location");
                    Integer is_healthy = dailyReportFound.getInt("is_healthy");
                    dailyReports.add(new DailyReport(report_num, student_ID, timestamp, location, is_healthy));
                }
                con.close();
                return dailyReports;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }

    //查询全校连续 n 天填写“健康日报”时间（精确到分钟）完全一致的学生
    public static ArrayList<ContinuousStudent> getContinunous(int n)
    {
        ArrayList<ContinuousStudent> students = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findStudent = con.prepareStatement(
                    "with temp1 as\n" +
                            "	(select student_ID, timestamp, date_format(date_sub(timestamp, INTERVAL rn*24*60 MINUTE), '%Y-%m-%d %H:%i') as dis,row_number() over (order by student_ID )rowNum\n" +
                            "	from \n" +
                            "		(\n" +
                            "		select \n" +
                            "		student_ID, timestamp, row_number() over (partition by student_ID order by timestamp asc)rn\n" +
                            "		from daily_report\n" +
                            "		)t1\n" +
                            "	)\n" +
                            "	select distinct student_ID, day_count,date_add(dis, INTERVAL 1 DAY) as start_day from(\n" +
                            "		select student_ID, count(*)day_count, dis from(	\n" +
                            "			select student_ID, dis, row_number()over(partition by dis order by rowNum) as orde1, rowNum, rowNum-row_number()over(partition by dis order by rowNum) as orde2\n" +
                            "			from temp1\n" +
                            "			ORDER BY rowNum\n" +
                            "			)as w\n" +
                            "		group by dis,orde2,student_ID\n" +
                            "		)as s where day_count = ?"
            );
            findStudent.setInt(1, n);
            try(ResultSet usersFound = findStudent.executeQuery())
            {
                while (usersFound.next())
                {
                    String student_ID = usersFound.getString("student_ID");
                    int day_count = usersFound.getInt("day_count");
                    Date start_day = new java.util.Date(usersFound.getTimestamp("start_day").getTime());
                    students.add(new ContinuousStudent(student_ID, day_count, start_day));
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
