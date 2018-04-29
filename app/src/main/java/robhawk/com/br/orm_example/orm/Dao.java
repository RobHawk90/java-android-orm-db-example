package robhawk.com.br.orm_example.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import robhawk.com.br.orm_example.orm.annotation.Column;
import robhawk.com.br.orm_example.orm.annotation.Id;
import robhawk.com.br.orm_example.orm.annotation.Ignore;
import robhawk.com.br.orm_example.orm.annotation.Table;
import robhawk.com.br.orm_example.util.DateUtil;

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

    public T getResultObject(Class<T> modelType, String sql, Object... params) {
        T result = null;
        Cursor cursor = getCursor(sql, params);
        if (cursor.moveToNext())
            result = extract(cursor, modelType);
        cursor.close();
        return result;
    }

    public List<T> getResultList(Class<T> modelType, String sql, Object... params) {
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

    private String[] map(Object[] params) {
        String[] mappedParams = new String[params.length];
        for (int i = 0; i < params.length; i++)
            mappedParams[i] = params[i] == null ? "" : params[i].toString();
        return mappedParams;
    }

    public boolean insert(T model) {
        try {
            Class<?> modelType = model.getClass();
            String tableName = getTableName(modelType);
            List<Field> fields = getNonIgnoredNorIdFields(modelType);
            Field idField = getIdField(modelType);
            ContentValues values = getContentValues(model, fields);

            long result = getWdb().insert(tableName, null, values);

            if (idField != null)
                idField.setInt(model, (int) result);

            return result > -1;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    @NonNull
    private ContentValues getContentValues(T model, List<Field> fields) throws IllegalAccessException {
        ContentValues values = new ContentValues();

        for (Field field : fields) {
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

        return values;
    }

    private String getFieldName(Field field) {
        if (field.isAnnotationPresent(Column.class))
            return field.getAnnotation(Column.class).value();
        return field.getName();
    }

    @Nullable
    private Field getIdField(Class<?> modelType) {
        Field idField = null;
        for (Field field : modelType.getDeclaredFields())
            if (field.isAnnotationPresent(Id.class))
                idField = field;
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

    public boolean update(T model) {
        try {
            Class<?> modelType = model.getClass();
            String tableName = getTableName(modelType);
            List<Field> fields = getNonIgnoredNorIdFields(modelType);
            Field idField = getIdField(modelType);
            ContentValues values = getContentValues(model, fields);

            if (idField != null) {
                String where = getIdFieldName(idField) + " = " + idField.getInt(model);
                int result = getWdb().update(tableName, values, where, null);
                return result > 0;
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    private String getIdFieldName(Field idField) {
        String fieldName = "";
        if (idField.isAnnotationPresent(Id.class))
            idField.getAnnotation(Id.class).value();
        if (fieldName.isEmpty())
            fieldName = idField.getName();
        return fieldName;
    }

    public boolean delete(T model) {
        try {
            Class<?> modelType = model.getClass();
            String tableName = getTableName(modelType);
            Field idField = getIdField(modelType);
            if (idField != null) {
                String where = getIdFieldName(idField) + " = " + idField.getInt(model);
                int result = getWdb().delete(tableName, where, null);
                return result > 0;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<T> listAll(Class<T> modelType) {
        String tableName = getTableName(modelType);
        String sql = "SELECT * FROM " + tableName;
        return getResultList(modelType, sql);
    }

    public T findById(int id, Class<T> modelType) {
        String tableName = getTableName(modelType);
        Field idField = getIdField(modelType);
        if (idField != null) {
            String where = getIdFieldName(idField) + " = ?";
            String sql = "SELECT * FROM " + tableName + " WHERE " + where;
            return getResultObject(modelType, sql, id);
        }
        return null;
    }

    public ContentValues values(T model) {
        try {
            Class<?> modelType = model.getClass();
            List<Field> fields = getNonIgnoredNorIdFields(modelType);
            return getContentValues(model, fields);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public T extract(Cursor cursor, Class<T> modelType) {
        try {
            T model = modelType.newInstance();

            List<Field> fields = getNonIgnoredNorIdFields(modelType);
            fields.add(getIdField(modelType));

            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                String fieldName = getFieldName(field);

                if (fieldType.equals(String.class))
                    field.set(model, cursor.getString(cursor.getColumnIndex(fieldName)));
                else if (fieldType.equals(Integer.class) || fieldType.equals(int.class))
                    field.set(model, cursor.getInt(cursor.getColumnIndex(fieldName)));
                else if (fieldType.equals(Double.class) || fieldType.equals(double.class))
                    field.set(model, cursor.getDouble(cursor.getColumnIndex(fieldName)));
                else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class))
                    field.set(model, cursor.getInt(cursor.getColumnIndex(fieldName)) == 1);
                else if (fieldType.equals(Long.class) || fieldType.equals(long.class))
                    field.set(model, cursor.getLong(cursor.getColumnIndex(fieldName)));
                else if (fieldType.equals(Float.class) || fieldType.equals(float.class))
                    field.set(model, cursor.getFloat(cursor.getColumnIndex(fieldName)));
                else if (fieldType.equals(Short.class) || fieldType.equals(short.class))
                    field.set(model, cursor.getShort(cursor.getColumnIndex(fieldName)));
                else if (fieldType.equals(byte[].class))
                    field.set(model, cursor.getBlob(cursor.getColumnIndex(fieldName)));
                else if (fieldType.equals(Date.class))
                    field.set(model, DateUtil.parsePtBr(cursor.getString(cursor.getColumnIndex(fieldName))));
            }

            return model;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
