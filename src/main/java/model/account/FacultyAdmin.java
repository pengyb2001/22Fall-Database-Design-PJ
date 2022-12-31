package model.account;

public class FacultyAdmin {
    private String ID;
    private String name;
    private String faculty_name;

    public FacultyAdmin(String ID, String name, String faculty_name)
    {
        this.ID = ID;
        this.name = name;
        this.faculty_name = faculty_name;
    }

    public String getID()
    {
        return this.ID;
    }
    public String getName()
    {
        return this.name;
    }
    public String getFacultyName()
    {
        return this.faculty_name;
    }

    public static FacultyAdmin getInstance(String ID,String name,String faculty_name)
    {
        return new FacultyAdmin(ID, name, faculty_name);
    }
}
