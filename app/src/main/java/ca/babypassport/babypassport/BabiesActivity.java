package ca.babypassport.babypassport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import ca.babypassport.babypassport.fragments.BabiesFragment.OnBabySelectedListener;
import ca.hajjar.babypassport.R;

public class BabiesActivity extends FragmentActivity implements OnBabySelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babies);

    }

    @Override
    public void onBabySelected(long id) {
        Intent showBabyInfo = new Intent(getApplicationContext(), BabyActivity.class);
        showBabyInfo.putExtra("baby_id", id);
        startActivity(showBabyInfo);
    }
}
