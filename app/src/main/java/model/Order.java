package model;

public class Order {

    private int idOrder, idShop,idUser, quantity;
    private Double totalPrice;
    private String date, note;
    private String name;
    private Long phone;
    private String address;
    private int status;

    public Order() {
    }

    public Order(int idOrder, int idShop, int idUser, int quantity, Double totalPrice, String date, String note, String name, Long phone, String address, int status) {
        this.idOrder = idOrder;
        this.idShop = idShop;
        this.idUser = idUser;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.date = date;
        this.note = note;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdShop() {
        return idShop;
    }

    public void setIdShop(int idShop) {
        this.idShop = idShop;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
