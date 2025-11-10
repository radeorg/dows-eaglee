package org.dows.eaglee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "项目")
@Data
public class TaskProjectResponse {

    @Schema(description = "项目名称")
    public String projectName;
    @Schema(description = "项目编码")
    public String projectCode;

}
