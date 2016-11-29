//package edu.wisc.my.lti.web;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@Controller
//@RequestMapping("/lti")
//public class ResourceProxyController {
//
//  @Autowired
//  private Environment env;
//
//  /**
//   * @param env the env to set
//   */
//  void setEnv(Environment env) {
//    this.env = env;
//  }
//
//  @RequestMapping(value="/", method=RequestMethod.GET)
//  public @ResponseBody Object proxyResource(HttpServletRequest request,
//                                            HttpServletResponse response) {
//      response.setStatus(HttpStatus.OK.value());
//      return "Yep, I'm here with a key! ";
//  }
//}
