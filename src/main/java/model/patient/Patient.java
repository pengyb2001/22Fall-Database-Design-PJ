/**
 * 患者
 */

package model.patient;

public class Patient
{
    public static String LIFE_RECOVERED = "康复出院";
    public static String LIFE_ILL = "在院治疗";
    public static String LIFE_DEAD = "病亡";
    public static String ILL_MODERATE = "轻症";
    public static String ILL_SERIOUS = "重症";
    public static String ILL_SEVERE = "危重症";
    
    private String id;
    private String name;
    private String residentID;
    private String address;
    private String lifeState;
    private String illState;
    private boolean canLeave;
    private String bID; // 床位号
    private String rID; // 房间号
    private String area; // 所在区域
    private String uID; // 护士号
    private String uName; // 护士名
    
    public Patient(String id, String name, String residentID, String address,
                   String lifeState, String illState, boolean canLeave,
                   String bID, String rID, String area,
                   String uID, String uName)
    {
        this.id = id;
        this.name = name;
        this.residentID = residentID;
        this.address = address;
        this.lifeState = lifeState;
        this.illState = illState;
        this.canLeave = canLeave;
        this.bID = bID;
        this.rID = rID;
        this.area = area;
        this.uID = uID;
        this.uName = uName;
    }
    
    public String getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getResidentID()
    {
        return residentID;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public String getLifeState()
    {
        return lifeState;
    }
    
    public String getIllState()
    {
        return illState;
    }
    
    public boolean isCanLeave()
    {
        return canLeave;
    }
    
    public String getbID()
    {
        return bID;
    }
    
    public String getrID()
    {
        return rID;
    }
    
    public String getArea()
    {
        return area;
    }
    
    public String getuID()
    {
        return uID;
    }
    
    public String getuName()
    {
        return uName;
    }
    
    public boolean shouldTransfer()
    {
        //待转移的标准是：在院治疗，在隔离区（所在区域为null），或所在区域与病情评级不符合
        return ((lifeState.equals(LIFE_ILL)) &&
                ((area == null) || !((illState.equals(ILL_MODERATE) && area.equals("轻症区域")) ||
                (illState.equals(ILL_SERIOUS) && area.equals("重症区域")) ||
                (illState.equals(ILL_SEVERE) && area.equals("危重症区域")))));
    }
    
    public static boolean isValidLifeState(String lifeState)
    {
        return (lifeState.equals(LIFE_RECOVERED) || lifeState.equals(LIFE_ILL) || lifeState.equals(LIFE_DEAD));
    }
    
    public static boolean isValidIllState(String illState)
    {
        return (illState.equals(ILL_MODERATE) || illState.equals(ILL_SERIOUS) || illState.equals(ILL_SEVERE));
    }
    
    public static boolean checkCanLeave(String lifeState, String illState, String temperature,
                                        String result1, String result2)
    {
        // 可以出院的标准是：在院治疗，病情评级为轻症，连续3天体温低于37.3摄氏度，连续两次核酸检测结果为阴性
        if (temperature == null || result1 == null || result2==null)
        {
            return false;
        }
        else
        {
            final double UNSAFE_TEMPERATURE = 37.3;
            final String NEGATIVE = "阴性";
            return (lifeState.equals(LIFE_ILL) &&
                    illState.equals(ILL_MODERATE) && (Double.parseDouble(temperature) < UNSAFE_TEMPERATURE)
                    && result1.equals(NEGATIVE) && result2.equals(NEGATIVE));
        }
    }
}
