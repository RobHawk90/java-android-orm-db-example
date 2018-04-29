package robhawk.com.br.orm_example.data.dao;

import android.content.Context;

import java.util.List;

import robhawk.com.br.orm_example.data.model.Task;
import robhawk.com.br.orm_example.orm.Dao;

public class TaskDao extends Dao<Task> {

    public TaskDao(Context context) {
        super(context);
    }

    @Override
    public Task findById(int id) {
        return getResultObject(Task.class, "SELECT * FROM task WHERE id = ?", id);
    }

    @Override
    public List<Task> listAll() {
        return getResultList(Task.class, "SELECT * FROM task");
    }
}
