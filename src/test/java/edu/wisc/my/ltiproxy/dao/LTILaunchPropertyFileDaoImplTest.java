package edu.wisc.my.ltiproxy.dao;

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LTILaunchPropertyFileDaoImplTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected Environment env;
    protected LTILaunchPropertyFileDaoImpl instance;
    protected String key;
    
    public LTILaunchPropertyFileDaoImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new LTILaunchPropertyFileDaoImpl();
        instance.setEnv(env);
        key = "test";
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getLaunchParameters method, of class LTILaunchPropertyFileDaoImpl.
     */
    @Test
    public void testGetLaunchParameters() throws Exception {
        logger.info("getLaunchParameters");
        Map<String, String> expResult = null;
        Map<String, String> result = instance.getLaunchParameters(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeadersToReplace method, of class LTILaunchPropertyFileDaoImpl.
     */
    @Test
    public void testGetHeadersToReplace() throws Exception {
        logger.info("getHeadersToReplace");
        Map expResult = null;
        Map result = instance.getHeadersToReplace(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProperty method, of class LTILaunchPropertyFileDaoImpl.
     */
    @Test
    public void testGetProperty() {
        logger.info("getProperty");
        String propertyName = "";
        String expResult = "";
        String result = instance.getProperty(key, propertyName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
