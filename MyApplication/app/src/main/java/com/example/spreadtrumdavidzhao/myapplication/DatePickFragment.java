package com.example.spreadtrumdavidzhao.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-8.
 */
public class DatePickFragment extends DialogFragment {

    public static final String EXTRA_DATE = "test_date";
    private Date mdate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mdate = (Date) getArguments().getSerializable(EXTRA_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mdate);
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = getActivity()
                .getLayoutInflater().inflate(R.layout.dialog_date,null);

        DatePicker datePicker = (DatePicker)view.findViewById(R.id.dialog_date_picker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mdate = new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime();
                getArguments().putSerializable(EXTRA_DATE,mdate);
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setTitle("Date")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                } )
                .create();
    }

    public static DatePickFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATE,date);
        DatePickFragment datePickFragment = new DatePickFragment();
        datePickFragment.setArguments(bundle);

        return datePickFragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() != null) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATE,mdate);
            getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
        }
    }
}
