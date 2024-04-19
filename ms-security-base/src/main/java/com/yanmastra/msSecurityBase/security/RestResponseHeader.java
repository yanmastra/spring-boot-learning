package com.yanmastra.msSecurityBase.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestResponseHeader(int status, String message, @JsonProperty("stack_trace") List<String> stackTrace) {
}
