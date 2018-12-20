package ca.babypassport.babypassport.fragments;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import ca.babypassport.babypassport.contract.BabyPassportContract.Log;
import ca.hajjar.babypassport.R;

public class AddLogFragment extends DialogFragment implements OnClickListener, OnTouchListener {
    private long babyId;

    @SuppressWarnings("unused")
    private static final String TAG = AddLogFragment.class.getCanonicalName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (bundle != null) {
            babyId = bundle.getLong("baby_id", -1);
        }

        View root = inflater.inflate(R.layout.log_types, container, false);
        Button button = root.findViewById(R.id.add_bowel_type);
        button.setOnClickListener(this);

        button = root.findViewById(R.id.add_feed_type);
        button.setOnClickListener(this);

        button = root.findViewById(R.id.add_wet_type);
        button.setOnClickListener(this);

        return root;
    }

    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_feed_type:
                insertLog(getString(R.string.log_type_feed));
                break;
            case R.id.add_bowel_type:
                insertLog(getString(R.string.log_type_bowel));
                break;
            case R.id.add_wet_type:
                insertLog(getString(R.string.log_type_wet));
                break;
            default:

        }
        dismiss();
        return;
    }

    private void insertLog(String type) {
        ContentValues mNewValues = new ContentValues();

        mNewValues.put(Log.COLUMN_NAME_BABY_ID, babyId);

        mNewValues.put(Log.COLUMN_NAME_TYPE, type);
        long utc = System.currentTimeMillis();

        String dateFormat = "E MMM dd, yyyy";
        String timeFormat = "hh:mm aa";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, getResources().getConfiguration().locale);
        sdf.setTimeZone(TimeZone.getDefault());
        mNewValues.put(Log.COLUMN_NAME_DATE, sdf.format(new Date(utc)));

        sdf = new SimpleDateFormat(timeFormat, getResources().getConfiguration().locale);
        mNewValues.put(Log.COLUMN_NAME_TIME, sdf.format(new Date(utc)));

        mNewValues.put(Log.COLUMN_NAME_DATETIME, utc);
        this.getActivity().getContentResolver().insert(Log.CONTENT_URI, mNewValues);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Object object = ((Spinner) v).getSelectedItem();
            String date = null;
            if (object != null) {
                date = object.toString();
            }

            DialogFragment newFragment = DatePickerFragment.newInstance(v.getId(), date);
            newFragment.show(getFragmentManager(), "datePicker");
            return true;
        }
        return false;
    }

}
