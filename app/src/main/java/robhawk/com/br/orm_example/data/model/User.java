package robhawk.com.br.orm_example.data.model;

import java.io.Serializable;

import robhawk.com.br.orm_example.orm.annotation.Id;
import robhawk.com.br.orm_example.orm.annotation.Ignore;
import robhawk.com.br.orm_example.orm.annotation.Table;

@Table("user")
public class User implements Serializable {

    @Id
    public int id;
    public String name;
    public String email;
    public String password;
    @Ignore
    public String passwordConfirm;

    public User() {
        this("", "", "");
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean isConfirmPassword() {
        return password.equals(passwordConfirm);
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
