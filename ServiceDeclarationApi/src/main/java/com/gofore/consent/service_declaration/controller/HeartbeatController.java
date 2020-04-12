package com.gofore.consent.service_declaration.controller;

import com.gofore.consent.service_declaration.ServiceDeclarationApiService;
import com.gofore.consent.service_declaration.model.HeartbeatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RequestMapping("/heartbeat")
@RestController
public class HeartbeatController {
  @Resource
  private ServiceDeclarationApiService systemService;

  @Value("${info.build.name}")
  private String appName;

  @Value("${info.build.version}")
  private String appVersion;

  @GetMapping(path = "/heartbeat", produces = "application/json")
  public HeartbeatResponse getHeartbeat() {
    return HeartbeatResponse.builder()
            .appName(appName)
            .appVersion(appVersion)
            .systemTime(LocalDateTime.now())
            .databaseUp(Boolean.valueOf(systemService.checkDatabaseConnection())).build();
  }
}
