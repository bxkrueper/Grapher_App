package com.example.differential_equation_grapher;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.CenterZoomCamera;
import com.example.differential_equation_grapher.function.BasicFunction;
import com.example.differential_equation_grapher.function.ExpressionFactory;
import com.example.differential_equation_grapher.function.Function;
import com.example.differential_equation_grapher.function.PolarFunction;
import com.example.differential_equation_grapher.function.Undefined;
import com.example.differential_equation_grapher.helper.CustomKeyboard;
import com.example.differential_equation_grapher.helper.MathExtraKeyCodeArchive;
import com.example.differential_equation_grapher.helper.PopUpForInput;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;
import com.example.differential_equation_grapher.worldObjects.CameraDraggerAndPincherZoomer;
import com.example.differential_equation_grapher.worldObjects.CartesianGrid;
import com.example.differential_equation_grapher.worldObjects.PolarEvaluator;
import com.example.differential_equation_grapher.worldObjects.PolarFunctionObject;
import com.example.differential_equation_grapher.worldObjects.PolarGrid;
import com.example.differential_equation_grapher.worldObjects.PolarIntegrator;
import com.example.differential_equation_grapher.worldObjects.PolarLengthFinder;
import com.example.differential_equation_grapher.worldObjects.ScreenText;

public class PolarGrapherActivity extends AppCompatActivity {
    private static final String CLASS_NAME = "PolarGrapherActivity";

    EditText formulaEditText;
    EditText thetaStartEditText;
    EditText thetaEndEditText;
    EditText numberOfStepsEditText;

    WorldView worldView;

    World world;

    PolarFunctionObject functionObject;

    WorldObject infoObject;//the object that is currently active. only one is active at a time
    CameraDraggerAndPincherZoomer cameraDraggerAndPincherZoomer;
    PolarEvaluator polarEvaluator;
    PolarIntegrator polarIntegrator;
    PolarLengthFinder polarLengthFinder;

    CartesianGrid cartesianGrid;
    PolarGrid polarGrid;

    Button highlightedButton;

    PopUpForInput popUpForInput;

