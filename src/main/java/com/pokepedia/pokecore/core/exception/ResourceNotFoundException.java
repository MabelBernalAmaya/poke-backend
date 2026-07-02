package com.pokepedia.pokecore.core.exception;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(resource + " con " + field + "=" + value + " no encontrado", "NOT_FOUND");
    }
}