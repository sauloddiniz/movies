package com.movies.model.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ErrorFieldDTO implements Serializable {
    private String objectName;
    private String field;
    private String defaultMessage;
    public static ErrorFieldDTO converter(FieldError objectError){
        return ErrorFieldDTO.builder()
                .objectName(objectError.getObjectName())
                .field(objectError.getField())
                .defaultMessage(objectError.getDefaultMessage())
                .build();
    }
}
