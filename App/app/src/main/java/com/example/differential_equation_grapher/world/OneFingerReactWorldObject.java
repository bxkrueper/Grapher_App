package com.example.differential_equation_grapher.world;

import com.example.differential_equation_grapher.Views.InputStatus;

public interface OneFingerReactWorldObject {
    void reactToOneFingerDown(InputStatus inputStatus);//when the first finger touches the screen
    void reactToOneFingerMove(InputStatus inputStatus);//when there is one finger being moved
    void reactToOneFingerUp(InputStatus inputStatus);//when the one and only finger is no longer touching the screen
}
