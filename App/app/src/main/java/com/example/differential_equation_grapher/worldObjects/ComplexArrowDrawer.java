package com.example.differential_equation_grapher.worldObjects;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.EditText;

import com.example.differential_equation_grapher.Complex_Grapher;
import com.example.differential_equation_grapher.R;
import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.Complex;
import com.example.differential_equation_grapher.function.ComplexRI;
import com.example.differential_equation_grapher.function.ExpressionFactory;
import com.example.differential_equation_grapher.helper.CustomKeyboard;
import com.example.differential_equation_grapher.myAlgs.MyMath;
import com.example.differential_equation_grapher.world.DrawScreenWorldObject;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.LongPressReactWorldObject;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class ComplexArrowDrawer extends WorldObject implements DrawableWorldObject, OneFingerReactWorldObject, DrawScreenWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "ComplexArrowDrawer";
    private static final float ROUND_DISPLAY_ANSWER_TO = 0.000001f;
    private static final float LINE_THICKNESS = 0.02f;//in inches

    ComplexFunctionObject complexFunctionObject;

    private Complex arrowSource;
    private Complex arrowTip;

    public ComplexArrowDrawer(World world,ComplexFunctionObject complexFunctionObject) {
        super(world);
        this.complexFunctionObject = complexFunctionObject;
        this.arrowSource = new ComplexRI(0,0);
        this.arrowTip = new ComplexRI(0,0);
    }

    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        recalculate();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        drawArrow((float) arrowSource.getReal(),(float) arrowSource.getImaginary(),(float) arrowTip.getReal(),(float) arrowTip.getImaginary(),paint,canvas,camera);
    }

    private void drawArrow(float x, float y, float arrowTipX, float arrowTipY, Paint paint, Canvas canvas,Camera camera) {
        final float HEAD_SIZE_INCHES = 0.1f;
        //draw arrow shaft
        canvas.drawLine(x,y,arrowTipX,arrowTipY,paint);

        //draw arrow head
        float headLength = HEAD_SIZE_INCHES*camera.getUnitsPerInchX();/////just using getUnitsPerInchX, not y
        double direction = MyMath.getDirection(x,y,arrowTipX,arrowTipY);//current value: direction arrow is pointing
        direction += Math.PI*3/4;//current value: direction from tip to left shaft
        canvas.drawLine(arrowTipX,arrowTipY,(float) (arrowTipX+headLength*Math.cos(direction)), (float) (arrowTipY+headLength*Math.sin(direction)),paint);
        direction += Math.PI/2;//current value: direction from tip to right shaft
        canvas.drawLine(arrowTipX,arrowTipY,(float) (arrowTipX+headLength*Math.cos(direction)), (float) (arrowTipY+headLength*Math.sin(direction)),paint);
    }

    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
        paint.setTextSize(100);
        String string = "f(" + arrowSource.round(ROUND_DISPLAY_ANSWER_TO).toDisplayString() + ")=" + arrowTip.round(ROUND_DISPLAY_ANSWER_TO).toDisplayString();
        if(string.length()>21){//the string is too long and might clip out of the right side of the screen
            canvas.drawText("f(" + arrowSource.round(ROUND_DISPLAY_ANSWER_TO).toDisplayString() + ")=",0,100,paint);
            canvas.drawText(arrowTip.round(ROUND_DISPLAY_ANSWER_TO).toDisplayString(),0,200,paint);
        }else{
            canvas.drawText(string,0,100,paint);
        }

//        string = complexFunctionObject.getFunction().getFunction().toDisplayString();
//        canvas.drawText(string,0,200,paint);
    }


    //if there is a grid, snap to the nearest minor interval
    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        float unRoundedNumber = inputStatus.getCurrentFingerWorldX();
        CartesianGrid cartesianGrid = (CartesianGrid) getWorld().getWorldObject("CartesianGrid");
        float arrowSourceReal;
        float arrowSourceImaginary;
        if(cartesianGrid ==null){
            arrowSourceReal = unRoundedNumber;
        }else{
            float spacing = cartesianGrid.getMinorSpacingX(getWorld().getWorldView().getCamera());
            arrowSourceReal = (float) MyMath.roundToNearestMultipleOf(unRoundedNumber,spacing);
        }

        unRoundedNumber = inputStatus.getCurrentFingerWorldY();
        if(cartesianGrid ==null){
            arrowSourceImaginary = unRoundedNumber;
        }else{
            float spacing = cartesianGrid.getMinorSpacingY(getWorld().getWorldView().getCamera());
            arrowSourceImaginary = (float) MyMath.roundToNearestMultipleOf(unRoundedNumber,spacing);
        }

        arrowSource = new ComplexRI(arrowSourceReal,arrowSourceImaginary);

        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        float arrowSourceReal = inputStatus.getCurrentFingerWorldX();
        float arrowSourceImaginary = inputStatus.getCurrentFingerWorldY();

        arrowSource = new ComplexRI(arrowSourceReal,arrowSourceImaginary);

        getWorld().getWorldView().redraw();
    }

    private void recalculate() {
        this.arrowTip = complexFunctionObject.getFunction().evaluate(arrowSource);
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        //do nothing
    }

    @Override
    public void reactToLongPress(InputStatus inputStatus) {
        Complex_Grapher activity = (Complex_Grapher) getWorld().getWorldView().getActivity();
        activity.getInput("z=",new ThisOnInput(activity));
    }

    private class ThisOnInput implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInput(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void doOnInput(EditText editText) {
            String stringInEditText = editText.getText().toString();
            Complex input = ExpressionFactory.getComplexNumber(stringInEditText);
            if(Double.isNaN(input.getReal()) || Double.isNaN(input.getImaginary())){
                editText.setTextColor(activity.getResources().getColor(R.color.invalidText));
            }else{
                editText.setTextColor(activity.getResources().getColor(R.color.validText));
            }

            arrowSource = input;
            getWorld().getWorldView().redraw();
        }
    }

}
