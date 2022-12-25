/**
 * 实现病床信息管理功能
 */

package service.patient;

import model.bed.Bed;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BedUtil
{
    // 查看某一区域的病床
    public static ArrayList<Bed> getBedsByArea(String area)
    {
        ArrayList<Bed> beds = new ArrayList<>();
        
        try
        {
            Connection con = SQLUtil.getConnection();
            PreparedStatement findBedsByArea = con.prepareStatement("select * from bed " +
                    "natural join room_in_bed natural join room where area=?");
            findBedsByArea.setString(1, area);
            try (ResultSet bedsFound = findBedsByArea.executeQuery())
            {
                while (bedsFound.next())
                {
                    String id = bedsFound.getString("b_id");
                    String rID = bedsFound.getString("r_id");
                    Bed bed = new Bed(id, rID, area);
                    beds.add(bed);
                }
            }
        
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
        
        return beds;
    }
}
