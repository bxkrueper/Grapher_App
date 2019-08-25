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

//shows a function's value at the x pointed at
public class StandardFunctionEvaluatorX extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject , OneFingerReactWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "StandardFuncEvaluatorX";
    private static final float POINT_SIZE = 0.04f;//in inches
    private static final float LINE_THICKNESS = 0.02f;//in inches
    private float xToEvaluate;
    private float yEvaluated;
    StandardFunctionObject functionObject;

    public StandardFunctionEvaluatorX(World world, StandardFunctionObject functionObject) {
        super(world);
        this.xToEvaluate = 0f;
        this.yEvaluated = Float.NaN;
        this.functionObject = functionObject;
    }

    //world only re-draws on actions, so helper objects need to re-calculate before every redraw
    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        recalculate();
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.line_boundary));
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        canvas.drawLine(xToEvaluate,yEvaluated,xToEvaluate,0,paint);
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.point));
        drawPoint(xToEvaluate,yEvaluated,canvas,paint,camera);
    }

    private void drawPoint(float x, float y, Canvas canvas, Paint paint, Camera camera) {
        canvas.drawCircle(x,y,POINT_SIZE*camera.getUnitsPerInchX(),paint);
    }

    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setTextSize(100);
        String string = "f(" + MyMath.toNiceString(xToEvaluate,5) + ") = " + MyMath.toNiceString(yEvaluated,5);
        canvas.drawText(string,0,100,paint);


//        //        /////////
//        ScreenText screenText = (ScreenText) getWorld().getWorldObject("Screen Text");
////        ///////
//        screenText.setString(functionObject.getFunction().getFunction().toDisplayString());
////        /////////
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

        recalculate();
        getWorld().getWorldView().redraw();
    }

    //recalculates y after xToEvaluate has changed
    private void recalculate() {
        yEvaluated = (float) functionObject.getFunction().evaluate(xToEvaluate);
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        xToEvaluate = inputStatus.getCurrentFingerWorldX();
        recalculate();
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        //do nothing
    }

    @Override
    public void reactToLongPress(InputStatus inputStatus) {
        final StandardGrapherActivity activity = (StandardGrapherActivity) getWorld().getWorldView().getActivity();
        activity.getInput("x=",new ThisOnInput(activity));
//        InputPopUpPrompt inputPopUpPrompt = new InputPopUpPrompt(activity,activity.getKeyboardviewId(), "x=",new InputPopUpPrompt.InputSenderDialogListener() {
//            @Override
//            public void onOK(final String stringInBox) {
//                double input = ExpressionFactory.getRealNumber(stringInBox);
//                Log.d(CLASS_NAME, "The user tapped OK, number is "+stringInBox+ " converted: " + input);
//
//                xToEvaluate = (float) input;
//                getWorld().getWorldView().redraw();
//
//            }
//
//            @Override
//            public void onCancel(String stringInBox) {
//                Log.d(CLASS_NAME, "The user tapped Cancel, number is "+stringInBox);
//                xToEvaluate = Float.NaN;
//                getWorld().getWorldView().redraw();
//            }
//        });
//        inputPopUpPrompt.show();
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
