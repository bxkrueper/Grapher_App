package com.example.differential_equation_grapher.worldObjects;

import android.util.Log;

import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class CameraDragger extends WorldObject implements OneFingerReactWorldObject {
    private static final String CLASS_NAME = "CameraDragger";

    private float originalCameraX;
    private float originalCameraY;

    public CameraDragger(World world) {
        super(world);
        this.originalCameraX = 0f;
        this.originalCameraY = 0f;
    }

    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        this.originalCameraX = getWorld().getWorldView().getCamera().getCenterX();
        this.originalCameraY = getWorld().getWorldView().getCamera().getCenterY();
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        Camera camera = getWorld().getWorldView().getCamera();
        float dxPixels = inputStatus.getOriginalFingerScreenX()-inputStatus.getCurrentFingerScreenX();
        float dyPixels = inputStatus.getOriginalFingerScreenY()-inputStatus.getCurrentFingerScreenY();
        camera.setCenterX(originalCameraX+dxPixels*camera.getUnitsPerPixelXNegPossible());
        camera.setCenterY(originalCameraY+dyPixels*camera.getUnitsPerPixelYNegPossible());
        Log.d(CLASS_NAME,inputStatus.toString());
//        Log.d(CLASS_NAME,"dxPixels: " + dxPixels + " dyPixels: " + dyPixels + " cameraX: " + inputStatus.getOriginalFingerWorldX()+dxPixels*camera.getUnitsPerPixelX() + " cameraY: " + inputStatus.getOriginalFingerWorldY()+dyPixels*camera.getUnitsPerPixelY());
//        Log.d(CLASS_NAME,"dxPixels: " + dxPixels + " dyPixels: " + dyPixels + " cameraX: " + camera.getCenterX() + " cameraY: " + camera.getCenterY());
        getWorld().getWorldView().redraw();
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        //do nothing
    }
}
