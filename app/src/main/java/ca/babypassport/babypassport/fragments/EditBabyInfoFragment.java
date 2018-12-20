package ca.babypassport.babypassport.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import ca.babypassport.babypassport.BabiesActivity;
import ca.babypassport.babypassport.Util;
import ca.babypassport.babypassport.contract.BabyPassportContract.BabyInfo;
import ca.babypassport.babypassport.contract.BabyPassportContract.Contact;
import ca.babypassport.babypassport.contract.BabyPassportContract.Log;
import ca.babypassport.babypassport.dialog.ConfirmationDialogFragment;
import ca.babypassport.babypassport.dialog.ConfirmationDialogFragment.OnConfirmationDialogListener;
import ca.hajjar.babypassport.R;

public class EditBabyInfoFragment extends Fragment implements OnTouchListener, OnConfirmationDialogListener {

    private static final String TAG = EditBabyInfoFragment.class.getName();
    boolean isAnUpdate = false;
    long babyId;
    static final String[] BABY_INFO_SUMMARY_PROJECTION = new String[]{
            BabyInfo._ID,
            BabyInfo.COLUMN_NAME_BABY_NAME,
            BabyInfo.COLUMN_NAME_DOB,
            BabyInfo.COLUMN_NAME_DUE_DATE,
            BabyInfo.COLUMN_NAME_BIRTH_WEIGHT,
            BabyInfo.COLUMN_NAME_DISCHARGE_DATE,
            BabyInfo.COLUMN_NAME_DISCHARGE_WEIGHT,
            BabyInfo.COLUMN_NAME_FEEDING_TYPE
    };

