package robhawk.com.br.orm_example.ui.task;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.view.View;

import robhawk.com.br.orm_example.data.dao.TaskDao;
import robhawk.com.br.orm_example.data.model.Task;
import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.orm.reflection.DaoFactory;

public class TaskViewModel extends ViewModel {
    private final MediatorLiveData<Task> task;
    private final TaskDao dao;
    private TaskNavigator navigator;
    private User user;
    public TaskAdapter adapter;

    public TaskViewModel() {
        task = new MediatorLiveData<>();
        task.setValue(new Task());
        dao = DaoFactory.create(TaskDao.class);

        adapter = new TaskAdapter();
        adapter.setOnComplete(new TaskAdapter.OnItemClick() {
            @Override
            public void click(Task task, int position, View view) {
                toggleCompleted(task);
            }
        });
        adapter.setOnRemove(new TaskAdapter.OnItemClick() {
            @Override
            public void click(Task task, int position, View view) {
                remove(task);
            }
        });
        adapter.setOnDetail(new TaskAdapter.OnItemClick() {
            @Override
            public void click(Task task, int position, View view) {
                navigator.startTaskDetailActivity(task);
            }
        });
        adapter.setDataSet(dao.listAll());
    }

    private void remove(Task task) {

    }

    private void toggleCompleted(Task task) {

    }

    public void setNavigator(TaskNavigator navigator) {
        this.navigator = navigator;
    }

    public Task getTask() {
        return task.getValue();
    }

    public void add() {
        Log.e("task", getTask().date.toString());
    }

    public void setUser(User user) {
        getTask().idUser = user.id;
        this.user = user;
    }

}
