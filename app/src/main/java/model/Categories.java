package model;

public class Categories {
    private int idCategories;
    private int idShop;
    private String name;
    private byte[] image;

    public Categories() {
    }

    public Categories(int idCategories, int idShop, String name, byte[] image) {
        this.idCategories = idCategories;
        this.idShop = idShop;
        this.name = name;
        this.image = image;
    }

    public int getIdCategories() {
        return idCategories;
    }

    public void setIdCategories(int idCategories) {
        this.idCategories = idCategories;
    }

    public int getIdShop() {
        return idShop;
    }

    public void setIdShop(int idShop) {
        this.idShop = idShop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}