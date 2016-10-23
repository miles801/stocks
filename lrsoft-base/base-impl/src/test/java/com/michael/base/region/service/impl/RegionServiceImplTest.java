package com.michael.base.region.service.impl;

import com.michael.base.AbstractTestWrapper;
import com.michael.base.region.service.RegionService;
import com.michael.base.region.vo.RegionVo;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class RegionServiceImplTest extends AbstractTestWrapper {

    private RegionService regionService;

    @Before
    public void setUp() throws Exception {
        regionService = beanFactory.getBean(RegionService.class);
    }

    @Test
    public void testGetBelongProvence() throws Exception {
        // 北京
        RegionVo vo = regionService.getBelongProvence("1001");
        Assert.assertNotNull(vo);
    }
}