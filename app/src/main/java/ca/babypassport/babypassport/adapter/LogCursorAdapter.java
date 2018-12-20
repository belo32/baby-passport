package ca.babypassport.babypassport.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ca.babypassport.babypassport.contract.BabyPassportContract.Log;
import ca.hajjar.babypassport.R;

public class LogCursorAdapter extends SimpleCursorAdapter {

    private final LayoutInflater inflater;
    private int layout;

    public LogCursorAdapter(Context context, int layout, Cursor c,
                            String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        // TODO Auto-generated constructor stub
        this.inflater = LayoutInflater.from(context);
        this.layout = layout;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        super.bindView(view, context, cursor);

        ImageView logIcon = view.findViewById(R.id.log_icon);
        TextView logDate = view.findViewById(R.id.log_date);
        TextView logTime = view.findViewById(R.id.log_time);

        int index;
        index = cursor.getColumnIndex(Log.COLUMN_NAME_DATE);
        if (index != -1 && !cursor.isNull(index)) {
            logDate.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(Log.COLUMN_NAME_TIME);
        if (index != -1 && !cursor.isNull(index)) {
            logTime.setText(cursor.getString(index));
        }

        index = cursor.getColumnIndex(Log.COLUMN_NAME_TYPE);
        if (index != -1 && !cursor.isNull(index)) {
            String type = cursor.getString(index);
            if (type.compareTo(context.getString(R.string.log_type_bowel)) == 0) {
                logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.poo));

                logIcon.setBackgroundResource(R.drawable.poo_icon_circle);

            } else if (type.compareTo(context.getString(R.string.log_type_feed)) == 0) {
                logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.bottle));
                logIcon.setBackgroundResource(R.drawable.bottle_icon_circle);
            } else if (type.compareTo(context.getString(R.string.log_type_wet)) == 0) {
                logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.water));
                logIcon.setBackgroundResource(R.drawable.wet_icon_circle);
            }
        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return this.inflater.inflate(this.layout, null);
    }


}
