package com.example.sarpe.cosmoswiss.Classes;

public class ContactPerson {

    private boolean isHerr;

    private String name;
    private String surname;

    private String telephoneNumber;

    public ContactPerson(boolean isHerr, String name, String surname, String telephoneNumber) {
        this.isHerr = isHerr;
        this.name = name;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
    }

    public boolean isHerr() {
        return isHerr;
    }

    public void setHerr(boolean herr) {
        isHerr = herr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    @Override
    public String toString() {
        return "ContactPerson{" +
                "isHerr=" + isHerr +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                '}';
    }
}
