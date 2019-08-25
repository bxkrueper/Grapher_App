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
import com.example.differential_equation_grapher.worldObjects.StandardFunctionDerivativeEvaluator;
import com.example.differential_equation_grapher.worldObjects.ScreenText;
import com.example.differential_equation_grapher.worldObjects.StandardFunctionEvaluatorX;
import com.example.differential_equation_grapher.worldObjects.StandardFunctionLengthFinder;
import com.example.differential_equation_grapher.worldObjects.StandardFunctionNewtonsMethod;
import com.example.differential_equation_grapher.worldObjects.StandardFunctionIntegrator;
import com.example.differential_equation_grapher.worldObjects.StandardFunctionMaxMinFinder;
import com.example.differential_equation_grapher.worldObjects.StandardFunctionObject;

public class StandardGrapherActivity extends AppCompatActivity {
    private static final String CLASS_NAME = "StandardGrapherActivity";

    TextView formulaLabel;
    EditText formulaEditText;
    WorldView worldView;

    World world;//standard graph world

    StandardFunctionObject functionObject;

    WorldObject infoObject;//the object that is currently active. only one is active at a time
    CameraDraggerAndPincherZoomer cameraDraggerAndPincherZoomer;
    StandardFunctionEvaluatorX standardFunctionEvaluatorX;
    StandardFunctionDerivativeEvaluator standardFunctionDerivativeEvaluator;
    StandardFunctionIntegrator standardFunctionIntegrator;
    StandardFunctionNewtonsMethod standardFunctionEvaluatorY;
    StandardFunctionMaxMinFinder standardFunctionMaxMinFinder;
    StandardFunctionLengthFinder standardFunctionLengthFinder;

    Button highlightedButton;

    CustomKeyboard customKeyboard;

    PopUpForInput popUpForInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_grapher);
        getSupportActionBar().hide();//hides the bar at the top that only shows the name
        formulaLabel = findViewById(R.id.yEqualsTextView);
        this.formulaEditText = (EditText) findViewById(R.id.formulaEditText);
        this.highlightedButton = (Button) findViewById(R.id.cameraButton);
        worldView = (WorldView) findViewById(R.id.standardWorldView);

        setCamera(worldView);


        world = worldView.getWorld();
        this.functionObject = new StandardFunctionObject(world,new StandardFunction("0",'x'));
        CartesianGrid cartesianGrid = new CartesianGrid(world);
        world.add(cartesianGrid);
        world.add(functionObject);
        ScreenText screenText = new ScreenText(world,0,700,100,"");
        screenText.setName("Screen Text");
        world.add(screenText);


        this.cameraDraggerAndPincherZoomer = new CameraDraggerAndPincherZoomer(world);
        this.standardFunctionEvaluatorX = new StandardFunctionEvaluatorX(world,functionObject);
        this.standardFunctionDerivativeEvaluator = new StandardFunctionDerivativeEvaluator(world,functionObject);
        this.standardFunctionIntegrator = new StandardFunctionIntegrator(world,50,functionObject);
        this.standardFunctionEvaluatorY = new StandardFunctionNewtonsMethod(world,functionObject);
        this.standardFunctionMaxMinFinder = new StandardFunctionMaxMinFinder(world,functionObject);
        this.standardFunctionLengthFinder = new StandardFunctionLengthFinder(world,50,functionObject);

        this.infoObject = cameraDraggerAndPincherZoomer;
        world.add(infoObject);

        setFormulaActionListener();



        this.customKeyboard = new CustomKeyboard(this, getKeyboardview(), R.layout.math_keyboard, MathExtraKeyCodeArchive.getInstance());
        customKeyboard.registerEditText(formulaEditText);
        this.popUpForInput = new PopUpForInput(this,findViewById(R.id.standardPopUpContainer),(TextView) findViewById(R.id.standardPopUpLabel),(EditText) findViewById(R.id.standardPopUpEditText),customKeyboard);

        setKeyBoardVarKeys();

    }

    //replace the dedicated variable keys with this activity's variable(s)
    private void setKeyBoardVarKeys() {
        Keyboard.Key key = customKeyboard.findKey("var1");
        key.codes = new int[]{120};
        key.label = "x";

        key = customKeyboard.findKey("var2");
        key.codes = new int[]{32};
        key.label = " ";
    }

    public KeyboardView getKeyboardview(){
        return (KeyboardView) findViewById(R.id.keyboardview1);
    }
    public int getKeyboardviewId(){
        return R.id.keyboardview1;
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


        popUpForInput.inputNeeded(textForLabel,onInput,onEnter);
    }
    public void getInput(String textForLabel,CustomKeyboard.OnInput onInput,CustomKeyboard.OnEnter onEnter){
        //disable normal edit texts
        formulaEditText.setEnabled(false);


        popUpForInput.inputNeeded(textForLabel,onInput,onEnter);
    }

    public void inputFinished(){
        popUpForInput.inputFinished();

        //re-enable normal edit texts
        formulaEditText.setEnabled(true);
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

    private void setFormulaActionListener() {
//        formulaEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
//                if (id == EditorInfo.IME_ACTION_DONE) {
//                    updateFunction();
//                    return true;
//                }
//
//                return false;
//            }
//        });
        formulaEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateFunction();
            }
        });
    }

    private void updateFunction(){
        formulaEditText.setTextColor(getResources().getColor(R.color.validText));
        String formulaString = formulaEditText.getText().toString();

        Function function = new BasicFunction(formulaString);
        function.setVariable('x',0.0);//set x to something so the function knows the variable is known
        if(function.getExpression()== Undefined.getInstance()){
            formulaEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        if(!function.allVariablesDefined()){//check for any variables other than x or y
//            Toast.makeText(getApplicationContext(),"Unrecognized variable!", Toast.LENGTH_SHORT).show();
            function = new BasicFunction(Undefined.getInstance());
            formulaEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        functionObject.setFunction(new StandardFunction(function,'x'));
        worldView.redraw();//world only re-draws on actions, so helper objects need to re-calculate before every redraw
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
        highlightButton((Button) findViewById(R.id.cameraButton));
        replaceInfoObject(this.cameraDraggerAndPincherZoomer);
    }
    public void useXEvaluator(View view){
        highlightButton((Button) findViewById(R.id.xEvalluateButton));
        replaceInfoObject(this.standardFunctionEvaluatorX);
    }
    public void useDerivativeEvaluator(View view){
        highlightButton((Button) findViewById(R.id.derivativeButton));
        replaceInfoObject(this.standardFunctionDerivativeEvaluator);
    }
    public void useIntegratorEvaluator(View view){
        highlightButton((Button) findViewById(R.id.integrationButton));
        replaceInfoObject(this.standardFunctionIntegrator);
    }
    public void useYEvaluator(View view){
        highlightButton((Button) findViewById(R.id.newtonsMethodButton));
        replaceInfoObject(this.standardFunctionEvaluatorY);
    }
    public void useMinMax(View view){
        highlightButton((Button) findViewById(R.id.minMaxFinderButton));
        replaceInfoObject(this.standardFunctionMaxMinFinder);
    }
    public void useLengthFinder(View view){
        highlightButton((Button) findViewById(R.id.standardLengthButton));
        replaceInfoObject(this.standardFunctionLengthFinder);
    }
}