package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ManagementSystem {
    private static ManagementSystem instance;
    private static Connection con;

    private ManagementSystem() throws Exception {
        try {
            Class.forName("org.h2.Driver");
            String url = "jdbc:h2:file:E:/proj/WebApp/lib.mv.db";
            con = DriverManager.getConnection(url, "sa", "");
        } catch (SQLException e) {
            throw new Exception(e);
        } catch (ClassNotFoundException e) {
            throw new Exception(e);
        }
    }

    public static synchronized ManagementSystem getInstance() throws Exception {
        if (instance == null)
            instance = new ManagementSystem();
        return instance;
    }

    // Перевести студентов из одной группы с одним годом обучения в другую группу с другим годом обучения
    public void moveStudentsToGroup(Group oldGroup, int oldYear, Group newGroup, int newYear) throws SQLException {
        PreparedStatement stmn = null;

        try {
            stmn = con.prepareStatement(
                    "UPDATE STUDENTS SET GROUP_ID=?, EDUCATIONYEAR=? WHERE GROUP_ID=? AND EDUCATIONYEAR=?"
            );
            stmn.setInt(1, newGroup.getGroupId());
            stmn.setInt(2, newYear);
            stmn.setInt(3, oldGroup.getGroupId());
            stmn.setInt(4, oldYear);
            stmn.execute();
        } finally {
            if (stmn != null)
                stmn.close();
        }
    }

    // Удалить всех студентов из определенной группы
    public void removeStudentsFromGroup(Group group, int year) throws  SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(
                    "DELETE FROM STUDENTS WHERE group_id=? AND educationYear=?");
            stmt.setInt(1, group.getGroupId());
            stmt.setInt(2, year);
            stmt.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    // Добавить студента
    public void insertStudent(Student student) throws SQLException {
        PreparedStatement stmn = null;
        try {
            stmn = con.prepareStatement("INSERT INTO STUDENTS " +
                "(NAME, SURNAME, MIDNAME, BDAY, SEX, GROUP_ID, EDUCATIONYEAR) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmn.setString(1, student.getFirstName());
            stmn.setString(2, student.getSurName());
            stmn.setString(3,student.getMidleName());
            stmn.setDate(4, (Date) student.getDateOfBirth());
            stmn.setString(5, String.valueOf(student.getSex()));
            stmn.setInt(6, student.getGroupId());
            stmn.setInt(7, student.getEducationYear());
        } finally {
            if (stmn != null)
                stmn.close();
        }
    }

    // Обновить данные о студенте
    public void updateStudent(Student student) throws SQLException {
        PreparedStatement stmn = null;
        try {
            stmn = con.prepareStatement("UPDATE STUDENTS SET " +
                    "NAME=?, SURNAME=?, MIDNAME=?, BDAY=?, SEX=?, GROUP_ID=?, EDUCATIONYEAR=? " +
                    "WHERE STUDENT_ID=?");
            stmn.setString(1, student.getFirstName());
            stmn.setString(2, student.getSurName());
            stmn.setString(3,student.getMidleName());
            stmn.setDate(4, (Date) student.getDateOfBirth());
            stmn.setString(5, String.valueOf(student.getSex()));
            stmn.setInt(6, student.getGroupId());
            stmn.setInt(7, student.getEducationYear());
        } finally {
            if (stmn != null)
                stmn.close();
        }
    }

    // Удалить студента
    public void deleteStudent(Student student) throws SQLException {
        PreparedStatement stmn = null;
        try {
            stmn = con.prepareStatement(
                    "DELETE FROM students WHERE student_id=?");
            stmn.setInt(1, student.getStudentId());
            stmn.execute();
        } finally {
            if (stmn != null)
                stmn.close();
        }
    }

    // Получить список студентов для определенной группы
    public Set<Student> getStudentsFromGroup(Group group, int year) throws SQLException {
        Set<Student> students = new TreeSet<>();
        PreparedStatement stmn = null;
        ResultSet rs = null;

        try {
            stmn = con.prepareStatement("SELECT * FROM STUDENTS WHERE GROUP_ID=? AND EDUCATIONYEAR=? ORDER BY SURNAME");
            stmn.setInt(1, group.getGroupId());
            stmn.setInt(2, year);
            rs = stmn.executeQuery();
            while (rs.next()) {
                Student st = new Student(rs);
                students.add(st);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmn != null)
                stmn.close();
        }
        return students;
    }

    public List<Group> getGroups() throws SQLException {
        List<Group> groups = new ArrayList<>();
        Statement stmn = null;
        ResultSet rs = null;
        try {
            stmn = con.createStatement();
            rs = stmn.executeQuery("SELECT GROUPS.GROUP_ID, GROUPS.GROUPNAME, CURATORS.CURATOR_ID, GROUPS.SPECIALITY from GROUPS, CURATORS WHERE GROUPS.CURATOR = CURATORS.CURATOR_ID;");
            while(rs.next()) {
                Group gr = new Group(rs);
                groups.add(gr);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmn != null)
                stmn.close();
        }
        return groups;
    }

    public List<Curator> getCurators() throws SQLException {
        List<Curator> curators = new ArrayList<>();
        Statement stmn = null;
        ResultSet rs = null;
        try {
            stmn = con.createStatement();
            rs = stmn.executeQuery("SELECT * FROM CURATORS");
            while(rs.next()) {
                Curator cr = new Curator(rs);
                curators.add(cr);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmn != null)
                stmn.close();
        }
        return curators;
    }

    public Set<Student> getAllStudents() throws SQLException {
        Set<Student> students = new TreeSet<>();
        Statement stmn = null;
        ResultSet rs = null;
        try {
            stmn = con.createStatement();
            rs = stmn.executeQuery("SELECT * FROM STUDENTS");
            while (rs.next()) {
                Student st = new Student(rs);
                students.add(st);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmn != null)
                stmn.close();
        }
        return students;
    }
}
