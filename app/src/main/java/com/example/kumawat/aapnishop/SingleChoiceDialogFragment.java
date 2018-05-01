package com.example.kumawat.aapnishop;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import java.util.List;

/**
 * Created by Kumawat on 4/24/2018.
 */

public class SingleChoiceDialogFragment extends DialogFragment{
    public static final String DATA = "items";
    public static final String SELECTED = "selected";
    private SelectionListener listener;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            this.listener = (SelectionListener)activity;
        }
        catch ( ClassCastException oops )
        {
            oops.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Resources res = getActivity().getResources();
        Bundle bundle = getArguments();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Transaction Type");
        dialog.setPositiveButton("OK", new PositiveButtonClickListener());

        List<String> list = (List<String>)bundle.get(DATA);
        int position = bundle.getInt(SELECTED);

        CharSequence[] cs = list.toArray(new CharSequence[list.size()]);
        dialog.setSingleChoiceItems(cs, position, selectItemListener);

        return dialog.create();
    }

    class PositiveButtonClickListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if ( listener != null )
            {
                listener.selectItem(0);
            }
            dialog.dismiss();
        }
    }

    DialogInterface.OnClickListener selectItemListener = new DialogInterface.OnClickListener()
    {

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            // process
            //which means position
            if ( listener != null )
            {
                listener.selectItem(which);
            }
            dialog.dismiss();
        }

    };
}
