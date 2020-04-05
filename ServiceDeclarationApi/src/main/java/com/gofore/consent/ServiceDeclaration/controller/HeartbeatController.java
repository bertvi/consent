package com.gofore.consent.ServiceDeclaration.controller;

import com.gofore.consent.ServiceDeclaration.ServiceDeclarationApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Api(description = "Service for monitoring system heartbeat", value = "/heartbeat")
@RequestMapping("/heartbeat")
@Controller
public class HeartbeatController {
  @Resource
  private ServiceDeclarationApiService systemService;

  @ApiOperation("Get system heartbeat")
  @ApiResponses({
    @ApiResponse(code = 200, message = "System is healthy, response body is 'OK'"),
    @ApiResponse(code = 500, message = "System is unhealthy, response body contains error description")
  })
  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public String getHeartbeat() {
    systemService.checkDatabaseConnection();
    return "OK";
  }
}
