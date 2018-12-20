package ca.babypassport.babypassport.fragments;

import android.app.ActionBar;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import ca.babypassport.babypassport.Util;
import ca.babypassport.babypassport.contract.BabyPassportContract.Contact;
import ca.hajjar.babypassport.R;

public class EditContactFragment extends Fragment {

    private static final String TAG = EditContactFragment.class.getCanonicalName();

    private boolean isAnUpdate = false;
    private static final String[] CONTACT_SUMMARY_PROJECTION = new String[]{
            Contact._ID,
            Contact.COLUMN_NAME_BABY_ID,
            Contact.COLUMN_NAME_CONTACT_NAME,
            Contact.COLUMN_NAME_TYPE,
            Contact.COLUMN_NAME_PHONE,
            Contact.COLUMN_NAME_ADDRESS
    };

    private static final String SELECT = Contact._ID + " = ?";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.setHasOptionsMenu(true);
    }

    private long babyId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_edit,
                container, false);


        Bundle bundle = getArguments();
        if (bundle != null) {
            babyId = bundle.getLong("baby_id", -1);
            contactId = bundle.getLong("contact_id", -1);
            Log.d(TAG, "baby id : " + babyId);
        }
        if (contactId != -1) {
            isAnUpdate = true;
            Cursor cursor = getActivity().getContentResolver().query(Contact.CONTENT_URI, CONTACT_SUMMARY_PROJECTION, SELECT, new String[]{Long.toString(contactId)}, null);
            populateView(cursor, rootView);
        }

        return rootView;
    }

    private long contactId = -1;

    private void populateView(Cursor cursor, View root) {
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return;
        }

        EditText editText;
        int index;

        index = cursor.getColumnIndex(Contact.COLUMN_NAME_CONTACT_NAME);
        if (index != -1 && !cursor.isNull(index)) {
            editText = root.findViewById(R.id.contact_name_edit);
            editText.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(Contact.COLUMN_NAME_TYPE);
        if (index != -1 && !cursor.isNull(index)) {
            RadioGroup radioGroup = root.findViewById(R.id.contact_type_edit);
            String type = cursor.getString(index);

            if (type.compareTo(getString(R.string.contact_type_doctor)) == 0) {
                radioGroup.check(R.id.contact_type_doctor);
            } else if (type.compareTo(getString(R.string.contact_type_midwife)) == 0) {
                radioGroup.check(R.id.contact_type_midwife);
            } else if (type.compareTo(getString(R.string.contact_type_other)) == 0) {
                radioGroup.check(R.id.contact_type_other);
            } else if (type.compareTo(getString(R.string.contact_type_paediatrician)) == 0) {
                radioGroup.check(R.id.contact_type_paediatrician);
            }
        }

        index = cursor.getColumnIndex(Contact.COLUMN_NAME_ADDRESS);
        if (index != -1 && !cursor.isNull(index)) {
            editText = root.findViewById(R.id.contact_address_edit);
            editText.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(Contact.COLUMN_NAME_PHONE);
        if (index != -1 && !cursor.isNull(index)) {
            editText = root.findViewById(R.id.contact_phone_edit);
            editText.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(Contact._ID);
        if (index != -1 && !cursor.isNull(index)) {
            contactId = cursor.getLong(index);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v(TAG, "called options menu");
        menu.clear();

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
        Log.v(TAG, "item selected");
        switch (item.getItemId()) {
            case android.R.id.home:
                insertContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void insertContact() {
        boolean isValid = true;
        ContentValues mNewValues = new ContentValues();

        EditText editText = getActivity().findViewById(R.id.contact_name_edit);
        if (editText != null) {
            editText.setError(null);
            if (isValid = Util.validateEditText(getActivity(), editText)) {
                mNewValues.put(Contact.COLUMN_NAME_CONTACT_NAME, editText.getText().toString().trim());
            }
        }
        RadioGroup radioGroup = getActivity().findViewById(R.id.contact_type_edit);
        if (radioGroup != null) {
            String type = null;
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.contact_type_doctor:
                    type = getString(R.string.contact_type_doctor);
                    break;
                case R.id.contact_type_midwife:
                    type = getString(R.string.contact_type_midwife);
                    break;
                case R.id.contact_type_other:
                    type = getString(R.string.contact_type_other);
                    break;
                case R.id.contact_type_paediatrician:
                    type = getString(R.string.contact_type_paediatrician);
                    break;

            }
            if (type != null) {
                mNewValues.put(Contact.COLUMN_NAME_TYPE, type);
            }
        }

        editText = getActivity().findViewById(R.id.contact_phone_edit);
        if (editText != null && !editText.getText().toString().trim().isEmpty()) {
            mNewValues.put(Contact.COLUMN_NAME_PHONE, editText.getText().toString().trim());
        }

        editText = getActivity().findViewById(R.id.contact_address_edit);
        if (editText != null && !editText.getText().toString().trim().isEmpty()) {
            mNewValues.put(Contact.COLUMN_NAME_ADDRESS, editText.getText().toString().trim());
        }

        mNewValues.put(Contact.COLUMN_NAME_BABY_ID, Long.toString(babyId));

        if (isValid && !isAnUpdate) {
            this.getActivity().getContentResolver().insert(Contact.CONTENT_URI, mNewValues);
            Toast.makeText(getActivity(), "Contact Saved", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();
        } else if (isValid && isAnUpdate) {
            int rows = this.getActivity().getContentResolver().update(Contact.CONTENT_URI, mNewValues, SELECT, new String[]{Long.toString(contactId)});
            if (rows > 0) {
                Toast.makeText(getActivity(), "Contact Updated", Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }


}
