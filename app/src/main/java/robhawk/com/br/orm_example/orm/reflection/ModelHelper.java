package robhawk.com.br.orm_example.orm.reflection;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import robhawk.com.br.orm_example.orm.annotation.Column;
import robhawk.com.br.orm_example.orm.annotation.Id;
import robhawk.com.br.orm_example.orm.annotation.Ignore;
import robhawk.com.br.orm_example.orm.annotation.Table;
import robhawk.com.br.orm_example.util.DateUtil;

public class ModelHelper {

    public String[] map(Object[] params) {
        String[] mappedParams = new String[params.length];
        for (int i = 0; i < params.length; i++)
            mappedParams[i] = params[i] == null ? "" : params[i].toString();
        return mappedParams;
    }

    @NonNull
    public <T> ContentValues getContentValues(List<Field> fields, T model) {
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

    public <T> T extract(Cursor cursor, Class<T> modelType) {
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

    public Field getIdField(Class<?> modelType) {
        Field idField = null;
        for (Field field : modelType.getDeclaredFields())
            if (field.isAnnotationPresent(Id.class))
                idField = field;
        if (idField == null)
            throw new IllegalArgumentException(modelType.getSimpleName() + " must have an @Id field");
        return idField;
    }

    @NonNull
    public List<Field> getNonIgnoredNorIdFields(Class<?> modelType) {
        List<Field> fields = new LinkedList<>();
        for (Field field : modelType.getDeclaredFields())
            if (!field.isAnnotationPresent(Ignore.class) && !field.isAnnotationPresent(Id.class) && !field.getName().equals("serialVersionUID"))
                fields.add(field);
        return fields;
    }

    public String getTableName(Class<?> modelType) {
        String tableName = "";
        if (modelType.isAnnotationPresent(Table.class))
            tableName = modelType.getAnnotation(Table.class).value();
        if (tableName.isEmpty())
            tableName = modelType.getSimpleName();
        return tableName;
    }

    public String getIdFieldName(Field idField) {
        String fieldName = "";
        if (idField.isAnnotationPresent(Id.class))
            idField.getAnnotation(Id.class).value();
        if (fieldName.isEmpty())
            fieldName = idField.getName();
        return fieldName;
    }

    public <T> void setFieldValue(Field field, Object value, T model) {
        try {
            field.set(model, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public <T> String getFieldValue(Field field, T model) {
        try {
            return field.get(model).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    public <T extends Dao> Class<?> getModelType(Class<T> daoType) {
        for (Type type : daoType.getGenericInterfaces())
            if (type.toString().contains(Dao.class.getName()))
                return (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
        return null;
    }

    public Class<?> getReturnType(Method method) {
        Type genericType = method.getGenericReturnType();
        try {
            ParameterizedType param = ParameterizedType.class.cast(genericType);
            return (Class<?>) param.getActualTypeArguments()[0];
        } catch (Exception e) {
            return (Class<?>) genericType;
        }
    }

}
