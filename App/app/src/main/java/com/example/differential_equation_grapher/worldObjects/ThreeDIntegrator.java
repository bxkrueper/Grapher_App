package com.example.differential_equation_grapher.worldObjects;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.EditText;

import com.example.differential_equation_grapher.R;
import com.example.differential_equation_grapher.ThreeDGrapherActivity;
import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.ExpressionFactory;
import com.example.differential_equation_grapher.helper.CustomKeyboard;
import com.example.differential_equation_grapher.myAlgs.MyMath;
import com.example.differential_equation_grapher.world.DrawScreenWorldObject;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.LongPressReactWorldObject;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class ThreeDIntegrator extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject, OneFingerReactWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "ThreeDIntegrator";
    private static final float LINE_THICKNESS = 0.01f;//in inches
    private float x0;//does not need to be lower than x0. Lowest and highest are calculated when needed
    private float x1;
    private float y0;//does not need to be lower than y0. Lowest and highest are calculated when needed
    private float y1;
    private int sampleCount;//one d (a sample count of 5 will calculate 25 rectangular prisms
    private float volume;
    ThreeDFunctionObject functionObject;

    public ThreeDIntegrator(World world, int sampleCount, ThreeDFunctionObject functionObject) {
        super(world);
        this.x0 = 0;
        this.x1 = 0;
        this.y0 = 0;
        this.y1 = 0;
        this.sampleCount = sampleCount;
        this.volume = 0;
        this.functionObject = functionObject;
//        this.changeRightNext = true;
    }

    //world only re-draws on actions, so helper objects need to re-calculate before every redraw
    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        float left = Math.min(x0,x1);
        float right = Math.max(x0,x1);
        float bottom = Math.min(y0,y1);
        float top = Math.max(y0,y1);

        float dx = (right - left)/sampleCount;
        float dy = (top-bottom)/sampleCount;
        float halfdx = dx/2;
        float halfdy = dy/2;

        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.integration));
        canvas.drawRect(x0,y1,x1,y0,paint);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        paint.setStyle(Paint.Style.STROKE);
        int xCount=0;
        volume=0;
        for(float x = left+halfdx; xCount<sampleCount; x+=dx,xCount++){
            int yCount=0;
            for(float y = bottom+halfdy;yCount<sampleCount;y+=dy,yCount++){
                double answer = functionObject.getFunction().evaluate(x,y);
                volume+=answer*dx*dy;
                canvas.drawRect(x-halfdx,y+halfdy,x+halfdx,y-halfdy,paint);
            }
        }
        paint.setStyle(Paint.Style.FILL);//set back for later
    }



    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setTextSize(100);
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
        String string = "volume: " + MyMath.toNiceString(volume,5);
        canvas.drawText(string,0,100,paint);
    }

    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        float unRoundedNumber = inputStatus.getCurrentFingerWorldX();
        CartesianGrid cartesianGrid = (CartesianGrid) getWorld().getWorldObject("CartesianGrid");
        if(cartesianGrid ==null){
            x0 = unRoundedNumber;
        }else{
            float spacing = cartesianGrid.getMinorSpacingX(getWorld().getWorldView().getCamera());
            x0 = (float) MyMath.roundToNearestMultipleOf(unRoundedNumber,spacing);
        }
        x1 = x0;

        unRoundedNumber = inputStatus.getCurrentFingerWorldY();
        if(cartesianGrid ==null){
            y0 = unRoundedNumber;
        }else{
            float spacing = cartesianGrid.getMinorSpacingY(getWorld().getWorldView().getCamera());
            y0 = (float) MyMath.roundToNearestMultipleOf(unRoundedNumber,spacing);
        }
        y1 = y0;
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        float unRoundedNumber = inputStatus.getCurrentFingerWorldX();
        CartesianGrid cartesianGrid = (CartesianGrid) getWorld().getWorldObject("CartesianGrid");
        if(cartesianGrid ==null){
            x1 = unRoundedNumber;
        }else{
            float spacing = cartesianGrid.getMinorSpacingX(getWorld().getWorldView().getCamera());
            x1 = (float) MyMath.roundToNearestMultipleOf(unRoundedNumber,spacing);
        }

        unRoundedNumber = inputStatus.getCurrentFingerWorldY();
        if(cartesianGrid ==null){
            y1 = unRoundedNumber;
        }else{
            float spacing = cartesianGrid.getMinorSpacingY(getWorld().getWorldView().getCamera());
            y1 = (float) MyMath.roundToNearestMultipleOf(unRoundedNumber,spacing);
        }
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        //do nothing
    }

    @Override
    public void reactToLongPress(InputStatus inputStatus) {
        x0Prompt();
    }

    private void x0Prompt(){
        ThreeDGrapherActivity activity = (ThreeDGrapherActivity) getWorld().getWorldView().getActivity();

        //calls inputFinished as well as summmons yPrompt
        CustomKeyboard.OnEnter onEnter = new CustomKeyboard.OnEnter() {
            @Override
            public void doOnEnter(EditText editText) {
//                .inputFinished();//do not call this. go strait to the next prompt
                x1Prompt();
            }

            //make sure not to close the keyboard so it will still be up for the next input
            @Override
            public boolean closeKeyboardOnEnter() {
                return false;
            }
        };


        activity.getInput("x0=",new ThisOnInputX0(activity),onEnter);
    }
    private void x1Prompt(){
        ThreeDGrapherActivity activity = (ThreeDGrapherActivity) getWorld().getWorldView().getActivity();

        //calls inputFinished as well as summmons yPrompt
        CustomKeyboard.OnEnter onEnter = new CustomKeyboard.OnEnter() {
            @Override
            public void doOnEnter(EditText editText) {
//                .inputFinished();//do not call this. go strait to the next prompt
                y0Prompt();
            }

            //make sure not to close the keyboard so it will still be up for the next input
            @Override
            public boolean closeKeyboardOnEnter() {
                return false;
            }
        };


        activity.getInput("x1=",new ThisOnInputX1(activity),onEnter);
    }
    private void y0Prompt(){
        ThreeDGrapherActivity activity = (ThreeDGrapherActivity) getWorld().getWorldView().getActivity();

        CustomKeyboard.OnEnter onEnter = new CustomKeyboard.OnEnter() {
            @Override
            public void doOnEnter(EditText editText) {
//                .inputFinished();//do not call this. go strait to the next prompt
                y1Prompt();
            }

            //make sure not to close the keyboard so it will still be up for the next input
            @Override
            public boolean closeKeyboardOnEnter() {
                return false;
            }
        };


        activity.getInput("y0=",new ThisOnInputY0(activity),onEnter);
    }
    private void y1Prompt(){
        ThreeDGrapherActivity activity = (ThreeDGrapherActivity) getWorld().getWorldView().getActivity();

        CustomKeyboard.OnEnter onEnter = new CustomKeyboard.OnEnter() {
            @Override
            public void doOnEnter(EditText editText) {
//                .inputFinished();//do not call this. go strait to the next prompt
                sampleCountPrompt();
            }

            //make sure not to close the keyboard so it will still be up for the next input
            @Override
            public boolean closeKeyboardOnEnter() {
                return false;
            }
        };


        activity.getInput("y1=",new ThisOnInputY1(activity),onEnter);
    }
    private void sampleCountPrompt(){
        ThreeDGrapherActivity activity = (ThreeDGrapherActivity) getWorld().getWorldView().getActivity();
        activity.getInput("sampleCount=",new ThisOnInputSampleCount(activity));
    }

    private class ThisOnInputX0 implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInputX0(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void doOnInput(EditText editText) {
            String stringInEditText = editText.getText().toString();
            double input = ExpressionFactory.getRealNumber(stringInEditText);
            if(Double.isNaN(input)){
                editText.setTextColor(activity.getResources().getColor(R.color.invalidText));
            }else{
                editText.setTextColor(activity.getResources().getColor(R.color.validText));
            }

            x0 = (float) input;
            getWorld().getWorldView().redraw();

        }
    }

    private class ThisOnInputX1 implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInputX1(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void doOnInput(EditText editText) {
            String stringInEditText = editText.getText().toString();
            double input = ExpressionFactory.getRealNumber(stringInEditText);
            if(Double.isNaN(input)){
                editText.setTextColor(activity.getResources().getColor(R.color.invalidText));
            }else{
                editText.setTextColor(activity.getResources().getColor(R.color.validText));
            }

            x1 = (float) input;
            getWorld().getWorldView().redraw();

        }
    }

    private class ThisOnInputY0 implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInputY0(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void doOnInput(EditText editText) {
            String stringInEditText = editText.getText().toString();
            double input = ExpressionFactory.getRealNumber(stringInEditText);
            if(Double.isNaN(input)){
                editText.setTextColor(activity.getResources().getColor(R.color.invalidText));
            }else{
                editText.setTextColor(activity.getResources().getColor(R.color.validText));
            }

            y0 = (float) input;
            getWorld().getWorldView().redraw();

        }
    }

    private class ThisOnInputY1 implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInputY1(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void doOnInput(EditText editText) {
            String stringInEditText = editText.getText().toString();
            double input = ExpressionFactory.getRealNumber(stringInEditText);
            if(Double.isNaN(input)){
                editText.setTextColor(activity.getResources().getColor(R.color.invalidText));
            }else{
                editText.setTextColor(activity.getResources().getColor(R.color.validText));
            }

            y1 = (float) input;
            getWorld().getWorldView().redraw();

        }
    }

    private class ThisOnInputSampleCount implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInputSampleCount(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void doOnInput(EditText editText) {
            String stringInEditText = editText.getText().toString();
            double input = ExpressionFactory.getRealNumber(stringInEditText);
            if(Double.isNaN(input)){
                editText.setTextColor(activity.getResources().getColor(R.color.invalidText));
            }else{
                editText.setTextColor(activity.getResources().getColor(R.color.validText));
            }

            sampleCount = (int) input;
            getWorld().getWorldView().redraw();
        }
    }




}
