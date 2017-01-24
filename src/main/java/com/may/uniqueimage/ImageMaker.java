/*
 * @(#)ImageMaker.java  2014. 12. 30.
 *
 * Copyright 2014 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.may.uniqueimage;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yuwook
 */
public class ImageMaker {
    private Color[] colors = {
            Color.black,
            Color.BLUE,
            Color.CYAN,
            Color.DARK_GRAY,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.YELLOW
    };

    private ThreadPoolExecutor executor =
        (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public Map<String, String> makeParallel(String srcFile, String destPath, int makeNum) {
        ArrayList<Future<Map<String, String>>> futureList = new ArrayList<>();

        for (int i = 0; i < makeNum ; i++) {
            futureList.add(executor.submit(() -> {
                return makeOne(srcFile, destPath);
            }));
        }

        int successCount = 0;
        ArrayList<String> errorMessages = new ArrayList<>();

        for (Future<Map<String, String>> future : futureList) {
            try {
                Map<String, String> futureResult = future.get();
                if (StringUtils.equals(futureResult.get("status"), "OK")) {
                    successCount++;
                } else {
                    errorMessages.add(futureResult.get("error_message"));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> result = new HashMap<>();
        result.put("status", makeNum == successCount ? "OK" : "ERROR");
        result.put("success_count", String.valueOf(successCount));
        result.put("error_message", errorMessages.toString());

        return result;
    }

    public Map<String, String> makeOne(String srcFile, String destPath) {
        Map<String, String> result = new HashMap<>();

        String watermarkText = UUID.randomUUID().toString();

        try {
            BufferedImage sourceImage = ImageIO.read(new File(srcFile));
            Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

            // initializes necessary graphic properties
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
            g2d.setComposite(alphaChannel);
            g2d.setColor(colors[new Random().nextInt(colors.length)]);
            g2d.setFont(new Font("Arial", Font.BOLD, 150));
            FontMetrics fontMetrics = g2d.getFontMetrics();

            Rectangle2D rect = fontMetrics.getStringBounds(watermarkText, g2d);

            // calculates the coordinate where the String is painted
            int centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
            int centerY = sourceImage.getHeight() / 2;

            // paints the textual watermark
            g2d.drawString(watermarkText, centerX, centerY);

            String fileName = watermarkText + "_" + new File(srcFile).getName();
            ImageIO.write(sourceImage, "png", new File(destPath + "/" + fileName));
            g2d.dispose();

            System.out.println("watermark add success.");
        } catch (Exception ex) {
            System.err.println(ex);

            result.put("status", "ERROR");
            result.put("error_message", ex.getMessage());

            return result;
        }

        result.put("status", "OK");

        return result;
    }
}
