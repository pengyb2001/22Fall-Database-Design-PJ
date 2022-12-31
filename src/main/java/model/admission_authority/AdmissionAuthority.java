package model.admission_authority;

public class AdmissionAuthority {
    private String student_ID;
    private String campus_name;

    public AdmissionAuthority(String student_ID, String campus_name)
    {
        this.student_ID = student_ID;
        this.campus_name = campus_name;
    }

    public String getStudent_ID(){ return student_ID;}

    public String getCampus_name(){ return campus_name;}
}
