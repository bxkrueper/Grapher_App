package com.example.differential_equation_grapher.function;

public interface MultiCondition {
    int indexToChoose(Substitution substitution);//if it returns a out of bounds index, Undefined us used
    boolean isEqualTo(MultiCondition condition);
}
