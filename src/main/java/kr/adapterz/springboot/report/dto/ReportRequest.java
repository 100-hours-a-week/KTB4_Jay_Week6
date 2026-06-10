package kr.adapterz.springboot.report.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReportRequest {
    private Long userId;
    private String reason;
}
