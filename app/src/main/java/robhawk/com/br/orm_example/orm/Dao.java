package robhawk.com.br.orm_example.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import robhawk.com.br.orm_example.orm.annotation.Column;
import robhawk.com.br.orm_example.orm.annotation.Id;
import robhawk.com.br.orm_example.orm.annotation.Ignore;
import robhawk.com.br.orm_example.orm.annotation.Table;
import robhawk.com.br.orm_example.util.DateUtil;

public class Dao {

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

    public <T> T getResultObject(Class<T> modelType, String sql, Object... params) {
        T result = null;
        Cursor cursor = getCursor(sql, params);
        if (cursor.moveToNext())
            result = extract(cursor, modelType);
        cursor.close();
        return result;
    }

    public <T> List<T> getResultList(Class<T> modelType, String sql, Object... params) {
        List<T> result = new LinkedList<>();
        Cursor cursor = getCursor(sql, params);
        while (cursor.moveToNext())
            result.add(extract(cursor, modelType));
        cursor.close();
        return result;
    }

    public Cursor getCursor(String sql, Object... params) {
        return getRdb().rawQuery(sql, map(params));
    }

    public <T> boolean insert(T model) {
        Class<?> modelType = model.getClass();
        List<Field> fields = getNonIgnoredNorIdFields(modelType);
        Field idField = getIdField(modelType);
        long result = getWdb().insert(getTableName(modelType), null, getContentValues(fields, model));
        setFieldValue(idField, (int) result, model);
        return result > -1;
    }

    public <T> boolean update(T model) {
        Class<?> modelType = model.getClass();
        List<Field> fields = getNonIgnoredNorIdFields(modelType);
        Field idField = getIdField(modelType);
        String where = getIdFieldName(idField) + " = " + getFieldValue(idField, model);
        int result = getWdb().update(getTableName(modelType), getContentValues(fields, model), where, null);
        return result > 0;
    }


    public <T> boolean delete(T model) {
        Class<?> modelType = model.getClass();
        Field idField = getIdField(modelType);
        String where = getIdFieldName(idField) + " = " + getFieldValue(idField, model);
        int result = getWdb().delete(getTableName(modelType), where, null);
        return result > 0;
    }

    public <T> List<T> listAll(Class<T> modelType) {
        String sql = "SELECT * FROM " + getTableName(modelType);
        return getResultList(modelType, sql);
    }

    public <T> T findById(int id, Class<T> modelType) {
        Field idField = getIdField(modelType);
        String sql = "SELECT * FROM " + getTableName(modelType) + " WHERE " + getIdFieldName(idField) + " = ?";
        return getResultObject(modelType, sql, id);
    }

    private String[] map(Object[] params) {
        String[] mappedParams = new String[params.length];
        for (int i = 0; i < params.length; i++)
            mappedParams[i] = params[i] == null ? "" : params[i].toString();
        return mappedParams;
    }

