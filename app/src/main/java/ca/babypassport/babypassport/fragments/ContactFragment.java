package ca.babypassport.babypassport.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ca.babypassport.babypassport.contract.BabyPassportContract.Contact;
import ca.babypassport.babypassport.dialog.ConfirmationDialogFragment;
import ca.babypassport.babypassport.dialog.ConfirmationDialogFragment.OnConfirmationDialogListener;
import ca.hajjar.babypassport.R;

public class ContactFragment extends DialogFragment implements OnClickListener, OnConfirmationDialogListener {

    private long contactId;
    private static final String TAG = ContactFragment.class.getCanonicalName();

    private static final String[] CONTACT_SUMMARY_PROJECTION = new String[]{
            Contact.COLUMN_NAME_ADDRESS,
            Contact.COLUMN_NAME_BABY_ID,
            Contact.COLUMN_NAME_CONTACT_NAME,
            Contact.COLUMN_NAME_PHONE,
            Contact.COLUMN_NAME_TYPE,
            Contact._ID
    };

    private static final String SELECT = Contact._ID + " = ?";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        Bundle bundle = getArguments();

        if (bundle != null) {
            Log.d(TAG, "contact id " + bundle.getLong("contact_id"));
            contactId = bundle.getLong("contact_id", -1);
        }


        View root = inflater.inflate(R.layout.fragment_contact, container, false);
        if (contactId != -1) {
            Cursor cursor = getActivity().getContentResolver().query(Contact.CONTENT_URI, CONTACT_SUMMARY_PROJECTION, SELECT, new String[]{Long.toString(contactId)}, null);
            populateView(cursor, root);
        }
        Button button = root.findViewById(R.id.contact_delete);
        button.setOnClickListener(this);

        button = root.findViewById(R.id.contact_edit);
        button.setOnClickListener(this);

        return root;
    }

    private void populateView(Cursor cursor, View root) {
        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return;
        }
        TextView textView;
        int index;

        index = cursor.getColumnIndex(Contact.COLUMN_NAME_CONTACT_NAME);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.contact_name);
            textView.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(Contact.COLUMN_NAME_TYPE);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.contact_type);
            textView.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(Contact.COLUMN_NAME_ADDRESS);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.contact_address);
            textView.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(Contact.COLUMN_NAME_PHONE);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.contact_phone);
            textView.setText(cursor.getString(index));
        }
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
            case R.id.contact_delete:
                dismiss();
                ConfirmationDialogFragment fragment = ConfirmationDialogFragment.newInstance(R.string.delete_contact_title, R.string.delete_contact_msg);
                fragment.setOnConfirmationDialogListener(this);
                fragment.show(getFragmentManager(), "Delete Contact Dialog");


                break;
            case R.id.contact_edit:
                dismiss();
                editContact();

                break;

        }
    }

    private String WHERE = Contact._ID + " = ?";

    private void deleteContact(Activity activity) {

        int rows = activity.getContentResolver().delete(Contact.CONTENT_URI, WHERE, new String[]{Long.toString(contactId)});
        if (rows > 0) {
            Toast.makeText(activity, "Contact Deleted", Toast.LENGTH_LONG).show();
        }
    }

    private void editContact() {
        EditContactFragment editContactFragment = new EditContactFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        //Bundle bundle = getActivity().getIntent().getExtras();
        Bundle bundle = getArguments();
        bundle.putLong("contact_id", contactId);
        editContactFragment.setArguments(bundle);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack

        transaction.add(android.R.id.content, editContactFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onPositiveClick(DialogInterface dialog, Activity activity) {
        // TODO Auto-generated method stub
        dialog.dismiss();
        deleteContact(activity);
    }

    @Override
    public void onNegativeClick(DialogInterface dialog) {
        // TODO Auto-generated method stub
        dialog.dismiss();
    }


}
