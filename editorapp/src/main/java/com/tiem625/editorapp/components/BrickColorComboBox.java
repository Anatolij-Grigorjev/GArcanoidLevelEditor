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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Callback;

/**
 *
 * @author Tiem625
 */
public class BrickColorComboBox extends ComboBox<BrickColors> {

    public static class BrickColorCell extends ListCell<BrickColors> {

        private final ComboBox<BrickColors> boxParent;
        
        public BrickColorCell(ComboBox<BrickColors> boxParent) {
            super();
            this.boxParent = boxParent;
        }

        @Override
        protected void updateItem(BrickColors item, boolean empty) {
            
            super.updateItem(item, empty);
            
            setCellPropsByItem(this, item, boxParent);
        }
        
        //extracted cell change logic to help when setting item
        public static void setCellPropsByItem(
                ListCell<BrickColors> cell, 
                BrickColors item, 
                ComboBox<BrickColors> box) {
            
            //200px or 90% width
            //150px or less height
            double brickWidth = Math.min(200.0, box.getWidth() - 0.1 * box.getWidth());
            double brickHeight = Math.min(150.0, box.getHeight());

            if (item == null) {
                cell.setGraphic(null);
            } else {

                Shape shape = new Rectangle(
                        brickWidth,
                        brickHeight,
                        Color.web(item.getColorCode())
                );
                
                cell.setGraphic(shape);
                cell.setText(item.getJsonCode());
            }
            
        }

    }
    
    

    private final Callback<ListView<BrickColors>, ListCell<BrickColors>> 
            BRICK_COLOR_CELL;
    
    

    public BrickColorComboBox() {

        getItems().clear();
        getItems().add(null);
        getItems().addAll(BrickColors.values());

        setEditable(false);
        getSelectionModel().selectFirst();
        
        BRICK_COLOR_CELL = (ListView<BrickColors> param) -> {
            return new BrickColorCell(this) ;
        };
        
        //set initial value
        setButtonCell(BRICK_COLOR_CELL.call(null));
        setCellFactory(BRICK_COLOR_CELL);

        //render when value changes
        valueProperty().addListener((obs, o, n) -> {
            
            setButtonCell(new BrickColorCell(this));
        });

    }

}
