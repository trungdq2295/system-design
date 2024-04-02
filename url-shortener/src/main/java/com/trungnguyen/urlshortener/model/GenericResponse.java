package com.trungnguyen.urlshortener.model;


import lombok.Builder;

@Builder
public record GenericResponse<T>(int status, String message, T data) {
}
