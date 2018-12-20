package ca.babypassport.babypassport.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import ca.babypassport.babypassport.adapter.LogCursorAdapter;
import ca.babypassport.babypassport.contract.BabyPassportContract.Log;
import ca.babypassport.babypassport.dialog.ConfirmationDialogFragment;
import ca.babypassport.babypassport.dialog.ConfirmationDialogFragment.OnConfirmationDialogListener;
import ca.hajjar.babypassport.R;

public class LogFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener, OnConfirmationDialogListener {

    private static final String TAG = LogFragment.class.getName();
    private LogCursorAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.setHasOptionsMenu(true);
        mAdapter = new LogCursorAdapter(getActivity(), R.layout.log_item, null, new String[]{Log.COLUMN_NAME_DATE, Log.COLUMN_NAME_TIME, Log.COLUMN_NAME_TYPE}, new int[]{R.id.log_date, R.id.log_time, R.id.log_icon}, 0);
        getListView().setOnItemClickListener(this);

        setListAdapter(mAdapter);
        Bundle bundle = getActivity().getIntent().getExtras();
        getLoaderManager().initLoader(0, bundle, this);
    }


    static final String[] LOG_SUMMARY_PROJECTION = new String[]{
            Log._ID,
            Log.COLUMN_NAME_TYPE,
            Log.COLUMN_NAME_DATE,
            Log.COLUMN_NAME_TIME,
            Log.COLUMN_NAME_DATETIME,
            Log.COLUMN_NAME_BABY_ID
    };

    static final String SELECT = Log.COLUMN_NAME_BABY_ID + " = ?";
    private long babyId;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // TODO Auto-generated method stub
        Uri baseUri;

        baseUri = Log.CONTENT_URI;

        babyId = args.getLong("baby_id");
        return new CursorLoader(getActivity(), baseUri,
                LOG_SUMMARY_PROJECTION, SELECT, new String[]{Long.toString(babyId)},
                Log.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // TODO Auto-generated method stub
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
        mAdapter.swapCursor(null);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        android.util.Log.v(TAG, "called options menu");

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.logs_actions, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        android.util.Log.v(TAG, "item selected");
        switch (item.getItemId()) {
            case R.id.add_log:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                AddLogFragment newFragment = new AddLogFragment();
                Bundle bundle = getActivity().getIntent().getExtras();
                if (bundle != null) {
                    newFragment.setArguments(bundle);
                }
                newFragment.show(fragmentManager, "addLogDialog");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	

	
	/*private void insertLog(String type){
		ContentValues mNewValues  = new ContentValues();
		
		mNewValues.put(Log.COLUMN_NAME_BABY_ID, babyId);
		Calendar c = Calendar.getInstance();
		mNewValues.put(Log.COLUMN_NAME_TYPE, type);
		long utc = System.currentTimeMillis();
		
		String dateFormat = "MMM dd, yyyy";
		String timeFormat = "hh:mm aa";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,getResources().getConfiguration().locale);
		sdf.setTimeZone(TimeZone.getDefault());
		mNewValues.put(Log.COLUMN_NAME_DATE,sdf.format(new Date(utc)) );
		
		sdf = new SimpleDateFormat(timeFormat,getResources().getConfiguration().locale);
		mNewValues.put(Log.COLUMN_NAME_TIME, sdf.format(new Date(utc)));
		
		mNewValues.put(Log.COLUMN_NAME_DATETIME, utc);
		this.getActivity().getContentResolver().insert(Log.CONTENT_URI, mNewValues);
	}*/


    protected void deleteLogItem(long id, Activity activity) {

        String WHERE = Log._ID + " = ? ";
        int rows = activity.getContentResolver().delete(Log.CONTENT_URI, WHERE, new String[]{Long.toString(id)});
        if (rows > 0) {
            Toast.makeText(activity, "Log Deleted", Toast.LENGTH_LONG).show();
        }
    }

    private Button button = null;
    private long itemId;

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
        itemId = id;
        if (button != null) {
            button.setVisibility(Button.GONE);
        }
        button = view.findViewById(R.id.log_delete);
        if (button != null) {
            button.setVisibility(Button.VISIBLE);
            button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ConfirmationDialogFragment fragment = ConfirmationDialogFragment.newInstance(R.string.delete_log_title, R.string.delete_logs_msg);
                    fragment.setOnConfirmationDialogListener(LogFragment.this);

                    fragment.show(getFragmentManager(), "Delete Log Item");
                    button.setVisibility(Button.GONE);

                }

            });
        }

    }

    @Override
    public void onPositiveClick(DialogInterface dialog, Activity activity) {
        // TODO Auto-generated method stub
        deleteLogItem(itemId, activity);


        dialog.dismiss();
    }

    @Override
    public void onNegativeClick(DialogInterface dialog) {
        // TODO Auto-generated method stub
        dialog.dismiss();
    }


}
