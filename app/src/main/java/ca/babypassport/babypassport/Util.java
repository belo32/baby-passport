package ca.babypassport.babypassport;


import android.content.Context;
import android.widget.EditText;

import ca.hajjar.babypassport.R;

public class Util {

    public static boolean validateEditText(Context context, EditText editText) {
        boolean isValid = true;


        String text = editText.getText().toString();
        editText.setError(null);
        if (text == null || text.trim().isEmpty()) {
            editText.setError(context.getString(R.string.error_name_required));
            isValid = false;
        }
        return isValid;
    }
}
