package com.example.differential_equation_grapher.helper;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.view.KeyboardShortcutGroup;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/*
references
https://stackoverflow.com/questions/3480715/how-to-develop-a-soft-keyboard-for-android
https://medium.com/@ssaurel/learn-to-create-a-system-keyboard-on-android-95aca21b1e5f
https://developer.android.com/reference/android/inputmethodservice/Keyboard.Key.html#attr_android:keyOutputText
 */


//copied from https://stackoverflow.com/questions/3480715/how-to-develop-a-soft-keyboard-for-android
public class CustomKeyboard {
    private static final String CLASS_NAME = "CustomKeyboard";
    /** A link to the KeyboardView that is used to render this CustomKeyboard. */
    private KeyboardView mKeyboardView;
    /** A link to the activity that hosts the {@link #mKeyboardView}. */
    private Activity mHostActivity;

    private ExtraKeyCodeArchive extraKeyCodeHandler;
    private OnEnter onEnter;//something to do when hitting enter with the option of closing the keyboard     can be null. if null, keyboard closes
    private OnInput onInput;//additional action to do when key is typed

    public void setDoOnEnter(OnEnter onEnter){
        this.onEnter = onEnter;
    }

    public void setDoOnInput(OnInput onInput){
        this.onInput = onInput;
    }

    public interface OnEnter{
        void doOnEnter(EditText editText);
        boolean closeKeyboardOnEnter();
    }
    public interface OnInput{
        void doOnInput(EditText editText);
    }

    public KeyboardView getKeyboardView(){
        return mKeyboardView;
    }

    public Keyboard.Key findKey(String label) {
        Keyboard keyboard = mKeyboardView.getKeyboard();
        for (Keyboard.Key key : keyboard.getKeys()) {
            if (key.label.equals(label)) {
                return key;
            }
        }
        return null;
    }

    /** The key (code) handler. */
    private OnKeyboardActionListener mOnKeyboardActionListener = new OnKeyboardActionListener() {

        public final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
        public final static int CodeCancel = -3; // Keyboard.KEYCODE_CANCEL
        public final static int CodePrev = 55000;
        public final static int CodeAllLeft = 55001;
        public final static int CodeLeft = 55002;
        public final static int CodeRight = 55003;
        public final static int CodeAllRight = 55004;
        public final static int CodeNext = 55005;
        public final static int CodeClear = 55006;

        public final static int Enter = 10;



        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            // NOTE We can say '<Key android:codes="49,50" ... >' in the xml
            // file; all codes come in keyCodes, the first in this list in
            // primaryCode
            // Get the EditText and its Editable
            View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
            if (focusCurrent == null)///////removed || focusCurrent.getClass() != EditText.class
                return;
            EditText edittext = (EditText) focusCurrent;
            Editable editable = edittext.getText();
            int start = edittext.getSelectionStart();
            // Apply the key to the edittext
            if (primaryCode == CodeCancel) {
                hideCustomKeyboard();
            } else if (primaryCode == CodeDelete) {
                if (editable != null && start > 0){
                    boolean deletedSomethind = false;
                    //look for a complete code string to delete
                    String[] keyCodeStrings = extraKeyCodeHandler.getCodeStrings();
                    for(String codeString:keyCodeStrings){
                        String currentStringInEditText = editable.toString();
                        if(codeString.length()>start){
                            //code string is too long. will not match. continue to avoid out of bounds error
                            continue;
                        }
                        if(currentStringInEditText.substring(start-codeString.length(),start).equals(codeString)){
                            //delete the whole code string
                            editable.delete(start - codeString.length(), start);
                            deletedSomethind = true;
                            break;
                        }
                    }

                    //didn't find any code strings. just delete the character to the left of the cursor
                    if(!deletedSomethind){
                        editable.delete(start - 1, start);
                    }
                }else{
                    //cursor is at start: do nothing
                }


                //try this to delete selected text
//                CharSequence selectedText = inputConnection.getSelectedText(0);
//
//                if (TextUtils.isEmpty(selectedText)) {
//                    inputConnection.deleteSurroundingText(1, 0);
//                } else {
//                    inputConnection.commitText("", 1);
//                }
            } else if (primaryCode == Enter) {
                if (editable != null){
                    if(onEnter!=null){
                        if(onEnter.closeKeyboardOnEnter()){
                            hideCustomKeyboard();//do this before doOnEnter because doOnEnter might set onEnter to null
                        }
                        onEnter.doOnEnter(edittext);
                    }else{
                        hideCustomKeyboard();
                    }

                }
            } else if (primaryCode == CodeClear) {
                if (editable != null)
                    editable.clear();
            } else if (primaryCode == CodeLeft) {
                if (start > 0)
                    edittext.setSelection(start - 1);
            } else if (primaryCode == CodeRight) {
                if (start < edittext.length())
                    edittext.setSelection(start + 1);
            } else if (primaryCode == CodeAllLeft) {
                edittext.setSelection(0);
            } else if (primaryCode == CodeAllRight) {
                edittext.setSelection(edittext.length());
            } else if (primaryCode == CodePrev) {
                View focusNew = edittext.focusSearch(View.FOCUS_LEFT);//////////originally View.FOCUS_BACKWARD
                if (focusNew != null)
                    focusNew.requestFocus();
            } else if (primaryCode == CodeNext) {
                View focusNew = edittext.focusSearch(View.FOCUS_RIGHT);//////////originallyView.FOCUS_FORWARD
                if (focusNew != null)
                    focusNew.requestFocus();
            } else{//insert either a special translation or insert the code as is
                String translation = extraKeyCodeHandler.codeToString(primaryCode);
                editable.insert(start, translation);
            }

            if(onInput!=null){
                onInput.doOnInput(edittext);
            }

        }

