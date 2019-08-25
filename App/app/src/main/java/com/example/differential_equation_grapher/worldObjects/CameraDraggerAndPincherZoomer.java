package com.example.differential_equation_grapher.worldObjects;

import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.PinchReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class CameraDraggerAndPincherZoomer extends WorldObject implements OneFingerReactWorldObject, PinchReactWorldObject {
    private CameraDragger cameraDragger;
    private CameraPinchZoomer cameraPinchZoomer;

    public CameraDraggerAndPincherZoomer(World world) {
        super(world);
        this.cameraDragger = new CameraDragger(world);
        this.cameraPinchZoomer = new CameraPinchZoomer(world);
    }

    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        cameraDragger.reactToOneFingerDown(inputStatus);
    }

    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        cameraDragger.reactToOneFingerMove(inputStatus);
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        cameraDragger.reactToOneFingerUp(inputStatus);
    }

    @Override
    public void reactToPinch(InputStatus inputStatus) {
        cameraPinchZoomer.reactToPinch(inputStatus);
    }
}
