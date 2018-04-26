
package robhawk.com.br.orm_example.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

import robhawk.com.br.orm_example.R;
import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.util.DateUtil;

public class TaskActivity extends AppCompatActivity {

    private DateDialog mDatePickerDialog;
    private Button mDate;
    private View mDescription;

    public static Intent getIntent(Context context, User user) {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra("user", user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initViews();
        mDatePickerDialog = new DateDialog();
        mDatePickerDialog.setOnDateChangedListener(new DateDialog.OnDateChangedListener() {
            @Override
            public void dateChanged(Calendar date) {
                mDate.setText(DateUtil.formatPtBr(date.getTime()));
            }
        });
    }

    private void initViews() {
        mDescription = findViewById(R.id.activity_task_description);
        mDate = findViewById(R.id.activity_task_date);
        mDate.setText(DateUtil.formatPtBrNow());
    }

    public void showDatePicker(View view) {
        mDatePickerDialog.show(getSupportFragmentManager(), "dateDialog");
    }
}
