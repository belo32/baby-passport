package ca.babypassport.babypassport.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import ca.hajjar.babypassport.R;

public class ConfirmationDialogFragment extends DialogFragment {

	private OnConfirmationDialogListener listener;
	public interface OnConfirmationDialogListener{
		void onPositiveClick(DialogInterface dialog, Activity activity);
		void onNegativeClick(DialogInterface dialog);
	}
	
	public void setOnConfirmationDialogListener(OnConfirmationDialogListener listener){
		this.listener = listener;
	}
	public static ConfirmationDialogFragment newInstance (int titleId, int messageId){
		ConfirmationDialogFragment frag = new ConfirmationDialogFragment();
		Bundle bundle = new Bundle ();
		bundle.putInt("title_id", titleId);
		bundle.putInt("message_id", messageId);
		frag.setArguments(bundle);
		frag.setRetainInstance(true);
		return frag;
	}
	
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title_id");
        int message = getArguments().getInt("message_id");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.delete_now,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        
                            listener.onPositiveClick(dialog, getActivity());
                        }
                    }
                )
                .setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	
                        	listener.onNegativeClick(dialog);
                        }
                    }
                )
                .create();
    }
	
	@Override
	public void onDestroyView() {
	  if (getDialog() != null && getRetainInstance())
	    getDialog().setDismissMessage(null);
	  super.onDestroyView();
	}
}
