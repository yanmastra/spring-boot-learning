package com.yanmastra.persistentBase;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseJson<E>(@JsonProperty("status") Boolean status, String message, E data) {
}
