package com.example.differential_equation_grapher;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.CenterZoomCamera;
import com.example.differential_equation_grapher.function.BasicFunction;
import com.example.differential_equation_grapher.function.ExpressionFactory;
import com.example.differential_equation_grapher.function.Function;
import com.example.differential_equation_grapher.function.StandardFunctionMultiParam;
import com.example.differential_equation_grapher.function.Undefined;
import com.example.differential_equation_grapher.helper.CustomKeyboard;
import com.example.differential_equation_grapher.helper.MathExtraKeyCodeArchive;
import com.example.differential_equation_grapher.helper.PopUpForInput;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;
import com.example.differential_equation_grapher.worldObjects.CameraDraggerAndPincherZoomer;
import com.example.differential_equation_grapher.worldObjects.CartesianGrid;
import com.example.differential_equation_grapher.worldObjects.ScreenText;
import com.example.differential_equation_grapher.worldObjects.ThreeDFunctionEvaluator;
import com.example.differential_equation_grapher.worldObjects.ThreeDFunctionObject;
import com.example.differential_equation_grapher.worldObjects.ThreeDGradientEvaluator;
import com.example.differential_equation_grapher.worldObjects.ThreeDIntegrator;
import com.example.differential_equation_grapher.worldObjects.ThreeDMaxMinFinder;

public class ThreeDGrapherActivity extends AppCompatActivity {
    private static final String CLASS_NAME = "ThreeDGrapherActivity";

    EditText threeDFormulaEditText;
    EditText threeDMinEditText;
    EditText threeDMaxEditText;
    WorldView worldView;


    World world;

    ThreeDFunctionObject functionObject;

    WorldObject infoObject;//the object that is currently active. only one is active at a time
    CameraDraggerAndPincherZoomer cameraDraggerAndPincherZoomer;
    ThreeDFunctionEvaluator threeDFunctionEvaluator;
    ThreeDGradientEvaluator threeDGradientEvaluator;
    ThreeDIntegrator threeDIntegrator;
    ThreeDMaxMinFinder threeDMaxMinFinder;

    Button highlightedButton;

    PopUpForInput popUpForInput;

