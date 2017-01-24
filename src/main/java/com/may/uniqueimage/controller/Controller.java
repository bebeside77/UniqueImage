package com.may.uniqueimage.controller;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.may.uniqueimage.ImageMaker;
import com.may.uniqueimage.stage.AlertStage;
import com.may.uniqueimage.stage.ShowResultStage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private Button srcFileChooseButton;
    @FXML
    private TextField srcFilePathText;
    @FXML
    private TextField destPathText;
    @FXML
    private Button destPathChooseButton;
    @FXML
    private TextField makeNumText;
    @FXML
    private Button makeButton;

    private ImageMaker imageMaker = new ImageMaker();

    public void srcFileChooseButton_onClick() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFileFilter = new FileChooser.ExtensionFilter(
            "Image File", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFileFilter);
        fileChooser.setTitle("원본 이미지 파일 선택");

        if (StringUtils.isNotEmpty(srcFilePathText.getText())) {
            File srcFile = new File(srcFilePathText.getText());

            if (srcFile.exists() && srcFile.getParentFile().isDirectory()) {
                fileChooser.setInitialDirectory(srcFile.getParentFile());
            }
        }

        File srcFile = fileChooser.showOpenDialog(null);

        if (srcFile != null) {
            srcFilePathText.setText(srcFile.getAbsolutePath());
        }
    }

    public void destPathChooseButton_onClick() {
        System.out.println("destPathChooseButton_onClick");

        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("이미지 생성 폴더 선택");

        if (StringUtils.isNotEmpty(srcFilePathText.getText())) {
            File srcFile = new File(srcFilePathText.getText());

            if (srcFile.exists() && srcFile.getParentFile().isDirectory()) {
                dirChooser.setInitialDirectory(srcFile.getParentFile());
            }
        }

        File destPath = dirChooser.showDialog(null);

        if (destPath != null) {
            destPathText.setText(destPath.getAbsolutePath());
        }
    }

    public void makeButton_onClick() {
        System.out.println("makeButton_onClick");

        String srcFilePath = srcFilePathText.getText();
        String destPath = destPathText.getText();
        Integer makeNum = Integer.valueOf(makeNumText.getText());

        if (srcFilePath == null || srcFilePath.isEmpty()) {
            new AlertStage("소스 이미지 경로를 입력해주세요.").show();
            return;
        }
        if (destPath == null || destPath.isEmpty()) {
            new AlertStage("이미지가 생성될 폴더를 입력해주세요.").show();
            return;
        }
        if (makeNum == null || makeNum < 1 || makeNum > 100) {
            new AlertStage("생성 이미지 갯수는 1 ~ 100 사이로 입력해주세요.").show();
            return;
        }

        Map<String, String> result = imageMaker.makeParallel(srcFilePath, destPath, makeNum);

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Button okButton = new Button("확인");
        okButton.setOnAction(event -> dialogStage.close());

        ShowResultStage showResultStage;
        if ("OK".equals(result.get("status"))) {
            showResultStage = new ShowResultStage(result.get("success_count") + "개 생성에 성공했습니다.");
        } else {
            String message = String.format("에러가 발생했습니다. %s 개 생성에 성공했습니다.\n 에러메시지 : %s",
                result.get("success_count"), result.get("error_message"));
            showResultStage = new ShowResultStage(message);
        }

        showResultStage.setDestDirectory(destPathText.getText());
        showResultStage.show();
    }
}
