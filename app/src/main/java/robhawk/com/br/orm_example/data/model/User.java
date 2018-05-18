package robhawk.com.br.orm_example.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

import robhawk.com.br.orm_example.orm.annotation.Id;
import robhawk.com.br.orm_example.orm.annotation.Ignore;
import robhawk.com.br.orm_example.orm.annotation.Table;

@Table("user")
public class User extends BaseObservable implements Serializable {

    @Id
    public int id;
    @Bindable
    public String name;
    @Bindable
    public String email;
    @Bindable
    public String password;
    @Bindable
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

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
