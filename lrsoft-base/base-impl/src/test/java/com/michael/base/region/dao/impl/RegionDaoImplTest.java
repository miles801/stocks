package com.michael.base.region.dao.impl;

import com.michael.base.AbstractTestWrapper;
import com.michael.base.region.dao.RegionDao;
import org.junit.Test;

import javax.annotation.Resource;

import static junit.framework.Assert.assertTrue;

/**
 * @author miles
 * @datetime 2014/4/18 15:17
 */
public class RegionDaoImplTest extends AbstractTestWrapper {
    @Resource
    private RegionDao regionDao;

    @Test
    public void testNextSequenceNo() throws Exception {
        int sequenceNo = regionDao.nextSequenceNo(null);
        assertTrue(sequenceNo > -1);
    }
}
