/**
 * 病床
 */

package model.bed;

public class Bed
{
    private String id;
    private String rID; // 房间号
    private String area; // 所属区域
    
    public Bed(String id, String rID, String area)
    {
        this.id = id;
        this.rID = rID;
        this.area = area;
    }
    
    public String getId()
    {
        return id;
    }
    
    public String getrID()
    {
        return rID;
    }
    
    public String getArea()
    {
        return area;
    }
}
