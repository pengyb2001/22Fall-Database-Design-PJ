package service.report_sheet;

import model.report_sheet.EnterApproval;
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
}
