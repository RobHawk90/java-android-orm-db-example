package robhawk.com.br.orm_example.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import robhawk.com.br.orm_example.data.model.Task;
import robhawk.com.br.orm_example.orm.Dao;
import robhawk.com.br.orm_example.util.DateUtil;

public class TaskDao extends Dao<Task> {

    public TaskDao(Context context) {
        super(context);
    }

    @Override
    public Task findById(int id) {
        return getResultObject("SELECT * FROM task WHERE id = ?", id);
    }

    @Override
    public List<Task> listAll() {
        return getResultList("SELECT * FROM task");
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
