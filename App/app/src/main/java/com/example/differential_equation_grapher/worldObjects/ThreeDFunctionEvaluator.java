package com.example.differential_equation_grapher.worldObjects;

import android.app.Activity;
import android.graphics.Canvas;
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

public class ThreeDFunctionEvaluator extends WorldObject implements DrawableWorldObject, OneFingerReactWorldObject, DrawScreenWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "ThreeDEvaluator";
    private static final float POINT_SIZE = 15;//in pixels

    ThreeDFunctionObject threeDFunctionObject;

    private float xInput;
    private float yInput;
    private float zAnswer;

    public ThreeDFunctionEvaluator(World world,ThreeDFunctionObject threeDFunctionObject) {
        super(world);
        this.threeDFunctionObject = threeDFunctionObject;
        this.xInput = 0;
        this.yInput = 0;
        this.zAnswer = Float.NaN;
    }

    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        recalculate();
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.point));
        drawPoint(xInput,yInput,canvas,paint,camera);
    }

    private void drawPoint(float x, float y, Canvas canvas, Paint paint, Camera camera) {
        canvas.drawCircle(x,y,POINT_SIZE*camera.getUnitsPerPixelX(),paint);
    }

    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
        paint.setTextSize(100);
        String string = "Z(" + MyMath.toNiceString(xInput,5) + "," + MyMath.toNiceString(yInput,5) + ")=" + MyMath.toNiceString(zAnswer,5);
        if(string.length()>21){//the string is too long and might clip out of the right side of the screen
            canvas.drawText("f(" + MyMath.toNiceString(xInput,5) + "," + MyMath.toNiceString(yInput,5) + ")=",0,100,paint);
            canvas.drawText(MyMath.toNiceString(zAnswer,5),0,200,paint);
        }else{
            canvas.drawText(string,0,100,paint);
        }

    }


    //if there is a grid, snap to the nearest minor interval
    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        float unRoundedNumber = inputStatus.getCurrentFingerWorldX();
        CartesianGrid cartesianGrid = (CartesianGrid) getWorld().getWorldObject("CartesianGrid");
        if(cartesianGrid ==null){
            xInput = unRoundedNumber;
        }else{
            float spacing = cartesianGrid.getMinorSpacingX(getWorld().getWorldView().getCamera());
            xInput = (float) MyMath.roundToNearestMultipleOf(unRoundedNumber,spacing);
        }

        unRoundedNumber = inputStatus.getCurrentFingerWorldY();
        if(cartesianGrid ==null){
            yInput = unRoundedNumber;
        }else{
            float spacing = cartesianGrid.getMinorSpacingY(getWorld().getWorldView().getCamera());
            yInput = (float) MyMath.roundToNearestMultipleOf(unRoundedNumber,spacing);
        }

        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        xInput = inputStatus.getCurrentFingerWorldX();
        yInput = inputStatus.getCurrentFingerWorldY();

        getWorld().getWorldView().redraw();
    }

    private void recalculate() {
        zAnswer = (float) threeDFunctionObject.getFunction().evaluate(xInput,yInput);
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        //do nothing
    }

    @Override
    public void reactToLongPress(InputStatus inputStatus) {
        xPrompt();
    }

    private void xPrompt(){
        final ThreeDGrapherActivity activity = (ThreeDGrapherActivity) getWorld().getWorldView().getActivity();

        //calls inputFinished as well as summmons yPrompt
        CustomKeyboard.OnEnter onEnter = new CustomKeyboard.OnEnter() {
            @Override
            public void doOnEnter(EditText editText) {
//                ((ThreeDGrapherActivity) getWorld().getWorldView().getActivity()).inputFinished();//do not call this. go strait to the next prompt
                yPrompt();
            }

            //make sure not to close the keyboard so it will still be up for the next input
            @Override
            public boolean closeKeyboardOnEnter() {
                return false;
            }
        };

        activity.getInput("x=",new ThisOnInputX(activity),onEnter);//onEnter replaces the activitie's normal onEnterClass
    }
    private void yPrompt(){
        ThreeDGrapherActivity activity = (ThreeDGrapherActivity) getWorld().getWorldView().getActivity();
        activity.getInput("y=",new ThisOnInputY(activity));
    }


    private class ThisOnInputX implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInputX(Activity activity) {
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

            xInput = (float) input;
            getWorld().getWorldView().redraw();


        }
    }

    private class ThisOnInputY implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInputY(Activity activity) {
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

            yInput = (float) input;
            getWorld().getWorldView().redraw();
        }
    }

//    private class InputXPrompt implements InputPopUpPrompt.InputSenderDialogListener{
//        @Override
//        public void onOK(final String stringInBox) {
//            double input = ExpressionFactory.getRealNumber(stringInBox);
//            Log.d(CLASS_NAME, "The user tapped OK, number is "+stringInBox+ " converted: " + input);
//
//            xInput = (float) input;
//            getWorld().getWorldView().redraw();
//
//            yPrompt();
//        }
//
//        @Override
//        public void onCancel(String stringInBox) {
//            Log.d(CLASS_NAME, "The user tapped Cancel, number is "+stringInBox);
//            xInput = Float.NaN;
//            getWorld().getWorldView().redraw();
//
//            yPrompt();
//        }
//    }
//
//    private class InputYPrompt implements InputPopUpPrompt.InputSenderDialogListener{
//        @Override
//        public void onOK(final String stringInBox) {
//            double input = ExpressionFactory.getRealNumber(stringInBox);
//            Log.d(CLASS_NAME, "The user tapped OK, number is "+stringInBox+ " converted: " + input);
//
//            yInput = (float) input;
//            getWorld().getWorldView().redraw();
//        }
//
//        @Override
//        public void onCancel(String stringInBox) {
//            Log.d(CLASS_NAME, "The user tapped Cancel, number is "+stringInBox);
//            yInput = Float.NaN;
//            getWorld().getWorldView().redraw();
//        }
//    }

}
