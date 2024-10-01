package model;

public class Product {
    private int idProduct, idCategories, idShop;
    private String name;
    private byte[] image;
    private double price;
    private String note;
    private int status;
    private int sold;


    public Product() {
    }

    public Product(int idProduct, int idCategories, int idShop, String name, byte[] image, double price, String note, int status, int sold) {
        this.idProduct = idProduct;
        this.idCategories = idCategories;
        this.idShop = idShop;
        this.name = name;
        this.image = image;
        this.price = price;
        this.note = note;
        this.status = status;
        this.sold = sold;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}
