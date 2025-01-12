package com.store.api.model.user;

public record LoginRequest(String username,String password,String sessionId) {
}
