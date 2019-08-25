package com.example.differential_equation_grapher.worldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.StandardFunction;
import com.example.differential_equation_grapher.helper.LineDrawer;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class StandardFunctionObject extends WorldObject implements DrawableWorldObject {
    private static final float LINE_THICKNESS = 0.025f;//in inches
    private StandardFunction standardFunction;
    private StandardFunction derivative;//stores the derivative so the helper objects don't need to recalculate it every time
    private LineDrawer lineDrawer;
    private int color;

    public StandardFunctionObject(World world, StandardFunction standardFunction) {
        super(world);
        setFunction(standardFunction);
        this.lineDrawer = new LineDrawer();
        this.color = Color.BLACK;
    }

    public StandardFunction getFunction(){
        return standardFunction;
    }

    //make a copy, simplify it, then use it to get the derivative
    public void setFunction(StandardFunction standardFunction) {
        this.standardFunction = new StandardFunction(standardFunction.getFunction().copy(),'x');
        this.standardFunction.getFunction().simplify();
        this.derivative = standardFunction.getDerivative();
    }

    public StandardFunction getDerivative() {
        return derivative;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
//                /////////
//        ScreenText screenText = (ScreenText) getWorld().getWorldObject("Screen Text");
//        ///////
////        screenText.setString("pix/inchX: " + camera.getPixelsPerInchX());
//        screenText.setString(": " + standardFunction.getFunction().toDisplayString());
//        /////////

        lineDrawer.getReadyToDraw();
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        paint.setColor(color);
        float left = camera.getLowestX();
        float right = camera.getHighestX();
        float step = camera.getUnitsPerPixelX();
        for(float x=left;x<=right;x+=step){
            float y = (float) standardFunction.evaluate(x);
            lineDrawer.drawTo(x,y,canvas,paint);
        }


    }


}
