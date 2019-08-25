package com.example.differential_equation_grapher.myAlgs;

public class MyEvent {
    private Object source;
    private String command;
    private Object data;
    
    public MyEvent(Object source, String command, Object data){
        this.source = source;
        this.command =command;
        this.data = data;
    }
    
    
    public Object getSource(){
        return source;
    }

    public String getCommand() {
        return command;
    }
    
    public Object getData(){
        return data;
    }
}
