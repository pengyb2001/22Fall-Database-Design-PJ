/**
 * 用户信息
 */

package model.user;

public abstract class User
{
    private String id;
    private String username;
    private String password;
    private String name;
    private String area;
    
    public User(String id, String username, String password, String name, String area)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.area = area;
    }
    
    public String getId()
    {
        return this.id;
    }
    
    public String getUsername()
    {
        return this.username;
    }
    
    public String getPassword()
    {
        return this.password;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getArea()
    {
        return this.area;
    }
    
    public abstract String getType();
    
    public abstract void routine(); // 用户对应的权限操作
    
    public static boolean isValidType(String type)
    {
        return (type.equals(Doctor.TYPE) || type.equals(ChiefNurse.TYPE) ||
                type.equals(EmergencyNurse.TYPE) || type.equals(WardNurse.TYPE));
    }
    
    public static User getInstance(String id, String username, String password, String name, String area, String type)
    {
        if (type.equals(Doctor.TYPE))
        {
            return new Doctor(id, username, password, name, area);
        }
        else if (type.equals(ChiefNurse.TYPE))
        {
            return new ChiefNurse(id, username, password, name, area);
        }
        else if (type.equals(EmergencyNurse.TYPE))
        {
            return new EmergencyNurse(id, username, password, name, area);
        }
        else if (type.equals(WardNurse.TYPE))
        {
            return new WardNurse(id, username, password, name, area);
        }
        else
        {
            return null;
        }
    }
}
