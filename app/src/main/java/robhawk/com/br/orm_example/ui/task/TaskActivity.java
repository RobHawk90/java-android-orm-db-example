
package robhawk.com.br.orm_example.ui.task;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import robhawk.com.br.orm_example.R;
import robhawk.com.br.orm_example.data.model.Task;
import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.databinding.ActivityTaskBinding;

public class TaskActivity extends AppCompatActivity implements TaskNavigator {

    private ActivityTaskBinding mBinding;
    private TaskViewModel mVM;

    public static Intent getIntent(Context context, User user) {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra("user", user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind();
        setup();
    }

    private void bind() {
        mVM = ViewModelProviders.of(this).get(TaskViewModel.class);
        mVM.setNavigator(this);
        mVM.setUser(getIntentData());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_task);
        mBinding.setVm(mVM);
    }

    private User getIntentData() {
        return (User) getIntent().getSerializableExtra("user");
    }

    private void setup() {
        registerForContextMenu(mBinding.list);
    }

    public void startTaskDetailActivity(Task task) {
        Intent taskDetail = TaskDetailActivity.getIntent(this, task);
        startActivity(taskDetail);
    }

    private void remove(Task task) {
//        if (mTaskDao.delete(task)) {
//            mAdapter.remove(task);
//            Toast.makeText(this, "Task '" + task.description + "' was removed", Toast.LENGTH_LONG).show();
//        } else
//            Toast.makeText(this, "Can't remove the '" + task.description + "' task", Toast.LENGTH_LONG).show();
    }

    private void toggleCompleted(Task task) {
//        task.completed = !task.completed;
//        if (!mTaskDao.update(task)) {
//            Toast.makeText(this, "Can't flag '" + task.description + "' task as " + (task.completed ? "completed" : "uncompleted"), Toast.LENGTH_SHORT).show();
//            task.completed = !task.completed;
//            mAdapter.notifyDataSetChanged();
//        }
    }

    public void add(View view) {
//        mTask.date = DateUtil.parsePtBr(mDate.getText().toString());
//
//        if (mTaskDao.insert(mTask)) {
//            mAdapter.add(mTask);
//
//            mTask = new Task();
//            mTask.idUser = mUser.id;
//
//            mDate.setText(DateUtil.formatPtBrNow());
//            mDescription.setText("");
//            mDescription.requestFocus();
//
//            Toast.makeText(this, "'" + mTask.description + "' was added", Toast.LENGTH_SHORT).show();
//        } else
//            Toast.makeText(this, "Can't add task '" + mTask.description + "'", Toast.LENGTH_SHORT).show();
    }
}
