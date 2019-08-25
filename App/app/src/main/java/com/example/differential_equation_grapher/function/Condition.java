package com.example.differential_equation_grapher.function;

public interface Condition {
    boolean passes(Substitution substitution);

    boolean isEqualTo(Condition condition);
}
