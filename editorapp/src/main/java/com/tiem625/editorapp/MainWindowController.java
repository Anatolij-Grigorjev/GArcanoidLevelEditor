/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tiem625.editorapp;

import com.tiem625.editorapp.components.BrickColorComboBox;
import com.tiem625.editorapp.components.BrickColorComboBox.BrickColorCell;
import com.tiem625.editorapp.components.Dialogs;
import com.tiem625.editorapp.enums.BrickColors;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

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

    private ComboBox<BrickColors>[][] gridNodes;
    private FileChooser fileWindow;
    
    //convenience map to iterate fields accessible by direct map key
    private Map<String, TextField> keyFieldsMap;

    @FXML
    private GridPane gridLevelGrid;

    @FXML
    private void handleCreateLevelBtn(ActionEvent e) {

        createNewLevel();
    }

    @FXML
    private void handleImportLevelBtn(ActionEvent e) {

        Button eventSource = (Button) e.getSource();
        
        Window ownerWindow = eventSource.getScene().getWindow();
        
        fileWindow.setTitle("Choose an existing level JSON...");
        File levelFile = fileWindow.showOpenDialog(ownerWindow);

        if (levelFile != null) {
            try {
                decodeLevelModel(new String(
                        Files.readAllBytes(levelFile.toPath()),
                        StandardCharsets.UTF_8));
            } catch (IOException ex) {
                ex.printStackTrace();
                
                Dialogs.exceptionDialogue(ownerWindow, ex);
            }
        }
    }

    @FXML
    private void handleExportLevelBtn(ActionEvent e) {

    }

    private final Predicate<Integer> MIN_1 = val -> val >= 1;
    private final Predicate<Integer> MIN_0 = val -> val >= 0;

    private final ChangeListener<String> listenerGridRows = new NumericPropChangeListener(1, MIN_1) {
        @Override
        protected void setGridProp(Integer validValue) {
            ObservableList<RowConstraints> rowConstraints = gridLevelGrid.getRowConstraints();
            //dont change value if its the same
            if (rowConstraints.size() == validValue) {
                return;
            }
            //set new constraints count by value
            rowConstraints.clear();
            IntStream.range(0, validValue).forEach(idx -> {
                RowConstraints rc = new RowConstraints();
                rc.setPercentHeight(100.0 / validValue);
                rowConstraints.add(rc);
            });

            populateGrid();
        }
    };

    private final ChangeListener<String> listenerGridCols = new NumericPropChangeListener(1, MIN_1) {
        @Override
        protected void setGridProp(Integer validValue) {
            ObservableList<ColumnConstraints> columnConstraints = gridLevelGrid.getColumnConstraints();
            //dont change value if its the smae
            if (columnConstraints.size() == validValue) {
                return;
            }
            //set new constraint count by value
            columnConstraints.clear();
            IntStream.range(0, validValue).forEach(idx -> {
                ColumnConstraints cc = new ColumnConstraints();
                cc.setPercentWidth(100.0 / validValue);
                columnConstraints.add(cc);
            });

            populateGrid();
        }
    };

    private final ChangeListener<String> listenerGridRowPadding = new NumericPropChangeListener(0, MIN_0) {

        @Override
        protected void setGridProp(Integer validValue) {

            if (gridLevelGrid.getVgap() != validValue) {
                gridLevelGrid.setVgap(validValue);
            }
        }
    };

    private final ChangeListener<String> listenerGridColPadding = new NumericPropChangeListener(0, MIN_0) {

        @Override
        protected void setGridProp(Integer validValue) {

            if (gridLevelGrid.getHgap() != validValue) {
                gridLevelGrid.setHgap(validValue);
            }
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        keyFieldsMap = new HashMap<>();
        fileWindow = new FileChooser();
        keyFieldsMap.put("level_name", fldLevelName);
        keyFieldsMap.put("c_padding", fldColPadding);
        keyFieldsMap.put("r_padding", fldRowPadding);
        
        createNewLevel();
    }

    private void createNewLevel() {
        fldGridRows.textProperty().removeListener(listenerGridRows);
        fldGridCols.textProperty().removeListener(listenerGridCols);
        fldRowPadding.textProperty().removeListener(listenerGridRowPadding);
        fldColPadding.textProperty().removeListener(listenerGridColPadding);

        fldGridRows.textProperty().addListener(listenerGridRows);
        fldGridCols.textProperty().addListener(listenerGridCols);
        fldRowPadding.textProperty().addListener(listenerGridRowPadding);
        fldColPadding.textProperty().addListener(listenerGridColPadding);

        fldLevelName.setText("<NONAME>");
        fldColPadding.setText("0");
        fldRowPadding.setText("0");
        fldGridCols.setText("1");
        fldGridRows.setText("1");

        populateGrid();
    }

    private void clearGrid() {

        Stream.of(gridNodes).forEach(nodes -> {
            Stream.of(nodes).forEach(node -> {

                gridLevelGrid.getChildren().remove(node);
            });
        });

    }

    private void populateGrid() {

        int numRows = gridLevelGrid.getRowConstraints().size();
        int numCols = gridLevelGrid.getColumnConstraints().size();

        if (gridNodes != null) {
            clearGrid();
        }
        gridNodes = new BrickColorComboBox[numRows][numCols];

        double ELEM_MARGIN = 5.0;

        IntStream.range(0, numRows).forEachOrdered(rIdx -> {

            IntStream.range(0, numCols).forEachOrdered(cIdx -> {
                ComboBox<BrickColors> elem = new BrickColorComboBox();
                gridNodes[rIdx][cIdx] = elem;
                gridLevelGrid.add(elem, cIdx, rIdx);

                double cellWidth = gridLevelGrid.getWidth() / gridLevelGrid.getColumnConstraints().size();
                double cellHeight = gridLevelGrid.getHeight() / gridLevelGrid.getRowConstraints().size();

                elem.setMinSize(cellWidth - 4 * ELEM_MARGIN, cellHeight - 4 * ELEM_MARGIN);
                elem.setMaxSize(cellWidth - 4 * ELEM_MARGIN, cellHeight - 4 * ELEM_MARGIN);

                GridPane.setMargin(elem, new Insets(ELEM_MARGIN));
            });
        });

    }

    private void decodeLevelModel(String fileContents) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
            
        JsonNode levelTree = mapper.readTree(fileContents);
        
        //settle level name + padding info
        keyFieldsMap.forEach((key, field) -> {
            
            field.setText(levelTree.get(key).asText());
        });
        
        ArrayNode gridRows = (ArrayNode) levelTree.get("grid");
        ArrayNode firstRow = (ArrayNode) gridRows.get(0);
        
        int numRows = gridRows.size();
        int numCols = firstRow.size();
        //set number of rows and cols
        fldGridRows.setText(String.valueOf(numRows));
        fldGridCols.setText(String.valueOf(numCols));
        
        //iterate contents of rows to set cell values
        IntStream.range(0, numRows).forEach(rIdx -> {
            
            IntStream.range(0, numCols).forEach(cIdx -> {
            
                String code = gridRows.get(rIdx).get(cIdx).getTextValue();
                BrickColorComboBox node = (BrickColorComboBox) gridNodes[rIdx][cIdx];
                node.setButtonCell(new BrickColorCell(node));
                node.setValue(BrickColors.fromJsonCode(code));
            });
            
        });
    }

    private abstract class NumericPropChangeListener implements ChangeListener<String> {

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

            setGridProp(validValue);
        }

        protected abstract void setGridProp(Integer validValue);
    }

}
