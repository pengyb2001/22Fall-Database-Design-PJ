package model.account;

import model.record.AvgOutTime;
import model.record.PassRecord;
import service.account.AccountUtil;
import service.authority.AuthorityUtil;
import service.record.RecordUtil;
import service.report_sheet.DailyReportUtil;
import service.report_sheet.EnterApprovalUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Root {
    public static final String USERNAME = "root";
    public static final String PASSWORD = "123123";

    public Root() {

    }

    public String getName()
    {
        return USERNAME;
    }

    public void routine()
    {
        // 进入账户操作界面
        while (true)
        {
            System.out.println("##【root账户权限操作】==========");
            System.out.println("##【root账户权限操作】root账户可以进行以下操作：");
            System.out.println("##【root账户权限操作】指令“list”：列出所有用户（不包括root）");
            System.out.println("##【root账户权限操作】指令“getStudentsByCampusName”：根据校区查询所有具有该权限的学生数量和名单");
            System.out.println("##【root账户权限操作】指令“manageAdmission”：按校区更改所有学生进入的权限(在该校区除外)");
            System.out.println("##【root账户权限操作】指令“getOutSchoolStudents”：查询全校已出校但尚未返回校园（即离校状态）的学生数量、个人信息及各自的离校时间(学生状态不在校但具有进校权限)");
            System.out.println("##【root账户权限操作】指令“getInSchoolLeaveStudents”：查询全校已提交出校申请但未离校的学生数量、个人信息；");
            System.out.println("##【root账户权限操作】指令“getCampusMaxVisit”：过去n天各院系学生产生最多出入校记录的校区");
            System.out.println("##【root账户权限操作】指令“getInSchoolStudent”：查询过去n天一直在校未曾出校的学生 支持按多级范围（全校、院系、班级）");
            System.out.println("##【root账户权限操作】指令“getMaxEnterApproval”：查询前n个提交入校申请最多的学生 支持按多级范围（全校、院系、班级）进行筛选");
            System.out.println("##【root账户权限操作】指令“getContinuous”：查询全校连续 n 天填写“健康日报”时间（精确到分钟）完全一致的学生数量，个人信息");
            System.out.println("##【root账户权限操作】指令“getAvgLeave”：查询前 n 个平均离校时间最长的学生 支持按多级范围（全校、院系、班级）");
            System.out.println("##【root账户权限操作】指令“logout”：注销");
            System.out.println("##【root账户权限操作】指令“exit”：退出系统");
            System.out.println("##【root账户权限操作】==========");
            System.out.print(">");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.equals("list"))
            {
                list();
            }
            else if (command.equals("getStudentsByCampusName"))
            {
                getStudentsByCampusName();
            }
            else if (command.equals("manageAdmission"))
            {
                manageAdmission();
            }
            else if (command.equals("getOutSchoolStudents"))
            {
                getOutSchoolStudents();
            }
            else if (command.equals("getInSchoolLeaveStudents"))
            {
                getInSchoolLeaveStudents();
            }
            else if (command.equals("getCampusMaxVisit"))
            {
                getCampusMaxVisit();
            }
            else if (command.equals("getInSchoolStudent"))
            {
                getInSchoolStudent();
            }
            else if (command.equals("getMaxEnterApproval"))
            {
                getMaxEnterApproval();
            }
            else if (command.equals("getContinuous"))
            {
                getContinuous();
            }
            else if (command.equals("getAvgLeave"))
            {
                getAvgLeave();
            }
            else if (command.equals("logout"))
            {
                break;
            }
            else if (command.equals("exit"))
            {
                System.exit(0);
            }
            else
            {
                System.out.println("##【root账户权限操作】无此指令，请重新输入。");
            }
        }
    }

    public void list()
    {
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Instructor> instructors = new ArrayList<>();
        ArrayList<FacultyAdmin> facultyAdmins = new ArrayList<>();
        students = AccountUtil.listStudent();
        instructors = AccountUtil.listInstructor();
        facultyAdmins = AccountUtil.listFacultyAdmin();
        System.out.printf("##学生有%d个，名单如下：\n",students.size());
        for (Student student: students)
        {
            System.out.println("##----------");
            System.out.printf("学号：%s\n姓名：%s\n班级：%s\n院系：%s\n",
                    student.getID(), student.getName(), student.getClassName(), student.getFacultyName());
        }
        System.out.println("##----------");
        System.out.printf("##辅导员有%d个，名单如下：\n",instructors.size());
        for (Instructor instructor: instructors)
        {
            System.out.println("##----------");
            System.out.printf("工号：%s\n姓名：%s\n班级：%s\n院系：%s\n",
                    instructor.getID(), instructor.getName(), instructor.getClassName(), instructor.getFacultyName());
        }
        System.out.println("##----------");
        System.out.printf("##院系管理员有%d个，名单如下：\n",facultyAdmins.size());
        for (FacultyAdmin facultyAdmin: facultyAdmins)
        {
            System.out.println("##----------");
            System.out.printf("工号：%s\n姓名：%s\n院系：%s\n",
                    facultyAdmin.getID(), facultyAdmin.getName(), facultyAdmin.getFacultyName());
        }
        System.out.println("##----------");

    }

    public void getStudentsByCampusName()
    {
        ArrayList<Student> students = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean invalid = true;
        do{
            System.out.println("##请问查询哪个校区？(H校区/J校区/F校区/Z校区)");
            System.out.print(">");
            input = scanner.nextLine();
            switch (input)
            {
                case "H校区":
                case "J校区":
                case "F校区":
                case "Z校区":
                    invalid = false;
                    students = AuthorityUtil.getStudentsByCampusName(input);
                    break;
                default:
                    System.out.println("##请输入正确的校区名！");
            }
        }while(invalid);
        System.out.printf("##具有%s入校权限的学生有%d个，名单如下：\n",input,students.size());
        for (Student student: students)
        {
            System.out.println("##----------");
            System.out.printf("学号：%s\n姓名：%s\n班级：%s\n院系：%s\n",
                    student.getID(), student.getName(), student.getClassName(), student.getFacultyName());
        }
        System.out.println("##----------");
    }

    public void manageAdmission()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean invalid = true;
        do{
            System.out.println("##请问管控哪个校区？(H校区/J校区/F校区/Z校区)");
            System.out.print(">");
            input = scanner.nextLine();
            switch (input)
            {
                case "H校区":
                case "J校区":
                case "F校区":
                case "Z校区":
                    invalid = false;
                    AuthorityUtil.deleteAuthorityByCampusName(input);
                    System.out.printf("##已完成%s的管控\n",input);
                    break;
                default:
                    System.out.println("##请输入正确的校区名！");
            }
        }while(invalid);
    }

    //getOutSchoolStudents查询全校已出校但尚未返回校园（即离校状态）的学生数量、个人信息及各自的离校时间(学生状态不在校但具有进校权限)
    public void getOutSchoolStudents()
    {
        ArrayList<Student> students = new ArrayList<>();
        students = AccountUtil.outSchoolInAuthorityStudents();
        if (students.isEmpty())
        {
            System.out.println("没有具有进校权限但不在校的学生！");
            return;
        }
        System.out.printf("##共%d名学生，学生名单如下：\n",students.size());
        for (Student student: students)
        {
            PassRecord passRecord = RecordUtil.getNearestOutPassRecordByID(student.getID());
            System.out.println("##----------");
            if (passRecord == null)
            {
                System.out.printf("学号：%s\n姓名：%s\n离校时间：无，可能是尚未报道的新生\n班级：%s\n院系：%s\n",
                        student.getID(), student.getName(), student.getClassName(), student.getFacultyName());
            }
            else
            {
                System.out.printf("学号：%s\n姓名：%s\n离校时间：%s\n班级：%s\n院系：%s\n",
                        student.getID(), student.getName(), passRecord.getTimestamp().toString(), student.getClassName(), student.getFacultyName());
            }
        }
        System.out.println("##----------");
    }

    public void getInSchoolLeaveStudents()
    {
        ArrayList<Student> students = new ArrayList<>();
        students = AccountUtil.getInSchoolLeaveStudents();
        if (students.isEmpty())
        {
            System.out.println("全校没有已提交出校申请但未离校的学生！");
            return;
        }
        System.out.printf("##共%d名学生，学生名单如下：\n",students.size());
        for (Student student: students)
        {
            System.out.println("##----------");
            System.out.printf("学号：%s\n姓名：%s\n电话：%s\n班级：%s\n院系：%s\n",
                    student.getID(), student.getName(), student.getPhone(), student.getClassName(), student.getFacultyName());
        }
        System.out.println("##----------");
    }

    //过去n天每个院系学生产生最多出入校记录的校区(格式为 [院系名]：[校区名])
    public void getCampusMaxVisit()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        int n = 0;
        do{
            System.out.println("##请输入查询限定范围为过去几天");
            System.out.print(">");
            input = scanner.nextLine();
            if(Student.isNumeric(input))
            {
                n = Integer.parseInt(input);
            }
            else
            {
                System.out.println("##请输入数字！");
            }
        }while(n == 0);
        ArrayList<String> allFaculties = new ArrayList<>();
        allFaculties = AccountUtil.getAllFaculty();
        for(String faculty: allFaculties)
        {
            System.out.printf("%s学生最近%d天出入校记录最多的校区为：%s\n",
                    faculty, n, RecordUtil.getCampusMaxVisitByFaculty(faculty, n));

        }
    }

    //查询过去n天一直在校未曾出校的学生支持按多级范围（全校、院系、班级）
    public void getInSchoolStudent()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        int n = 0;
        do{
            System.out.println("##请输入查询限定范围为过去几天");
            System.out.print(">");
            input = scanner.nextLine();
            if(Student.isNumeric(input))
            {
                n = Integer.parseInt(input);
            }
            else
            {
                System.out.println("##请输入数字！");
            }
        }while(n == 0);

        ArrayList<Student> students = new ArrayList<>();
        String faculty;
        String classname;
        boolean invalid = true;
        do
        {
            System.out.println("##全校查询请输1，按院系查询请输2，按班级查询请输3");
            System.out.print(">");
            scanner = new Scanner(System.in);
            input = scanner.nextLine();
            switch (input){
                case "1":
                    students = RecordUtil.getInSchoolStudent(n);
                    invalid = false;
                    break;
                case "2":
                    do{
                        System.out.println("##请输入查询院系的名称");
                        System.out.print(">");
                        faculty = scanner.nextLine();
                        if(AccountUtil.facultyExists(faculty))
                        {
                            students = RecordUtil.getInSchoolStudent(faculty, n);
                        }
                        else
                        {
                            System.out.println("##请输入正确的院系名称！");
                        }
                    }while(!AccountUtil.facultyExists(faculty));
                    invalid = false;
                    break;
                case "3":
                    do{
                        System.out.println("##请输入班级所在院系的名称");
                        System.out.print(">");
                        faculty = scanner.nextLine();
                        if(!AccountUtil.facultyExists(faculty))
                        {
                            System.out.println("##请输入正确的院系名称！");
                        }
                    }while(!AccountUtil.facultyExists(faculty));
                    do{
                        System.out.println("##请输入班级的名称");
                        System.out.print(">");
                        classname = scanner.nextLine();
                        if(!AccountUtil.instructorExists(classname, faculty))
                        {
                            System.out.println("##请输入正确的班级名称！");
                        }
                    }while(!AccountUtil.instructorExists(classname, faculty));
                    students = RecordUtil.getInSchoolStudent(classname, faculty, n);
                    invalid = false;
                    break;
                default:
                    System.out.println("##输入的数字错误，请重试");
            }
        }while (invalid);
        System.out.printf("##过去%d天一直在校未曾出校的学生如下\n",n);
        if (students.isEmpty()){
            System.out.println("##无记录！");
            return;
        }
        for(Student student:students)
        {
            System.out.println("##----------");
            System.out.printf("学号：%s\n姓名：%s\n电话：%s\n所在校区：%s\n班级：%s\n院系：%s\n",
                    student.getID(), student.getName(), student.getPhone(), student.getInSchool(), student.getClassName(), student.getFacultyName());
        }
        System.out.println("##----------");
    }

    //查询前n个提交入校申请最多的学生，支持按多级范围（全校、院系、班级）进行筛选
    public void getMaxEnterApproval()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        int n = 0;
        do{
            System.out.println("##请输入查询前几位入校申请最多的学生");
            System.out.print(">");
            input = scanner.nextLine();
            if(Student.isNumeric(input))
            {
                n = Integer.parseInt(input);
            }
            else
            {
                System.out.println("##请输入数字！");
            }
        }while(n == 0);
        ArrayList<Student> students = new ArrayList<>();
        String faculty;
        String classname;
        boolean invalid = true;
        do
        {
            System.out.println("##全校查询请输1，按院系查询请输2，按班级查询请输3");
            System.out.print(">");
            scanner = new Scanner(System.in);
            input = scanner.nextLine();
            switch (input){
                case "1":
                    students = EnterApprovalUtil.getMaxEnterApproval();
                    invalid = false;
                    break;
                case "2":
                    do{
                        System.out.println("##请输入查询院系的名称");
                        System.out.print(">");
                        faculty = scanner.nextLine();
                        if(AccountUtil.facultyExists(faculty))
                        {
                            students = EnterApprovalUtil.getMaxEnterApproval(faculty);
                        }
                        else
                        {
                            System.out.println("##请输入正确的院系名称！");
                        }
                    }while(!AccountUtil.facultyExists(faculty));
                    invalid = false;
                    break;
                case "3":
                    do{
                        System.out.println("##请输入班级所在院系的名称");
                        System.out.print(">");
                        faculty = scanner.nextLine();
                        if(!AccountUtil.facultyExists(faculty))
                        {
                            System.out.println("##请输入正确的院系名称！");
                        }
                    }while(!AccountUtil.facultyExists(faculty));
                    do{
                        System.out.println("##请输入班级的名称");
                        System.out.print(">");
                        classname = scanner.nextLine();
                        if(!AccountUtil.instructorExists(classname, faculty))
                        {
                            System.out.println("##请输入正确的班级名称！");
                        }
                    }while(!AccountUtil.instructorExists(classname, faculty));
                    students = EnterApprovalUtil.getMaxEnterApproval(classname, faculty);
                    invalid = false;
                    break;
                default:
                    System.out.println("##输入的数字错误，请重试");
            }
        }while (invalid);
        System.out.printf("##提交入校申请最多的前%d位的学生如下\n",n);
        if (students.isEmpty()){
            System.out.println("##无记录！");
            return;
        }
        int j = 0;
        int max = 0;
        for(Student student:students)
        {
            j++;
            int count = EnterApprovalUtil.getCount(student.getID());
            if((j <= n) || (count == max))
            {
                System.out.println("##----------");
                System.out.printf("学号：%s\n姓名：%s\n班级：%s\n院系：%s\n提交入校申请次数：%d\n",
                        student.getID(), student.getName(), student.getClassName(), student.getFacultyName(),
                        count);
                max = count;
            }
            else break;

        }
        System.out.println("##----------");
    }

    //查询全校连续 n 天填写“健康日报”时间（精确到分钟）完全一致的学生数量，个人信息
    public void getContinuous()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        int n = 0;
        do{
            System.out.println("##请输入要查询连续几天的");
            System.out.print(">");
            input = scanner.nextLine();
            if(Student.isNumeric(input))
            {
                n = Integer.parseInt(input);
            }
            else
            {
                System.out.println("##请输入数字！");
            }
        }while(n == 0);
        ArrayList<ContinuousStudent> students = new ArrayList<>();
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        students = DailyReportUtil.getContinunous(n);

        if (students.isEmpty()){
            System.out.println("##无记录！");
            return;
        }
        System.out.printf("##连续%d天填报时间相同的学生有%d位，信息如下\n", n, students.size());
        for (ContinuousStudent student: students)
        {
            Student studentInfo = AccountUtil.getStudent(student.getStudent_ID());
            System.out.println("##----------");
            System.out.printf("学号：%s\n姓名：%s\n电话：%s\n班级：%s\n院系：%s\n连续填报的开始时间：%s\n",
                    studentInfo.getID(), studentInfo.getName(), studentInfo.getPhone(), studentInfo.getClassName(),
                    studentInfo.getFacultyName(), sdf.format(student.getStart_day()));
        }
        System.out.println("##----------");

    }

    //查询前 n 个平均离校时间最长的学生 支持按多级范围（全校、院系、班级）
    public void getAvgLeave()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        int n = 0;
        do{
            System.out.println("##请输入要查询前几个平均离校时间最长的学生");
            System.out.print(">");
            input = scanner.nextLine();
            if(Student.isNumeric(input))
            {
                n = Integer.parseInt(input);
            }
            else
            {
                System.out.println("##请输入数字！");
            }
        }while(n == 0);
        ArrayList<AvgOutTime> avgOutTimes = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        String faculty;
        String classname;
        boolean invalid = true;
        do
        {
            System.out.println("##全校查询请输1，按院系查询请输2，按班级查询请输3");
            System.out.print(">");
            scanner = new Scanner(System.in);
            input = scanner.nextLine();
            switch (input){
                case "1":
                    students = AccountUtil.listStudent();
                    for(Student student:students)
                    {
                        AvgOutTime getNew = RecordUtil.getOutSchoolTimeAvg(student.getID(), student.getInSchool());
                        avgOutTimes.add(getNew);
                    }
                    invalid = false;
                    break;
                case "2":
                    do{
                        System.out.println("##请输入查询院系的名称");
                        System.out.print(">");
                        faculty = scanner.nextLine();
                        if(AccountUtil.facultyExists(faculty))
                        {
                            students = AccountUtil.getFacultyList(faculty);
                            for(Student student:students)
                            {
                                AvgOutTime getNew = RecordUtil.getOutSchoolTimeAvg(student.getID(), student.getInSchool());
                                avgOutTimes.add(getNew);
                            }
                        }
                        else
                        {
                            System.out.println("##请输入正确的院系名称！");
                        }
                    }while(!AccountUtil.facultyExists(faculty));
                    invalid = false;
                    break;
                case "3":
                    do{
                        System.out.println("##请输入班级所在院系的名称");
                        System.out.print(">");
                        faculty = scanner.nextLine();
                        if(!AccountUtil.facultyExists(faculty))
                        {
                            System.out.println("##请输入正确的院系名称！");
                        }
                    }while(!AccountUtil.facultyExists(faculty));
                    do{
                        System.out.println("##请输入班级的名称");
                        System.out.print(">");
                        classname = scanner.nextLine();
                        if(!AccountUtil.instructorExists(classname, faculty))
                        {
                            System.out.println("##请输入正确的班级名称！");
                        }
                    }while(!AccountUtil.instructorExists(classname, faculty));
                    students = AccountUtil.getClassList(classname, faculty);
                    for(Student student:students)
                    {
                        AvgOutTime getNew = RecordUtil.getOutSchoolTimeAvg(student.getID(), student.getInSchool());
                        avgOutTimes.add(getNew);
                    }
                    invalid = false;
                    break;
                default:
                    System.out.println("##输入的数字错误，请重试");
            }
        }while (invalid);
        Collections.sort(avgOutTimes);
        System.out.printf("##平均离校时长最长的前%d位的学生如下\n",n);
        if (avgOutTimes.isEmpty()){
            System.out.println("##无记录！");
            return;
        }
        int j=1;
        for(AvgOutTime avgOutTime: avgOutTimes)
        {
            if (j>n) break;
            System.out.println("##----------");
            System.out.printf("学号：%s\n平均离校时长：%s\n",
                    avgOutTime.getStudent_ID(),
                    RecordUtil.secondToTime(avgOutTime.getAvgOutTime()));
            j++;

        }
    }
}
