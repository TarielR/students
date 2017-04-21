package View;

import model.Student;

import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.util.Vector;

public class StudentTableModel extends AbstractTableModel {
    // Сделаем хранилище для нашего списка студентов
    private Vector<Student> students;
    // Количество столбцов - 4.
    private final int COLUMN_COUNT = 4;

    // Модель при создании получает список студентов
    public StudentTableModel(Vector students) {
        this.students = students;
    }

    // Количество строк равно числу записей
    @Override
    public int getRowCount() {
        if (students != null) {
            return students.size();
        }
        return 0;
    }

    // Количество столбцов - 4. Фамилия, Имя, Отчество, Дата рождения
    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    // Вернем наименование колонки
    public String getColumnName(int column) {
        String[] colNames = {"Фамилия", "Имя", "Отчество", "Дата"};
        return colNames[column];
    }

    // Возвращаем данные для определенной строки и столбца
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (students != null) {
            // Получаем из сета студента
            Student st = (Student) students.get(rowIndex);
            // В зависимости от колонки возвращаем имя, фамилия и т.д.
            switch (columnIndex) {
                case 0:
                    return st.getSurName();
                case 1:
                    return st.getFirstName();
                case 2:
                    return st.getMidleName();
                case 3:
                    return DateFormat.getDateInstance(DateFormat.SHORT).format(st.getDateOfBirth());
            }
        }
        return null;
    }

    // Добавим метод, который возвращает студента по номеру строки
    // Это нам пригодится чуть позже
    public Student getStudent(int rowIndex) {
        if (students != null) {
            if (rowIndex < students.size() && rowIndex >= 0) {
                return (Student) students.get(rowIndex);
            }
        }
        return null;
    }
}
