package com.example.differential_equation_grapher.helper;

public interface NumberToColorConverter {
    int getColor(float number);
    float getMin();
    void setMin(float min);
    float getMax();
    void setMax(float max);
}
