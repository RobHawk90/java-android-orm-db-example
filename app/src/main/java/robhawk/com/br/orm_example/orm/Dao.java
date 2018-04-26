package robhawk.com.br.orm_example.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public abstract class Dao<T> {

    private final DbHelper helper;

    public Dao(Context context) {
        helper = new DbHelper(context);
    }

    public SQLiteDatabase getWdb() {
        return helper.getWritableDatabase();
    }

    public SQLiteDatabase getRdb() {
        return helper.getReadableDatabase();
    }

    public abstract boolean insert(T model);

    public abstract boolean update(T model);

    public abstract boolean delete(T model);

    public abstract T findById(int id);

    public abstract List<T> listAll();

    public abstract ContentValues values(T model);

    public abstract T extract(Cursor cursor);

}
