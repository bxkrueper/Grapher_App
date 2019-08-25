package com.example.differential_equation_grapher.helper;

import android.widget.EditText;

public interface ExtraKeyCodeArchive {
    String[] getCodeStrings();
    int getStartCode();
    String codeToString(int keyCode);//ex keyCode 1000 could return "ln("
}
