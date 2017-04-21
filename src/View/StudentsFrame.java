package View;

import model.Group;
import model.ManagementSystem;
import model.Student;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

public class StudentsFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener {
    // Введем сразу имена для кнопок - потом будем их использовать в обработчиках
    private static final String MOVE_GR = "moveGroup";
    private static final String CLEAR_GR = "clearGroup";
    private static final String INSERT_ST = "insertStudent";
    private static final String UPDATE_ST = "updateStudent";
    private static final String DELETE_ST = "deleteStudent";
    private static final String ALL_STUDENTS = "allStudent";

    private ManagementSystem ms;

    private JList<Group> grpList;
    private JTable stdList;
    private JSpinner spYear;

    public StudentsFrame(String title) throws Exception {
        ms = ManagementSystem.getInstance();
        setTitle(title);
        setLayout(new BorderLayout());

        // Создаем строку меню
        JMenuBar menuBar = new JMenuBar();
        // Создаем выпадающее меню
        JMenu menu = new JMenu("Отчеты");
        // Создаем пункт в выпадающем меню
        JMenuItem menuItem = new JMenuItem("Все студенты");
        menuItem.setName(ALL_STUDENTS);
        // Вставляем пункт меню в выпадающее меню
        menu.add(menuItem);
        // Вставляем выпадающее меню в строку меню
        menuBar.add(menu);
        // Устанавливаем меню для формы
        setJMenuBar(menuBar);

        // Создаем верхнюю панел, где будет поле для ввода года
        JPanel top = new JPanel();
        // Устанавливаем для нее layout
        top.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Вставляем пояснительную надпись
        top.add(new JLabel("Год обучения:"));
        // Делаем спин-поле
        //   1. Задаем модель поведения - только цифры
        //   2. Вставляем в панель
        SpinnerModel sm = new SpinnerNumberModel(2006, 1900, 2100, 1);
        spYear = new JSpinner(sm);
        spYear.addChangeListener(this);
        top.add(spYear);

        // Создаем нижнюю панель и задаем ей layout
        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());

        // Создаем левую панель для вывода списка групп
        GroupPanel left = new GroupPanel();
        // Задаем layout и задаем "бордюр" вокруг панели
        left.setLayout(new BorderLayout());
        left.setBorder(new BevelBorder(BevelBorder.LOWERED));

        // Получаем список групп
        Group [] gr = ms.getGroups().toArray(new Group[0]);
        // Создаем надпись
        left.add(new JLabel("  Группы:"), BorderLayout.NORTH);
        // Создаем визуальный список и вставляем его в скроллируемую
        // панель, которую в свою очередь уже кладем на панель left
        grpList = new JList(gr);
        // Добавляем листенер
        grpList.addListSelectionListener(this);
        // Сразу выделяем первую группу
        grpList.setSelectedIndex(0);
        left.add(new JScrollPane(grpList), BorderLayout.CENTER);

        // Создаем кнопки для групп
        JButton btnMvGr = new JButton("Переместить");
        btnMvGr.setName(MOVE_GR);
        JButton btnClGr = new JButton("Очистить");
        btnClGr.setName(CLEAR_GR);
        btnMvGr.addActionListener(this);
        btnClGr.addActionListener(this);
        // Создаем панель, на которую положим наши кнопки и кладем ее вниз
        JPanel pnlBtnGr = new JPanel();
        pnlBtnGr.setLayout(new GridLayout(1, 2));
        pnlBtnGr.add(btnMvGr);
        pnlBtnGr.add(btnClGr);
        left.add(pnlBtnGr, BorderLayout.SOUTH);

        // Создаем правую панель для вывода списка студентов
        JPanel right = new JPanel();
        // Задаем layout и задаем "бордюр" вокруг панели
        right.setLayout(new BorderLayout());
        right.setBorder(new BevelBorder(BevelBorder.LOWERED));

        // Создаем надпись
        right.add(new JLabel("  Студенты:"), BorderLayout.NORTH);

        // Создаем таблицу и вставляем ее в скроллируемую
        // панель, которую в свою очередь уже кладем на панель right
        // Наша таблица пока ничего не умеет - просто положим ее как заготовку
        // Сделаем в ней 4 колонки - Фамилия, Имя, Отчество, Дата рождения
        stdList = new JTable(1, 4);
        right.add(new JScrollPane(stdList), BorderLayout.CENTER);
        // Создаем кнопки для студентов
        JButton btnAddSt = new JButton("Добавить");
        btnAddSt.setName(INSERT_ST);
        btnAddSt.addActionListener(this);
        JButton btnUpdSt = new JButton("Исправить");
        btnUpdSt.setName(UPDATE_ST);
        btnUpdSt.addActionListener(this);
        JButton btnDelSt = new JButton("Удалить");
        btnDelSt.setName(DELETE_ST);
        btnDelSt.addActionListener(this);

        // Создаем панель, на которую положим наши кнопки и кладем ее вниз
        JPanel pnlBtnSt = new JPanel();
        pnlBtnSt.setLayout(new GridLayout(1, 3));
        pnlBtnSt.add(btnAddSt);
        pnlBtnSt.add(btnUpdSt);
        pnlBtnSt.add(btnDelSt);
        right.add(pnlBtnSt, BorderLayout.SOUTH);

        // Вставляем панели со списками групп и студентов в нижнюю панель
        bot.add(left, BorderLayout.WEST);
        bot.add(right, BorderLayout.CENTER);

