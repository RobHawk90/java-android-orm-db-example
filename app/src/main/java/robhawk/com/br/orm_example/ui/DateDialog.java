package robhawk.com.br.orm_example.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class DateDialog extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDateChangedListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar now = Calendar.getInstance();
        int year = now.get(YEAR);
        int month = now.get(MONTH);
        int day = now.get(DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker picker, int year, int month, int day) {
        Calendar selected = Calendar.getInstance();
        selected.set(YEAR, year);
        selected.set(MONTH, month);
        selected.set(DAY_OF_MONTH, day);
        if (listener != null)
            listener.dateChanged(selected);
    }

    public void setOnDateChangedListener(OnDateChangedListener listener) {
        this.listener = listener;
    }

    interface OnDateChangedListener {
        void dateChanged(Calendar calendar);
    }
}
