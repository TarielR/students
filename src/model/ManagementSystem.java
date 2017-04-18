package model;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class ManagementSystem {
    private static ManagementSystem instance;
    private Set<Student> students;
    private List<Curator> curators;
    private List<Group> groups;

    private ManagementSystem() {
        loadCurators();
        loadGroups();
        loadStudents();

    }

    public static synchronized ManagementSystem getInstance() {
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
        s.setName("Иван");
        s.setMidname("Сергеевич");
        s.setSurname("Степанов");
        s.setSex(Sex.MALE);
        c.set(1990, 3, 20);
        s.setbDate(c.getTime());
        s.setGroupid(2);
        s.setEducationYear(2006);
        students.add(s);

        s = new Student();
        s.setStudentId(2);
        s.setName("Наталья");
        s.setMidname("Андреевна");
        s.setSurname("Чичикова");
        s.setSex(Sex.FEMALE);
        c.set(1990, 6, 10);
        s.setbDate(c.getTime());
        s.setGroupid(2);
        s.setEducationYear(2006);
        students.add(s);

        // Первая группа
        s = new Student();
        s.setStudentId(3);
        s.setName("Петр");
        s.setMidname("Викторович");
        s.setSurname("Сушкин");
        s.setSex(Sex.MALE);
        c.set(1991, 3, 12);
        s.setbDate(c.getTime());
        s.setEducationYear(2006);
        s.setGroupid(1);
        students.add(s);

        s = new Student();
        s.setStudentId(4);
        s.setName("Вероника");
        s.setMidname("Сергеевна");
        s.setSurname("Ковалева");
        s.setSex(Sex.FEMALE);
        c.set(1991, 7, 19);
        s.setbDate(c.getTime());
        s.setEducationYear(2006);
        s.setGroupid(1);
        students.add(s);
    }

    private void loadCurators() {
        if (curators == null)
            curators = new ArrayList<>();
        else
            curators.clear();

        Curator c = null;

        c = new Curator();
        c.setSurname("Борменталь");
        c.setRang("Доктор");
        c.setSex(Sex.MALE);
        curators.add(c);

        c = new Curator();
        c.setSurname("Преображенский");
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
    public void moveStudentsToGroup(Group oldGroup, int oldYear, Group newGroup, int newYear) {
        for (Student si : students) {
            if (si.getGroupid() == oldGroup.getGroupId() && si.getEducationYear() == oldYear) {
                si.setGroupid(newGroup.getGroupId());
                si.setEducationYear(newYear);
            }
        }
    }

    // Удалить всех студентов из определенной группы
    public void removeStudentsFromGroup(Group group, int year) {
        // Мы создадим новый список студентов БЕЗ тех, кого мы хотим удалить.
        // Возможно не самый интересный вариант. Можно было бы продемонстрировать
        // более элегантный метод, но он требует погрузиться в коллекции более глубоко
        // Здесь мы не ставим себе такую цель
        Set<Student> tmp = new TreeSet<Student>();
        for (Student si : students) {
            if (si.getGroupid() != group.getGroupId() || si.getEducationYear() != year) {
                tmp.add(si);
            }
        }
        students = tmp;
    }

    // Добавить студента
    public void insertStudent(Student student) {
        // Просто добавляем объект в коллекцию
        students.add(student);
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
        updStudent.setName(student.getName());
        updStudent.setMidname(student.getMidname());
        updStudent.setSurname(student.getSurname());
        updStudent.setSex(student.getSex());
        updStudent.setbDate(student.getbDate());
        updStudent.setGroupid(student.getGroupid());
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
    public Set<Student> getStudentsFromGroup(Group group, int year) {
        Set<Student> l = new TreeSet<>();
        for (Student si : students) {
            if (si.getGroupid() == group.getGroupId() && si.getEducationYear() == year) {
                l.add(si);
            }
        }
        return l;
    }

    public List<Group> getGroups() { return groups; }

    public List<Curator> getCurators() { return curators; }

    public Set<Student> getAllStudents() { return students; }

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
        s.setName("Игорь");
        s.setMidname("Владимирович");
        s.setSurname("Перебежкин");
        s.setSex(Sex.MALE);
        Calendar c = Calendar.getInstance();
        c.set(1991, 8, 31);
        s.setbDate(c.getTime());
        s.setGroupid(1);
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
