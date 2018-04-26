package robhawk.com.br.orm_example.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.LinkedList;
import java.util.List;

import robhawk.com.br.orm_example.data.model.Task;
import robhawk.com.br.orm_example.orm.Dao;
import robhawk.com.br.orm_example.util.DateUtil;

public class TaskDao extends Dao<Task> {

    public TaskDao(Context context) {
        super(context);
    }

    @Override
    public boolean insert(Task model) {
        long result = getWdb().insert("task", null, values(model));
        model.id = (int) result;
        return result > -1;
    }

    @Override
    public boolean update(Task model) {
        String where = "id = " + model.id;
        int result = getWdb().update("task", values(model), where, null);
        return result > 0;
    }

    @Override
    public boolean delete(Task model) {
        String where = "id = " + model.id;
        int result = getWdb().delete("task", where, null);
        return result > 0;
    }

    @Override
    public Task findById(int id) {
        Task result = null;
        Cursor cursor = getRdb().rawQuery("SELECT * FROM task WHERE id = ?", new String[]{id + ""});
        if (cursor.moveToNext())
            result = extract(cursor);
        cursor.close();
        return result;
    }

    @Override
    public List<Task> listAll() {
        List<Task> result = new LinkedList<>();
        Cursor cursor = getRdb().rawQuery("SELECT * FROM task", null);
        while (cursor.moveToNext())
            result.add(extract(cursor));
        cursor.close();
        return result;
    }

    @Override
    public ContentValues values(Task model) {
        ContentValues values = new ContentValues();
        values.put("description", model.description);
        values.put("date", DateUtil.formatPtBr(model.date));
        values.put("completed", model.completed);
        values.put("idUser", model.idUser);
        return values;
    }

    @Override
    public Task extract(Cursor cursor) {
        Task task = new Task();
        task.id = cursor.getInt(cursor.getColumnIndex("id"));
        task.description = cursor.getString(cursor.getColumnIndex("description"));
        task.date = DateUtil.parsePtBr(cursor.getString(cursor.getColumnIndex("date")));
        task.completed = cursor.getInt(cursor.getColumnIndex("completed")) == 1;
        task.idUser = cursor.getInt(cursor.getColumnIndex("idUser"));
        return task;
    }

}
