package com.dev.commons.exception;

import com.dev.commons.response.ErrorModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CustomException extends RuntimeException{
    private ErrorModel errorModel;
}