    @NonNull
    private <T> ContentValues getContentValues(List<Field> fields, T model) {
        ContentValues values = new ContentValues();

        try {
            for (Field field : fields)
                putValue(values, field, model);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return values;
    }

    private <T> void putValue(ContentValues values, Field field, T model) throws IllegalAccessException {
        Class<?> fieldType = field.getType();
        String fieldName = getFieldName(field);

        if (fieldType.equals(String.class))
            values.put(fieldName, field.get(model).toString());
        else if (fieldType.equals(Integer.class) || fieldType.equals(int.class))
            values.put(fieldName, field.getInt(model));
        else if (fieldType.equals(Double.class) || fieldType.equals(double.class))
            values.put(fieldName, field.getDouble(model));
        else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class))
            values.put(fieldName, field.getBoolean(model));
        else if (fieldType.equals(Long.class) || fieldType.equals(long.class))
            values.put(fieldName, field.getLong(model));
        else if (fieldType.equals(Float.class) || fieldType.equals(float.class))
            values.put(fieldName, field.getFloat(model));
        else if (fieldType.equals(Short.class) || fieldType.equals(short.class))
            values.put(fieldName, field.getShort(model));
        else if (fieldType.equals(byte[].class))
            values.put(fieldName, (byte[]) field.get(model));
        else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class))
            values.put(fieldName, field.getByte(model));
        else if (fieldType.equals(Date.class))
            values.put(fieldName, DateUtil.formatPtBr((Date) field.get(model)));
    }

    private <T> T extract(Cursor cursor, Class<T> modelType) {
        T model = getNewInstance(modelType);

        List<Field> fields = getNonIgnoredNorIdFields(modelType);
        fields.add(getIdField(modelType));

        for (Field field : fields)
            setFieldValue(field, cursor, model);

        return model;
    }

    private <T> void setFieldValue(Field field, Cursor cursor, T model) {
        Class<?> fieldType = field.getType();
        String fieldName = getFieldName(field);

        if (fieldType.equals(String.class))
            setFieldValue(field, cursor.getString(cursor.getColumnIndex(fieldName)), model);
        else if (fieldType.equals(Integer.class) || fieldType.equals(int.class))
            setFieldValue(field, cursor.getInt(cursor.getColumnIndex(fieldName)), model);
        else if (fieldType.equals(Double.class) || fieldType.equals(double.class))
            setFieldValue(field, cursor.getDouble(cursor.getColumnIndex(fieldName)), model);
        else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class))
            setFieldValue(field, cursor.getInt(cursor.getColumnIndex(fieldName)) == 1, model);
        else if (fieldType.equals(Long.class) || fieldType.equals(long.class))
            setFieldValue(field, cursor.getLong(cursor.getColumnIndex(fieldName)), model);
        else if (fieldType.equals(Float.class) || fieldType.equals(float.class))
            setFieldValue(field, cursor.getFloat(cursor.getColumnIndex(fieldName)), model);
        else if (fieldType.equals(Short.class) || fieldType.equals(short.class))
            setFieldValue(field, cursor.getShort(cursor.getColumnIndex(fieldName)), model);
        else if (fieldType.equals(byte[].class))
            setFieldValue(field, cursor.getBlob(cursor.getColumnIndex(fieldName)), model);
        else if (fieldType.equals(Date.class))
            setFieldValue(field, DateUtil.parsePtBr(cursor.getString(cursor.getColumnIndex(fieldName))), model);
    }

    private <T> T getNewInstance(Class<T> modelType) {
        try {
            return modelType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getFieldName(Field field) {
        if (field.isAnnotationPresent(Column.class))
            return field.getAnnotation(Column.class).value();
        return field.getName();
    }

    private Field getIdField(Class<?> modelType) {
        Field idField = null;
        for (Field field : modelType.getDeclaredFields())
            if (field.isAnnotationPresent(Id.class))
                idField = field;
        if (idField == null)
            throw new IllegalArgumentException(modelType.getSimpleName() + " must have an @Id field");
        return idField;
    }

    @NonNull
    private List<Field> getNonIgnoredNorIdFields(Class<?> modelType) {
        List<Field> fields = new LinkedList<>();
        for (Field field : modelType.getDeclaredFields())
            if (!field.isAnnotationPresent(Ignore.class) && !field.isAnnotationPresent(Id.class) && !field.getName().equals("serialVersionUID"))
                fields.add(field);
        return fields;
    }

    private String getTableName(Class<?> modelType) {
        String tableName = "";
        if (modelType.isAnnotationPresent(Table.class))
            tableName = modelType.getAnnotation(Table.class).value();
        if (tableName.isEmpty())
            tableName = modelType.getSimpleName();
        return tableName;
    }

    private String getIdFieldName(Field idField) {
        String fieldName = "";
        if (idField.isAnnotationPresent(Id.class))
            idField.getAnnotation(Id.class).value();
        if (fieldName.isEmpty())
            fieldName = idField.getName();
        return fieldName;
    }

    private <T> void setFieldValue(Field field, Object value, T model) {
        try {
            field.set(model, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private <T> String getFieldValue(Field field, T model) {
        try {
            return field.get(model).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

}
