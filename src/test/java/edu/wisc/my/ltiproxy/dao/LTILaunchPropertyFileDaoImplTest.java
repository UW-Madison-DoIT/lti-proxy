package edu.wisc.my.ltiproxy.dao;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.LinkedHashMap;
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
    
    protected static String key;
    protected static String expectedActionURL;
    protected static Map<String, String> expectedLaunchParameters;
    protected static Multimap<String, String> expectedHeadersToReplace;
    
    public LTILaunchPropertyFileDaoImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        key = "test";
        
        expectedActionURL = "http://localhost:8080/lti";
        
        expectedLaunchParameters = new LinkedHashMap<>();
        expectedLaunchParameters.put("context_id", "test");
        expectedLaunchParameters.put("context_label", "test");
        expectedLaunchParameters.put("context_title", "test");
        expectedLaunchParameters.put("launch_presentation_document_target", "frame");
        expectedLaunchParameters.put("launch_presentation_locale", "EN-US__");
        expectedLaunchParameters.put("lis_outcome_service_url", "http://localhost:8080/d2l/le/lti/Outcome");
        expectedLaunchParameters.put("lis_person_contact_email_primary", "");
        expectedLaunchParameters.put("lis_person_name_family", "");
        expectedLaunchParameters.put("lis_person_name_full", "");
        expectedLaunchParameters.put("lis_person_name_given", "");
        expectedLaunchParameters.put("lti_message_type", "basic-lti-launch-request");
        expectedLaunchParameters.put("lti_version", "LTI-1p0");
        expectedLaunchParameters.put("resource_link_description", "MyUW Blackboard Ultra Collaborate LTI Launcher");
        expectedLaunchParameters.put("resource_link_title", "Collaborate Ultra (LTI)");
        expectedLaunchParameters.put("roles", "urn:lti:instrole:ims/lis/Staff,Staff");
        expectedLaunchParameters.put("tool_consumer_info_product_family_code", "desire2learn");
        expectedLaunchParameters.put("tool_consumer_info_version", "10.6.0");
        expectedLaunchParameters.put("user_id", "");
        
        expectedHeadersToReplace = ArrayListMultimap.create();
        expectedHeadersToReplace.put("lis_person_contact_email_primary", "mail");
        expectedHeadersToReplace.put("lis_person_name_family", "eduWisconsinSurname");
        expectedHeadersToReplace.put("lis_person_name_family", "wiscEduSORLastName");
        expectedHeadersToReplace.put("lis_person_name_full", "eduWisconsinCommonName");
        expectedHeadersToReplace.put("lis_person_name_full", "displayName");
        expectedHeadersToReplace.put("lis_person_name_full", "uid");
        expectedHeadersToReplace.put("lis_person_name_given", "eduWisconsinGivenName");
        expectedHeadersToReplace.put("lis_person_name_given", "givenName");
        expectedHeadersToReplace.put("user_id", "eduWisconsinSPVI");
        expectedHeadersToReplace.put("user_id", "wiscEduPVI");

    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new LTILaunchPropertyFileDaoImpl();
        instance.setEnv(env);
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
        Map<String, String> expResult = expectedLaunchParameters;
        Map<String, String> result = instance.getLaunchParameters(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of getHeadersToReplace method, of class LTILaunchPropertyFileDaoImpl.
     */
    @Test
    public void testGetHeadersToReplace() throws Exception {
        logger.info("getHeadersToReplace");
        Multimap<String, String> expResult = expectedHeadersToReplace;
        Multimap<String, String> result = instance.getHeadersToReplace(key);
        assertEquals(expResult.asMap(), result.asMap());
    }

    /**
     * Test of getProperty method, of class LTILaunchPropertyFileDaoImpl.
     */
    @Test
    public void testGetProperty() {
        logger.info("getProperty");
        String propertyName = null;
        String expResult = null;
        String result = instance.getProperty(key, propertyName);
        assertEquals(expResult, result);
        
        propertyName = "";
        expResult = null;
        result = instance.getProperty(key, propertyName);
        assertEquals(expResult, result);
        
        propertyName = "not_a_property";
        expResult = null;
        result = instance.getProperty(key, propertyName);
        assertEquals(expResult, result);
        
        propertyName = "actionURL";
        expResult = expectedActionURL;
        result = instance.getProperty(key, propertyName);
        assertEquals(expResult, result);
    }
    
}
