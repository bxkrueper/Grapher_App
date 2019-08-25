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
import com.example.differential_equation_grapher.function.StandardFunctionMultiParam;
import com.example.differential_equation_grapher.function.Undefined;
import com.example.differential_equation_grapher.helper.LineDrawer;
import com.example.differential_equation_grapher.myAlgs.MyMath;
import com.example.differential_equation_grapher.world.DrawScreenWorldObject;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class ThreeDMaxMinFinder extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject, OneFingerReactWorldObject {
    private static final float POINT_SIZE = 0.04f;//in inches
    private static final float CLOSE_ENOUGH = 0.00001f;
    private static final float LINE_THICKNESS = 0.01f;//in inches
    private ThreeDFunctionObject functionObject;
    private float extremeX;
    private float extremeY;
    private float extremeZ;
    private float worldx1;
    private float worldx2;
    private float worldy1;
    private float worldy2;
    private boolean selecting;

    private LineDrawer lineDrawer;

    private boolean usingMinMode;


    public ThreeDMaxMinFinder(World world, ThreeDFunctionObject functionObject,boolean usingMinMode) {
        super(world);
        this.functionObject = functionObject;
        this.extremeX = 0;
        this.extremeY = 0;
        this.extremeZ = 0;
        this.worldx1 = 0;
        this.worldx2 = 0;
        this.worldy1 = 0;
        this.worldy2 = 0;
        this.selecting = false;
        this.lineDrawer = new LineDrawer();
        this.usingMinMode = usingMinMode;
    }

    public boolean isUsingMinMode() {
        return usingMinMode;
    }

    public void setUsingMinMode(boolean usingMinMode) {
        this.usingMinMode = usingMinMode;
    }

    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        //draw boundary
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.STROKE);//set to outline mode
        canvas.drawRect(worldx1,worldy2,worldx2,worldy1,paint);
        paint.setStyle(Paint.Style.FILL);//set back
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.ai_path));
        lineDrawer.getReadyToDraw();

        //get boundaries
        float left = Math.min(worldx1,worldx2);
        float right = Math.max(worldx1,worldx2);
        float bottom = Math.min(worldy1,worldy2);
        float top = Math.max(worldy1,worldy2);

        //set other variables
        boolean onHorizontalWall = false;
        boolean onVerticalWall = false;
        float step = camera.getUnitsPerPixelX();

        //set startling point to center
        extremeX = (left+right)/2;
        extremeY = (bottom+top)/2;
        extremeZ = functionObject.evaluate(extremeX,extremeY);
        lineDrawer.drawTo(extremeX,extremeY,canvas,paint);



        while(step>CLOSE_ENOUGH){
            //get direction (normalizes gradient)
            float gradientX = functionObject.getGradientX(extremeX,extremeY);
            float gradientY = functionObject.getGradientY(extremeX,extremeY);
            if(usingMinMode){
                gradientX = -gradientX;
                gradientY = -gradientY;
            }
            //normalize
            float hypot = (float) Math.hypot(gradientX,gradientY);
            float directionX = gradientX/hypot;
            float directionY = gradientY/hypot;
            //if on a wall, snaps the vector to go along it, even if it is not directly uphill
            if(onHorizontalWall) directionY = 0;
            if(onVerticalWall) directionX = 0;


            //move
            float tryX = extremeX+directionX*step;
            float tryY = extremeY+directionY*step;
            float tryZ = functionObject.evaluate(tryX,tryY);
            if(tryX<left || tryX>right || tryY<bottom || tryY>top){
                //out of bounds. snap to a wall by estimating the intersection and setting the right boolean variable
                if(tryX<left){
                    extremeX = left;
                    onVerticalWall = true;
                }else if(tryX>right){
                    extremeX = right;
                    onVerticalWall = true;
                }else if(tryY<bottom){
                    extremeY = bottom;
                    onHorizontalWall = true;
                }else{//tryY>top
                    extremeY = top;
                    onHorizontalWall = true;
                }
                //if on corner, set extremes to corner and finish
                if(onHorizontalWall&&onVerticalWall){
                    extremeX = tryX;
                    extremeY = tryY;
                    extremeZ = tryZ;
                    lineDrawer.drawTo(extremeX,extremeY,canvas,paint);
                    step=0;//break out later
                }

            }else if((usingMinMode&&tryZ<extremeZ) || (!usingMinMode && tryZ>extremeZ)){
                //made progress. update extremes
                extremeX = tryX;
                extremeY = tryY;
                extremeZ = tryZ;
                lineDrawer.drawTo(extremeX,extremeY,canvas,paint);
            }else{
                //lost progress or went to exact opposite side of hill (equal). don't update extremes and instead, reduce the step to try again
                step/=2;
            }
        }


        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.point));
        drawPoint(extremeX,extremeY,canvas,paint,camera);
    }


    private void drawPoint(float x, float y, Canvas canvas, Paint paint, Camera camera) {
        canvas.drawCircle(x,y,POINT_SIZE*camera.getUnitsPerInchX(),paint);
    }

    //draw on screen is done after draw, so values are already calculated
    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        if(!selecting){
            paint.setTextSize(100);

            paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
            String string = "(" + MyMath.toNiceString(extremeX,5) + "," + MyMath.toNiceString(extremeY,5) + ")";
            canvas.drawText(string,0,100,paint);
            string = "z=" + MyMath.toNiceString(extremeZ,5);
            canvas.drawText(string,0,200,paint);
        }
    }

    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        Camera camera = getWorld().getWorldView().getCamera();
        this.selecting = true;
        this.worldx1 = camera.screenXToWorldX(inputStatus.getCurrentFingerScreenX());
        this.worldx2 = worldx1;
        this.worldy1 = camera.screenYToWorldY(inputStatus.getCurrentFingerScreenY());
        this.worldy2 = worldy1;
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        Camera camera = getWorld().getWorldView().getCamera();
        this.worldx2 = camera.screenXToWorldX(inputStatus.getCurrentFingerScreenX());
        this.worldy2 = camera.screenYToWorldY(inputStatus.getCurrentFingerScreenY());
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        this.selecting = false;
        getWorld().getWorldView().redraw();
    }

}
