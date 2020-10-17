package com.example.sarpe.cosmoswiss.Classes;

import java.util.ArrayList;

public class User {

    //Ask in login screen
    private boolean isHerr;

    private String name;
    private String surname;
    private String email;
    private String password;
    private String telephoneNumber;
    private String adress;

    //afterwards
    private ArrayList<Product> shoppingCart;
    private ArrayList<Product> favorites;

    private ArrayList<Order> allOrders;

    public User(){

    }

    public User(boolean isHerr, String name, String surname, String email, String password, String telephoneNumber, String adress) {
        this.isHerr = true;  //change it later
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.telephoneNumber = telephoneNumber;
        this.adress = adress;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public ArrayList<Product> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ArrayList<Product> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public ArrayList<Product> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Product> favorites) {
        this.favorites = favorites;
    }

    public ArrayList<Order> getAllOrders() {
        return allOrders;
    }

    public void setAllOrders(ArrayList<Order> allOrders) {
        this.allOrders = allOrders;
    }
}
