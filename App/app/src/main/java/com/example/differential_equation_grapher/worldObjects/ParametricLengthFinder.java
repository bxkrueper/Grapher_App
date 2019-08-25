package com.example.differential_equation_grapher.worldObjects;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.EditText;

import com.example.differential_equation_grapher.ParametricGrapherActivity;
import com.example.differential_equation_grapher.R;
import com.example.differential_equation_grapher.StandardGrapherActivity;
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

public class ParametricLengthFinder extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject, OneFingerReactWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "ParametricLengthFinder";
    private static final float LINE_THICKNESS = 0.03f;//in inches
    private float t0;//does not need to be lower than t0. Lowest and highest are calculated when needed
    private float t1;
    private int sampleCount;
    private float length;
    ParametricFunctionObject functionObject;

    public ParametricLengthFinder(World world, int sampleCount, ParametricFunctionObject functionObject) {
        super(world);
        this.t0 = 0;
        this.t1 = 0;
        this.sampleCount = sampleCount;
        this.length = 0;
        this.functionObject = functionObject;
//        this.changeRightNext = true;
    }

    //world only re-draws on actions, so helper objects need to re-calculate before every redraw
    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        float lowT = Math.min(t0, t1);
        float highT = Math.max(t0, t1);
        float dt = (highT - lowT)/sampleCount;
        if(dt==0){
            return;
        }

        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.length));
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        this.length = 0;
        float prevX = (float) functionObject.getFunctionX().evaluate(lowT);
        float prevY = (float) functionObject.getFunctionY().evaluate(lowT);
        for(float t = lowT+dt; t<highT; t+=dt){
            float x = (float) functionObject.getFunctionX().evaluate(t);
            float y = (float) functionObject.getFunctionY().evaluate(t);
            canvas.drawLine(prevX, prevY,x,y,paint);
            length+=Math.hypot(x-prevX,y-prevY);

            prevX = x;
            prevY = y;
        }
        float x = (float) functionObject.getFunctionX().evaluate(highT);
        float y = (float) functionObject.getFunctionY().evaluate(highT);
        canvas.drawLine(prevX, prevY,x,y,paint);
        length+=Math.hypot(x-prevX,y-prevY);
    }



    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setTextSize(100);
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
        String string = "Length: " + MyMath.toNiceString(length,5);
        canvas.drawText(string,0,100,paint);

        functionObject.drawTRange(t0,t1,paint,canvas,camera);
    }

    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        t0=functionObject.getTFromXPositionInCamera(inputStatus.getCurrentFingerWorldX(),getWorld().getWorldView().getCamera());
        t1 = t0;
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        t1=functionObject.getTFromXPositionInCamera(inputStatus.getCurrentFingerWorldX(),getWorld().getWorldView().getCamera());
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {

    }

    @Override
    public void reactToLongPress(InputStatus inputStatus) {
        x0Prompt();
    }

    private void x0Prompt(){
        ParametricGrapherActivity activity = (ParametricGrapherActivity) getWorld().getWorldView().getActivity();

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


        activity.getInput("t0=",new ThisOnInputX0(activity),onEnter);
    }
    private void x1Prompt(){
        ParametricGrapherActivity activity = (ParametricGrapherActivity) getWorld().getWorldView().getActivity();

        //calls inputFinished as well as summmons yPrompt
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


        activity.getInput("t1=",new ThisOnInputX1(activity),onEnter);
    }
    private void sampleCountPrompt(){
        ParametricGrapherActivity activity = (ParametricGrapherActivity) getWorld().getWorldView().getActivity();
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

            t0 = (float) input;
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

            t1 = (float) input;
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
