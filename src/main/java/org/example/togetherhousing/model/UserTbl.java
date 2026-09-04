package org.example.togetherhousing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

//@Entity creates the table with provided name in database
//UserTbl -> user_tbl
@Entity
@Data
public class UserTbl {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    private String fullname;

    private String email;

    private String phone;

    private String address;

    private String password;

    private String role;


}