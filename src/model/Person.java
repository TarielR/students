package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Person implements Comparable {
    private String firstName;
    private String surName;
    private String midleName;
    private Date dateOfBirth;
    private Sex sex;


    public Person(ResultSet rs) throws SQLException {
        setFirstName(rs.getString(2));
        setSurName(rs.getString(3));
        setMidleName(rs.getString(4));
        setDateOfBirth(rs.getDate(5));

    }

    public Person() { }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSurName() { return surName; }

    public void setSurName(String surName) { this.surName = surName; }

    public String getMidleName() { return midleName; }

    public void setMidleName(String midleName) { this.midleName = midleName; }

    public Date getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Sex getSex() { return sex; }

    public void setSex(Sex sex) { this.sex = sex; }


    @Override
    public int compareTo(Object obj) {
        Collator c = Collator.getInstance(new Locale("ru"));
        c.setStrength(Collator.PRIMARY);
        return c.compare(this.toString(), obj.toString());
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", midleName='" + midleName + '\'' +
                ", surName='" + surName + '\'' +
                ", dateOfBirth=" + SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(dateOfBirth) +
                ", sex=" + sex +
                '}';
    }
}
