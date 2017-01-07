package edu.wisc.my.ltiproxy.web;

import edu.wisc.my.ltiproxy.service.LTILaunchService;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.imsglobal.lti.launch.LtiSigningException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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


  @RequestMapping(value="/{key}", method=RequestMethod.GET)
  public @ResponseBody Object proxyResource(HttpServletRequest request, HttpServletResponse response, @PathVariable String key) throws ClientProtocolException, IOException, LtiSigningException, JSONException {

     JSONObject jsonToReturn = LTILaunchService.getFormData(key, request);
     response.setStatus(HttpServletResponse.SC_OK);
     return jsonToReturn.toString();
  }

}
