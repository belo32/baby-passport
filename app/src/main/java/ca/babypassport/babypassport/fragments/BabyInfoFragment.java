package ca.babypassport.babypassport.fragments;

import android.app.ActionBar;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.babypassport.babypassport.contract.BabyPassportContract.BabyInfo;
import ca.hajjar.babypassport.R;

public class BabyInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = BabyInfoFragment.class.getName();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.setHasOptionsMenu(true);
        Bundle bundle = getActivity().getIntent().getExtras();
        getLoaderManager().initLoader(0, bundle, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_baby_info,
                container, false);


        return rootView;
    }


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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri;

        baseUri = BabyInfo.CONTENT_URI;
        long babyId = args.getLong("baby_id");
        return new CursorLoader(getActivity(), baseUri,
                BABY_INFO_SUMMARY_PROJECTION, SELECT, new String[]{Long.toString(babyId)}, BabyInfo.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        updateView(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


    }

    private void updateView(Cursor cursor) {
        View root = getView();
        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return;
        }

        TextView textView;
        int index;

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_BABY_NAME);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.baby_name);
            textView.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_DOB);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.baby_dob);
            textView.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_DUE_DATE);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.baby_due_date);
            textView.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_BIRTH_WEIGHT);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.baby_birth_weight);
            textView.setText(Long.toString(cursor.getLong(index)));
        }

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_DISCHARGE_DATE);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.baby_discharge_date);
            textView.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_DISCHARGE_WEIGHT);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.baby_discharge_weight);
            textView.setText(Long.toString(cursor.getLong(index)));
        }

        index = cursor.getColumnIndex(BabyInfo.COLUMN_NAME_FEEDING_TYPE);
        if (index != -1 && !cursor.isNull(index)) {
            textView = root.findViewById(R.id.baby_feeding_type);
            textView.setText(cursor.getString(index));
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        android.util.Log.v(TAG, "called options menu");

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.edit_action, menu);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.app_name);
        actionBar.setIcon(R.drawable.ic_launcher);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        android.util.Log.v(TAG, "item selected");
        switch (item.getItemId()) {
            case R.id.edit_baby_info_action:
                EditBabyInfoFragment editProfileFragment = new EditBabyInfoFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = getActivity().getIntent().getExtras();
                editProfileFragment.setArguments(bundle);
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack

                transaction.replace(android.R.id.content, editProfileFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commitAllowingStateLoss();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	/*public void showDialog() {
	    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
	    EditBabyInfoDialogFragment newFragment = new EditBabyInfoDialogFragment();
	    
	
	        // The device is smaller, so show the fragment fullscreen
	        FragmentTransaction transaction = fragmentManager.beginTransaction();
	        // For a little polish, specify a transition animation
	        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	        // To make it fullscreen, use the 'content' root view as the container
	        // for the fragment, which is always the root view for the activity
	        transaction.add(android.R.id.content, newFragment)
	                   .addToBackStack(null).commit();
	    }*/


}
