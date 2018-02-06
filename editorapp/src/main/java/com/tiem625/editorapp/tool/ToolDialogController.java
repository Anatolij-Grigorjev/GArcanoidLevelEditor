/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tiem625.editorapp.tool;

import com.tiem625.editorapp.MainWindowController;
import com.tiem625.editorapp.components.BrickColorComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    
    private MainWindowController parentController;
    
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentController(MainWindowController parentController) {
        this.parentController = parentController;
    }
    
    @FXML
    private void handleToolClose(ActionEvent e) {
        
        //just close window
        closeDialog();
    }
    
    @FXML
    private void handleToolApply(ActionEvent e) {
        
        Platform.runLater(() -> {
            //TODO: apply user input to grid in parent
        });
        
        closeDialog();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        fldColsIndices.setText("");
        fldRowsIndices.setText("");
        ddColColor.setValue(null);
        ddRowColor.setValue(null);
    }
    
    private void closeDialog() {
        stage.close();
    }

}
