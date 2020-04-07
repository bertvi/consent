package com.gofore.consent.service_declaration.controller;

import com.gofore.consent.service_declaration.ServiceDeclarationApiService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@RequestMapping("/heartbeat")
@Controller
public class HeartbeatController {
  @Resource
  private ServiceDeclarationApiService systemService;

  @GetMapping(path = "/heartbeat")
  @ResponseBody
  public String getHeartbeat() {
    systemService.checkDatabaseConnection();
    return "OK";
  }
}
