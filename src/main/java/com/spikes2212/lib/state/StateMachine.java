package com.spikes2212.lib.state;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.HashMap;
import java.util.Map;

public abstract class StateMachine<T extends Enum<T>> {
    private Map<T, Command> transformations;
    private T state;

    public StateMachine(T initialState) {
        setState(initialState);
        transformations = new HashMap<>();
        generateTransformations();

    }

    protected void setState(T state) {
        this.state = state;
    }

    public T getState() {
        return state;
    }

    protected abstract void generateTransformations();

    protected void addTransformation(T state, Command command) {
        transformations.put(state, command.andThen(() -> setState(state)));
    }

    public Command getTransformationFor(T state) {
        return transformations.get(state);
    }
}
