package com.example.differential_equation_grapher.worldObjects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.FieldFunction;
import com.example.differential_equation_grapher.function.Function;
import com.example.differential_equation_grapher.myAlgs.MyMath;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class FieldFunctionObject extends WorldObject implements DrawableWorldObject {
    private static final float LINE_THICKNESS = 0.02f;//in inches

    private FieldFunction fieldFunction;
    private float pixelsBetweenArrows;

    public FieldFunctionObject(World world, FieldFunction fieldFunction) {
        super(world);
        this.fieldFunction = fieldFunction;
        this.pixelsBetweenArrows = 100;//between the starts of the arrows
    }

    public FieldFunction getFieldFunction() {
        return fieldFunction;
    }

    public void setFieldFunction(FieldFunction fieldFunction) {
        this.fieldFunction = fieldFunction;
    }



    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);

        float left = camera.getLowestX();
        float right = camera.getHighestX();
        float top = camera.getHighestY();
        float bottom = camera.getLowestY();

        float stepX = camera.getUnitsPerPixelX()*pixelsBetweenArrows;
        float stepY = camera.getUnitsPerPixelY()*pixelsBetweenArrows;

        for(float x=left;x<=right;x+=stepX){
            for(float y=bottom;y<=top;y+=stepY){

                float xMagOfArrow = (float) fieldFunction.evaluateXComponent(x,y);
                float yMagOfArrow = (float) fieldFunction.evaluateYComponent(x,y);
                float hypotOfArrow = (float) Math.hypot(xMagOfArrow,yMagOfArrow);

                float screenMagnitudeOfArrow = stepX/2;///////////only using stepX?
                float screenMagnitudeX = xMagOfArrow/hypotOfArrow*screenMagnitudeOfArrow;
                float screenMagnitudeY = yMagOfArrow/hypotOfArrow*screenMagnitudeOfArrow;

                float arrowTipX = x+screenMagnitudeX;
                float arrowTipY = y+screenMagnitudeY;

                drawArrow(x,y,arrowTipX,arrowTipY,paint,canvas);
            }
        }
    }

    private void drawArrow(float x, float y, float arrowTipX, float arrowTipY, Paint paint, Canvas canvas) {
        final float HEAD_REL_SIZE = 0.3f;
        //draw arrow shaft
        canvas.drawLine(x,y,arrowTipX,arrowTipY,paint);

        //draw arrow head
        float headLength = (float) Math.hypot(arrowTipX-x,arrowTipY-y)*HEAD_REL_SIZE;
        double direction = MyMath.getDirection(x,y,arrowTipX,arrowTipY);//current value: direction arrow is pointing
        direction += Math.PI*3/4;//current value: direction from tip to left shaft
        canvas.drawLine(arrowTipX,arrowTipY,(float) (arrowTipX+headLength*Math.cos(direction)), (float) (arrowTipY+headLength*Math.sin(direction)),paint);
        direction += Math.PI/2;//current value: direction from tip to right shaft
        canvas.drawLine(arrowTipX,arrowTipY,(float) (arrowTipX+headLength*Math.cos(direction)), (float) (arrowTipY+headLength*Math.sin(direction)),paint);
    }




}