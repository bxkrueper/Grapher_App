package com.example.differential_equation_grapher.worldObjects;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.EditText;

import com.example.differential_equation_grapher.R;
import com.example.differential_equation_grapher.StandardGrapherActivity;
import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.ExpressionFactory;
import com.example.differential_equation_grapher.helper.CustomKeyboard;
import com.example.differential_equation_grapher.helper.LineDrawer;
import com.example.differential_equation_grapher.myAlgs.MyMath;
import com.example.differential_equation_grapher.world.DrawScreenWorldObject;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.LongPressReactWorldObject;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

//shows a function's value at the y pointed at
public class StandardFunctionNewtonsMethod extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject, OneFingerReactWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "StandardFuncNewton";
    private static final int NEWTON_ITERATIONS = 30;
    private static final float POINT_SIZE = 0.04f;//in inches
    private static final float LINE_THICKNESS = 0.01f;//in inches
    private float x0;
    private float xEstimate;
    private float yEstimate;
    private float yGoal;
    StandardFunctionObject functionObject;
    private LineDrawer lineDrawer;

    public StandardFunctionNewtonsMethod(World world, StandardFunctionObject functionObject) {
        super(world);
        this.xEstimate = 0f;
        this.yEstimate = 0f;
        this.yGoal = 0f;
        this.x0 = 0f;
        this.functionObject = functionObject;
        this.lineDrawer = new LineDrawer();
    }

    //uses newton's method
    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.ai_path));
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        canvas.drawLine(camera.getLeft(),yGoal,camera.getRight(),yGoal,paint);
        lineDrawer.getReadyToDraw();
        lineDrawer.drawTo(x0,yGoal,canvas,paint);


        xEstimate = x0;
        //newton's method: x2==x1-(f(x1)-yGoal)/f'(x1)
        for(int i=0;i<NEWTON_ITERATIONS;i++){
            yEstimate = (float) functionObject.getFunction().evaluate(xEstimate);
            lineDrawer.drawTo(xEstimate,yEstimate,canvas,paint);
            xEstimate = xEstimate -(yEstimate-yGoal)/(float) functionObject.getDerivative().evaluate(xEstimate);
            lineDrawer.drawTo(xEstimate,yGoal,canvas,paint);
        }

        //keep y estimate updated
        yEstimate = (float) functionObject.getFunction().evaluate(xEstimate);

        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.point));
        drawPoint(xEstimate,yEstimate,canvas,paint,camera);
    }

    private void drawPoint(float x, float y, Canvas canvas, Paint paint, Camera camera) {
        canvas.drawCircle(x,y,POINT_SIZE*camera.getUnitsPerInchX(),paint);
    }


    //drawOnScreen is called after draw, so x should be calculated already
    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setTextSize(100);
        String string = "f(" + MyMath.toNiceString(xEstimate,5) + ") = " + MyMath.toNiceString(yEstimate,5);
        canvas.drawText(string,0,100,paint);
    }

    //set the x value to evaluate the function at
    //if there is a grid, snap to the nearest minor interval
    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        float unRoundedNumber = inputStatus.getCurrentFingerWorldY();
        CartesianGrid cartesianGrid = (CartesianGrid) getWorld().getWorldObject("CartesianGrid");
        if(cartesianGrid ==null){
            yGoal = unRoundedNumber;
        }else{
            float spacing = cartesianGrid.getMinorSpacingY(getWorld().getWorldView().getCamera());
            yGoal = (float) MyMath.roundToNearestMultipleOf(unRoundedNumber,spacing);
        }

        x0 = inputStatus.getCurrentFingerWorldX();

        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        yGoal = inputStatus.getCurrentFingerWorldY();
        x0 = inputStatus.getCurrentFingerWorldX();
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        //do nothing
    }

    @Override
    public void reactToLongPress(InputStatus inputStatus) {
        StandardGrapherActivity activity = (StandardGrapherActivity) getWorld().getWorldView().getActivity();
        activity.getInput("y=",new ThisOnInput(activity));

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

            yGoal = (float) input;
            getWorld().getWorldView().redraw();
        }
    }
}