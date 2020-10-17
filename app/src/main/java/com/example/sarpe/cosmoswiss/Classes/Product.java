package com.example.sarpe.cosmoswiss.Classes;

import java.io.Serializable;

public class Product implements Serializable {
    private String code;
    private String nameEnglish;
    private String nameGerman;
    private String brand;
    private String mainCategory;
    private String subCategory;
    private String barcode;
    private String costSalon;
    private String costWeb;
    private String picture;
    private String quantity;

    public Product(String code, String nameEnglish, String nameGerman, String brand, String mainCategory, String subCategory, String barcode, String costSalon, String costWeb, String quantity, String picture) {
        this.code = code;
        this.nameEnglish = nameEnglish;
        this.nameGerman = nameGerman;
        this.brand = brand;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.barcode = barcode;
        this.costSalon = costSalon;
        this.costWeb = costWeb;
        this.picture = picture;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public String getNameGerman() {
        return nameGerman;
    }

    public void setNameGerman(String nameGerman) {
        this.nameGerman = nameGerman;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCostSalon() {
        return costSalon;
    }

    public void setCostSalon(String costSalon) {
        this.costSalon = costSalon;
    }

    public String getCostWeb() {
        return costWeb;
    }

    public void setCostWeb(String costWeb) {
        this.costWeb = costWeb;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "code='" + code + '\'' +
                ", nameEnglish='" + nameEnglish + '\'' +
                ", nameGerman='" + nameGerman + '\'' +
                ", brand='" + brand + '\'' +
                ", mainCategory='" + mainCategory + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", barcode='" + barcode + '\'' +
                ", costSalon='" + costSalon + '\'' +
                ", costWeb='" + costWeb + '\'' +
                ", picture='" + picture + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
