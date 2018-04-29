package robhawk.com.br.orm_example.data.dao;

import android.content.Context;
import android.database.Cursor;

import java.util.List;

import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.orm.Dao;

public class UserDao extends Dao {

    public UserDao(Context context) {
        super(context);
    }

    public User findById(int id) {
        return findById(id, User.class);
    }

    public List<User> listAll() {
        return listAll(User.class);
    }

    public User auth(String email, String password) {
        return getResultObject(User.class, "SELECT * FROM user WHERE email = ? AND password = ?", email, password);
    }

    public boolean isEmailExists(String email) {
        Cursor cursor = getCursor("SELECT * FROM user WHERE email = ?", email);
        return cursor.moveToNext();
    }

}
