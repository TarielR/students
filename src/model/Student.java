package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Student extends Person {
    private int studentId;
    private int groupId;
    private int educationYear;

    public Student() {
        super();
    }

    public Student(ResultSet rs) throws SQLException {
        super(rs);
        setStudentId(rs.getInt(1));
        setEducationYear(rs.getDate(8).toLocalDate().getYear());
    }

    public int getEducationYear() { return educationYear; }

    public void setEducationYear(int educationYear) { this.educationYear = educationYear; }

    public int getStudentId() { return studentId; }

    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getGroupId() { return groupId; }

    public void setGroupId(int groupId) { this.groupId = groupId; }


    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", groupId=" + groupId +
                ", educationYear=" + educationYear +
                "} " + super.toString();
    }
}
