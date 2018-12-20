package ca.babypassport.babypassport.fragments;

import android.app.ActionBar;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import ca.babypassport.babypassport.contract.BabyPassportContract.BabyInfo;
import ca.hajjar.babypassport.R;

public class BabiesFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = BabiesFragment.class.getName();
    private SimpleCursorAdapter mAdapter;

    public interface OnBabySelectedListener {
        void onBabySelected(long id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setHasOptionsMenu(true);

        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.babies_item, null, new String[]{BabyInfo.COLUMN_NAME_BABY_NAME}, new int[]{R.id.baby_name_list_item}, 0);

        setListAdapter(mAdapter);


        getLoaderManager().initLoader(0, null, this);


    }

    @SuppressWarnings("unused")
    private boolean isFirstBaby() {
        Cursor cursor = getActivity().getContentResolver().query(BabyInfo.CONTENT_URI, BABY_INFO_SUMMARY_PROJECTION, null, null, null);
        return cursor != null && cursor.getCount() < 1;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        android.util.Log.v(TAG, "called options menu");

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(R.string.app_name);
        actionBar.setIcon(R.drawable.ic_launcher);

    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setIcon(R.drawable.ic_launcher);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        android.util.Log.v(TAG, "item selected");
        switch (item.getItemId()) {
            case R.id.action_new_profile:
                EditBabyInfoFragment newProfileFragment = new EditBabyInfoFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(android.R.id.content, newProfileFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static final String[] BABY_INFO_SUMMARY_PROJECTION = new String[]{
            BabyInfo._ID,
            BabyInfo.COLUMN_NAME_BABY_NAME
    };

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        Uri baseUri;

        baseUri = BabyInfo.CONTENT_URI;

        return new CursorLoader(getActivity(), baseUri,
                BABY_INFO_SUMMARY_PROJECTION, null, null,
                BabyInfo.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapter.swapCursor(null);

    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        OnBabySelectedListener babySelectedListener = (OnBabySelectedListener) getActivity();
        babySelectedListener.onBabySelected(id);

    }
}
