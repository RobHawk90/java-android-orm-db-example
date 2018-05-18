package robhawk.com.br.orm_example.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import robhawk.com.br.orm_example.R;
import robhawk.com.br.orm_example.data.dao.TaskDao;
import robhawk.com.br.orm_example.data.dao.UserDao;
import robhawk.com.br.orm_example.data.model.Task;
import robhawk.com.br.orm_example.orm.reflection.DaoFactory;
import robhawk.com.br.orm_example.ui.custom.DateDialog;
import robhawk.com.br.orm_example.util.DateUtil;

public class TaskDetailActivity extends AppCompatActivity {

    private DateDialog mDatePickerDialog;
    private Button mDate;
    private EditText mDescription;
    private CheckBox mCompleted;
    private Task mTask;
    private UserDao mUserDao;
    private TaskDao mTaskDao;
    private TextView mUser;

    public static Intent getIntent(TaskActivity context, Task task) {
        Intent intent = new Intent(context, TaskDetailActivity.class);
        intent.putExtra("task", task);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        initData();
        initViews();
        setupViews();
    }

    private void initData() {
        mTask = (Task) getIntent().getSerializableExtra("task");
        mUserDao = DaoFactory.create(UserDao.class);
        mTaskDao = DaoFactory.create(TaskDao.class);
    }

    private void initViews() {
        mDate = findViewById(R.id.activity_task_detail_date);
        mDescription = findViewById(R.id.activity_task_detail_description);
        mCompleted = findViewById(R.id.activity_task_detail_completed);
        mUser = findViewById(R.id.activity_task_detail_user);
        initDatePickerDialog();
    }

    private void setupViews() {
        mDate.setText(DateUtil.formatPtBr(mTask.date));
        mDescription.setText(mTask.description);
        mCompleted.setChecked(mTask.completed);
        mUser.setText(mUserDao.findById(mTask.idUser).name);
    }

    public void showDatePicker(View view) {
        mDatePickerDialog.show(getSupportFragmentManager(), "datePickerDialog");
    }

    public void edit(View view) {
        mTask.date = DateUtil.parsePtBr(mDate.getText().toString());
        mTask.completed = mCompleted.isChecked();
        mTask.description = mDescription.getText().toString();

        if (mTaskDao.update(mTask)) {
            Toast.makeText(this, "'" + mTask.description + "' was updated", Toast.LENGTH_SHORT).show();
            finish();
        } else
            Toast.makeText(this, "Can't update task '" + mTask.description + "'", Toast.LENGTH_SHORT).show();
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
}
