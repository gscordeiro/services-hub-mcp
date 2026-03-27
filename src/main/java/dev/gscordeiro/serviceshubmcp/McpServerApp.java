package dev.gscordeiro.serviceshubmcp;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class McpServerApp {
    static void main(String[] args) {

        var logger = LoggerFactory.getLogger(McpServerApp.class);

        ToolRepository repository = new ToolRepository();
        RestToolExecutor executor = new RestToolExecutor();

        List<McpServerFeatures.SyncToolSpecification> tools = new ArrayList<>();

        JsonMapper jsonMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        McpJsonMapper mcpMapper = new JacksonMcpJsonMapper(jsonMapper);

        for (ToolDefinition def : repository.findAll()) {

            logger.debug("Processing tool: {}", def.name());

            McpSchema.JsonSchema schema;
            try {
                logger.debug("Parsing tool schema for: {} -> schema: {}", def.name(), def.jsonSchema());
                schema = jsonMapper.readValue(def.jsonSchema(), McpSchema.JsonSchema.class);
            } catch (Exception e) {
                logger.error("Error parsing tool schema: {} -> schema: {}", def.name(), def.jsonSchema(), e);
                throw new RuntimeException("Error parsing tool schema: " + def.name(), e);
            }

            var tool = McpServerFeatures.SyncToolSpecification.builder()
                    .tool(McpSchema.Tool.builder()
                            .name(def.name())
                            .description(def.description())
                            .inputSchema(schema)
                            .build())
                    .callHandler((exchange, request) -> {

                        Map<String, Object> params = request.arguments();

                        Object result = executor.execute(def, params);

                        return McpSchema.CallToolResult.builder()
                                .content(List.of(new McpSchema.TextContent(jsonMapper.writeValueAsString(result))))
                                .build();
                    })
                    .build();

            tools.add(tool);
        }

        var transportProvider = new StdioServerTransportProvider(mcpMapper);
        McpSyncServer syncServer = McpServer.sync(transportProvider)
                .serverInfo("service-hub-mcp", "0.0.1")
                .capabilities(McpSchema.ServerCapabilities.builder()
//                        .resources(false, true)  // Resource support: subscribe=false, listChanged=true
                        .tools(true)             // Enable tool support with list changes
//                        .prompts(true)           // Enable prompt support with list changes
//                        .completions()           // Enable completions support
                        .logging()               // Enable logging support
                        .build())
                .tools(tools)
                .build();

        logger.info("Dynamic MCP Server running...");
    }
}
