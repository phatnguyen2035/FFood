package model;

public class User {
    private int idUser;
    private String user;
    private String pass;
    private String name;
    private long phone;
    private long cccd;
    private int role;
    private String address;
    private int status;

    public User() {
    }

    public User(int idUser) {
        this.idUser = idUser;
    }

    public User(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public User(int idUser, String user, String pass, String name, long phone, long cccd, int role, String address) {
        this.idUser = idUser;
        this.user = user;
        this.pass = pass;
        this.name = name;
        this.phone = phone;
        this.cccd = cccd;
        this.role = role;
        this.address = address;
    }

    public User(int idUser, String user, String pass, String name, long phone, long cccd, int role, String address, int status) {
        this.idUser = idUser;
        this.user = user;
        this.pass = pass;
        this.name = name;
        this.phone = phone;
        this.cccd = cccd;
        this.role = role;
        this.address = address;
        this.status = status;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public long getCccd() {
        return cccd;
    }

    public void setCccd(long cccd) {
        this.cccd = cccd;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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