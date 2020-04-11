package com.gofore.consent.service_declaration.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HeartbeatResponse implements Serializable {

    private static final long serialVersionUID = 4048861576368846345L;

    private Boolean databaseUp;

    private String appName;

    private String appVersion;

    private LocalDateTime systemTime;

}
