/**
 * 对创建和修改用户的限制
 */

package restriction.user;

import model.user.ChiefNurse;
import model.user.Doctor;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRestriction
{
    public static boolean checkUserAreaRestriction(String type, String area)
    {
        if (type.equals(Doctor.TYPE) || type.equals(ChiefNurse.TYPE))
        {
            try
            {
                // 查找特定区域的医生
                Connection con = SQLUtil.getConnection();
                PreparedStatement findDoctorInArea = con.prepareStatement("select * from user where post=? " +
                        "and area=?");
                findDoctorInArea.setString(1, type);
                findDoctorInArea.setString(2, area);
                try (ResultSet userFound = findDoctorInArea.executeQuery())
                {
                    boolean found = userFound.next();
                    con.close();
                    return found;
                }
            }
            catch (Exception e)
            {
                SQLUtil.handleExceptions(e);
            }
        }
        
        return true;
    }
    
    // 检查病房护士是否有负责的患者
    public static boolean checkWardNurseHasResponsibility(String uID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findResponsiblityByUID = con.prepareStatement("select * from nurse_for_patient " +
                    "where u_ID=?");
            findResponsiblityByUID.setString(1, uID);
            try (ResultSet responsibilityFound = findResponsiblityByUID.executeQuery())
            {
                boolean found = responsibilityFound.next();
                con.close();
                return found;
            }
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        return false;
    }
}