    CustomKeyboard customKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_dgrapher);
        getSupportActionBar().hide();//hides the bar at the top that only shows the name
        this.threeDFormulaEditText = (EditText) findViewById(R.id.threeDFormulaEditText);
        this.threeDMinEditText = (EditText) findViewById(R.id.threeDMinEditText);
        this.threeDMaxEditText = (EditText) findViewById(R.id.threeDMaxEditText);
        this.highlightedButton = (Button) findViewById(R.id.threeDCameraButton);
        this.worldView = (WorldView) findViewById(R.id.threeDWorldView);

        setCamera(worldView);


        world = worldView.getWorld();
        this.functionObject = new ThreeDFunctionObject(world,new StandardFunctionMultiParam("0",'x','y'));
        functionObject.setMin(ExpressionFactory.getRealNumber(threeDMinEditText.getText().toString()));
        functionObject.setMax(ExpressionFactory.getRealNumber(threeDMaxEditText.getText().toString()));
        world.add(functionObject);
        CartesianGrid cartesianGrid = new CartesianGrid(world);
        world.add(cartesianGrid);
        ScreenText screenText = new ScreenText(world,0,700,100,"");
        screenText.setName("Screen Text");
        world.add(screenText);


        this.cameraDraggerAndPincherZoomer = new CameraDraggerAndPincherZoomer(world);
        this.threeDFunctionEvaluator = new ThreeDFunctionEvaluator(world,functionObject);
        this.threeDGradientEvaluator = new ThreeDGradientEvaluator(world,functionObject);
        this.threeDMaxMinFinder = new ThreeDMaxMinFinder(world,functionObject,false);
        this.threeDIntegrator = new ThreeDIntegrator(world,5,functionObject);

        this.infoObject = cameraDraggerAndPincherZoomer;
        world.add(infoObject);

        setFormulaActionListeners();

        this.customKeyboard = new CustomKeyboard(this, getKeyboardview(), R.layout.math_keyboard, MathExtraKeyCodeArchive.getInstance());
        customKeyboard.registerEditText(threeDFormulaEditText);
        customKeyboard.registerEditText(threeDMinEditText);
        customKeyboard.registerEditText(threeDMaxEditText);

        this.popUpForInput = new PopUpForInput(this,findViewById(R.id.threeDPopUpContainer),(TextView) findViewById(R.id.threeDPopUpLabel),(EditText) findViewById(R.id.threeDPopUpEditText),customKeyboard);

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
        return (KeyboardView) findViewById(R.id.threeDkeyboardview);
    }
    public int getKeyboardviewId(){
        return R.id.threeDkeyboardview;
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
        threeDFormulaEditText.setEnabled(false);


        popUpForInput.inputNeeded(textForLabel,onInput,onEnter);
    }

    public void getInput(String textForLabel,CustomKeyboard.OnInput onInput,CustomKeyboard.OnEnter onEnter){
        //disable normal edit texts
        threeDFormulaEditText.setEnabled(false);


        popUpForInput.inputNeeded(textForLabel,onInput,onEnter);
    }

    public void inputFinished(){
        popUpForInput.inputFinished();

        //re-enable normal edit texts
        threeDFormulaEditText.setEnabled(true);
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

    private void setFormulaActionListeners() {
        threeDFormulaEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateFunction();
            }
        });

        threeDMinEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateMin();
            }
        });

        threeDMaxEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateMax();
            }
        });
    }

    private void updateFunction(){
        threeDFormulaEditText.setTextColor(getResources().getColor(R.color.validText));
        String formulaString = threeDFormulaEditText.getText().toString();

        Function function = new BasicFunction(formulaString);
        function.setVariable('x',0.0);//set x to something so the function knows the variable is known
        function.setVariable('y',0.0);//set y to something so the function knows the variable is known
        if(function.getExpression()== Undefined.getInstance()){
            threeDFormulaEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        if(!function.allVariablesDefined()){//check for any variables other than z
            function = new BasicFunction(Undefined.getInstance());
            threeDFormulaEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        functionObject.setFunction(new StandardFunctionMultiParam(function,'x','y'));
        worldView.redraw();//world only re-draws on actions, so helper objects need to re-calculate before every redraw
    }

    private void updateMin() {
        threeDMinEditText.setTextColor(getResources().getColor(R.color.validText));

        double newValue = ExpressionFactory.getRealNumber(threeDMinEditText.getText().toString());
        if(Double.isNaN(newValue)){
            threeDMinEditText.setTextColor(getResources().getColor(R.color.invalidText));
            return;
        }

        functionObject.setMin(newValue);
        worldView.redraw();
    }

    private void updateMax() {
        threeDMaxEditText.setTextColor(getResources().getColor(R.color.validText));

        double newValue = ExpressionFactory.getRealNumber(threeDMaxEditText.getText().toString());
        if(Double.isNaN(newValue)){
            threeDMaxEditText.setTextColor(getResources().getColor(R.color.invalidText));
            return;
        }

        functionObject.setMax(newValue);
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
        world.add(infoObject);//already in front
        worldView.redraw();
    }

    public void useCameraMover(View view){
        highlightButton((Button) findViewById(R.id.threeDCameraButton));
        replaceInfoObject(this.cameraDraggerAndPincherZoomer);
    }
    public void use3DEvaluator(View view){
        highlightButton((Button) findViewById(R.id.threeDEvaluateButton));
        replaceInfoObject(this.threeDFunctionEvaluator);
    }
    public void use3DGradientEvaluator(View view){
        highlightButton((Button) findViewById(R.id.threeDGradientButton));
        replaceInfoObject(this.threeDGradientEvaluator);
    }
    public void use3DIntegrator(View view){
        highlightButton((Button) findViewById(R.id.threeDIntegratorButton));
        replaceInfoObject(this.threeDIntegrator);
    }
    public void use3DMaxMinFinder(View view){
        if(infoObject==threeDMaxMinFinder){
            //toggle between min and max
            if(threeDMaxMinFinder.isUsingMinMode()){
                //toggle to max
                threeDMaxMinFinder.setUsingMinMode(false);
                ((Button) findViewById(R.id.threeDMinMaxButton)).setText(R.string.three_d_max_min_button_max_mode);
            }else{
                //toggle to min
                threeDMaxMinFinder.setUsingMinMode(true);
                ((Button) findViewById(R.id.threeDMinMaxButton)).setText(R.string.three_d_max_min_button_min_mode);
            }
            worldView.redraw();
        }else{
            //swap out as normal
            highlightButton((Button) findViewById(R.id.threeDMinMaxButton));
            replaceInfoObject(this.threeDMaxMinFinder);
        }
    }


}
