package com.example.differential_equation_grapher.worldObjects;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.EditText;

import com.example.differential_equation_grapher.PolarGrapherActivity;
import com.example.differential_equation_grapher.R;
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

//shows a function's value at the x pointed at
public class PolarEvaluator extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject , OneFingerReactWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "PolarEvaluatorX";
    private static final float POINT_SIZE = 0.04f;//in inches
    private static final float LINE_THICKNESS = 0.02f;//in inches
    private float thetaToEvaluate;
    private int windingNumber;//0: use MyMath.getDirection as is   1: add 2PI  -1: subtract 2PI  2: add 4PI...
    private int quadrantNumber;//1 is top right, 2 is top left, 3 is bottom left, 4 is bottom right. used to determint what to do with the winding number
    private float radiusEvaluated;
    private float xEvaluated;
    private float yEvaluated;
    PolarFunctionObject functionObject;

    public PolarEvaluator(World world, PolarFunctionObject functionObject) {
        super(world);
        this.thetaToEvaluate = 0f;
        this.radiusEvaluated = Float.NaN;
        this.xEvaluated = Float.NaN;
        this.yEvaluated = Float.NaN;
        this.windingNumber = 0;
        this.quadrantNumber = 0;

        this.functionObject = functionObject;
    }

    //world only re-draws on actions, so helper objects need to re-calculate before every redraw
    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        recalculate();
        if(radiusEvaluated>=0){
            paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.line_boundary));
        }else{
            paint.setColor(Color.MAGENTA);
        }

        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        canvas.drawLine(0, 0, xEvaluated,yEvaluated,paint);
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.point));
        drawPoint(xEvaluated, yEvaluated,canvas,paint,camera);
    }

    private void drawPoint(float x, float y, Canvas canvas, Paint paint, Camera camera) {
        canvas.drawCircle(x,y,POINT_SIZE*camera.getUnitsPerInchX(),paint);
    }

    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setTextSize(100);
        String string = "f(" + MyMath.toNiceString(thetaToEvaluate,5) + ") = " + MyMath.toNiceString(radiusEvaluated,5);
        canvas.drawText(string,0,100,paint);
        if(getWorld().getWorldObject("CartesianGrid")!=null){//if the world has a normal grid
            string = "x=" + MyMath.toNiceString(xEvaluated,5) + "  y=" + MyMath.toNiceString(yEvaluated,5);
            canvas.drawText(string,0,200,paint);
        }
    }

    //set the x value to evaluate the function at
    //if there is a grid, snap to the nearest minor interval
    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        float unRoundedNumber = (float) MyMath.getDirectionFromOrigin(inputStatus.getCurrentFingerWorldX(),inputStatus.getCurrentFingerWorldY());
        quadrantNumber = functionObject.getQuadrantNumber(inputStatus.getCurrentFingerWorldX(),inputStatus.getCurrentFingerWorldY());
        windingNumber = 0;
        Grid grid = (Grid) getWorld().getWorldObject("CartesianGrid");
        if(grid==null){
            thetaToEvaluate = unRoundedNumber;
        }else{
            thetaToEvaluate = grid.getClosestSignificantTheta(unRoundedNumber);
        }

        recalculate();
        getWorld().getWorldView().redraw();
    }



    //recalculates y after thetaToEvaluate has changed
    private void recalculate() {
        radiusEvaluated = (float) functionObject.getFunction().evaluate(thetaToEvaluate);
        xEvaluated = (float) functionObject.getFunction().polarToX(radiusEvaluated,thetaToEvaluate);
        yEvaluated = (float) functionObject.getFunction().polarToY(radiusEvaluated,thetaToEvaluate);
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        int prevQuadrant = quadrantNumber;
        quadrantNumber = functionObject.getQuadrantNumber(inputStatus.getCurrentFingerWorldX(),inputStatus.getCurrentFingerWorldY());
        if(prevQuadrant==4&&quadrantNumber==1){
            windingNumber++;
        }else if(prevQuadrant==1&&quadrantNumber==4){
            windingNumber--;
        }
        thetaToEvaluate = (float) MyMath.getDirectionFromOrigin(inputStatus.getCurrentFingerWorldX(),inputStatus.getCurrentFingerWorldY());
        thetaToEvaluate+=windingNumber*Math.PI*2.0;
        recalculate();
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        //do nothing
    }

    @Override
    public void reactToLongPress(InputStatus inputStatus) {
        PolarGrapherActivity activity = (PolarGrapherActivity) getWorld().getWorldView().getActivity();
        activity.getInput("theta=",new ThisOnInput(activity));
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

            thetaToEvaluate = (float) input;
            getWorld().getWorldView().redraw();
        }
    }
}
