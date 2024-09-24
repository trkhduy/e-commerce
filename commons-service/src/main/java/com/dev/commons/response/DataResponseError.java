package com.dev.commons.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataResponseError {
    private boolean status;
    private ResultErrorModel result;
}
