package com.example.differential_equation_grapher.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.inputmethodservice.KeyboardView;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.example.differential_equation_grapher.R;

//from https://stackoverflow.com/questions/10903754/input-text-dialog-android
//all instances of this class use the view in dialog_input_sender_number.xml
//use android:inputType="textFilter" in the editText section of the xml to disable auto correct
public class InputPopUpPrompt extends AlertDialog.Builder {

    public interface InputSenderDialogListener{
        void onOK(String stringInBox);
        void onCancel(String stringInBox);
    }

    private EditText mNumberEdit;
    private Activity activity;
    CustomKeyboard mCustomKeyboard;
    KeyboardView keyboardView;

    public InputPopUpPrompt(Activity activity, int keyboardViewId, String titleText, final InputSenderDialogListener listener) {
        super( new ContextThemeWrapper(activity, R.style.AppTheme) );

        this.activity = activity;
        this.keyboardView = (KeyboardView) activity.findViewById(keyboardViewId);

        @SuppressLint("InflateParams") // It's OK to use NULL in an AlertDialog it seems...
        View dialogLayout = LayoutInflater.from(activity).inflate(R.layout.dialog_input_sender_number, null);
        setView(dialogLayout);

        mNumberEdit = dialogLayout.findViewById(R.id.numberEdit);

        //set title
        TextView titleTextView = dialogLayout.findViewById(R.id.inputPromptTitle);
        titleTextView.setText(titleText);

        setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if( listener != null )
                    listener.onOK(String.valueOf(mNumberEdit.getText()));

            }
        });

        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if( listener != null )
                    listener.onCancel(String.valueOf(mNumberEdit.getText()));
            }
        });

        //set keyboard
        mCustomKeyboard = new CustomKeyboard(activity, keyboardView, R.layout.math_keyboard,MathExtraKeyCodeArchive.getInstance());
        mCustomKeyboard.registerEditText(mNumberEdit);
    }

    @Override
    public AlertDialog show() {
        AlertDialog dialog = super.show();
        //need to show keyboard
//        mCustomKeyboard.showCustomKeyboard();

//        mNumberEdit.requestFocus();
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        //this was here originally
//        Window window = dialog.getWindow();
//        if( window != null )
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }
}