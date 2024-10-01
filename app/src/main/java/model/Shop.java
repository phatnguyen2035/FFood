package model;

public class Shop {

    private int idShop;
    private int idUser;
    private String name;
    private String address;
    private byte[] image;
    private int status;
    private int totalSold = -1; //

    public Shop() {
    }

    public Shop(int idShop, int idUser, String name, String address, byte[] image, int status) {
        this.idShop = idShop;
        this.idUser = idUser;
        this.name = name;
        this.address = address;
        this.image = image;
        this.status = status;
    }

    public Shop(int idShop, int idUser, String name, String address, byte[] image, int status, int totalSold) {
        this(idShop, idUser, name, address, image, status);
        this.totalSold = totalSold;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }
}