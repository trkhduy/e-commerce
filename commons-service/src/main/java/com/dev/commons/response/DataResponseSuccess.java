package com.dev.commons.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataResponseSuccess<T> {
    private boolean status;
    private T result;
}
