/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tiem625.editorapp.components;

import java.util.function.Consumer;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;

/**
 *
 * @author anatolij
 */
public class TextFieldUtils {
    
    private TextFieldUtils() {};
    
    private static final Predicate<Integer> MIN_1 = val -> val >= 1;
    private static final Predicate<Integer> MIN_0 = val -> val >= 0;
    
    public static ChangeListener<String> makeNumericListener(
            int defaultValue, 
            Predicate<Integer> propConstraint,
            Consumer<Integer> valUsage) {
        
        return new NumericPropChangeListener(defaultValue, propConstraint) {
            @Override
            protected void useValidNumeric(Integer validValue) {
                valUsage.accept(validValue);
            }
        };
        
    }
    
    
    public static ChangeListener<String> makeMin0NumericListener(
            Consumer<Integer> valUsage) {
        
        return makeNumericListener(0, MIN_0, valUsage);   
    }
    
    public static ChangeListener<String> makeMin1NumericListener(
            Consumer<Integer> valUsage) {
        
        return makeNumericListener(1, MIN_1, valUsage);
    }
    
}
