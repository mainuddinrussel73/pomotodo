package com.example.mainuddin.pomotodo.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.mainuddin.pomotodo.R;
import com.example.mainuddin.pomotodo.model.Movie;
import com.google.android.material.textfield.TextInputLayout;

public class NewMovieDialogFragment extends DialogFragment {

    public interface DialogListener {
        void onMovieCreated(Movie movie);
    }

    private DialogListener dialogListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dialogListener = (DialogListener) getTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogurDark);
        builder.setView(R.layout.fragment_dialog_movie);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(android.R.string.cancel, (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }));
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewMovieDialogFragment.this.onPositiveButtonClicked();
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void onPositiveButtonClicked() {
        final TextInputLayout tilName = getDialog().findViewById(R.id.tilName);
        final TextInputLayout tilGenre = getDialog().findViewById(R.id.tilGenre);

        if (TextUtils.isEmpty(tilName.getEditText().getText().toString())
                || TextUtils.isEmpty(tilGenre.getEditText().getText().toString())
        ) {

            tilName.setError(null);
            tilGenre.setError(null);

            if (TextUtils.isEmpty(tilName.getEditText().getText().toString())) {
                tilName.setError("Title is required");
            }
            if (TextUtils.isEmpty(tilGenre.getEditText().getText().toString())) {
                tilGenre.setError("Group is required");
            }


        } else {
            dialogListener.onMovieCreated(new Movie(tilName.getEditText().getText().toString(),
                    tilGenre.getEditText().getText().toString()));
            getDialog().dismiss();
        }
    }
}
