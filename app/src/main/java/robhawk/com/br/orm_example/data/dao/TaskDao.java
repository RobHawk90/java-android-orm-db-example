package robhawk.com.br.orm_example.data.dao;

import java.util.List;

import robhawk.com.br.orm_example.data.model.Task;
import robhawk.com.br.orm_example.orm.Dao;

public class TaskDao extends Dao {

    public Task findById(int id) {
        return findById(id, Task.class);
    }

    public List<Task> listAll() {
        return listAll(Task.class);
    }
}
