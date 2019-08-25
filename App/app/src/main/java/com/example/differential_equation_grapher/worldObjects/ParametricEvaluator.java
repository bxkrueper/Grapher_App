package com.example.differential_equation_grapher.worldObjects;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.EditText;

import com.example.differential_equation_grapher.ParametricGrapherActivity;
import com.example.differential_equation_grapher.R;
import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.ExpressionFactory;
import com.example.differential_equation_grapher.helper.CustomKeyboard;
import com.example.differential_equation_grapher.helper.InputPopUpPrompt;
import com.example.differential_equation_grapher.myAlgs.MyMath;
import com.example.differential_equation_grapher.world.DrawScreenWorldObject;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.LongPressReactWorldObject;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

//shows a function's value at the x pointed at
public class ParametricEvaluator extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject , OneFingerReactWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "ParametricEvaluator";
    private static final float POINT_SIZE = 0.04f;//in inches
    private float tToEvaluate;
    private float xEvaluated;
    private float yEvaluated;
    ParametricFunctionObject functionObject;

    public ParametricEvaluator(World world, ParametricFunctionObject functionObject) {
        super(world);
        this.tToEvaluate = 0f;
        this.xEvaluated = Float.NaN;
        this.yEvaluated = Float.NaN;
        this.functionObject = functionObject;
    }

    //world only re-draws on actions, so helper objects need to re-calculate before every redraw
    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        recalculate();
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.point));
        drawPoint(xEvaluated,yEvaluated,canvas,paint,camera);
    }

    private void drawPoint(float x, float y, Canvas canvas, Paint paint, Camera camera) {
        canvas.drawCircle(x,y,POINT_SIZE*camera.getUnitsPerInchX(),paint);
    }

    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setTextSize(100);
        String string = "f(" + MyMath.toNiceString(tToEvaluate,5) + ") = (" + MyMath.toNiceString(xEvaluated,5) + "," + MyMath.toNiceString(yEvaluated,5)+")";
        canvas.drawText(string,0,100,paint);
        functionObject.drawTRange(functionObject.getTStart(),tToEvaluate,paint,canvas,camera);
    }

    //set the x value to evaluate the function at
    //if there is a grid, snap to the nearest minor interval
    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        Camera camera = getWorld().getWorldView().getCamera();
        float fingerRatio = inputStatus.getCurrentFingerScreenX()/camera.getScreenWidth();//percentage of the screen the finger is from the left
        tToEvaluate = functionObject.getTStart()+ (functionObject.getTEnd()-functionObject.getTStart())*fingerRatio;

        recalculate();
        getWorld().getWorldView().redraw();
    }

    //recalculates y after xToEvaluate has changed
    private void recalculate() {
        xEvaluated = (float) functionObject.getFunctionX().evaluate(tToEvaluate);
        yEvaluated = (float) functionObject.getFunctionY().evaluate(tToEvaluate);
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        reactToOneFingerDown(inputStatus);
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        //do nothing
    }

    @Override
    public void reactToLongPress(InputStatus inputStatus) {
        ParametricGrapherActivity activity = (ParametricGrapherActivity) getWorld().getWorldView().getActivity();
        activity.getInput("t=",new ThisOnInput(activity));
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

            tToEvaluate = (float) input;
            getWorld().getWorldView().redraw();
        }
    }
}
