package service.record;

import model.record.PassRecord;

public class RecordUtil {

    public static void addNewPassRecord(String student_ID, String campus_name, Integer type)
    {
        PassRecord.addRecord(student_ID,campus_name,type);
    }
}
