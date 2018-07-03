package com.example.siddharth.have_more.Model;

/**
 * Created by Siddharth on 16-12-2017.
 */

public class Category {
    private String Image;
    private String  MenuId;
    private String Name;
    private String price;

    public Category() {
    }

    public Category(String image, String menuId, String name, String price) {
        Image = image;
        MenuId = menuId;
        Name = name;
        this.price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
