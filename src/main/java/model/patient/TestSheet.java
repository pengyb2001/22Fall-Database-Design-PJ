/**
 * 核酸检测单
 */

package model.patient;

public class TestSheet
{
    private String id;
    private String pID;
    private String date;
    private String result;
    private String illState;
    
    public TestSheet(String id, String pID, String date, String result, String illState)
    {
        this.id = id;
        this.pID = pID;
        this.date = date;
        this.result = result;
        this.illState = illState;
    }
    
    public String getId()
    {
        return id;
    }
    
    public String getpID()
    {
        return pID;
    }
    
    public String getDate()
    {
        return date;
    }
    
    public String getResult()
    {
        return result;
    }
    
    public String getIllState()
    {
        return illState;
    }
}
