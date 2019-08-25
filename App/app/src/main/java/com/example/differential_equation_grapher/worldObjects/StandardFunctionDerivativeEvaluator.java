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

public class StandardFunctionDerivativeEvaluator extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject, OneFingerReactWorldObject, LongPressReactWorldObject {
    private static final float LINE_THICKNESS = 0.02f;//in inches
    private static final String CLASS_NAME = "StandardFuncDerEval";
    private float xToEvaluate;
    StandardFunctionObject functionObject;
    float y;
    float slope;

    public StandardFunctionDerivativeEvaluator(World world, StandardFunctionObject functionObject) {
        super(world);
        this.xToEvaluate = 0f;
        this.functionObject = functionObject;
        this.y = Float.NaN;
        this.slope = Float.NaN;
    }

    //world only re-draws on actions, so helper objects need to re-calculate before every redraw
    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        recalculate();
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.derivative));

        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        drawTangent(xToEvaluate,y,slope,canvas,paint,camera);

    }



    private void drawTangent(float xToEvaluate, float y, float slope, Canvas canvas, Paint paint,Camera camera) {
        //names left and right assume standard axis orientation, but should work with flipped x axis
        float left = camera.getLowestX();
        float right = camera.getHighestX();
        float yTangentLeft = y-(xToEvaluate-left)*slope;
        float yTangentRight = y+(right-xToEvaluate)*slope;
        canvas.drawLine(left,yTangentLeft,right,yTangentRight,paint);
    }

    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        recalculate();
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
        paint.setTextSize(100);
        String string = "f'(" + MyMath.toNiceString(xToEvaluate,5) + ") = " + MyMath.toNiceString(slope,5);
        canvas.drawText(string,0,100,paint);
    }

    //set the x value to evaluate the function at
    //if there is a grid, snap to the nearest minor interval
    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        float unRoundedNumber = inputStatus.getCurrentFingerWorldX();
        CartesianGrid cartesianGrid = (CartesianGrid) getWorld().getWorldObject("CartesianGrid");
        if(cartesianGrid ==null){
            xToEvaluate = unRoundedNumber;
        }else{
            float spacing = cartesianGrid.getMinorSpacingX(getWorld().getWorldView().getCamera());
            xToEvaluate = (float) MyMath.roundToNearestMultipleOf(unRoundedNumber,spacing);
        }
        getWorld().getWorldView().redraw();
    }

    //recalculates y and slope after xToEvaluate has changed
    /////////make activity call this when function changed
    private void recalculate() {

        y = (float) functionObject.getFunction().evaluate(xToEvaluate);
        slope = (float) functionObject.getDerivative().evaluate(xToEvaluate);
    }



    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        xToEvaluate = inputStatus.getCurrentFingerWorldX();
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        //do nothing
    }

    @Override
    public void reactToLongPress(InputStatus inputStatus) {
        StandardGrapherActivity activity = (StandardGrapherActivity) getWorld().getWorldView().getActivity();
        activity.getInput("x=",new ThisOnInput(activity));

    }

    private class ThisOnInput implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInput(Activity activity) {
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

            xToEvaluate = (float) input;
            getWorld().getWorldView().redraw();
        }
    }
}

