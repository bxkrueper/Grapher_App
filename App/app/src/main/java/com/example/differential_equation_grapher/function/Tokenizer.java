package com.example.differential_equation_grapher.function;

//only one of these classes is used per possible token. Used in Expression factory to try to parse the expression string into tokens
//if it fails to find what it is looking for, it returns null and lets the next tokenizer look at the string
public interface Tokenizer {
    Token tryTokenize(StringParser sp);
}
