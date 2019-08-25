package com.example.differential_equation_grapher.worldObjects;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.EditText;

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

public class StandardFunctionLengthFinder extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject, OneFingerReactWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "StandardFunctionLengthFinder";
    private static final float LINE_THICKNESS = 0.03f;//in inches
    private float x0;//does not need to be lower than x0. Lowest and highest are calculated when needed
    private float x1;
    private int sampleCount;
    private float length;
    StandardFunctionObject functionObject;

    public StandardFunctionLengthFinder(World world, int sampleCount, StandardFunctionObject functionObject) {
        super(world);
        this.x0 = 0;
        this.x1 = 0;
        this.sampleCount = sampleCount;
        this.length = 0;
        this.functionObject = functionObject;
    }

    //world only re-draws on actions, so helper objects need to re-calculate before every redraw
    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        float left = Math.min(x0,x1);
        float right = Math.max(x0,x1);

        //draw left and right boundaries
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.line_boundary));
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        canvas.drawLine(left,camera.getLowestY(),left,camera.getHighestY(),paint);
        canvas.drawLine(right,camera.getLowestY(),right,camera.getHighestY(),paint);

        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.length));
        float dx = (right - left)/sampleCount;
        if(dx==0){
            return;
        }
        this.length = 0;
        float prevX = left;
        float prevY = (float) functionObject.getFunction().evaluate(prevX);
        for(float x = left+dx; x<right; x+=dx){
            float y = (float) functionObject.getFunction().evaluate(x);
            canvas.drawLine(prevX, prevY,x,y,paint);
            length+=Math.hypot(x-prevX,y-prevY);

            prevX = x;
            prevY = y;
        }
        float y = (float) functionObject.getFunction().evaluate(right);
        canvas.drawLine(prevX, prevY,right,y,paint);
        length+=Math.hypot(right-prevX,y-prevY);
    }



    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setTextSize(100);
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
        String string = "length: " + MyMath.toNiceString(length,5);
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
        StandardGrapherActivity activity = (StandardGrapherActivity) getWorld().getWorldView().getActivity();

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
        StandardGrapherActivity activity = (StandardGrapherActivity) getWorld().getWorldView().getActivity();

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


        activity.getInput("x1=",new ThisOnInputX1(activity),onEnter);
    }
    private void sampleCountPrompt(){
        StandardGrapherActivity activity = (StandardGrapherActivity) getWorld().getWorldView().getActivity();
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
