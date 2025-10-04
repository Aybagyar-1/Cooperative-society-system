package model;

import java.util.Date;

public class Member {
    private int memberId;
    private String surname;
    private String firstName;
    private String middleName;
    private String email;
    private String phone;
    private String password;
    private Date joinDate;
    private String status;

    public Member(String surname, String firstName, String middleName, String email, String phone, String password, Date joinDate, String status) {
        this.surname = surname;
        this.firstName = firstName;
        this.middleName = middleName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.joinDate = joinDate;
        this.status = status;
    }

    public Member(int memberId, String surname, String firstName, String middleName, String email, String phone, String password, Date joinDate, String status) {
        this.memberId = memberId;
        this.surname = surname;
        this.firstName = firstName;
        this.middleName = middleName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.joinDate = joinDate;
        this.status = status;
    }

    public int getMemberId() { return memberId; }
    public String getSurname() { return surname; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public Date getJoinDate() { return joinDate; }
    public String getStatus() { return status; }
}
