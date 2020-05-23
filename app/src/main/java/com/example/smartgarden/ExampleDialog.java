package com.example.smartgarden;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import org.json.JSONException;
import java.io.IOException;

public class ExampleDialog extends AppCompatDialogFragment {
    private ExampleDialogListerner listener ;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Attention !")
                .setMessage("Do you want to delete this plant ?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            listener.onYesClicked();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return builder.create();
    }
    public interface ExampleDialogListerner {
        void onYesClicked() throws JSONException, IOException;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListerner) context;
        } catch(ClassCastException e){
            throw new ClassCastException(context.toString()
                    + "must implement exaple dialog");
        }

    }
}
