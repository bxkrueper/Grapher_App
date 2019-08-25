package com.example.differential_equation_grapher.worldObjects;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.EditText;

import com.example.differential_equation_grapher.R;
import com.example.differential_equation_grapher.ThreeDGrapherActivity;
import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.ExpressionFactory;
import com.example.differential_equation_grapher.function.StandardFunctionMultiParam;
import com.example.differential_equation_grapher.helper.CustomKeyboard;
import com.example.differential_equation_grapher.myAlgs.MyMath;
import com.example.differential_equation_grapher.world.DrawScreenWorldObject;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.LongPressReactWorldObject;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class ThreeDGradientEvaluator extends WorldObject implements DrawableWorldObject, OneFingerReactWorldObject, DrawScreenWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "ThreeDGradientEvaluator";
    private static final float LINE_THICKNESS = 0.02f;//in inches
    private static final float POINT_SIZE = 0.04f;//in inches
    private static final float LINE_LENGTH = 0.3f;//in inches

    ThreeDFunctionObject threeDFunctionObject;

    private float xToEvaluate;
    private float yToEvaluate;
    private float gradX;
    private float gradY;


    public ThreeDGradientEvaluator(World world,ThreeDFunctionObject threeDFunctionObject) {
        super(world);
        this.threeDFunctionObject = threeDFunctionObject;
        this.xToEvaluate = 0;
        this.yToEvaluate = 0;
        this.gradX = 0;
        this.gradY = 0;
    }

    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        recalculate();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);

        if(gradX==0&&gradY==0){
            //draw point
            drawPoint(xToEvaluate,yToEvaluate,canvas,paint,camera);
        }else{
            //draw arrow
            double hypot = Math.hypot(gradX,gradY);
            float standardX = (float) (gradX/hypot);
            float standardY = (float) (gradY/hypot);
            float arrowXSpan = standardX*camera.getUnitsPerInchX()*LINE_LENGTH;
            float arrowYSpan = standardY*camera.getUnitsPerInchY()*LINE_LENGTH;
            drawArrow(xToEvaluate,yToEvaluate,xToEvaluate+arrowXSpan,yToEvaluate+arrowYSpan,paint,canvas,camera);
        }

    }

    private void drawPoint(float x, float y, Canvas canvas, Paint paint, Camera camera) {
        canvas.drawCircle(x,y,POINT_SIZE*camera.getUnitsPerInchX(),paint);
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
        String string = "f'(" + xToEvaluate + "," + yToEvaluate + ")=";
        String answer = "<"+ gradX + "," + gradY + ">";
        if(string.length()>21){//the string is too long and might clip out of the right side of the screen
            canvas.drawText(string,0,100,paint);
            canvas.drawText(answer,0,200,paint);
        }else{
            canvas.drawText(string+answer,0,100,paint);
        }

//        canvas.drawText("f'x = " + threeDFunctionObject.getPartialDerivativeX(),0,300,paint);
//        canvas.drawText("f'y = " + threeDFunctionObject.getPartialDerivativeY(),0,400,paint);
    }


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

        unRoundedNumber = inputStatus.getCurrentFingerWorldY();
        if(cartesianGrid ==null){
            yToEvaluate = unRoundedNumber;
        }else{
            float spacing = cartesianGrid.getMinorSpacingY(getWorld().getWorldView().getCamera());
            yToEvaluate = (float) MyMath.roundToNearestMultipleOf(unRoundedNumber,spacing);
        }


        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        xToEvaluate = inputStatus.getCurrentFingerWorldX();
        yToEvaluate = inputStatus.getCurrentFingerWorldY();

        getWorld().getWorldView().redraw();
    }

    private void recalculate() {
        this.gradX = threeDFunctionObject.getGradientX(xToEvaluate,yToEvaluate);
        this.gradY = threeDFunctionObject.getGradientY(xToEvaluate,yToEvaluate);
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

            xToEvaluate = (float) input;
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

            yToEvaluate = (float) input;
            getWorld().getWorldView().redraw();
        }
    }

}