    static final String SELECT = BabyInfo._ID + " = ?";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_baby_info_edit,
                container, false);


        Spinner dob = rootView.findViewById(R.id.edit_dob);
        dob.setOnTouchListener(this);

        Spinner dischargeDate = rootView.findViewById(R.id.edit_dis_date);
        dischargeDate.setOnTouchListener(this);

        Spinner dueDate = rootView.findViewById(R.id.edit_due_date);
        dueDate.setOnTouchListener(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            babyId = bundle.getLong("baby_id", -1);

            if (babyId != -1) {
                isAnUpdate = true;
                Cursor cursor = getActivity().getContentResolver().query(BabyInfo.CONTENT_URI, BABY_INFO_SUMMARY_PROJECTION, SELECT, new String[]{Long.toString(babyId)}, null);
                populateView(cursor, rootView);

            }
        }


        return rootView;
    }


    private void populateView(Cursor cursor, View root) {

        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return;
        }

        EditText textView;
        int index;

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_BABY_NAME);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.edit_name);
            textView.setText(cursor.getString(index));
        }

        Spinner spinner;
        ArrayAdapter<String> adapter;
        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_DOB);
        if (index != -1 && !cursor.isNull(index)) {
            spinner = root.findViewById(R.id.edit_dob);
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{cursor.getString(index)});
            spinner.setAdapter(adapter);
        }

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_DUE_DATE);
        if (index != -1 && !cursor.isNull(index)) {
            spinner = root.findViewById(R.id.edit_due_date);
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{cursor.getString(index)});
            spinner.setAdapter(adapter);
        }

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_BIRTH_WEIGHT);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.edit_birth_weight);
            textView.setText(Long.toString(cursor.getLong(index)));
        }

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_DISCHARGE_DATE);
        if (index != -1 && !cursor.isNull(index)) {
            spinner = root.findViewById(R.id.edit_dis_date);
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{cursor.getString(index)});
            spinner.setAdapter(adapter);
        }

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_DISCHARGE_WEIGHT);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.edit_dis_weight);
            textView.setText(Long.toString(cursor.getLong(index)));
        }

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_FEEDING_TYPE);
        if (index != -1 && !cursor.isNull(index)) {
            RadioGroup radioGroup = root.findViewById(R.id.edit_feeding_types);
            String type = cursor.getString(index);

            if (type.compareTo(getString(R.string.baby_bottle_feed_section)) == 0) {
                radioGroup.check(R.id.edit_bottle);
            } else if (type.compareTo(getString(R.string.baby_breast_feed_section)) == 0) {
                radioGroup.check(R.id.edit_breast);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        android.util.Log.v(TAG, "called options menu");

        menu.clear();
        if (isAnUpdate) {
            inflater.inflate(R.menu.delete_action, menu);
        }
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(R.string.done_title);
        actionBar.setIcon(R.drawable.ic_action_accept);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        android.util.Log.v(TAG, "item selected");
        switch (item.getItemId()) {
            case android.R.id.home:
                insertBabyInfo();
                return true;
            case R.id.delete_action:
                ConfirmationDialogFragment fragment = ConfirmationDialogFragment.newInstance(R.string.delete_title, R.string.delete_msg);
                fragment.setOnConfirmationDialogListener(this);
                fragment.show(getFragmentManager(), "Confirmation Dialog");


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean insertBabyInfo() {
        boolean isValid = true;
        ContentValues mNewValues = new ContentValues();

        EditText editText = getActivity().findViewById(R.id.edit_name);
        editText.setError(null);
        if (isValid = Util.validateEditText(getActivity(), editText)) {
            mNewValues.put(BabyInfo.COLUMN_NAME_BABY_NAME, editText.getText().toString().trim());
        }

        Spinner spinner = getActivity().findViewById(R.id.edit_dob);
        if (spinner != null && spinner.getCount() != 0) {
            mNewValues.put(BabyInfo.COLUMN_NAME_DOB, spinner.getSelectedItem().toString());
        }

        spinner = getActivity().findViewById(R.id.edit_due_date);
        if (spinner != null && spinner.getCount() != 0) {
            mNewValues.put(BabyInfo.COLUMN_NAME_DUE_DATE, spinner.getSelectedItem().toString());
        }

        editText = getActivity().findViewById(R.id.edit_birth_weight);
        if (editText != null) {
            if (!editText.getText().toString().trim().isEmpty()) {
                mNewValues.put(BabyInfo.COLUMN_NAME_BIRTH_WEIGHT, editText.getText().toString());
            }
        }

        spinner = getActivity().findViewById(R.id.edit_dis_date);
        if (spinner != null && spinner.getCount() != 0) {
            mNewValues.put(BabyInfo.COLUMN_NAME_DISCHARGE_DATE, spinner.getSelectedItem().toString());
        }


        editText = getActivity().findViewById(R.id.edit_dis_weight);
        if (editText != null) {
            if (!editText.getText().toString().trim().isEmpty()) {
                mNewValues.put(BabyInfo.COLUMN_NAME_DISCHARGE_WEIGHT, editText.getText().toString());
            }
        }

        RadioGroup radioGroup = getActivity().findViewById(R.id.edit_feeding_types);
        if (radioGroup != null) {
            String type = null;
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.edit_bottle:
                    type = getString(R.string.baby_bottle_feed_section);
                    break;
                case R.id.edit_breast:
                    type = getString(R.string.baby_breast_feed_section);
                    break;
            }
            if (type != null) {
                mNewValues.put(BabyInfo.COLUMN_NAME_FEEDING_TYPE, type);
            }
        }
        if (isValid && !isAnUpdate) {
            this.getActivity().getContentResolver().insert(BabyInfo.CONTENT_URI, mNewValues);
            Toast.makeText(getActivity(), "Baby Profile Saved", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();

        } else if (isValid && isAnUpdate) {

            int rows = this.getActivity().getContentResolver().update(BabyInfo.CONTENT_URI, mNewValues, WHERE, new String[]{Long.toString(babyId)});
            if (rows > 0) {
                Toast.makeText(getActivity(), "Baby Profile Saved", Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }

        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return isValid;


    }

    private static final String WHERE = BabyInfo._ID + " = ?";


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Object object = ((Spinner) v).getSelectedItem();
            String date = null;
            if (object != null) {
                date = object.toString();
            }
            //Log.d(TAG,((Spinner)v).getSelectedItem().toString());
            DialogFragment newFragment = DatePickerFragment.newInstance(v.getId(), date);
            newFragment.show(getFragmentManager(), "datePicker");
            return true;
        }
        return false;
    }

    private void deleteBaby(Activity activity) {


        if (babyId == -1) {
            return;
        }
        String[] id = new String[]{Long.toString(babyId)};
        activity.getContentResolver().delete(Contact.CONTENT_URI, Contact.COLUMN_NAME_BABY_ID + " = ?", id);
        activity.getContentResolver().delete(Log.CONTENT_URI, Log.COLUMN_NAME_BABY_ID + " = ?", id);
        activity.getContentResolver().delete(BabyInfo.CONTENT_URI, BabyInfo._ID + " = ?", id);
        Toast.makeText(activity, "Baby Info Deleted", Toast.LENGTH_LONG).show();
        Intent showBabyInfo = new Intent(activity.getApplicationContext(), BabiesActivity.class);
        activity.startActivity(showBabyInfo);

    }

    @Override
    public void onPositiveClick(DialogInterface dialog, Activity activity) {
        // TODO Auto-generated method stub
        deleteBaby(activity);
        dialog.dismiss();
    }

    @Override
    public void onNegativeClick(DialogInterface dialog) {
        // TODO Auto-generated method stub
        dialog.dismiss();
    }

}
