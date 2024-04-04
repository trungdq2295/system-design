package com.trungnguyen.linkshortener.model;


import lombok.Builder;

@Builder
public record GenericResponse<T>(int status, String message, T data) {
}
