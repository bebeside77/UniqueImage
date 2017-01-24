/*
 * @(#)AlertStage.java 2015. 01. 15.
 *
 * Copyright 2015 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.may.uniqueimage.stage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author yuwook
 */
public class AlertStage extends Stage {
    private String message;
    private Button okButton;

    /**
     * Creates a new instance of decorated {@code Stage}.
     *
     * @throws IllegalStateException if this constructor is called on a thread
     *                                         other than the JavaFX Application Thread.
     */
    public AlertStage(String message) {
        super();

        okButton = new Button("확인");
        okButton.setOnAction(event -> close());

        initModality(Modality.WINDOW_MODAL);
        setScene(new Scene(VBoxBuilder.create().
                children(new Text(message), okButton).
                alignment(Pos.CENTER).padding(new Insets(15)).build()));

        this.message = message;
    }


}
