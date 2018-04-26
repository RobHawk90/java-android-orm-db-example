package robhawk.com.br.orm_example.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
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

    public T getResultObject(String sql, Object... params) {
        T result = null;
        Cursor cursor = getCursor(sql, params);
        if (cursor.moveToNext())
            result = extract(cursor);
        cursor.close();
        return result;
    }

    public List<T> getResultList(String sql, Object... params) {
        List<T> result = new LinkedList<>();
        Cursor cursor = getCursor(sql, params);
        while (cursor.moveToNext())
            result.add(extract(cursor));
        cursor.close();
        return result;
    }

    public Cursor getCursor(String sql, Object... params) {
        return getRdb().rawQuery(sql, map(params));
    }

    private String[] map(Object[] params) {
        String[] mappedParams = new String[params.length];
        for (int i = 0; i < params.length; i++)
            mappedParams[i] = params[i] == null ? "" : params[i].toString();
        return mappedParams;
    }

    public abstract boolean insert(T model);

    public abstract boolean update(T model);

    public abstract boolean delete(T model);

    public abstract T findById(int id);

    public abstract List<T> listAll();

    public abstract ContentValues values(T model);

    public abstract T extract(Cursor cursor);

}
