package com.wqlabs.sgxstocks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

/**
 * Created by RadianceOng on 13-Dec-15.
 */
public class AddStockFragment extends DialogFragment {

    // Use this instance of the interface to deliver action events
    AddStockDialogListener mListener;
    AddStockAutoCompleteAdapter model;
    AutoCompleteTextView.Validator val;

    public void setModel(AddStockAutoCompleteAdapter model) {
        this.model = model;
    }

    public void setValidator(AutoCompleteTextView.Validator val) {
        this.val = val;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddStockDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    public interface AddStockDialogListener {
        boolean addCode(String code);
        boolean removeCode(String code);
    }

    private String processCode(String data) {
        int i = data.indexOf(' ');
        if(i >= 0) {
            return data.substring(0, i).toUpperCase();
        } else {
            return data.toUpperCase();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_add_stock, null);
        final AutoCompleteTextView tv = (AutoCompleteTextView) view.findViewById(R.id.stockCodeField);
        tv.setThreshold(2);
        if(model != null) {
            tv.setAdapter(model);
        }
        if(val != null) {
            tv.setValidator(new AutoCompleteTextView.Validator() {
                @Override
                public boolean isValid(CharSequence text) {
                    return val.isValid(text);
                }

                @Override
                public CharSequence fixText(CharSequence invalidText) {
                    return processCode(invalidText.toString());
                }
            });
        }
        tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv.performValidation();
            }
        });

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.action_add, null)
                .setNeutralButton(R.string.action_remove, null)
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog d =  builder.create();
        d.show();
        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // need to pass back the stock code...
                AutoCompleteTextView tv = (AutoCompleteTextView) view.findViewById(R.id.stockCodeField);
                String code = tv.getText().toString();
                if (mListener.addCode(processCode(code))) {
                    tv.setText(null);
                }

            }
        });
        d.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView tv = (AutoCompleteTextView) view.findViewById(R.id.stockCodeField);
                String code = tv.getText().toString();
                if(mListener.removeCode(processCode(code))) {
                    tv.setText(null);
                }

            }
        });

        return d;

    }
}
