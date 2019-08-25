package com.example.differential_equation_grapher.worldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

import com.example.differential_equation_grapher.R;
import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.StandardFunction;
import com.example.differential_equation_grapher.function.Undefined;
import com.example.differential_equation_grapher.myAlgs.MyMath;
import com.example.differential_equation_grapher.world.DrawScreenWorldObject;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class StandardFunctionMaxMinFinder extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject, OneFingerReactWorldObject {
    private static final float POINT_SIZE = 0.04f;//in inches
    private static final float CLOSE_ENOUGH = 0.000001f;
    private static final float LINE_THICKNESS = 0.01f;//in inches
    private StandardFunctionObject functionObject;
    private float extremeX;
    private float extremeY;
    private float screenX1;
    private float screenX2;
    private boolean selecting;


    public StandardFunctionMaxMinFinder(World world, StandardFunctionObject functionObject) {
        super(world);
        this.functionObject = functionObject;
        this.extremeX = Float.NaN;
        this.extremeY = Float.NaN;
        this.screenX1 = 0;
        this.screenX2 = 0;
        this.selecting = false;
    }

    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        if(!selecting){
            recalculate();
            paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.point));
            drawPoint(extremeX,extremeY,canvas,paint,camera);
        }
    }

    private void drawPoint(float x, float y, Canvas canvas, Paint paint, Camera camera) {
        canvas.drawCircle(x,y,POINT_SIZE*camera.getUnitsPerInchX(),paint);
    }

    //draw on screen is done after draw, so values are already calculated
    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.line_boundary));
        paint.setStrokeWidth(camera.getPixelsPerInchX()*LINE_THICKNESS);
        canvas.drawLine(screenX1,0,screenX1,camera.getScreenHeight(),paint);
        canvas.drawLine(screenX2,0,screenX2,camera.getScreenHeight(),paint);
        if(!selecting){
            paint.setTextSize(100);

            paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
            String string = "x=" + MyMath.toNiceString(extremeX,5);
            canvas.drawText(string,0,100,paint);
            string = "y=" + MyMath.toNiceString(extremeY,5);
            canvas.drawText(string,0,200,paint);
        }
    }



    //sets extremeX and extremeY
    private void recalculate() {
        StandardFunction function = functionObject.getFunction();
        StandardFunction derivative = functionObject.getDerivative();
        StandardFunction secondDerivitave = derivative.getDerivative();////////recalculating every time

        ///////quick patch
        if((derivative.getFunction().getExpression() instanceof  Undefined) || secondDerivitave.getFunction().getExpression() instanceof  Undefined){
            extremeX = Float.NaN;
            extremeY = Float.NaN;
            return;
        }

        Camera camera = getWorld().getWorldView().getCamera();

        if(screenX1!=screenX2){
            boolean a = decideMoveLeftNaN(true,true,true);
            a=!a;
        }

        double left = camera.screenXToWorldX(Math.min(screenX1,screenX2));
        double right = camera.screenXToWorldX(Math.max(screenX1,screenX2));
        double mid = (left+right)/2;
        double fLeft = function.evaluate(left);
        double fMid = function.evaluate(mid);
        double fRight = function.evaluate(right);

        while(right-left>CLOSE_ENOUGH){
            //decide whether to move right or left
            boolean moveLeft;
            if(Double.isNaN(fLeft) || Double.isNaN(fMid) || Double.isNaN(fRight)){/////////////////NaN!=NaN
                moveLeft = decideMoveLeftNaN(Double.isNaN(fLeft),Double.isNaN(fMid),Double.isNaN(fRight));
            }else{
                moveLeft = decideMoveLeftWithDerivitaves(mid,derivative,secondDerivitave);
            }

            //move a bound and update the values
            if(moveLeft){
                right = mid;
                fRight = fMid;
            }else{
                left = mid;
                fLeft = fMid;
            }
            mid = (left+right)/2;
            fMid = function.evaluate(mid);
        }

        //check for cases like sec(x) where the derivative is not 0 now (dir may have hit a local min)
        //if there are no discontinuities and the derivative is not 0
        //////////CLOSE_ENOUGH*100: came up with *100 as a good tolerance empirically
        if(!Double.isNaN(fMid) && !Double.isNaN(fLeft) && !Double.isNaN(fRight) && Math.abs(derivative.evaluate(mid))>CLOSE_ENOUGH*100){
            extremeX = Float.NaN;
            extremeY = Float.NaN;
            return;
        }

        //set answer, make sure extremeY is not NaN if possible by choosing left or right instead (still really close)
        if(Double.isNaN(fMid)){
            if(Double.isNaN(fLeft)){
                //assign fRight
                extremeX = (float) right;
                extremeY = (float) fRight;
            }else{
                //assign fLeft
                extremeX = (float) left;
                extremeY = (float) fLeft;
            }
        }else{
            //assign fMid
            extremeX = (float) mid;
            extremeY = (float) fMid;
        }
    }

    private boolean decideMoveLeftWithDerivitaves(double mid,StandardFunction derivative, StandardFunction secondDerivitave){
        double fPrimeOfMid = derivative.evaluate(mid);
        double fDoublePrimeOfMid = secondDerivitave.evaluate(mid);
        if((fPrimeOfMid>=0) == (fDoublePrimeOfMid>=0)){//both mids are positive or both are negative
            //move left
            return true;
        }else{
            //move right
            return false;
        }
    }

    private boolean decideMoveLeftNaN(boolean leftNaN, boolean midNaN, boolean rightNaN) {
        if(leftNaN){
            if(midNaN){
                if(rightNaN){
                    //NNN
                    return true;//does not matter
                }else{
                    //NN#
                    return false;
                }
            }else{
                if(rightNaN){
                    //N#N
                    return true;//does not matter
                }else{
                    //N##
                    return true;
                }
            }
        }else{
            if(midNaN){
                if(rightNaN){
                    //#NN
                    return true;
                }else{
                    //#N#
                    return true;//does not matter
                }
            }else{
                if(rightNaN){
                    //##N
                    return false;
                }else{
                    //###
                    return true;//if this is reached, this function shouldn't have been called
                }
            }
        }
    }


    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        this.selecting = true;
        this.screenX1 = inputStatus.getCurrentFingerScreenX();
        this.screenX2 = screenX1;
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        this.screenX2 = inputStatus.getCurrentFingerScreenX();
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        this.selecting = false;
        getWorld().getWorldView().redraw();
    }
}
