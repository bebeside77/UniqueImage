/*
 * @(#)ImageMakerTest.java  2014. 12. 30.
 *
 * Copyright 2014 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.may.uniqueimage;


import org.junit.Test;

/**
 * @author yuwook
 */
public class ImageMakerTest {
    private ImageMaker imageMaker = new ImageMaker();

    @Test
    public void testMake() throws Exception {
        imageMaker.makeParallel("D:\\멀티미디어\\이미지\\222.jpg", "D:\\멀티미디어\\이미지", 1);
    }
}