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
import com.example.differential_equation_grapher.function.ComplexFunction;
import com.example.differential_equation_grapher.function.Function;
import com.example.differential_equation_grapher.function.Undefined;
import com.example.differential_equation_grapher.helper.CustomKeyboard;
import com.example.differential_equation_grapher.helper.MathExtraKeyCodeArchive;
import com.example.differential_equation_grapher.helper.PopUpForInput;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;
import com.example.differential_equation_grapher.worldObjects.CameraDraggerAndPincherZoomer;
import com.example.differential_equation_grapher.worldObjects.Complex0Finder;
import com.example.differential_equation_grapher.worldObjects.ComplexArrowDrawer;
import com.example.differential_equation_grapher.worldObjects.ComplexFunctionObject;
import com.example.differential_equation_grapher.worldObjects.CartesianGrid;
import com.example.differential_equation_grapher.worldObjects.ScreenText;

public class Complex_Grapher extends AppCompatActivity {
    private static final String CLASS_NAME = "Complex_Grapher";

    EditText complexFormulaEditText;
    WorldView worldView;

    World world;//standard graph world

    ComplexFunctionObject functionObject;

    WorldObject infoObject;//the object that is currently active. only one is active at a time
    CameraDraggerAndPincherZoomer cameraDraggerAndPincherZoomer;
    ComplexArrowDrawer complexArrowDrawer;
    Complex0Finder complex0Finder;

    CustomKeyboard customKeyboard;

    Button highlightedButton;

    PopUpForInput popUpForInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex__grapher);
        getSupportActionBar().hide();//hides the bar at the top that only shows the name
        this.complexFormulaEditText = (EditText) findViewById(R.id.complexFormulaEditText);
        this.highlightedButton = (Button) findViewById(R.id.complexCameraButton);
        worldView = (WorldView) findViewById(R.id.complexWorldView);

        setCamera(worldView);


        world = worldView.getWorld();
        this.functionObject = new ComplexFunctionObject(world,new ComplexFunction(complexFormulaEditText.getText().toString()));
        world.add(functionObject);
        CartesianGrid cartesianGrid = new CartesianGrid(world);
        world.add(cartesianGrid);
        ScreenText screenText = new ScreenText(world,0,700,100,"");
        screenText.setName("Screen Text");
        world.add(screenText);


        this.cameraDraggerAndPincherZoomer = new CameraDraggerAndPincherZoomer(world);
        this.complexArrowDrawer = new ComplexArrowDrawer(world,functionObject);
        this.complex0Finder = new Complex0Finder(world,functionObject);

        this.infoObject = cameraDraggerAndPincherZoomer;
        world.add(infoObject);

        setFormulaActionListener();

        this.customKeyboard = new CustomKeyboard(this, getKeyboardview(), R.layout.math_keyboard, MathExtraKeyCodeArchive.getInstance());
        customKeyboard.registerEditText(complexFormulaEditText);

        this.popUpForInput = new PopUpForInput(this,findViewById(R.id.complexPopUpContainer),(TextView) findViewById(R.id.complexPopUpLabel),(EditText) findViewById(R.id.complexPopUpEditText),customKeyboard);

        setKeyBoardVarKeys();
    }

    //replace the dedicated variable keys with this activity's variable(s)
    private void setKeyBoardVarKeys() {
        Keyboard.Key key = customKeyboard.findKey("var1");
        key.codes = new int[]{122};
        key.label = "z";

        key = customKeyboard.findKey("var2");
        key.codes = new int[]{32};
        key.label = " ";
    }

    public KeyboardView getKeyboardview(){
        return (KeyboardView) findViewById(R.id.complexKeyboardview);
    }
    public int getKeyboardviewId(){
        return R.id.complexKeyboardview;
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
        complexFormulaEditText.setEnabled(false);


        popUpForInput.inputNeeded(textForLabel,onInput,onEnter);
    }
    public void getInput(String textForLabel,CustomKeyboard.OnInput onInput,CustomKeyboard.OnEnter onEnter){
        //disable normal edit texts
        complexFormulaEditText.setEnabled(false);


        popUpForInput.inputNeeded(textForLabel,onInput,onEnter);
    }

    public void inputFinished(){
        popUpForInput.inputFinished();

        //re-enable normal edit texts
        complexFormulaEditText.setEnabled(true);
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
        complexFormulaEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateFunction();
            }
        });
    }

    private void updateFunction(){
        complexFormulaEditText.setTextColor(getResources().getColor(R.color.validText));
        String formulaString = complexFormulaEditText.getText().toString();

        Function function = new BasicFunction(formulaString);
        function.setVariable('z',0.0);//set z to something so the function knows the variable is known
        if(function.getExpression()== Undefined.getInstance()){
            complexFormulaEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        if(!function.allVariablesDefined()){//check for any variables other than z
            function = new BasicFunction(Undefined.getInstance());
            complexFormulaEditText.setTextColor(getResources().getColor(R.color.invalidText));
        }
        functionObject.setFunction(new ComplexFunction(function));
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
        highlightButton((Button) findViewById(R.id.complexCameraButton));
        replaceInfoObject(this.cameraDraggerAndPincherZoomer);
    }
    public void useArrowDrawer(View view){
        highlightButton((Button) findViewById(R.id.complexArrowButton));
        replaceInfoObject(this.complexArrowDrawer);
    }
    public void useZeroFinder(View view){
        highlightButton((Button) findViewById(R.id.complexZeroButton));
        replaceInfoObject(this.complex0Finder);
    }
}
