/**
 * 每日状态
 */

package model.patient;

public class DailyState
{
    private String id;
    private String pID;
    private String date;
    private String temperature;
    private String symptom;
    private String result;
    private String lifeState;
    
    public DailyState(String id, String pID, String date, String temperature,
                      String symptom, String result, String lifeState)
    {
        this.id = id;
        this.pID = pID;
        this.date = date;
        this.temperature = temperature;
        this.symptom = symptom;
        this.result = result;
        this.lifeState = lifeState;
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
    
    public String getTemperature()
    {
        return temperature;
    }
    
    public String getSymptom()
    {
        return symptom;
    }
    
    public String getResult()
    {
        return result;
    }
    
    public String getLifeState()
    {
        return lifeState;
    }
}
