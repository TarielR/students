package model;

public class Student extends Person {
    private int studentId;
    private int educationYear;

    public int getEducationYear() { return educationYear; }

    public void setEducationYear(int educationYear) { this.educationYear = educationYear; }

    public int getStudentId() { return studentId; }

    public void setStudentId(int studentId) { this.studentId = studentId; }


    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", educationYear=" + educationYear +
                "} " + super.toString();
    }
}
