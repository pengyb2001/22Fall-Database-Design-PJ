package service.authority;

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
}
