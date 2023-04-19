package com.qc.competition.utils;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

public class StringUtilsTest {
    static Logger LOGGER = Logger.getLogger(StringUtilsTest.class);


    @Test(groups = {"unit"})
    public void testMD5() {
        String md5 = StringUtils.toMD5Hash("wanna_gg_local");
        Assert.assertEquals(md5, "62a1a6c806d11f4885092bbe105c5e54");
        md5 = StringUtils.toMD5Hash("qc_local");
        Assert.assertEquals(md5, "26cf1fbd1434d0b459f0b211b3d5853e");


    }

}
