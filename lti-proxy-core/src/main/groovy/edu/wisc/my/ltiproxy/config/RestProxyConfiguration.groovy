package edu.wisc.my.ltiproxy.config

import groovy.transform.CompileStatic
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@CompileStatic
@ComponentScan(value = ["edu.wisc.my.ltiproxy.dao", "edu.wisc.my.ltiproxy.service", "edu.wisc.my.ltiproxy.web"])
@Configuration
public class RestProxyConfiguration {

}
