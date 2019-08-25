package com.example.differential_equation_grapher.worldObjects;

import android.util.Log;

import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.PinchReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class CameraPinchZoomer extends WorldObject implements PinchReactWorldObject {
    private static final String CLASS_NAME = "CameraPinchZoomer";

    public CameraPinchZoomer(World world) {
        super(world);
    }

    @Override
    public void reactToPinch(InputStatus inputStatus) {
        Camera camera = getWorld().getWorldView().getCamera();
        float toMultiply = inputStatus.getScaleFactor();
        camera.setPixelsPerUnitX(camera.getPixelsPerUnitXNegPossible()*toMultiply);
        camera.setPixelsPerUnitY(camera.getPixelsPerUnitYNegPossible()*toMultiply);
        getWorld().getWorldView().redraw();
//        Log.d(CLASS_NAME,"reacted to pinch");
//        ScreenText screenText = (ScreenText) getWorld().getWorldObject("Screen Text");
//        screenText.setString("toMultiply: " + toMultiply);
//        getWorld().getWorldView().redraw();
    }
}
