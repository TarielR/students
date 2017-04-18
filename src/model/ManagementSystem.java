package model;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;

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

    private void loadStudents() {
        if (students == null) {
            // Мы используем коллекцию, которая автоматически сортирует свои элементы
            students = new TreeSet<>();
        } else {
            students.clear();
        }

        Student s = null;
        Calendar c = Calendar.getInstance();

        // Вторая группа
        s = new Student();
        s.setStudentId(1);
        s.setFirstName("Иван");
        s.setMidleName("Сергеевич");
        s.setSurName("Степанов");
        s.setSex(Sex.MALE);
        c.set(1990, 3, 20);
        s.setDateOfBirth(c.getTime());
        s.setGroupId(2);
        s.setEducationYear(2006);
        students.add(s);

        s = new Student();
        s.setStudentId(2);
        s.setFirstName("Наталья");
        s.setMidleName("Андреевна");
        s.setSurName("Чичикова");
        s.setSex(Sex.FEMALE);
        c.set(1990, 6, 10);
        s.setDateOfBirth(c.getTime());
        s.setGroupId(2);
        s.setEducationYear(2006);
        students.add(s);

        // Первая группа
        s = new Student();
        s.setStudentId(3);
        s.setFirstName("Петр");
        s.setMidleName("Викторович");
        s.setSurName("Сушкин");
        s.setSex(Sex.MALE);
        c.set(1991, 3, 12);
        s.setDateOfBirth(c.getTime());
        s.setEducationYear(2006);
        s.setGroupId(1);
        students.add(s);

        s = new Student();
        s.setStudentId(4);
        s.setFirstName("Вероника");
        s.setMidleName("Сергеевна");
        s.setSurName("Ковалева");
        s.setSex(Sex.FEMALE);
        c.set(1991, 7, 19);
        s.setDateOfBirth(c.getTime());
        s.setEducationYear(2006);
        s.setGroupId(1);
        students.add(s);
    }

    private void loadCurators() {
        if (curators == null)
            curators = new ArrayList<>();
        else
            curators.clear();

        Curator c = null;

        c = new Curator();
        c.setSurName("Борменталь");
        c.setRang("Доктор");
        c.setSex(Sex.MALE);
        curators.add(c);

        c = new Curator();
        c.setSurName("Преображенский");
        c.setRang("Профессор");
        c.setSex(Sex.MALE);
        curators.add(c);
    }

    private void loadGroups() {
        if (groups == null)
            groups = new ArrayList<>();
        else
            groups.clear();

        Group g = null;

        g = new Group();
        g.setGroupId(1);
        g.setGroupName("Первая");
        g.setCurator(curators.get(0));
        g.setSpeciality("Создание собачек из человеков");
        groups.add(g);

        g = new Group();
        g.setGroupId(2);
        g.setGroupName("Вторая");
        g.setCurator(curators.get(1));
        g.setSpeciality("Создание человеков из собачек");
        groups.add(g);
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
    public void insertStudent(Student student) {

    }

    // Обновить данные о студенте
    public void updateStudent(Student student) {
        // Надо найти нужного студента (по его ИД) и заменить поля
        Student updStudent = null;
        for (Student si : students) {
            if (si.getStudentId() == student.getStudentId()) {
                // Вот этот студент - запоминаем его и прекращаем цикл
                updStudent = si;
                break;
            }
        }
        updStudent.setFirstName(student.getFirstName());
        updStudent.setMidleName(student.getMidleName());
        updStudent.setSurName(student.getSurName());
        updStudent.setSex(student.getSex());
        updStudent.setDateOfBirth(student.getDateOfBirth());
        updStudent.setGroupId(student.getGroupId());
        updStudent.setEducationYear(student.getEducationYear());
    }

    // Удалить студента
    public void deleteStudent(Student student) {
        // Надо найти нужного студента (по его ИД) и удалить
        Student delStudent = null;
        for (Student si : students) {
            if (si.getStudentId() == student.getStudentId()) {
                // Вот этот студент - запоминаем его и прекращаем цикл
                delStudent = si;
                break;
            }
        }
        students.remove(delStudent);
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
            rs = stmn.executeQuery("SELECT * FROM GROUPS");
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

    public static void main(String[] args) {
        // Этот код позволяет нам перенаправить стандартный вывод в файл т.к. на экран выводится не совсем
        // удобочитаемая кодировка, файл в данном случае более удобен
        try {
            System.setOut(new PrintStream("out.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ManagementSystem ms = ManagementSystem.getInstance();

        // Просмотр полного списка кураторов
        System.out.println("Полный список кураторов");
        System.out.println("***********************");
        List<Curator> allCurators = ms.getCurators();
        for (Curator cu : allCurators) {
            System.out.println(cu);
        }
        // Просмотр полного списка групп
        System.out.println("\nПолный список групп");
        System.out.println("*******************");
        List<Group> allGroups = ms.getGroups();
        for (Group gi : allGroups) {
            System.out.println(gi);
        }

        // Просмотр полного списка студентов
        System.out.println("\nПолный список студентов");
        System.out.println("***********************");
        Collection<Student> allStudends = ms.getAllStudents();
        for (Student si : allStudends) {
            System.out.println(si);
        }

        // Вывод списков студентов по группам
        System.out.println("\nСписок студентов по группам");
        System.out.println("***************************");
        List<Group> groups = ms.getGroups();
        // Проверяем все группы
        for (Group gi : groups) {
            System.out.println("---> Группа:" + gi.getGroupName());
            // Получаем список студентов для конкретной группы
            Set<Student> students = ms.getStudentsFromGroup(gi, 2006);
            for (Student si : students) {
                System.out.println(si);
            }
        }

        Student s = new Student();
        s.setStudentId(5);
        s.setFirstName("Игорь");
        s.setMidleName("Владимирович");
        s.setSurName("Перебежкин");
        s.setSex(Sex.MALE);
        Calendar c = Calendar.getInstance();
        c.set(1991, 8, 31);
        s.setDateOfBirth(c.getTime());
        s.setGroupId(1);
        s.setEducationYear(2006);
        System.out.println("Добавление студента:" + s);
        System.out.println("********************");
        ms.insertStudent(s);

        // Вывод списков студентов по группам
        System.out.println("\nСписок студентов по группам");
        System.out.println("***************************");
        groups = ms.getGroups();
        // Проверяем все группы
        for (Group gi : groups) {
            System.out.println("---> Группа:" + gi.getGroupName());
            // Получаем список студентов для конкретной группы
            Set<Student> students = ms.getStudentsFromGroup(gi, 2006);
            for (Student si : students) {
                System.out.println(si);
            }
        }
    }
}
