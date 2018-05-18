package robhawk.com.br.orm_example.ui.custom;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

import robhawk.com.br.orm_example.util.DateUtil;

public class CalendarView extends AppCompatButton {

    private DateDialog mDatePickerDialog;
    private Date date;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setText(DateUtil.formatPtBrNow());
        setDate(DateUtil.now());

        final FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

        mDatePickerDialog = new DateDialog();
        mDatePickerDialog.setOnDateChangedListener(new DateDialog.OnDateChangedListener() {
            @Override
            public void dateChanged(Calendar date) {
                setText(DateUtil.formatPtBr(date.getTime()));
                setDate(date.getTime());
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatePickerDialog.show(fragmentManager, "calendar");
            }
        });
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
