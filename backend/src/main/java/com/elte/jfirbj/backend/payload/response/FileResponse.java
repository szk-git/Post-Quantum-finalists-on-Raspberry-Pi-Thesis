package com.elte.jfirbj.backend.payload.response;

import com.elte.jfirbj.backend.models.enums.AlgorithmEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponse {
    private String name;
    private String url;
    private String type;
    private long time;
    private long creationTime;
    private AlgorithmEnum algorithm;
    private long size;
}
