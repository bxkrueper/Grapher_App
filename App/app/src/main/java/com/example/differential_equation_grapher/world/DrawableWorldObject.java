package com.example.differential_equation_grapher.world;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;

public interface DrawableWorldObject {
    void draw(WorldView view, Paint paint, Canvas canvas, Camera camera);
}