    CustomKeyboard customKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polar_grapher);
        getSupportActionBar().hide();//hides the bar at the top that only shows the name
        this.formulaEditText = (EditText) findViewById(R.id.polarEditText);
        this.thetaStartEditText = (EditText) findViewById(R.id.thetaStartEditText);
        this.thetaEndEditText = (EditText) findViewById(R.id.thetaEndEditText);
        this.numberOfStepsEditText = (EditText) findViewById(R.id.numberOfStepsEditText);
        this.highlightedButton = (Button) findViewById(R.id.polarCameraButton);
        this.worldView = (WorldView) findViewById(R.id.polarWorldView);

        setCamera(worldView);


        world = worldView.getWorld();
        String functionString = formulaEditText.getText().toString();
        this.functionObject = new PolarFunctionObject(world,new PolarFunction(functionString));
        updateThetaStart();
        updateThetaEnd();
        updatenumberOfSteps();
        cartesianGrid = new CartesianGrid(world);
        polarGrid = new PolarGrid(world);
        world.add(polarGrid);
        world.add(functionObject);
        ScreenText screenText = new ScreenText(world,0,700,100,"");
        screenText.setName("Screen Text");
        world.add(screenText);


        this.cameraDraggerAndPincherZoomer = new CameraDraggerAndPincherZoomer(world);
        this.polarEvaluator = new PolarEvaluator(world,functionObject);
        this.polarIntegrator = new PolarIntegrator(world,functionObject.getNumberOfSteps(),functionObject);
        this.polarLengthFinder = new PolarLengthFinder(world,functionObject.getNumberOfSteps(),functionObject);

        this.infoObject = cameraDraggerAndPincherZoomer;
        world.add(infoObject);

        setActionListeners();

        this.customKeyboard = new CustomKeyboard(this, getKeyboardview(), R.layout.math_keyboard, MathExtraKeyCodeArchive.getInstance());
        customKeyboard.registerEditText(formulaEditText);
        customKeyboard.registerEditText(thetaStartEditText);
        customKeyboard.registerEditText(thetaEndEditText);
        customKeyboard.registerEditText(numberOfStepsEditText);

        this.popUpForInput = new PopUpForInput(this,findViewById(R.id.polarPopUpContainer),(TextView) findViewById(R.id.polarPopUpLabel),(EditText) findViewById(R.id.polarPopUpEditText),customKeyboard);

        setKeyBoardVarKeys();
    }

    //replace the dedicated variable keys with this activity's variable(s)
    private void setKeyBoardVarKeys() {
        Keyboard.Key key = customKeyboard.findKey("var1");
        key.codes = new int[]{0x03B8};
        key.label = "\u03B8";

        key = customKeyboard.findKey("var2");
        key.codes = new int[]{32};
        key.label = " ";
    }

    public KeyboardView getKeyboardview(){
        return (KeyboardView) findViewById(R.id.polarKeyboardview);
    }
    public int getKeyboardviewId(){
        return R.id.polarKeyboardview;
    }

    public void getInput(String textForLabel,CustomKeyboard.OnInput onInput){
        CustomKeyboard.OnEnter onEnter = new CustomKeyboard.OnEnter() {
            @Override
            public void doOnEnter(EditText editText) {
                inputFinished();
            }
            @Override
            public boolean closeKeyboardOnEnter() {
                return true;
            }
        };

        //disable normal edit texts
        formulaEditText.setEnabled(false);
        thetaStartEditText.setEnabled(false);
        thetaEndEditText.setEnabled(false);
        numberOfStepsEditText.setEnabled(false);


        popUpForInput.inputNeeded(textForLabel,onInput,onEnter);
    }
    public void getInput(String textForLabel,CustomKeyboard.OnInput onInput,CustomKeyboard.OnEnter onEnter){
        //disable normal edit texts
        formulaEditText.setEnabled(false);
        thetaStartEditText.setEnabled(false);
        thetaEndEditText.setEnabled(false);
        numberOfStepsEditText.setEnabled(false);

        popUpForInput.inputNeeded(textForLabel,onInput,onEnter);
    }

    public void inputFinished(){
        popUpForInput.inputFinished();

        //re-enable normal edit texts
        formulaEditText.setEnabled(true);
        thetaStartEditText.setEnabled(true);
        thetaEndEditText.setEnabled(true);
        numberOfStepsEditText.setEnabled(true);
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
        formulaEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateFunction();
            }
        });

        thetaStartEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateThetaStart();
            }
        });

        thetaEndEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateThetaEnd();
            }
        });

        numberOfStepsEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatenumberOfSteps();
            }
        });
    }

    private void updateFunction(){
        formulaEditText.setTextColor(getResources().getColor(R.color.validText));
        String formulaString = formulaEditText.getText().toString();

        Function function = new BasicFunction(formulaString);
        function.setVariable('\u03B8',0.0);//set theta to something so the function knows the variable is known
        if(function.getExpression()== Undefined.getInstance()){
            formulaEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        if(!function.allVariablesDefined()){//check for any variables other than theta
            function = new BasicFunction(Undefined.getInstance());
            formulaEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        functionObject.setFunction(new PolarFunction(function));
        worldView.redraw();//world only re-draws on actions, so helper objects need to re-calculate before every redraw
    }

    private void updateThetaStart(){
        thetaStartEditText.setTextColor(getResources().getColor(R.color.validText));

        double newValue = ExpressionFactory.getRealNumber(thetaStartEditText.getText().toString());
        if(Double.isNaN(newValue)){
            thetaStartEditText.setTextColor(getResources().getColor(R.color.invalidText));
            return;
        }

        functionObject.setThetaStart((float) newValue);
        worldView.redraw();
    }

    private void updateThetaEnd(){
        thetaEndEditText.setTextColor(getResources().getColor(R.color.validText));

        double newValue = ExpressionFactory.getRealNumber(thetaEndEditText.getText().toString());
        if(Double.isNaN(newValue)){
            thetaEndEditText.setTextColor(getResources().getColor(R.color.invalidText));
            return;
        }

        functionObject.setThetaEnd((float) newValue);
        worldView.redraw();
    }


    private void updatenumberOfSteps() {
        final int MIN = 1;
        final int MAX = 1000;
        numberOfStepsEditText.setTextColor(getResources().getColor(R.color.validText));
        String numString = numberOfStepsEditText.getText().toString();
        double newValueDouble = ExpressionFactory.getRealNumber(numString);

        int newValue;
        if(newValueDouble<MIN){
            newValue=MIN;
            numberOfStepsEditText.setText(Integer.toString(MIN));
            Toast.makeText(getApplicationContext(),"Min Steps is " + MIN, Toast.LENGTH_SHORT).show();
        }else if(newValueDouble>MAX){
            newValue=MAX;
            numberOfStepsEditText.setText(Integer.toString(MAX));
            Toast.makeText(getApplicationContext(),"Max steps is " + MAX, Toast.LENGTH_SHORT).show();
        }else if(Double.isNaN(newValueDouble)){
            numberOfStepsEditText.setTextColor(getResources().getColor(R.color.invalidText));//change color to show error
            return;
        }else{
            newValue = (int) newValueDouble;
        }



        functionObject.setNumberOfSteps(newValue);
        worldView.redraw();
    }

    private void highlightButton(Button button){
        highlightedButton.setBackgroundColor(getResources().getColor(R.color.unSelectedButton));
        this.highlightedButton = button;
        highlightedButton.setBackgroundColor(getResources().getColor(R.color.selectedButton));
    }

    private void replaceInfoObject(WorldObject newInfoObject){
        world.delete(infoObject);
        this.infoObject = newInfoObject;
        world.add(infoObject);
        worldView.redraw();
    }

    public void useCameraMover(View view){
        highlightButton((Button) findViewById(R.id.polarCameraButton));
        replaceInfoObject(this.cameraDraggerAndPincherZoomer);
    }

    public void usePolarEvaluator(View view){
        highlightButton((Button) findViewById(R.id.polarEvaluateButton));
        replaceInfoObject(this.polarEvaluator);
    }

    public void usePolarIntegrator(View view){
        highlightButton((Button) findViewById(R.id.polarIntegrationButton));
        replaceInfoObject(this.polarIntegrator);
    }

    public void usePolarLengthFinder(View view){
        highlightButton((Button) findViewById(R.id.polarLengthButton));
        replaceInfoObject(this.polarLengthFinder);
    }

    public void swapGrids(View view){
        if(world.containsWorldObject(cartesianGrid)){
            world.delete(cartesianGrid);
            world.add(polarGrid);
            world.sendToBackDrawableWrold(polarGrid);
        }else{
            world.delete(polarGrid);
            world.add(cartesianGrid);
            world.sendToBackDrawableWrold(cartesianGrid);
        }
        world.getWorldView().redraw();
    }
}