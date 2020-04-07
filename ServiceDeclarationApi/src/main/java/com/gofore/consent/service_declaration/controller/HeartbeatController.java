package com.gofore.consent.service_declaration.controller;

import com.gofore.consent.service_declaration.ServiceDeclarationApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Api(value = "/heartbeat")
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
  @GetMapping(path = "/heartbeat")
  @ResponseBody
  public String getHeartbeat() {
    systemService.checkDatabaseConnection();
    return "OK";
  }
}
