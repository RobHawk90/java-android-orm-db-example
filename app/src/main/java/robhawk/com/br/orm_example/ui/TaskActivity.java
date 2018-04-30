
package robhawk.com.br.orm_example.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import robhawk.com.br.orm_example.R;
import robhawk.com.br.orm_example.data.dao.TaskDao;
import robhawk.com.br.orm_example.data.model.Task;
import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.util.DateUtil;

public class TaskActivity extends AppCompatActivity {

    private DateDialog mDatePickerDialog;
    private Button mDate;
    private EditText mDescription;
    private TaskDao mTaskDao;

    private User mUser;
    private TaskAdapter mAdapter;

    public static Intent getIntent(Context context, User user) {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra("user", user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initData();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setDataSet(mTaskDao.listAll());
    }

    private void initData() {
        mUser = (User) getIntent().getSerializableExtra("user");
        mTaskDao = new TaskDao();
    }

    private void initViews() {
        mDescription = findViewById(R.id.activity_task_description);
        initDate(R.id.activity_task_date);
        initList(R.id.activity_task_list);
        initDatePickerDialog();
    }

    private void initDate(int resId) {
        mDate = findViewById(resId);
        mDate.setText(DateUtil.formatPtBrNow());
    }

    private void initDatePickerDialog() {
        mDatePickerDialog = new DateDialog();
        mDatePickerDialog.setOnDateChangedListener(new DateDialog.OnDateChangedListener() {
            @Override
            public void dateChanged(Calendar date) {
                mDate.setText(DateUtil.formatPtBr(date.getTime()));
            }
        });
    }

    private void initList(int resId) {
        mAdapter = new TaskAdapter();
        mAdapter.setOnComplete(new TaskAdapter.OnItemClick() {
            @Override
            public void click(Task task, int position, View view) {
                toggleCompleted(task);
            }
        });
        mAdapter.setOnRemove(new TaskAdapter.OnItemClick() {
            @Override
            public void click(Task task, int position, View view) {
                remove(task);
            }
        });
        mAdapter.setOnDetail(new TaskAdapter.OnItemClick() {
            @Override
            public void click(Task task, int position, View view) {
                startTaskDetailActivity(task);
            }
        });

        RecyclerView list = findViewById(resId);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(mAdapter);
        registerForContextMenu(list);
    }

    private void startTaskDetailActivity(Task task) {
        Intent taskDetail = TaskDetailActivity.getIntent(this, task);
        startActivity(taskDetail);
    }

    private void remove(Task task) {
        if (mTaskDao.delete(task)) {
            mAdapter.remove(task);
            Toast.makeText(this, "Task '" + task.description + "' was removed", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, "Can't remove the '" + task.description + "' task", Toast.LENGTH_LONG).show();
    }

    private void toggleCompleted(Task task) {
        task.completed = !task.completed;
        if (!mTaskDao.update(task)) {
            Toast.makeText(this, "Can't flag '" + task.description + "' task as " + (task.completed ? "completed" : "uncompleted"), Toast.LENGTH_SHORT).show();
            task.completed = !task.completed;
            mAdapter.notifyDataSetChanged();
        }
    }

    public void showDatePicker(View view) {
        mDatePickerDialog.show(getSupportFragmentManager(), "dateDialog");
    }

    public void add(View view) {
        Date date = DateUtil.parsePtBr(mDate.getText().toString());
        String description = mDescription.getText().toString();

        Task task = new Task(description, date, mUser.id);
        if (mTaskDao.insert(task)) {
            mAdapter.add(task);

            mDate.setText(DateUtil.formatPtBrNow());
            mDescription.setText("");
            mDescription.requestFocus();

            Toast.makeText(this, "'" + description + "' was added", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Can't add task '" + description + "'", Toast.LENGTH_SHORT).show();
    }
}
