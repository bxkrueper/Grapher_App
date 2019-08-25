package com.example.differential_equation_grapher.worldObjects;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
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

public class PolarIntegrator extends WorldObject implements DrawableWorldObject, DrawScreenWorldObject, OneFingerReactWorldObject, LongPressReactWorldObject {
    private static final String CLASS_NAME = "PolarIntegrator";
    private float theta0;//does not need to be lower than theta0. Lowest and highest are calculated when needed
    private float theta1;
    private int sampleCount;
    private float area;
    private int windingNumber;//0: use MyMath.getDirection as is   1: add 2PI  -1: subtract 2PI  2: add 4PI...
    private int quadrantNumber;//1 is top right, 2 is top left, 3 is bottom left, 4 is bottom right. used to determint what to do with the winding number
    private RectF rectForArcDrawing;
    PolarFunctionObject functionObject;

    public PolarIntegrator(World world, int sampleCount, PolarFunctionObject functionObject) {
        super(world);
        this.theta0 = 0;
        this.theta1 = 0;
        this.sampleCount = sampleCount;
        this.area = 0;
        this.functionObject = functionObject;
        this.windingNumber = 0;
        this.quadrantNumber = 0;
        this.rectForArcDrawing = new RectF(1,1,1,1);//parameters will be set before needing
    }

    //world only re-draws on actions, so helper objects need to re-calculate before every redraw
    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        float thetaLow = Math.min(theta0, theta1);
        float thataHigh = Math.max(theta0, theta1);
        float dt = (thataHigh - thetaLow)/sampleCount;
        if(dt==0){
            return;
        }
        float halfdt = dt/2;

        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.integration));
        this.area = 0;
        for(float t = thetaLow+halfdt; t<thataHigh; t+=dt){
            float r = (float) functionObject.getFunction().evaluate(t);
            drawArc(r,t-halfdt,dt,paint,canvas);
            area += dt*r*r/2;//area of an arc: .5theta*r^2
        }
    }

    private void drawArc(float radius,float startAngleRads,float sweepAngleRads,Paint paint,Canvas canvas){
        if(radius<0){
            radius = -radius;
            startAngleRads+=(float) Math.PI;
        }

        rectForArcDrawing.bottom = radius;//pos because rectF is designed for screed coordinates
        rectForArcDrawing.left = -radius;
        rectForArcDrawing.right = radius;
        rectForArcDrawing.top = -radius;//neg because rectF is designed for screed coordinates

        canvas.drawArc(rectForArcDrawing,(float) MyMath.radiansToDegrees(startAngleRads),(float) MyMath.radiansToDegrees(sweepAngleRads),true,paint);
    }



    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setTextSize(100);
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
        String string = "area: " + MyMath.toNiceString(area,5);
        canvas.drawText(string,0,100,paint);
    }

    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        windingNumber = 0;
        quadrantNumber = functionObject.getQuadrantNumber(inputStatus.getCurrentFingerWorldX(),inputStatus.getCurrentFingerWorldY());
        theta0 = (float) MyMath.getDirectionFromOrigin(inputStatus.getCurrentFingerWorldX(),inputStatus.getCurrentFingerWorldY());
        Grid grid = (Grid) getWorld().getWorldObject("CartesianGrid");
        if(grid==null){

        }else{
            theta0 = grid.getClosestSignificantTheta(theta0);
        }
        theta1 = theta0;
        getWorld().getWorldView().redraw();
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
        theta1 = (float) MyMath.getDirectionFromOrigin(inputStatus.getCurrentFingerWorldX(),inputStatus.getCurrentFingerWorldY());
        theta1+=windingNumber*Math.PI*2.0;
        Grid grid = (Grid) getWorld().getWorldObject("CartesianGrid");
        if(grid!=null){
            theta1 = grid.getClosestSignificantTheta(theta1);
        }
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {

    }

    @Override
    public void reactToLongPress(InputStatus inputStatus) {
        x0Prompt();
    }

    private void x0Prompt(){
        PolarGrapherActivity activity = (PolarGrapherActivity) getWorld().getWorldView().getActivity();

        //calls inputFinished as well as summmons yPrompt
        CustomKeyboard.OnEnter onEnter = new CustomKeyboard.OnEnter() {
            @Override
            public void doOnEnter(EditText editText) {
//                .inputFinished();//do not call this. go strait to the next prompt
                x1Prompt();
            }

            //make sure not to close the keyboard so it will still be up for the next input
            @Override
            public boolean closeKeyboardOnEnter() {
                return false;
            }
        };


        activity.getInput("theta0=",new ThisOnInputX0(activity),onEnter);
    }
    private void x1Prompt(){
        PolarGrapherActivity activity = (PolarGrapherActivity) getWorld().getWorldView().getActivity();

        //calls inputFinished as well as summmons yPrompt
        CustomKeyboard.OnEnter onEnter = new CustomKeyboard.OnEnter() {
            @Override
            public void doOnEnter(EditText editText) {
//                .inputFinished();//do not call this. go strait to the next prompt
                sampleCountPrompt();
            }

            //make sure not to close the keyboard so it will still be up for the next input
            @Override
            public boolean closeKeyboardOnEnter() {
                return false;
            }
        };


        activity.getInput("theta1=",new ThisOnInputX1(activity),onEnter);
    }
    private void sampleCountPrompt(){
        PolarGrapherActivity activity = (PolarGrapherActivity) getWorld().getWorldView().getActivity();
        activity.getInput("sampleCount=",new ThisOnInputSampleCount(activity));
    }

    private class ThisOnInputX0 implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInputX0(Activity activity) {
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

            theta0 = (float) input;
            getWorld().getWorldView().redraw();

        }
    }

    private class ThisOnInputX1 implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInputX1(Activity activity) {
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

            theta1 = (float) input;
            getWorld().getWorldView().redraw();

        }
    }

    private class ThisOnInputSampleCount implements CustomKeyboard.OnInput{
        private Activity activity;

        public ThisOnInputSampleCount(Activity activity) {
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

            sampleCount = (int) input;
            getWorld().getWorldView().redraw();
        }
    }




}
