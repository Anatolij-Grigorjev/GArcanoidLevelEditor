/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tiem625.editorapp.components;

import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class NumericPropChangeListener implements ChangeListener<String> {

    private final int defaultVal;
    private final Predicate<Integer> propConstraint;

    public NumericPropChangeListener(int defaultVal, Predicate<Integer> propConstraint) {
        this.defaultVal = defaultVal;
        this.propConstraint = propConstraint;
    }

    protected Integer parseValidValue(String oldVal, String newVal) {

        Integer propVal = null;

        try {
            propVal = Integer.parseInt(newVal);
        } catch (NumberFormatException ex1) {
            try {
                propVal = Integer.parseInt(oldVal);
            } catch (NumberFormatException ex2) {
                propVal = defaultVal;
            }
        }

        return propVal;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

        Integer validValue = parseValidValue(oldValue, newValue);

        //check constraint, use default if value doesnt pass it
        if (validValue != defaultVal) {
            validValue = propConstraint.test(validValue) ? validValue : defaultVal;
        }

        useValidNumeric(validValue);
    }

    protected abstract void useValidNumeric(Integer validValue);
}
