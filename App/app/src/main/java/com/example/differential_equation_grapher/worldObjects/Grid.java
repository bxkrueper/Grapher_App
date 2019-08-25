package com.example.differential_equation_grapher.worldObjects;

import com.example.differential_equation_grapher.myAlgs.MyMath;

public interface Grid {
    float getClosestSignificantXTo(float rawX);

    float getClosestSignificantYTo(float rawY);

    float getClosestSignificantMagnitudeFromOrigin(float rawMagnitude);

    float getClosestSignificantTheta(float rawTheta);
}
