package com.example.sarpe.cosmoswiss.Classes;

import java.util.ArrayList;

public class Order {

    private ArrayList<Product> products;
    private ArrayList<String> productQuantities;
    private String date;
    private String orderNumber;
    private  String currentSituation;

    public Order(ArrayList<Product> products, ArrayList<String> productQuantities, String orderNumber, String date, String currentSituation) {
        this.products = products;
        this.productQuantities = productQuantities;
        this.date = date;
        this.orderNumber = orderNumber;
        this.currentSituation = currentSituation;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<String> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(ArrayList<String> productQuantities) {
        this.productQuantities = productQuantities;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCurrentSituation() {
        return currentSituation;
    }

    public void setCurrentSituation(String currentSituation) {
        this.currentSituation = currentSituation;
    }

    @Override
    public String toString() {
        return "Order{" +
                "products=" + products +
                ", productQuantities=" + productQuantities +
                ", date='" + date + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", currentSituation='" + currentSituation + '\'' +
                '}';
    }
}
