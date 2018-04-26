package robhawk.com.br.orm_example.data.dao;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import robhawk.com.br.orm_example.data.model.Task;
import robhawk.com.br.orm_example.util.Repository;

public class TaskDao {
    private static final AtomicInteger KEY = new AtomicInteger();
    private static final Repository<Task> REPOSITORY = new Repository<>();

    public void insert(Task task) {
        task.id = KEY.incrementAndGet();
        REPOSITORY.append(task.id, task);
    }

    public void update(Task task) {
        REPOSITORY.put(task.id, task);
    }

    public void delete(Task task) {
        REPOSITORY.delete(task.id);
    }

    public List<Task> listAll() {
        return REPOSITORY.listAll();
    }
}
