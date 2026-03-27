package dev.gscordeiro.serviceshubmcp;

import java.util.List;

public class ToolRepository {

    public List<ToolDefinition> findAll() {
        return List.of(
                new ToolDefinition(
                        "get_car",
                        "Fetches a car by ID. Use when the user wants details about a specific car.",
                        "http://localhost:8080/cars/{id}",
                        "GET",
                        """
                        {
                          "type": "object",
                          "properties": {
                            "id": {
                              "type": "string",
                              "description": "Unique car ID. Ask the user if it has not been provided."
                            }
                          },
                          "required": ["id"]
                        }
                        """
                ),
                new ToolDefinition(
                        "list_employees",
                        "List all employees with their name, role, and department.",
                        "http://localhost:8081/employees",
                        "GET",
                        """
                        {
                          "type": "object"
                        }
                        """
                )
        );
    }
}