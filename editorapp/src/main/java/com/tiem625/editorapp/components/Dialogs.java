package com.tiem625.editorapp.components;

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 *
 * @author Anatolij
 */
public class Dialogs {

    private Dialogs() {
    }

    private static Alert makeAlert(Window owner, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(owner);

        return alert;
    }

    public static Alert exceptionDialogue(Window owner, Throwable ex) {
        Alert alert = makeAlert(owner, Alert.AlertType.ERROR);
        alert.setTitle("Error Occured!");
        alert.setContentText(ex.getLocalizedMessage());

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        return alert;
    }

    public static Alert infoDialog(Window owner, String info) {
        Alert alert = makeAlert(owner, Alert.AlertType.INFORMATION);
        alert.setContentText(info);
        return alert;
    }

}