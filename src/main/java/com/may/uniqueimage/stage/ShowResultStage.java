/*
 * @(#)AlertStage.java 2015. 01. 15.
 *
 * Copyright 2015 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.may.uniqueimage.stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 처리 결과를 보여주는 얼럿창.
 *
 * @author yuwook
 */
public class ShowResultStage extends Stage {
    private String message;
    private Button okButton;
    private Button openDirectory;
    private String destDirectory;

    public void setDestDirectory(String destDirectory) {
        this.destDirectory = destDirectory;
    }

    /**
     * Creates a new instance of decorated {@code Stage}.
     *
     * @throws IllegalStateException if this constructor is called on a thread
     *                                         other than the JavaFX Application Thread.
     */
    public ShowResultStage(String message) {
        super();

        okButton = new Button("닫기");
        okButton.setOnAction(event -> close());

        openDirectory = new Button("작업 폴더 열기");
        openDirectory.setOnAction(event -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(new File(destDirectory));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        initModality(Modality.WINDOW_MODAL);
        setScene(new Scene(HBoxBuilder.create().
                children(new Text(message), okButton, openDirectory).
                alignment(Pos.CENTER).padding(new Insets(20)).build()));

        this.message = message;
    }


}
