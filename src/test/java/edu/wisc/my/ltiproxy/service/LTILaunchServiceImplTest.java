package edu.wisc.my.ltiproxy.service;

import edu.wisc.my.ltiproxy.LTIParameters;
import edu.wisc.my.ltiproxy.dao.LTILaunchPropertyFileDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hamcrest.core.IsCollectionContaining;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LTILaunchServiceImplTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected LTILaunchPropertyFileDao dao;
    protected LTILaunchServiceImpl instance;
    
    protected static String key;
    protected static Map<String, String> exampleHeaders;
    protected static JSONObject expectedFormData;
    protected static Map<String, String> expectedPreparedParameters;
    protected static Map<String, String> expectedReplacedHeaders;
    protected static String actionURL;
    protected static Map<String, String> expectedSignedParameters;
    protected static LTIParameters expectedSignedLTIParameters;
    protected static List<NameValuePair> expectedFormBody;
    
    public LTILaunchServiceImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        key = "test";
        
        exampleHeaders = new HashMap<String, String>();
        exampleHeaders.put("Host", "localhost:8080");
        exampleHeaders.put("Connection", "keep-alive");
        exampleHeaders.put("Pragma", "no-cache");
        exampleHeaders.put("Cache-Control", "no-cache");
        exampleHeaders.put("Upgrade-Insecure-Requests", "1");
        exampleHeaders.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/58.0.3029.110 Chrome/58.0.3029.110 Safari/537.36");
        exampleHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        exampleHeaders.put("DNT", "1");
        exampleHeaders.put("Referer", "http://localhost:8080/web/apps/details/blackboard-ultra-launcher?q=blackboard");
        exampleHeaders.put("Accept-Encoding", "gzip, deflate, sdch, br");
        exampleHeaders.put("Accept-Language", "en-US,en;q=0.8");
        exampleHeaders.put("eppn", "testman@wisc.edu");
        exampleHeaders.put("isMemberOf", "uw:domain:my.wisc.edu:my_uw_administrators;uw:domain:ohr.wisc.edu:trems;uw:domain:apps.mumaa.doit.wisc.edu:lumen_access;uw:domain:my.wisc.edu:my_uw_hr_officers");
        exampleHeaders.put("mail", "TESTMAN@WISC.EDU");
        exampleHeaders.put("pubcookie-user", "testman");
        exampleHeaders.put("uid", "testman");
        exampleHeaders.put("wiscEduHRPersonID", "01234567");
        exampleHeaders.put("wiscEduHRSEmplID", "01234567");
        exampleHeaders.put("wiscEduISISEmplID", "0123456789");
        exampleHeaders.put("wiscEduPVI", "UW123B456");
        exampleHeaders.put("wiscEduSORLastName", "TESTMAN");
        exampleHeaders.put("wiscEduWiscardAccountNumber", "01234567890");
        exampleHeaders.put("eduWisconsinGivenName", "TESTY TESTMAN");
        
        expectedPreparedParameters = new HashMap<>();
        expectedPreparedParameters.put("context_id", "test");
        expectedPreparedParameters.put("context_label", "test");
        expectedPreparedParameters.put("context_title", "test");
        expectedPreparedParameters.put("launch_presentation_document_target", "frame");
        expectedPreparedParameters.put("launch_presentation_locale", "EN-US__");
        expectedPreparedParameters.put("lis_outcome_service_url", "http://localhost:8080/d2l/le/lti/Outcome");
        expectedPreparedParameters.put("lis_person_contact_email_primary", "TESTMAN@WISC.EDU");
        expectedPreparedParameters.put("lis_person_name_family", "TESTMAN");
        expectedPreparedParameters.put("lis_person_name_full", "testman");
        expectedPreparedParameters.put("lis_person_name_given", "TESTY TESTMAN");
        expectedPreparedParameters.put("lti_message_type", "basic-lti-launch-request");
        expectedPreparedParameters.put("lti_version", "LTI-1p0");
        expectedPreparedParameters.put("resource_link_description", "MyUW Blackboard Ultra Collaborate LTI Launcher");
        expectedPreparedParameters.put("resource_link_title", "Collaborate Ultra (LTI)");
        expectedPreparedParameters.put("roles", "urn:lti:instrole:ims/lis/Staff,Staff");
        expectedPreparedParameters.put("tool_consumer_info_product_family_code", "desire2learn");
        expectedPreparedParameters.put("tool_consumer_info_version", "10.6.0");
        expectedPreparedParameters.put("user_id", "UW123B456");
        
        expectedReplacedHeaders = new HashMap<>();
        expectedReplacedHeaders.put("lis_person_contact_email_primary", "TESTMAN@WISC.EDU");
        expectedReplacedHeaders.put("lis_person_name_family", "TESTMAN");
        expectedReplacedHeaders.put("lis_person_name_full", "testman");
        expectedReplacedHeaders.put("lis_person_name_given", "TESTY TESTMAN");
        expectedReplacedHeaders.put("user_id", "UW123B456");
        
        actionURL = "http://localhost:8080/lti";
        expectedSignedParameters = new HashMap<>();
        expectedSignedParameters.put("oauth_nonce", "179248056711247"); //UNIQUE
        expectedSignedParameters.put("oauth_signature", "9gf64nLk+8IAsEbpxMozwaKuOng="); //UNIQUE
        expectedSignedParameters.put("oauth_consumer_key", "TESTKEY");
        expectedSignedParameters.put("oauth_signature_method", "HMAC-SHA1");
        expectedSignedParameters.put("oauth_timestamp", "1497456878"); //UNIQUE
        expectedSignedParameters.put("oauth_version", "1.0");
        expectedSignedParameters.putAll(expectedPreparedParameters);
        expectedSignedLTIParameters = new LTIParameters(actionURL, expectedSignedParameters);
        
        expectedFormBody = new ArrayList<>();
        expectedFormBody.add(new BasicNameValuePair("oauth_nonce", "179248056711247"));
        expectedFormBody.add(new BasicNameValuePair("oauth_signature", "9gf64nLk+8IAsEbpxMozwaKuOng="));
        expectedFormBody.add(new BasicNameValuePair("oauth_consumer_key", "TESTKEY"));
        expectedFormBody.add(new BasicNameValuePair("oauth_signature_method", "HMAC-SHA1"));
        expectedFormBody.add(new BasicNameValuePair("oauth_timestamp", "1497456878"));
        expectedFormBody.add(new BasicNameValuePair("oauth_version", "1.0"));
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new LTILaunchServiceImpl();
        instance.setLTILaunchProperityFileDao(dao);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getRedirectUri method, of class LTILaunchServiceImpl.
     */
    @Test
    @Ignore
    public void testGetRedirectUri() throws Exception {
        logger.info("getRedirectUri");
        fail("TODO inject Mock HttpClient for test getRedirectUri");
    }

    /**
     * Test of getFormData method, of class LTILaunchServiceImpl.
     */
    @Test
    @Ignore
    public void testGetFormData() throws Exception {
        logger.info("getFormData");
        fail("TODO write this test!");
    }

    /**
     * Test of prepareParameters method, of class LTILaunchServiceImpl.
     */
    @Test
    public void testPrepareParameters() throws Exception {
        logger.info("prepareParameters");
        Map<String, String> expResult = expectedPreparedParameters;
        Map<String, String> result = instance.prepareParameters(key, exampleHeaders);
        assertEquals(expResult, result);
    }

    /**
     * Test of signParameters method, of class LTILaunchServiceImpl.
     */
    @Test
    public void testSignParameters() throws Exception {
        logger.info("signParameters");
        Map<String, String> paramsToSign = expectedPreparedParameters;
        LTIParameters expResult = expectedSignedLTIParameters;
        LTIParameters result = instance.signParameters(key, paramsToSign);
        assertNotEquals(expResult, result);

        assertEquals(expResult.getActionURL(), result.getActionURL());
        assertEquals(expResult.getSignedParameters().keySet(), result.getSignedParameters().keySet());
        assertEquals(
                expResult.getSignedParameters().get("oauth_consumer_key"), 
                "TESTKEY"
        );
        assertEquals(
                expResult.getSignedParameters().get("oauth_signature_method"), 
                "HMAC-SHA1"
        );
        assertEquals(
                expResult.getSignedParameters().get("oauth_version"), 
                "1.0"
        );
    }

    /**
     * Test of replaceHeaders method, of class LTILaunchServiceImpl.
     */
    @Test
    public void testReplaceHeaders() throws Exception {
        logger.info("replaceHeaders");
        Map<String, String> requestHeaders = exampleHeaders;
        Map<String, String> expResult = expectedReplacedHeaders;
        Map<String, String> result = instance.replaceHeaders(key, requestHeaders);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildFormBody method, of class LTILaunchServiceImpl.
     */
    @Test
    @Ignore
    public void testBuildFormBody() throws Exception {
        logger.info("buildFormBody");
        fail("trivial");
    }
    
}
