package com.example.differential_equation_grapher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.CenterZoomCamera;
import com.example.differential_equation_grapher.function.BasicFunction;
import com.example.differential_equation_grapher.function.ExpressionFactory;
import com.example.differential_equation_grapher.function.FieldFunction;
import com.example.differential_equation_grapher.function.Function;
import com.example.differential_equation_grapher.function.Undefined;
import com.example.differential_equation_grapher.helper.CustomKeyboard;
import com.example.differential_equation_grapher.helper.MathExtraKeyCodeArchive;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.worldObjects.CameraDragger;
import com.example.differential_equation_grapher.worldObjects.CameraPinchZoomer;
import com.example.differential_equation_grapher.worldObjects.CartesianGrid;
import com.example.differential_equation_grapher.worldObjects.FieldFunctionFollower;
import com.example.differential_equation_grapher.worldObjects.FieldFunctionObject;
import com.example.differential_equation_grapher.worldObjects.ScreenText;

public class DifferentialFieldActivity extends AppCompatActivity {
    private static final String CLASS_NAME = "DifferentialFieldActivity";

    EditText xPrimeEditText;
    EditText yPrimeEditText;
    EditText dtEditText;
    WorldView worldView;

    World world;//differential flow world

    FieldFunctionObject functionObject;
    FieldFunctionFollower flowFollower;

    CustomKeyboard customKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differential_field);
        getSupportActionBar().hide();//hides the bar at the top that only shows the name
        xPrimeEditText = (EditText) findViewById(R.id.forceXTextBox);
        yPrimeEditText = (EditText) findViewById(R.id.forceYTextBox);
        dtEditText = (EditText) findViewById(R.id.ticksPerSecondTextBox);
        worldView = (WorldView) findViewById(R.id.differentialWorldView);


        setCamera(worldView);

        world = worldView.getWorld();
        this.functionObject = new FieldFunctionObject(world,new FieldFunction(new BasicFunction("0"),new BasicFunction("0")));
        this.flowFollower = new FieldFunctionFollower(world,functionObject,1,1);

        world.add(new CartesianGrid(world));
        world.add(functionObject);////////causing black screen bug (not right here, but later)
        world.add(flowFollower);
        world.add(new CameraDragger(world));
        world.add(new CameraPinchZoomer(world));
        ScreenText screenText = new ScreenText(world,0,100,100,"");
        screenText.setName("Screen Text");
        world.add(screenText);

        setActionListeners();

        this.customKeyboard = new CustomKeyboard(this, getKeyboardview(), R.layout.math_keyboard, MathExtraKeyCodeArchive.getInstance());
        customKeyboard.registerEditText(xPrimeEditText);
        customKeyboard.registerEditText(yPrimeEditText);
        customKeyboard.registerEditText(dtEditText);

        setKeyBoardVarKeys();
    }

    //replace the dedicated variable keys with this activity's variable(s)
    private void setKeyBoardVarKeys() {
        Keyboard.Key key = customKeyboard.findKey("var1");
        key.codes = new int[]{120};
        key.label = "x";

        key = customKeyboard.findKey("var2");
        key.codes = new int[]{121};
        key.label = "y";
    }


    public KeyboardView getKeyboardview(){
        return (KeyboardView) findViewById(R.id.differentialKeyboardview);
    }
    public int getKeyboardviewId(){
        return R.id.differentialKeyboardview;
    }


    public void inputFinished(){
        //todo: copy from other inputFinished methods if this activity ever uses PopUpForInput
    }
    @Override
    public void onBackPressed()
    {
        if(customKeyboard.isCustomKeyboardVisible()){
            closeKeyBoard();
        }else{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

    }
    private void closeKeyBoard() {
        customKeyboard.hideCustomKeyboard();
        inputFinished();
    }

    private void setCamera(WorldView worldView) {
        float initialWorldWidth = 10f;
        int screenWidth = worldView.getScreenWidth();
        int screenHeight = worldView.getScreenHeight();
        CenterZoomCamera camera = new CenterZoomCamera(0,0,screenWidth,initialWorldWidth,screenHeight,-initialWorldWidth*screenHeight/screenWidth);//camera height: negative to make y axis go up, also set 1:1 ratio
        camera.setMinAbsForPixelsPerUnitX(1e-30f);
        camera.setMinAbsForPixelsPerUnitY(1e-30f);
        camera.setMaxAbsForPixelsPerUnitX(1000000f);
        camera.setMaxAbsForPixelsPerUnitY(1000000f);
        worldView.setCamera(camera);
    }

    private void setActionListeners() {
        xPrimeEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateXFunction();
            }
        });

        yPrimeEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateYFunction();
            }
        });

        dtEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateDT();
            }
        });
    }



    private void updateXFunction(){
        xPrimeEditText.setTextColor(getResources().getColor(R.color.validText));
        String formulaString = xPrimeEditText.getText().toString();

        Function function = new BasicFunction(formulaString);
        function.setVariable('x',0.0);//set x to something so the function knows the variable is known
        function.setVariable('y',0.0);//set y to something so the function knows the variable is known
        if(function.getExpression()== Undefined.getInstance()){
            xPrimeEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        if(!function.allVariablesDefined()){//check for any variables other than x or y
            function = new BasicFunction(Undefined.getInstance());
            xPrimeEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        functionObject.getFieldFunction().setxComponentFunction(function);
        worldView.redraw();//world only re-draws on actions, so helper objects need to re-calculate before every redraw
    }

    private void updateYFunction(){
        yPrimeEditText.setTextColor(getResources().getColor(R.color.validText));
        String formulaString = yPrimeEditText.getText().toString();

        Function function = new BasicFunction(formulaString);
        function.setVariable('x',0.0);//set x to something so the function knows the variable is known
        function.setVariable('y',0.0);//set y to something so the function knows the variable is known
        if(function.getExpression()== Undefined.getInstance()){//check for any variables other than x or y
            yPrimeEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        if(!function.allVariablesDefined()){//check for any variables other than x or y
            function = new BasicFunction(Undefined.getInstance());
            yPrimeEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        functionObject.getFieldFunction().setyComponentFunction(function);
        worldView.redraw();//world only re-draws on actions, so helper objects need to re-calculate before every redraw
    }

    private void updateDT() {
        final int MIN = 1;
        final int MAX = 60;
        dtEditText.setTextColor(getResources().getColor(R.color.validText));
        String numString = dtEditText.getText().toString();
        double newValueDouble = ExpressionFactory.getRealNumber(numString);

        int newValue;
        if(newValueDouble<MIN){
            newValue=MIN;
            dtEditText.setText(Integer.toString(MIN));
            Toast.makeText(getApplicationContext(),"Min Steps is " + MIN, Toast.LENGTH_SHORT).show();
        }else if(newValueDouble>MAX){
            newValue=MAX;
            dtEditText.setText(Integer.toString(MAX));
            Toast.makeText(getApplicationContext(),"Max steps is " + MAX, Toast.LENGTH_SHORT).show();
        }else if(Double.isNaN(newValueDouble)){
            dtEditText.setTextColor(getResources().getColor(R.color.invalidText));//change color to show error
            return;
        }else{
            newValue = (int) newValueDouble;
        }


        world.setTickPeriodMills((int) (1000f/newValue));
    }


    @Override
    public void onStart() {
        super.onStart();
        world.setTickPeriodMills((int) (1000f/ExpressionFactory.getRealNumber(dtEditText.getText().toString())));
    }

    @Override
    public void onStop() {
        super.onStop();
        world.unload();
    }
}
