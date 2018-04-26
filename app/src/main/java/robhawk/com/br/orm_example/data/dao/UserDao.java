package robhawk.com.br.orm_example.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.orm.Dao;

public class UserDao extends Dao<User> {

    public UserDao(Context context) {
        super(context);
    }

    @Override
    public boolean insert(User model) {
        long result = getWdb().insert("user", null, values(model));
        model.id = (int) result;
        return result > -1;
    }

    @Override
    public boolean update(User model) {
        String where = "id = " + model.id;
        int result = getWdb().update("user", values(model), where, null);
        return result > 0;
    }

    @Override
    public boolean delete(User model) {
        String where = "id = " + model.id;
        int result = getWdb().delete("user", where, null);
        return result > 0;
    }

    @Override
    public User findById(int id) {
        return getResultObject("SELECT * FROM user WHERE id = ?", id);
    }

    @Override
    public List<User> listAll() {
        return getResultList("SELECT * FROM user");
    }

    public User auth(String email, String password) {
        return getResultObject("SELECT * FROM user WHERE email = ? AND password = ?", email, password);
    }

    public boolean isEmailExists(String email) {
        Cursor cursor = getCursor("SELECT * FROM user WHERE email = ?", email);
        return cursor.moveToNext();
    }

    @Override
    public ContentValues values(User model) {
        ContentValues values = new ContentValues();
        values.put("name", model.name);
        values.put("email", model.email);
        values.put("password", model.password);
        return values;
    }

    @Override
    public User extract(Cursor cursor) {
        User user = new User();
        user.id = cursor.getInt(cursor.getColumnIndex("id"));
        user.name = cursor.getString(cursor.getColumnIndex("name"));
        user.email = cursor.getString(cursor.getColumnIndex("email"));
        user.password = cursor.getString(cursor.getColumnIndex("password"));
        return user;
    }

}
