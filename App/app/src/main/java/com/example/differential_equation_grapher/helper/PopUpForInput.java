package com.example.differential_equation_grapher.helper;

import android.app.Activity;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PopUpForInput {
    private Activity activity;
    private View popupContainer;
    private TextView popupTextView;
    private EditText popupEditText;
    private KeyboardView keyboardView;
    private CustomKeyboard customKeyboard;

    public PopUpForInput(Activity activity,View popupContainer, TextView popupTextView, EditText popupEditText,CustomKeyboard customKeyboard) {
        this.popupContainer = popupContainer;
        this.popupTextView = popupTextView;
        this.popupEditText = popupEditText;

        popupContainer.bringToFront();

        popupContainer.setVisibility(View.GONE);
        popupTextView.setVisibility(View.GONE);
        popupEditText.setVisibility(View.GONE);


        this.customKeyboard = customKeyboard;
        customKeyboard.registerEditText(popupEditText);
    }

    public void inputNeeded(String textForLabel,CustomKeyboard.OnInput onInput, CustomKeyboard.OnEnter onEnter) {
        popupTextView.setText(textForLabel);
        customKeyboard.setDoOnInput(onInput);
        customKeyboard.setDoOnEnter(onEnter);

        popupContainer.setVisibility(View.VISIBLE);
        popupTextView.setVisibility(View.VISIBLE);
        popupEditText.setVisibility(View.VISIBLE);

        customKeyboard.showCustomKeyboard(popupEditText);
        popupEditText.setText("");
        popupEditText.requestFocus();
    }

    public void inputFinished() {
        popupContainer.setVisibility(View.GONE);
        popupTextView.setVisibility(View.GONE);
        popupEditText.setVisibility(View.GONE);

        customKeyboard.setDoOnInput(null);
        customKeyboard.setDoOnEnter(null);
    }
}
