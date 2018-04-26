package robhawk.com.br.orm_example.data.model;

import java.io.Serializable;

public class User implements Serializable {
    public int id;
    public String nome;
    public String email;
    public String password;

    public User() {
        this("", "", "");
    }

    public User(String nome, String email, String password) {
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return id + " - " + nome;
    }
}
