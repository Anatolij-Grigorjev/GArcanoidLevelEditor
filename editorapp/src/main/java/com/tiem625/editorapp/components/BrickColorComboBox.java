/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tiem625.editorapp.components;

import com.tiem625.editorapp.enums.BrickColors;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Callback;

/**
 *
 * @author Tiem625
 */
public class BrickColorComboBox extends ComboBox<BrickColors> {
    
    public BrickColorComboBox() {
        
        getItems().clear();
        getItems().add(null);
        getItems().addAll(BrickColors.values());
        
        setEditable(false);
        getSelectionModel().selectFirst();
        
        
        
        setCellFactory((ListView<BrickColors> param) -> {
            ListCell<BrickColors> cell = new ListCell<BrickColors>() {
                @Override
                protected void updateItem(BrickColors item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null) {
                        setGraphic(null);
                    } else {
                       
                        Shape shape = new Rectangle(
                                this.getWidth(), this.getHeight(), 
                                Color.web(item.getColorCode())
                        );
                        setGraphic(shape);
                    }
                }

            };
            
            return cell;
        });
        
    }
    
}
