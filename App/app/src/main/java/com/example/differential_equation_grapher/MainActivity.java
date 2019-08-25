package com.example.differential_equation_grapher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.differential_equation_grapher.worldObjects.ParametricFunctionObject;
import com.example.differential_equation_grapher.worldObjects.ThreeDFunctionObject;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    private static final String CLASS_NAME = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the button
     * to be visible to buttons, methods must be public, void, and have a View as the only parameter*/
    public void openStadardGrapher(View view) {
        Intent intent = new Intent(this, StandardGrapherActivity.class);
        startActivity(intent);
    }

    public void openComplexGrapher(View view) {
        Intent intent = new Intent(this, Complex_Grapher.class);
        startActivity(intent);
    }

    public void openDifferentialGrapher(View view) {
        Intent intent = new Intent(this, DifferentialFieldActivity.class);
        startActivity(intent);
    }

    public void openPolarGrapher(View view) {
        Intent intent = new Intent(this, PolarGrapherActivity.class);
        startActivity(intent);
    }

    public void openParemetricGrapher(View view) {
        Intent intent = new Intent(this, ParametricGrapherActivity.class);
        startActivity(intent);
    }

    public void open3DGrapher(View view) {
        Intent intent = new Intent(this, ThreeDGrapherActivity.class);
        startActivity(intent);
    }


    /*
    todo:
    test on phone, put on phone

    simplify # of steps: just use interface

    polar integration blurry for phone

    editText.clearFocus();  when done editing



    side menu for non-formulas

    button sizes: bigger if possible


    complex auto brightness

    sample number on bottom left

    low definition on finger down, high definition on finger up: implemented, but lag when going to hd

    other? dot,cross,other divide,=,and,or

    multiple functions?

    pictures
    make enter bigger?

    can't find 0's for sqrt(z) (complex)

    ceil(4.4,4.4)
    arcsin(complex)
     */
}