        // Вставляем верхнюю и нижнюю панели в форму
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(bot, BorderLayout.CENTER);

        // Задаем границы формы
        setBounds(100, 100, 1200, 400);
    }

    public static void main(String args[]) {
        // Запуск формы лучше производить в специальном треде
        // event-dispatching thread - EDT
        SwingUtilities.invokeLater(() -> {
            try {
                StudentsFrame sf = new StudentsFrame("http://java-course.ru");
                sf.setDefaultCloseOperation(EXIT_ON_CLOSE);
                sf.setVisible(true);
                // Перегрузка списка нам нужна в этом треде
                // т.к. при создании формы списка студентов еще нет
                sf.reloadStudents();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    // Метод для обеспечения интерфейса ActionListener
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component c = (Component) e.getSource();
            if (c.getName().equals(MOVE_GR)) {
                moveGroup();
            }
            if (c.getName().equals(CLEAR_GR)) {
                clearGroup();
            }
            if (c.getName().equals(ALL_STUDENTS)) {
                showAllStudents();
            }
            if (c.getName().equals(INSERT_ST)) {
                insertStudent();
            }
            if (c.getName().equals(UPDATE_ST)) {
                updateStudent();
            }
            if (c.getName().equals(DELETE_ST)) {
                deleteStudent();
            }
        }
    }

    // Метод для обеспечения интерфейса ListSelectionListener
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            reloadStudents();
        }
    }

    /**************  Заглушки  ***********************/
    // метод для обновления списка студентов для определенной группы
    private void reloadStudents() {
        Runnable r = () -> {
            if (stdList != null) {
                // Получаем выделенную группу
                Group g = (Group) grpList.getSelectedValue();
                // Получаем число из спинера
                int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                try {
                    // Получаем список студентов
                    Collection<Student> s = ms.getStudentsFromGroup(g, y);
                    // И устанавливаем модель для таблицы с новыми данными
                    SwingUtilities.invokeLater(() ->
                    stdList.setModel(new StudentTableModel(new Vector<Student>(s)))
                    );
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                }
            }
            // Вводим искусственную задержку на 3 секунды
            try {
                Thread.sleep(3000);
            } catch (Exception e) {  }
        };
        // И теперь мы запускаем наш поток
        Thread t = new Thread(r);
        t.start();
    }

    // Метод для обеспечения интерфейса ChangeListener
    public void stateChanged(ChangeEvent e) {
        reloadStudents();
    }

    // метод для переноса группы
    private void moveGroup() {
        Runnable r = () -> {
            // Если группа не выделена - выходим. Хотя это крайне маловероятно
            if (grpList.getSelectedValue() == null) {
                return;
            }
            try {
                // Получаем выделенную группу
                Group g = (Group) grpList.getSelectedValue();
                // Получаем число из спинера
                int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                // Создаем наш диалог
                GroupDialog gd = new GroupDialog(y, ms.getGroups());
                // Задаем ему режим модальности - нельзя ничего кроме него выделить
                gd.setModal(true);
                // Показываем диалог
                gd.setVisible(true);

                // Если нажали кнопку OK - перемещаем в новую группу с новым годом
                // и перегружаем список студентов
                if (gd.getResult()) {
                    ms.moveStudentsToGroup(g, y, gd.getGroup(), gd.getYear());
                    reloadStudents();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    // метод для очистки группы
    private void clearGroup() {
        Runnable r = () -> {
            // Проверяем - выделена ли группа
            if (grpList.getSelectedValue() != null) {
                if (JOptionPane.showConfirmDialog(StudentsFrame.this,
                        "Вы хотите удалить студентов из группы?", "Удаление студентов",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    // Получаем выделенную группу
                    Group g = (Group) grpList.getSelectedValue();
                    // Получаем число из спинера
                    int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                    try {
                        // Удалили студентов из группы
                        ms.removeStudentsFromGroup(g, y);
                        // перегрузили список студентов
                        reloadStudents();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    // метод для добавления студента
    private void insertStudent() {
        JOptionPane.showMessageDialog(this, "insertStudent");
    }

    // метод для редактирования студента
    private void updateStudent() {
        JOptionPane.showMessageDialog(this, "updateStudent");
    }

    // метод для удаления студента
    private void deleteStudent() {
        Runnable r = () -> {
            if (stdList != null) {
                StudentTableModel stm = (StudentTableModel) stdList.getModel();
                // Проверяем - выделен ли хоть какой-нибудь студент
                if (stdList.getSelectedRow() >= 0) {
                    if (JOptionPane.showConfirmDialog(StudentsFrame.this,
                            "Вы хотите удалить студента?", "Удаление студента",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        // Вот где нам пригодился метод getStudent(int rowIndex)
                        Student s = stm.getStudent(stdList.getSelectedRow());
                        try {
                            ms.deleteStudent(s);
                            reloadStudents();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                        }
                    }
                }
                // Если студент не выделен - сообщаем пользователю, что это необходимо
                else {
                    JOptionPane.showMessageDialog(StudentsFrame.this, "Необходимо выделить студента в списке");
                }
            }
        };
            Thread t = new Thread(r);
            t.start();
    }


    // метод для показа всех студентов
    private void showAllStudents() {
        JOptionPane.showMessageDialog(this, "showAllStudents");
    }

    /**************  Заглушки  ***********************/

    private class GroupPanel extends JPanel {
        public Dimension getPreferredSize() {
            return new Dimension(600, 0);
        }
    }
}
