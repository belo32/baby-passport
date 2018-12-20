package ca.babypassport.babypassport.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private int id;
    private String date;
    String format = "MMM dd, yyyy";
    SimpleDateFormat sdf;

    static DatePickerFragment newInstance(int id, String date) {
        DatePickerFragment f = new DatePickerFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("date", date);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        sdf = new SimpleDateFormat(format, getResources().getConfiguration().locale);
        sdf.setTimeZone(TimeZone.getDefault());
        date = getArguments().getString("date");
        if (date != null) {
            try {
                Date dateobj = sdf.parse(date);
                c.setTimeInMillis(dateobj.getTime());
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        id = getArguments().getInt("id");
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);

        Spinner dateSpinner = getActivity().findViewById(id);
        if (dateSpinner == null) {
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{sdf.format(new Date(date.getTimeInMillis()))});

        dateSpinner.setAdapter(adapter);
    }
}
