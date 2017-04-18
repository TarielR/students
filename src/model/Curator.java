package model;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Curator extends Person {
    private String rang;

    public Curator() { }

    public Curator(ResultSet rs) throws SQLException {
        super(rs);
        setRang(rs.getString(3));
    }
    public String getRang() {return rang; }

    public void setRang(String rang) { this.rang = rang; }

    @Override
    public String toString() {
        return "Curator{" +
                "rang='" + rang + "\' " +
                "Surname='" + super.getSurName() + "\' " +
                "sex='" + super.getSex() + '\'' +
                "} " ;
    }
}
