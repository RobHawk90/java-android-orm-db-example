package robhawk.com.br.orm_example.orm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import robhawk.com.br.orm_example.orm.reflection.ModelHelper;

public class Dao {

    private final DbHelper dbHelper;
    private final ModelHelper modelHelper;

    public Dao() {
        dbHelper = DbHelper.getInstance();
        modelHelper = new ModelHelper();
    }

    public SQLiteDatabase getWdb() {
        return dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getRdb() {
        return dbHelper.getReadableDatabase();
    }

    public <T> T getResultObject(Class<T> modelType, String sql, Object... params) {
        T result = null;
        Cursor cursor = getCursor(sql, params);
        if (cursor.moveToNext())
            result = modelHelper.extract(cursor, modelType);
        cursor.close();
        return result;
    }

    public <T> List<T> getResultList(Class<T> modelType, String sql, Object... params) {
        List<T> result = new LinkedList<>();
        Cursor cursor = getCursor(sql, params);
        while (cursor.moveToNext())
            result.add(modelHelper.extract(cursor, modelType));
        cursor.close();
        return result;
    }

    public Cursor getCursor(String sql, Object... params) {
        return getRdb().rawQuery(sql, modelHelper.map(params));
    }

    public <T> boolean insert(T model) {
        Class<?> modelType = model.getClass();
        List<Field> fields = modelHelper.getNonIgnoredNorIdFields(modelType);
        Field idField = modelHelper.getIdField(modelType);
        long result = getWdb().insert(modelHelper.getTableName(modelType), null, modelHelper.getContentValues(fields, model));
        modelHelper.setFieldValue(idField, (int) result, model);
        return result > -1;
    }

    public <T> boolean update(T model) {
        Class<?> modelType = model.getClass();
        List<Field> fields = modelHelper.getNonIgnoredNorIdFields(modelType);
        Field idField = modelHelper.getIdField(modelType);
        String where = modelHelper.getIdFieldName(idField) + " = " + modelHelper.getFieldValue(idField, model);
        int result = getWdb().update(modelHelper.getTableName(modelType), modelHelper.getContentValues(fields, model), where, null);
        return result > 0;
    }

    public <T> boolean delete(T model) {
        Class<?> modelType = model.getClass();
        Field idField = modelHelper.getIdField(modelType);
        String where = modelHelper.getIdFieldName(idField) + " = " + modelHelper.getFieldValue(idField, model);
        int result = getWdb().delete(modelHelper.getTableName(modelType), where, null);
        return result > 0;
    }

    public <T> List<T> listAll(Class<T> modelType) {
        String sql = "SELECT * FROM " + modelHelper.getTableName(modelType);
        return getResultList(modelType, sql);
    }

    public <T> T findById(int id, Class<T> modelType) {
        Field idField = modelHelper.getIdField(modelType);
        String sql = "SELECT * FROM " + modelHelper.getTableName(modelType) + " WHERE " + modelHelper.getIdFieldName(idField) + " = ?";
        return getResultObject(modelType, sql, id);
    }


}
