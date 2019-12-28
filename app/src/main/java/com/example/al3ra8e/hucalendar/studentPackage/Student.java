package com.example.al3ra8e.hucalendar.studentPackage;


public class Student {
    private int id ;
    private int personId ;
    private String firstName ;
    private String lastName  ;
    private String username ;
    private String password ;
    private String image ;

    public Student() {
    }

    public Student(int id, String firstName, String lastName, String username, String password, String image) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.image = image;
    }

    public Student setPersonId(int personId) {
        this.personId = personId;
        return this ;
    }

    public int getPersonId() {
        return personId;
    }

    public int getId() {
        return id;
    }

    public Student setId(int id) {
        this.id = id;
        return this ;
    }

    public String getFirstName() {
        return firstName;
    }

    public Student setFirstName(String firstName) {
        this.firstName = firstName;
        return this ;
    }

    public String getLastName() {
        return lastName;
    }

    public Student setLastName(String lastName) {
        this.lastName = lastName;
        return this ;
    }

    public String getFullName() {
        return firstName+" "+lastName;
    }


    public String getUsername() {
        return username;
    }

    public Student setUsername(String username) {
        this.username = username;
        return this ;
    }

    public String getPassword() {
        return password;
    }

    public Student setPassword(String password) {
        this.password = password;
        return this ;
    }

    public String getImage() {
        return image;
    }

    public Student setImage(String image) {
        this.image = image;
        return this ;
    }

    public Student getStudent() {
        return this;
    }


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + firstName+"  "+lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
