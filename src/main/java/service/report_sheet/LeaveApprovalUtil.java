package service.report_sheet;

import model.account.Student;
import model.report_sheet.LeaveApproval;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LeaveApprovalUtil {
    //查询是否存在未处理完的离校申请
    public  static boolean leaveApprovalExists(String id) {
        return getLeaveApproval(id, 4) != null;//4代表查找状态为0、1、2的离校申请
    }

    //根据学生ID和审批状态查找离校申请
    public static LeaveApproval getLeaveApproval(String id, Integer sta) {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findLeaveApprovalByIdandStatus;
            if(sta != 4){
                findLeaveApprovalByIdandStatus = con.prepareStatement(
                        "select * from leave_approval " +
                                "where student_ID=? " +
                                "and status=? " +
                                "order by form_num DESC;"
                );
                findLeaveApprovalByIdandStatus.setString(1, id);
                findLeaveApprovalByIdandStatus.setInt(2, sta);
            }
            else
            {
                findLeaveApprovalByIdandStatus = con.prepareStatement(
                        "select * from leave_approval " +
                                "where student_ID=? " +
                                "and status=0 or status=1 or status=2 " +
                                "order by form_num DESC;"
                );
                findLeaveApprovalByIdandStatus.setString(1, id);
            }
            try (ResultSet leaveApprovalFound = findLeaveApprovalByIdandStatus.executeQuery())
            {
                if(leaveApprovalFound.next())
                {
                    Integer form_num = leaveApprovalFound.getInt("form_num");
                    String student_ID = leaveApprovalFound.getString("student_ID");
                    Date timestamp = leaveApprovalFound.getDate("timestamp");
                    String reason = leaveApprovalFound.getString("reason");
                    String destination = leaveApprovalFound.getString("destination");
                    Date leave_date = leaveApprovalFound.getDate("leave_date");
                    Date entry_date = leaveApprovalFound.getDate("entry_date");
                    Integer status = leaveApprovalFound.getInt("status");
                    String refuse_reason = leaveApprovalFound.getString("refuse_reason");
                    con.close();
                    return new LeaveApproval(form_num, student_ID, timestamp, reason, destination, leave_date, entry_date,
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

    // 将string类型的时间转换成java util date 型
    public static Date dateChange(String date1)
    {
        SimpleDateFormat sdf =new  SimpleDateFormat("yyyy-MM-dd");
        java.util.Date cDate;
        Date dd=null;
        try {
            cDate = sdf.parse(date1);
            dd =new Date(cDate.getTime());

        } catch (ParseException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dd;

    }

    public static void addLeaveApproval(String student_ID, String reason, String destination, String leave_date,
                                        String entry_date)
    {
        try
        {
            //增加离校申请
            Connection con = SQLUtil.getConnection();
            PreparedStatement addNewLeaveApproval = con.prepareStatement("insert into " +
                    "leave_approval(student_ID, timestamp, reason, destination, leave_date, entry_date, status)" +
                    "values (?, now(), ?, ?, ?, ?, 0)");

            //把时间类型转换为sql Date
            java.sql.Date leaveDate = new java.sql.Date(dateChange(leave_date).getTime());
            java.sql.Date entryDate = new java.sql.Date(dateChange(entry_date).getTime());

            addNewLeaveApproval.setString(1, student_ID);
            addNewLeaveApproval.setString(2, reason);
            addNewLeaveApproval.setString(3, destination);
            addNewLeaveApproval.setDate(4, leaveDate);
            addNewLeaveApproval.setDate(5, entryDate);

            addNewLeaveApproval.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }

    //根据班级和院系和审批状态查找离校申请
    public static ArrayList<LeaveApproval> getLeaveApprovals(String class_name, String faculty_name, Integer sta) {
        ArrayList<LeaveApproval> leaveApprovals = new ArrayList<>();
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findLeaveApprovalByClassFacultyandStatus;
            if(sta != 4){
                findLeaveApprovalByClassFacultyandStatus = con.prepareStatement(
                        "select * from leave_approval, student " +
                                "where student_ID=ID " +
                                "and class_name = ? and faculty_name = ? and status = ?;"
                );
                findLeaveApprovalByClassFacultyandStatus.setString(1, class_name);
                findLeaveApprovalByClassFacultyandStatus.setString(2, faculty_name);
                findLeaveApprovalByClassFacultyandStatus.setInt(3, sta);
            }
            else
            {
                findLeaveApprovalByClassFacultyandStatus = con.prepareStatement(
                        "select * from leave_approval, student " +
                                "where student_ID=ID " +
                                "and class_name = ? and faculty_name = ? and status=0 or status=1 or status=2;"
                );
                findLeaveApprovalByClassFacultyandStatus.setString(1, class_name);
                findLeaveApprovalByClassFacultyandStatus.setString(2, faculty_name);
            }
            try (ResultSet leaveApprovalFound = findLeaveApprovalByClassFacultyandStatus.executeQuery())
            {
                while (leaveApprovalFound.next())
                {
                    Integer form_num = leaveApprovalFound.getInt("form_num");
                    String student_ID = leaveApprovalFound.getString("student_ID");
                    Date timestamp = leaveApprovalFound.getDate("timestamp");
                    String reason = leaveApprovalFound.getString("reason");
                    String destination = leaveApprovalFound.getString("destination");
                    Date leave_date = leaveApprovalFound.getDate("leave_date");
                    Date entry_date = leaveApprovalFound.getDate("entry_date");
                    Integer status = leaveApprovalFound.getInt("status");
                    String refuse_reason = leaveApprovalFound.getString("refuse_reason");
                    leaveApprovals.add(new LeaveApproval(form_num, student_ID, timestamp, reason, destination, leave_date, entry_date,
                            status, refuse_reason));
                }
                con.close();
                return leaveApprovals;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return null;
    }
    //变更离校申请
    public static void updateLeaveApproval(LeaveApproval leaveApproval)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement updateLeaveApprovalByFormNum = con.prepareStatement("update leave_approval set " +
                    "student_ID = ?,timestamp = ?,reason = ?,destination = ?,leave_date = ?,entry_date = ?," +
                    "status = ?,refuse_reason = ? where form_num = ?");
            updateLeaveApprovalByFormNum.setInt(9, leaveApproval.getForm_num());
            updateLeaveApprovalByFormNum.setString(1, leaveApproval.getStudent_ID());
            updateLeaveApprovalByFormNum.setDate(2, (java.sql.Date) leaveApproval.getTimestamp());
            updateLeaveApprovalByFormNum.setString(3, leaveApproval.getReason());
            updateLeaveApprovalByFormNum.setString(4, leaveApproval.getDestination());
            updateLeaveApprovalByFormNum.setDate(5, (java.sql.Date) leaveApproval.getLeave_date());
            updateLeaveApprovalByFormNum.setDate(6, (java.sql.Date) leaveApproval.getEntry_date());
            updateLeaveApprovalByFormNum.setInt(7, leaveApproval.getStatus());
            updateLeaveApprovalByFormNum.setString(8, leaveApproval.getRefuse_reason());
            updateLeaveApprovalByFormNum.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
}
