package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Person implements Comparable {
    private String name;
    private String surname;
    private String midname;
    private Date bDate;
    private Sex sex;
    private int groupid;

    public Person(ResultSet rs) throws SQLException {
        setName(rs.getString(2));
        setSurname(rs.getString(3));
        setMidname(rs.getString(4));
        setbDate(rs.getDate(5));

    }

    public Person() { }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getMidname() { return midname; }

    public void setMidname(String midname) { this.midname = midname; }

    public Date getbDate() { return bDate; }

    public void setbDate(Date bDate) { this.bDate = bDate; }

    public Sex getSex() { return sex; }

    public void setSex(Sex sex) { this.sex = sex; }

    public int getGroupid() { return groupid; }

    public void setGroupid(int groupid) { this.groupid = groupid; }


    @Override
    public int compareTo(Object obj) {
        Collator c = Collator.getInstance(new Locale("ru"));
        c.setStrength(Collator.PRIMARY);
        return c.compare(this.toString(), obj.toString());
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", midname='" + midname + '\'' +
                ", surname='" + surname + '\'' +
                ", bDate=" + SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(bDate) +
                ", sex=" + sex +
                ", groupid=" + groupid +
                '}';
    }
}
