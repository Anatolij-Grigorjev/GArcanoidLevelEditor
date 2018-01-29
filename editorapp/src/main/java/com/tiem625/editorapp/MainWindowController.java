/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tiem625.editorapp;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Tiem625
 */
public class MainWindowController implements Initializable {
    
    @FXML
    private TextField fldLevelName;
    @FXML
    private TextField fldGridRows;
    @FXML
    private TextField fldGridCols;
    @FXML
    private TextField fldRowPadding;
    @FXML
    private TextField fldColPadding; 
    
    @FXML 
    private GridPane gridLevelGrid;
    
    
    @FXML
    private void handleCreateLevelBtn(ActionEvent e) {
        
    }
    
    @FXML
    private void handleImportLevelBtn(ActionEvent e) {
        
    }
    
    @FXML
    private void handleExportLevelBtn(ActionEvent e) {
        
    }
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        fldGridRows.textProperty().addListener(new NumericPropChangeListener(1) {
            @Override
            protected void setGridProp(Integer validValue) {
                //TODO: grid rows/cols
            }
        });
        
        
        fldLevelName.setText("<NONAME>");
        fldColPadding.setText("0");
        fldRowPadding.setText("0");
        fldGridCols.setText("1");
        fldGridRows.setText("1");
    }
    
    
    private abstract class NumericPropChangeListener implements ChangeListener<String> {

        private final int defaultVal;
        
        public NumericPropChangeListener(int defaultVal) {
            this.defaultVal = defaultVal;
        }
        
        protected Integer resolveValidValue(String oldVal, String newVal) {
            
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
            
            Integer validValue = resolveValidValue(oldValue, newValue);
            
            setGridProp(validValue);
        }

        protected abstract void setGridProp(Integer validValue);
    }
    
}
