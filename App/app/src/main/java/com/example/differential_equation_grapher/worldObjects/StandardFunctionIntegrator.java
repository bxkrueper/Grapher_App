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

public class StandardFunctionIntegrator extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject, OneFingerReactWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "StandardFuncIntegrator";
    private float x0;//does not need to be lower than x0. Lowest and highest are calculated when needed
    private float x1;
    private int sampleCount;
    StandardFunctionObject functionObject;
    private float area;

    public StandardFunctionIntegrator(World world, int sampleCount, StandardFunctionObject functionObject) {
        super(world);
        this.x0 = 0;
        this.x1 = 0;
        this.area = 0;
        this.sampleCount = sampleCount;
        this.functionObject = functionObject;
    }

    //world only re-draws on actions, so helper objects need to re-calculate before every redraw
    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        float left = Math.min(x0,x1);
        float right = Math.max(x0,x1);
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.integration));
        float dx = (right - left)/sampleCount;
        if(dx==0){
            return;
        }
        float halfdx = dx/2;
        area = 0;
        for(float x = left +halfdx; x<right; x+=dx){
            float y = (float) functionObject.getFunction().evaluate(x);
            area+=y;//multiplying by dx is done at the end to the whole some for efficiency
            canvas.drawRect(x-halfdx,y,x+halfdx,0,paint);
        }
        area*=dx;
    }



    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        float left = Math.min(x0,x1);
        float right = Math.max(x0,x1);
        paint.setTextSize(100);
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
        String string = "area: " + MyMath.toNiceString(area,5);
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


//    private class InputX0Prompt implements InputPopUpPrompt.InputSenderDialogListener{
//        @Override
//        public void onOK(final String stringInBox) {
//            double input = ExpressionFactory.getRealNumber(stringInBox);
//            Log.d(CLASS_NAME, "The user tapped OK, number is "+stringInBox+ " converted: " + input);
//
//            x0 = (float) input;
//            getWorld().getWorldView().redraw();
//
//            x1Prompt();
//        }
//
//        @Override
//        public void onCancel(String stringInBox) {
//            Log.d(CLASS_NAME, "The user tapped Cancel, number is "+stringInBox);
//            x0 = Float.NaN;
//            getWorld().getWorldView().redraw();
//
//            x1Prompt();
//        }
//    }
//
//    private class InputX1Prompt implements InputPopUpPrompt.InputSenderDialogListener{
//        @Override
//        public void onOK(final String stringInBox) {
//            double input = ExpressionFactory.getRealNumber(stringInBox);
//            Log.d(CLASS_NAME, "The user tapped OK, number is "+stringInBox+ " converted: " + input);
//
//            x1 = (float) input;
//            getWorld().getWorldView().redraw();
//
//            sampleCountPrompt();
//        }
//
//        @Override
//        public void onCancel(String stringInBox) {
//            Log.d(CLASS_NAME, "The user tapped Cancel, number is "+stringInBox);
//            x1 = Float.NaN;
//            getWorld().getWorldView().redraw();
//
//            sampleCountPrompt();
//        }
//    }
//
//    private class SampleCountPrompt implements InputPopUpPrompt.InputSenderDialogListener{
//        @Override
//        public void onOK(final String stringInBox) {
//            double input = ExpressionFactory.getRealNumber(stringInBox);
//            Log.d(CLASS_NAME, "The user tapped OK, number is "+stringInBox+ " converted: " + input);
//
//            sampleCount = (int) input;
//            getWorld().getWorldView().redraw();
//
//        }
//
//        @Override
//        public void onCancel(String stringInBox) {
//            Log.d(CLASS_NAME, "The user tapped Cancel, number is "+stringInBox);
//            sampleCount = 1;
//            getWorld().getWorldView().redraw();
//        }
//    }


}
