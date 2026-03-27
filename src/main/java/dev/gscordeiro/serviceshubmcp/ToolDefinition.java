package dev.gscordeiro.serviceshubmcp;

public record ToolDefinition(
        String name,
        String description,
        String endpoint,
        String method,
        String jsonSchema
) {}