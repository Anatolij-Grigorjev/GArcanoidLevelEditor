/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tiem625.editorapp.tool;

import com.tiem625.editorapp.MainWindowController;
import com.tiem625.editorapp.components.BrickColorComboBox;
import com.tiem625.editorapp.components.TextFieldUtils;
import com.tiem625.editorapp.enums.BrickColors;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Tiem625
 */
public class ToolDialogController implements Initializable {
    
    @FXML
    private BrickColorComboBox ddColColor;
    
    @FXML
    private BrickColorComboBox ddRowColor;
    
    @FXML
    private TextField fldColsIndices;
    
    @FXML
    private TextField fldRowsIndices;
    
    @FXML
    private TextField fldRowsStride;
    
    @FXML
    private TextField fldColsStride;
    
    @FXML
    private TextField fldRowsOffset;
    
    @FXML
    private TextField fldColsOffset;
    
    private MainWindowController parentController;
    
    private Stage stage;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setParentController(MainWindowController parentController) {
        this.parentController = parentController;
    }
    
    private final ChangeListener<String> listenerRowsStride = TextFieldUtils.makeMin1NumericListener((validValue) -> {
        
        fldRowsStride.setText(String.valueOf(validValue));
    });
    
    private final ChangeListener<String> listenerColsStride = TextFieldUtils.makeMin1NumericListener((validValue) -> {
        
        fldColsStride.setText(String.valueOf(validValue));
    });
    
    private final ChangeListener<String> listenerRowsOffset = TextFieldUtils.makeMin0NumericListener((validValue) -> {
        
        fldRowsOffset.setText(String.valueOf(validValue));
    });
    
    private final ChangeListener<String> listenerColsOffset = TextFieldUtils.makeMin0NumericListener((validValue) -> {
        
        fldColsOffset.setText(String.valueOf(validValue));
    });
    
    @FXML
    private void handleToolClose(ActionEvent e) {

        //just close window
        closeDialog();
    }
    
    @FXML
    private void handleToolApply(ActionEvent e) {
        
        List<Integer> rowIndices = new ArrayList<>();
        List<Integer> colIndices = new ArrayList<>();
        
        final ComboBox<BrickColors>[][] gridNodes = parentController.getGridNodes();
        
        rowIndices.addAll(parseChangeIndices(fldRowsIndices, gridNodes.length));
        if (gridNodes.length > 0) {
            colIndices.addAll(parseChangeIndices(fldColsIndices, gridNodes[0].length));
        }
        
        System.out.println("Rows to change: " + Arrays.toString(rowIndices.toArray()));
        System.out.println("Cols to change: " + Arrays.toString(colIndices.toArray()));
        
        Platform.runLater(() -> {
            
            Integer rowStride = Integer.parseInt(fldRowsStride.getText());
            Integer colStride = Integer.parseInt(fldColsStride.getText());
            Integer rowOffset = Integer.parseInt(fldRowsOffset.getText());
            Integer colOffset = Integer.parseInt(fldColsOffset.getText());
            
            
            //apply to row indices
            if (!rowIndices.isEmpty()) {
                
                for (Integer idx : rowIndices) {
                    
                    for (int i = rowOffset; i < gridNodes[idx].length; i += rowStride) {
                        gridNodes[idx][i].setValue(ddRowColor.getValue());
                    }
                }
            }

            //apply to col indices
            if (!colIndices.isEmpty()) {
                
                for (Integer idx : colIndices) {
                    
                    for (int i = colOffset; i < gridNodes.length; i += colStride) {
                        gridNodes[i][idx].setValue(ddColColor.getValue());
                    }
                }
            }
        });
        
        closeDialog();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        fldRowsStride.textProperty().removeListener(listenerRowsStride);
        fldColsStride.textProperty().removeListener(listenerColsStride);
        fldRowsOffset.textProperty().removeListener(listenerRowsOffset);
        fldRowsOffset.textProperty().removeListener(listenerColsOffset);
        
        fldRowsStride.textProperty().addListener(listenerRowsStride);
        fldColsStride.textProperty().addListener(listenerColsStride);
        fldRowsOffset.textProperty().addListener(listenerRowsOffset);
        fldColsOffset.textProperty().addListener(listenerColsOffset);

        
        fldColsIndices.setText("");
        fldRowsIndices.setText("");
        ddColColor.setValue(null);
        ddRowColor.setValue(null);
        fldRowsStride.setText("1");
        fldColsStride.setText("1");
        fldRowsOffset.setText("0");
        fldColsOffset.setText("0");
    }
    
    private void closeDialog() {
        stage.close();
    }

    /**
     * Parse the change-selection along hte supplied dimension
     *
     * @param field textfield with input to parse (such as
     * number/even/odd/csv/all)
     * @param maxIndices size of available indices from 0 to max - 1. Input of
     * higher indices will be ignored
     * @return
     */
    private List<Integer> parseChangeIndices(TextField field, int maxIndices) {
        
        String input = field.getText().trim().toLowerCase(Locale.getDefault());
        
        if (input.isEmpty() || maxIndices < 1) {
            return Collections.EMPTY_LIST;
        }
        
        Stream<Integer> indices = IntStream.range(0, maxIndices).boxed();
        
        switch (input) {
            //apply to all indices in the dimension
            case "all":
                break;
            //apply to odd indices
            case "odd":
                indices = indices.filter(idx -> idx % 2 != 0);
                break;
            //apply to even indices
            case "even":
                indices = indices.filter(idx -> idx % 2 == 0);
                break;
            default:
                String[] manualIndices = input.split(",");
                indices = Stream.of(manualIndices).map(strIdx -> {
                    try {
                        return Integer.parseInt(strIdx);
                    } catch (NumberFormatException ex) {
                        return null;
                    }
                }).filter(Objects::nonNull)
                        .filter(idx -> 0 <= idx && idx < maxIndices);
        }
        
        return indices.collect(Collectors.toList());
    }
    
}
