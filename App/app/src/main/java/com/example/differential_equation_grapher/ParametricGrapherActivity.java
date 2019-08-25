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
import com.example.differential_equation_grapher.function.StandardFunction;
import com.example.differential_equation_grapher.function.Undefined;
import com.example.differential_equation_grapher.helper.CustomKeyboard;
import com.example.differential_equation_grapher.helper.MathExtraKeyCodeArchive;
import com.example.differential_equation_grapher.helper.PopUpForInput;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;
import com.example.differential_equation_grapher.worldObjects.CameraDraggerAndPincherZoomer;
import com.example.differential_equation_grapher.worldObjects.CartesianGrid;
import com.example.differential_equation_grapher.worldObjects.ParametricEvaluator;
import com.example.differential_equation_grapher.worldObjects.ParametricFunctionObject;
import com.example.differential_equation_grapher.worldObjects.ParametricLengthFinder;
import com.example.differential_equation_grapher.worldObjects.ScreenText;

public class ParametricGrapherActivity extends AppCompatActivity {
    private static final String CLASS_NAME = "ParametricGrapherActivity";

    EditText formulaXEditText;
    EditText formulaYEditText;
    EditText tStartEditText;
    EditText tEndEditText;
    EditText numberOfStepsEditText;

    WorldView worldView;

    World world;

    ParametricFunctionObject functionObject;

    WorldObject infoObject;//the object that is currently active. only one is active at a time
    CameraDraggerAndPincherZoomer cameraDraggerAndPincherZoomer;
    ParametricEvaluator parametricEvaluator;
    ParametricLengthFinder parametricLengthFinder;

    CartesianGrid cartesianGrid;

    Button highlightedButton;

    PopUpForInput popUpForInput;

