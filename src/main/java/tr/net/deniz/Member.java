/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.net.deniz;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.persistence.*;

/**
 *
 * @author yukseldeniz
 */
@Entity
public class Member {

    @Id
    private String userName;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String userType;

    public Member() {
    }

    //JSON CONSTRUCTOR
    public Member(String json) {
        Gson gson = new Gson();
        JsonObject jObj = gson.fromJson(json, JsonObject.class);
        this.userName = jObj.get("userName").getAsString();
        this.password = jObj.get("password").getAsString();

        if (jObj.get("email") != null) {
            this.email = jObj.get("email").getAsString();
        }

        if (jObj.get("name") != null) {
            this.name = jObj.get("name").getAsString();
        }

        if (jObj.get("surname") != null) {
            this.surname = jObj.get("surname").getAsString();
        }

        if (jObj.get("userType") != null) {
            this.userType = jObj.get("userType").getAsString();
        }

    }

    public Member(String userName, String email, String password, String name, String surname, String userType) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.userType = userType;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