        @Override
        public void onPress(int arg0) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeUp() {
        }
    };

    /**
     * Create a custom keyboard, that uses the KeyboardView (with resource id
     * <var>viewid</var>) of the <var>host</var> activity, and load the keyboard
     * layout from xml file <var>layoutid</var> (see {@link Keyboard} for
     * description). Note that the <var>host</var> activity must have a
     * <var>KeyboardView</var> in its layout (typically aligned with the bottom
     * of the activity). Note that the keyboard layout xml file may include key
     * codes for navigation; see the constants in this class for their values.
     * Note that to enable EditText's to use this custom keyboard, call the
     * {@link #registerEditText(int)}.
     *
     * @param host
     *            The hosting activity.
     * @param keyboardView
     *            The KeyboardView.
     * @param layoutid
     *            The id of the xml file containing the keyboard layout.
     */
    public CustomKeyboard(Activity host, KeyboardView keyboardView, int layoutid, ExtraKeyCodeArchive extraKeyCodeHandler) {
        mHostActivity = host;

        mKeyboardView = keyboardView;
//        mKeyboardView = (KeyboardView) mHostActivity.findViewById(viewid);
        mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutid));
        mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview
        // balloons
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        // Hide the standard keyboard initially
        mHostActivity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.extraKeyCodeHandler = extraKeyCodeHandler;
    }

    /** Returns whether the CustomKeyboard is visible. */
    public boolean isCustomKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    /**
     * Make the CustomKeyboard visible, and hide the system keyboard for view v.
     */
    public void showCustomKeyboard(View v) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if (v != null)
            ((InputMethodManager) mHostActivity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /** Make the CustomKeyboard invisible. */
    public void hideCustomKeyboard() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }

    /**
     * Register <var>EditText<var> with resource id <var>resid</var> (on the
     * hosting activity) for using this custom keyboard.
     *
     * @param edittext
     *            The EditText that registers to the custom
     *            keyboard.
     */
    public void registerEditText(EditText edittext) {
        // Find the EditText 'resid'
//        EditText edittext = (EditText) mHostActivity.findViewById(resid);
        // Make the custom keyboard appear
        edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
            // NOTE By setting the on focus listener, we can show the custom
            // keyboard when the edit box gets focus, but also hide it when the
            // edit box loses focus
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showCustomKeyboard(v);
                else
                    hideCustomKeyboard();
            }
        });
        edittext.setOnClickListener(new OnClickListener() {
            // NOTE By setting the on click listener, we can show the custom
            // keyboard again, by tapping on an edit box that already had focus
            // (but that had the keyboard hidden).
            @Override
            public void onClick(View v) {
                showCustomKeyboard(v);
            }
        });
        // Disable standard keyboard hard way
        // NOTE There is also an easy way:
        // 'edittext.setInputType(InputType.TYPE_NULL)' (but you will not have a
        // cursor, and no 'edittext.setCursorVisible(true)' doesn't work )
        edittext.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType(); // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard
                // keyboard
                edittext.onTouchEvent(event); // Call native handler
                edittext.setInputType(inType); // Restore input type

                ////////added this here from https://stackoverflow.com/questions/13829462/android-ontouchlistener-schedule-a-thread-to-position-cursor
                //so the cursor will still go where the edit text was touched
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Layout layout = ((EditText) v).getLayout();
                        float x = event.getX() + edittext.getScrollX();
                        int offset = layout.getOffsetForHorizontal(0, x);
                        if(offset>0)
                            if(x>layout.getLineMax(0))
                                edittext.setSelection(offset);     // touch was at end of text
                            else
                                edittext.setSelection(offset - 1);
                        break;
                }


                return true; // Consume touch event
            }
        });
        // Disable spell check (hex strings look like words to Android)
        edittext.setInputType(edittext.getInputType()
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

}

// NOTE How can we change the background color of some keys (like the
// shift/ctrl/alt)?
// NOTE What does android:keyEdgeFlags do/mean