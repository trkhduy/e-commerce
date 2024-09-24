package com.dev.commons.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class DataResponse {
    private boolean status;
    private ResultModel<?> result;

    public DataResponse(boolean status, ResultModel<?> result) {
        this.status = status;
        this.result = result;
    }

}
