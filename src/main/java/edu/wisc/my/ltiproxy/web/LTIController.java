package edu.wisc.my.ltiproxy.web;

import edu.wisc.my.ltiproxy.service.LTILaunchService;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.imsglobal.lti.launch.LtiSigningException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.EnumerableCompositePropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/lti-launch")
public class LTIController {

  private LTILaunchService LTILaunchService;

  
  @Autowired
  public void setLTILaunchService(LTILaunchService LTILaunchService){
      this.LTILaunchService = LTILaunchService;
  }

  @RequestMapping(value="/go/{key}", method=RequestMethod.GET)
  public void proxyRedirect(HttpServletRequest request, HttpServletResponse response, @PathVariable String key) throws 
          ClientProtocolException, IOException, LtiSigningException, JSONException {
      int statusCode = HttpServletResponse.SC_BAD_REQUEST;
      
      URI uri = LTILaunchService.getRedirectUri(key, getHeaders(request));
      
      if (null != uri) {
          response.sendRedirect(uri.toString());
      } else {
          response.sendError(statusCode, "Could not build redirect URI!");
      }
  }

  @RequestMapping(value="/{key}", method=RequestMethod.GET)
  public @ResponseBody Object proxyResource(HttpServletRequest request, HttpServletResponse response, @PathVariable String key) throws 
          ClientProtocolException, IOException, LtiSigningException, JSONException {
      JSONObject jsonToReturn = LTILaunchService.getFormData(key, getHeaders(request));
      response.setStatus(HttpServletResponse.SC_OK);
      return jsonToReturn.toString();
  }

  private Map<String, String> getHeaders(HttpServletRequest req) {
      Map<String, String> result = new HashMap<>();
      
      for (String headerName : Collections.list(req.getHeaderNames())) {
          result.put(headerName, req.getHeader(headerName));
      }
      
      return result;
  }
}
