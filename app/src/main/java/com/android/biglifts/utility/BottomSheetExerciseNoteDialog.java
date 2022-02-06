package com.android.biglifts.utility;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.android.biglifts.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetExerciseNoteDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    // Constants
    private static final String TAG = "BottomSheetExerciseNoteDialog";
    private static final String EXTRA_EXERCISE_NOTE = "com.android.biglifts.EXTRA_EXERCISE_NOTE";

    // UI
    private ImageButton imgbtn_editConfirm;
    private TextView tv_header, tv_mode;
    private EditText et_note;

    // Variables
    private BottomSheetExerciseNoteListener mListener;
    private String mCurrentNote;
    private String mNewNote;
    private boolean mIsEditable = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_exercise_notes, container, false);

        initUI(view);
        setListeners();

        mCurrentNote = this.getArguments().getString(EXTRA_EXERCISE_NOTE);
        if (mCurrentNote == null) {
            mCurrentNote = "";
        }
        et_note.setText(mCurrentNote);

        return view;
    }

    private void setListeners() {
        imgbtn_editConfirm.setOnClickListener(this);
    }

    private void initUI(View view) {
        imgbtn_editConfirm = view.findViewById(R.id.bottom_sheet_exercise_notes_imgbtn_editConfirm);
        tv_header = view.findViewById(R.id.bottom_sheet_exercise_notes_tv_header);
        tv_mode = view.findViewById(R.id.bottom_sheet_exercise_notes_tv_mode);
        et_note = view.findViewById(R.id.bottom_sheet_exercise_notes_et_editNote);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_sheet_exercise_notes_imgbtn_editConfirm:
                if (mIsEditable) {
                    mNewNote = et_note.getText().toString();
                    if (isNewNoteValid()) {
                        tv_mode.setText("Note");
                        imgbtn_editConfirm.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_pen_24));
                        setEditable(false);
                        mListener.onButtonInBottomSheetExerciseNoteListenerClicked(mNewNote);
                    }
                    else {
                        Toast.makeText(getContext(), "Make a change first", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    tv_mode.setText("Editing");
                    imgbtn_editConfirm.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_save_24));
                    setEditable(true);
                }
                break;
        }
    }

    private void setEditable (boolean isEnabled) {
        mIsEditable = isEnabled;
        et_note.setFocusable(isEnabled);
        et_note.setClickable(isEnabled);
        et_note.setCursorVisible(isEnabled);
        et_note.setFocusableInTouchMode(isEnabled);
    }

    private boolean isNewNoteValid() {
        boolean isValid = true;
        if (mCurrentNote.equals(mNewNote)) {
            isValid = false;
        }
        return isValid;
    }

    public interface BottomSheetExerciseNoteListener {
        void onButtonInBottomSheetExerciseNoteListenerClicked(String updatedNote);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetExerciseNoteListener) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetExerciseNoteListener");
        }
    }
}
