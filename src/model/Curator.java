package model;


public class Curator extends Person {
    private String rang;

    public String getRang() {return rang; }

    public void setRang(String rang) { this.rang = rang; }


    @Override
    public String toString() {
        return "Curator{" +
                "rang='" + rang + "\' " +
                "Surname='" + super.getSurname() + "\' " +
                "sex='" + super.getSex() + '\'' +
                "} " ;
    }
}
