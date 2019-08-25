package com.example.differential_equation_grapher.Views;

import com.example.differential_equation_grapher.camera.Camera;

public class InputStatus {
    private static final String CLASS_NAME = "InputStatus";

    WorldView worldView;

    private boolean firstFingerDown;

    private float originalFingerScreenX;
    private float originalFingerScreenY;
    private float originalFingerWorldX;
    private float originalFingerWorldY;

    private float currentFingerScreenX;
    private float currentFingerScreenY;
    private float currentFingerWorldX;
    private float currentFingerWorldY;

    private float scaleFactor;//is usually 1 unless the last action was a scale. gets reset to 1 every other time

    public InputStatus(WorldView worldView){
        this.worldView = worldView;
        this.firstFingerDown = false;
        this.originalFingerScreenX = Float.NaN;
        this.originalFingerScreenY = Float.NaN;
        this.originalFingerWorldX = Float.NaN;
        this.originalFingerWorldY = Float.NaN;
        this.currentFingerScreenX = Float.NaN;
        this.currentFingerScreenY = Float.NaN;
        this.currentFingerWorldX = Float.NaN;
        this.currentFingerWorldY = Float.NaN;
        this.scaleFactor = 1f;
    }

    //setters: only WorldView should edit______________________________________________________________________________________

    protected void setFirstFingerDown(boolean down){
        this.firstFingerDown = down;
    }

    protected void setOriginalFingerScreenX(float originalFingerScreenX) {
        this.originalFingerScreenX = originalFingerScreenX;
    }

    protected void setOriginalFingerScreenY(float originalFingerScreenY) {
        this.originalFingerScreenY = originalFingerScreenY;
    }

    protected void setOriginalFingerWorldX(float originalFingerWorldX) {
        this.originalFingerWorldX = originalFingerWorldX;
    }

    protected void setOriginalFingerWorldY(float originalFingerWorldY) {
        this.originalFingerWorldY = originalFingerWorldY;
    }

    protected void setCurrentFingerScreenX(float currentFingerScreenX) {
        this.currentFingerScreenX = currentFingerScreenX;
    }

    protected void setCurrentFingerScreenY(float currentFingerScreenY) {
        this.currentFingerScreenY = currentFingerScreenY;
    }

    protected void setCurrentFingerWorldX(float currentFingerWorldX) {
        this.currentFingerWorldX = currentFingerWorldX;
    }

    protected void setCurrentFingerWorldY(float currentFingerWorldY) {
        this.currentFingerWorldY = currentFingerWorldY;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    //getters_________________________________________________________________________________________

    public boolean isFingerDown() {
        return firstFingerDown;
    }

    public float getOriginalFingerScreenX() {
        return originalFingerScreenX;
    }

    public float getOriginalFingerScreenY() {
        return originalFingerScreenY;
    }

    public float getOriginalFingerWorldX() {
        return originalFingerWorldX;
    }

    public float getOriginalFingerWorldY() {
        return originalFingerWorldY;
    }

    public float getCurrentFingerScreenX() {
        return currentFingerScreenX;
    }

    public float getCurrentFingerScreenY() {
        return currentFingerScreenY;
    }

    public float getCurrentFingerWorldX() {
        return currentFingerWorldX;
    }

    public float getCurrentFingerWorldY() {
        return currentFingerWorldY;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }




    //bundled stuff_______________________________________________________
    protected void fingerDown(float x, float y, Camera camera){
        this.originalFingerScreenX = x;
        this.originalFingerScreenY = y;
        this.currentFingerScreenX = originalFingerScreenX;
        this.currentFingerScreenY = originalFingerScreenY;

        this.originalFingerWorldX = camera.screenXToWorldX(x);
        this.originalFingerWorldY = camera.screenYToWorldY(y);
        this.currentFingerWorldX = originalFingerWorldX;
        this.currentFingerWorldY = originalFingerWorldY;
    }



    protected void fingerMove(float x, float y, Camera camera){
        this.currentFingerScreenX = x;
        this.currentFingerScreenY = y;

        this.currentFingerWorldX = camera.screenXToWorldX(x);
        this.currentFingerWorldY = camera.screenYToWorldY(y);
    }










    @Override
    public String toString() {
        return "InputStatus{" +
                "originalFingerScreenX=" + originalFingerScreenX +
                ", originalFingerScreenY=" + originalFingerScreenY +
                ", originalFingerWorldX=" + originalFingerWorldX +
                ", originalFingerWorldY=" + originalFingerWorldY +
                ", currentFingerScreenX=" + currentFingerScreenX +
                ", currentFingerScreenY=" + currentFingerScreenY +
                ", currentFingerWorldX=" + currentFingerWorldX +
                ", currentFingerWorldY=" + currentFingerWorldY +
                '}';
    }



}
