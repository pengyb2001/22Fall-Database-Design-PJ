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

    public static void deleteAuthorityByID(String ID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement deleteAuthorityByID = con.prepareStatement("delete from admission_authority where student_ID=?");
            deleteAuthorityByID.setString(1, ID);
            deleteAuthorityByID.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }

    public static void addAuthorityByID(String ID)
    {
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement addAuthorityByID = con.prepareStatement("insert into admission_authority(student_ID, campus_name) " +
                    "values " +
                    "(?, 'H校区'), (?, 'J校区'), (?,'F校区'), (?, 'Z校区');");
            addAuthorityByID.setString(1, ID);
            addAuthorityByID.setString(2, ID);
            addAuthorityByID.setString(3, ID);
            addAuthorityByID.setString(4, ID);
            addAuthorityByID.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
}
