package com.example.differential_equation_grapher.worldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.differential_equation_grapher.R;
import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.Complex;
import com.example.differential_equation_grapher.function.ComplexFunction;
import com.example.differential_equation_grapher.function.ComplexRI;
import com.example.differential_equation_grapher.function.Constant;
import com.example.differential_equation_grapher.function.Undefined;
import com.example.differential_equation_grapher.helper.LineDrawer;
import com.example.differential_equation_grapher.myAlgs.MyMath;
import com.example.differential_equation_grapher.world.DrawScreenWorldObject;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class Complex0Finder extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject, OneFingerReactWorldObject {
    private static final float CLOSE_ENOUGH = 0.000001f;
    private static final float ROUND_DISPLAY_ANSWER_TO = 0.000001f;
    private static final int MAX_ITERATIONS = 30;
    private static final float POINT_SIZE = 0.04f;//in inches
    private static final float LINE_THICKNESS = 0.02f;//in inches

    private ComplexFunctionObject functionObject;
    private Complex initialGuess;
    private Complex answer;
    private Constant actualFuncValue;

    LineDrawer lineDrawer;

    public Complex0Finder(World world, ComplexFunctionObject functionObject) {
        super(world);
        this.functionObject = functionObject;
        this.initialGuess = new ComplexRI(1,1);
        this.answer = new ComplexRI(0,0);
        this.actualFuncValue = new ComplexRI(0,0);
        lineDrawer = new LineDrawer();
    }

    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        paint.setColor(Color.BLACK);
        lineDrawer.getReadyToDraw();

        //compute and draw
        answer = initialGuess;
        lineDrawer.drawTo((float) answer.getReal(),(float) answer.getImaginary(),canvas,paint);
        int iterations = 0;
        while(!answer.isEqualTo(ComplexRI.additiveIdentity) && iterations<MAX_ITERATIONS){
            //newton's method: z1=z0-f(z0)/f'(z0)
            Constant nextAnswer = answer.subtract(functionObject.evaluate(answer).divide(functionObject.evaluateDerivative(answer))).convertToInstanceOf(ComplexRI.additiveIdentity);
            if(nextAnswer instanceof Complex){
                answer = (Complex) nextAnswer;
                lineDrawer.drawTo((float) answer.getReal(),(float) answer.getImaginary(),canvas,paint);
            }else{
                answer = ComplexRI.nan;
            }
            iterations++;
        }
        actualFuncValue = functionObject.evaluate(answer);

        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.point));
        drawPoint((float) answer.getReal(),(float) answer.getImaginary(),canvas,paint,camera);
    }

    private void drawPoint(float x, float y, Canvas canvas, Paint paint, Camera camera) {
        canvas.drawCircle(x,y,POINT_SIZE*camera.getUnitsPerInchX(),paint);
    }

    //draw on screen is done after draw, so values are already calculated
    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
        paint.setTextSize(100);
        String string = "f(" + answer.round(ROUND_DISPLAY_ANSWER_TO).toDisplayString() + ")=" + actualFuncValue.round(ROUND_DISPLAY_ANSWER_TO).toDisplayString();
        if(string.length()>21){//the string is too long and might clip out of the right side of the screen
            canvas.drawText("f(" + answer.round(ROUND_DISPLAY_ANSWER_TO).toDisplayString() + ")=",0,100,paint);
            canvas.drawText(actualFuncValue.round(ROUND_DISPLAY_ANSWER_TO).toDisplayString(),0,200,paint);
        }else{
            canvas.drawText(string,0,100,paint);
        }

//        //draw boundary
//        paint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(screenX1,screenY2,screenX2,screenY1,paint);
//        paint.setStyle(Paint.Style.FILL);//set back
//
//        if(!selecting){
//            paint.setTextSize(100);
//
//            paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
//            String string = answer.toDisplayString();
//            canvas.drawText(string,0,100,paint);
//        }
    }




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

        initialGuess = new ComplexRI(arrowSourceReal,arrowSourceImaginary);

        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        float arrowSourceReal = inputStatus.getCurrentFingerWorldX();
        float arrowSourceImaginary = inputStatus.getCurrentFingerWorldY();

        initialGuess = new ComplexRI(arrowSourceReal,arrowSourceImaginary);

        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        //do nothing
    }
}
