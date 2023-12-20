package stu.edu.vn.myapplication.model;

public class Users {
    private int id;
    private  String name;
    private  String age;
    private  String phone;
    private  String social;
    private  String mail;

    private byte[]image;

    public Users(int id, String name, String age, String phone, String social, String mail, byte[] image) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.social = social;
        this.mail = mail;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}