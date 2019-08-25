package com.example.differential_equation_grapher.worldObjects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.differential_equation_grapher.R;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.world.DrawScreenWorldObject;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class ScreenText extends WorldObject implements DrawScreenWorldObject {
    private float screenX;
    private float screenY;
    private float size;
    private String string;

    public ScreenText(World world, float screenX, float screenY, float size, String string) {
        super(world);
        this.screenX = screenX;
        this.screenY = screenY;
        this.size = size;
        this.string = string;
    }

    public float getScreenX() {
        return screenX;
    }

    public void setScreenX(float screenX) {
        this.screenX = screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public void setScreenY(float screenY) {
        this.screenY = screenY;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public void drawOnScreen(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        paint.setTextSize(size);
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.screen_text));
        canvas.drawText(string,screenX,screenY,paint);
    }
}