    CustomKeyboard customKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametric_grapher);
        getSupportActionBar().hide();//hides the bar at the top that only shows the name
        this.formulaXEditText = (EditText) findViewById(R.id.parametricXTEditText);
        this.formulaYEditText = (EditText) findViewById(R.id.parametricYTEditText);
        this.tStartEditText = (EditText) findViewById(R.id.parametricTStartEditText);
        this.tEndEditText = (EditText) findViewById(R.id.parametricTEndEditText);
        this.numberOfStepsEditText = (EditText) findViewById(R.id.parametricNumberStepsEditText);
        this.highlightedButton = (Button) findViewById(R.id.parametricCameraButton);
        this.worldView = (WorldView) findViewById(R.id.parametricWorldView);

        setCamera(worldView);


        world = worldView.getWorld();
        String functionStringX = formulaXEditText.getText().toString();
        String functionStringY = formulaYEditText.getText().toString();
        this.functionObject = new ParametricFunctionObject(world,new StandardFunction(functionStringX,'t'),new StandardFunction(functionStringY,'t'));
        updateTStart();
        updateTEnd();
        updatenumberOfSteps();
        cartesianGrid = new CartesianGrid(world);
        world.add(cartesianGrid);
        world.add(functionObject);
        ScreenText screenText = new ScreenText(world,0,700,100,"");
        screenText.setName("Screen Text");
        world.add(screenText);


        this.cameraDraggerAndPincherZoomer = new CameraDraggerAndPincherZoomer(world);
        this.parametricEvaluator = new ParametricEvaluator(world,functionObject);
        this.parametricLengthFinder = new ParametricLengthFinder(world,functionObject.getNumberOfSteps(),functionObject);

        this.infoObject = cameraDraggerAndPincherZoomer;
        world.add(infoObject);

        setActionListeners();

        this.customKeyboard = new CustomKeyboard(this, getKeyboardview(), R.layout.math_keyboard, MathExtraKeyCodeArchive.getInstance());
        customKeyboard.registerEditText(formulaXEditText);
        customKeyboard.registerEditText(formulaYEditText);
        customKeyboard.registerEditText(tStartEditText);
        customKeyboard.registerEditText(tEndEditText);
        customKeyboard.registerEditText(numberOfStepsEditText);

        this.popUpForInput = new PopUpForInput(this,findViewById(R.id.parametricPopUpContainer),(TextView) findViewById(R.id.parametricPopUpLabel),(EditText) findViewById(R.id.parametricPopUpEditText),customKeyboard);

        setKeyBoardVarKeys();
    }

    //replace the dedicated variable keys with this activity's variable(s)
    private void setKeyBoardVarKeys() {
        Keyboard.Key key = customKeyboard.findKey("var1");
        key.codes = new int[]{116};
        key.label = "t";

        key = customKeyboard.findKey("var2");
        key.codes = new int[]{32};
        key.label = " ";
    }



    public KeyboardView getKeyboardview(){
        return (KeyboardView) findViewById(R.id.parametricKeyboardview);
    }
    public int getKeyboardviewId(){
        return R.id.parametricKeyboardview;
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
        formulaXEditText.setEnabled(false);
        formulaYEditText.setEnabled(false);
        tStartEditText.setEnabled(false);
        tEndEditText.setEnabled(false);
        numberOfStepsEditText.setEnabled(false);


        popUpForInput.inputNeeded(textForLabel,onInput,onEnter);
    }
    public void getInput(String textForLabel,CustomKeyboard.OnInput onInput,CustomKeyboard.OnEnter onEnter){
        //disable normal edit texts
        formulaXEditText.setEnabled(false);
        formulaYEditText.setEnabled(false);
        tStartEditText.setEnabled(false);
        tEndEditText.setEnabled(false);
        numberOfStepsEditText.setEnabled(false);


        popUpForInput.inputNeeded(textForLabel,onInput,onEnter);
    }

    public void inputFinished(){
        popUpForInput.inputFinished();

        //re-enable normal edit texts
        formulaXEditText.setEnabled(true);
        formulaYEditText.setEnabled(true);
        tStartEditText.setEnabled(true);
        tEndEditText.setEnabled(true);
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
        formulaXEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateFunctionX();
            }
        });

        formulaYEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateFunctionY();
            }
        });

        tStartEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTStart();
            }
        });

        tEndEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTEnd();
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

    private void updateFunctionX(){
        formulaXEditText.setTextColor(getResources().getColor(R.color.validText));
        String formulaString = formulaXEditText.getText().toString();

        Function function = new BasicFunction(formulaString);
        function.setVariable('t',0.0);//set t to something so the function knows the variable is known
        if(function.getExpression()== Undefined.getInstance()){
            formulaXEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        if(!function.allVariablesDefined()){//check for any variables other than t
            function = new BasicFunction(Undefined.getInstance());
            formulaXEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        functionObject.setFunctionX(new StandardFunction(function,'t'));
        worldView.redraw();//world only re-draws on actions, so helper objects need to re-calculate before every redraw
    }

    private void updateFunctionY(){
        formulaYEditText.setTextColor(getResources().getColor(R.color.validText));
        String formulaString = formulaYEditText.getText().toString();

        Function function = new BasicFunction(formulaString);
        function.setVariable('t',0.0);//set t to something so the function knows the variable is known
        if(function.getExpression()== Undefined.getInstance()){
            formulaYEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        if(!function.allVariablesDefined()){//check for any variables other than t
            function = new BasicFunction(Undefined.getInstance());
            formulaYEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        functionObject.setFunctionY(new StandardFunction(function,'t'));
        worldView.redraw();//world only re-draws on actions, so helper objects need to re-calculate before every redraw
    }

    private void updateTStart(){
        tStartEditText.setTextColor(getResources().getColor(R.color.validText));

        double newValue = ExpressionFactory.getRealNumber(tStartEditText.getText().toString());
        if(Double.isNaN(newValue)){
            tStartEditText.setTextColor(getResources().getColor(R.color.invalidText));
            return;
        }

        functionObject.setTStart((float) newValue);
        worldView.redraw();
    }

    private void updateTEnd(){
        tEndEditText.setTextColor(getResources().getColor(R.color.validText));

        double newValue = ExpressionFactory.getRealNumber(tEndEditText.getText().toString());
        if(Double.isNaN(newValue)){
            tEndEditText.setTextColor(getResources().getColor(R.color.invalidText));
            return;
        }

        functionObject.setTEnd((float) newValue);
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
        highlightButton((Button) findViewById(R.id.parametricCameraButton));
        replaceInfoObject(this.cameraDraggerAndPincherZoomer);
    }

    public void useParametricEvaluator(View view){
        highlightButton((Button) findViewById(R.id.parametricEvaluatorButton));
        replaceInfoObject(this.parametricEvaluator);
    }
    public void useParametricLengthFinder(View view){
        highlightButton((Button) findViewById(R.id.parametricLengthButton));
        replaceInfoObject(this.parametricLengthFinder);
    }

}