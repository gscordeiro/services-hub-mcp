# services-hub-mcp

A dynamic MCP (Model Context Protocol) server that bridges your existing REST APIs to AI agents and MCP-compatible clients — no code required per endpoint.

## Overview

`services-hub-mcp` lets you register any REST API endpoint through a simple admin interface, describe it with a name, description, and JSON Schema, and instantly expose it as an MCP tool. AI clients like Claude Desktop can then discover and call your internal services just like any other tool.

The goal is to make internal company APIs — previously consumed only by other applications — directly accessible to AI agents and, in the future, to employees through a chat interface.

## How It Works
```
REST APIs → [services-hub-mcp] → MCP Tools → Claude Desktop / AI Agents / Chat UI
```

1. **Register** your REST endpoints in the database (URL, method, headers, auth, etc.)
2. **Describe** each endpoint with a name, description, and JSON Schema for its inputs
3. **Connect** any MCP client to the server
4. **Call** your APIs naturally through conversation or agent workflows

## Use Cases

- Give Claude Desktop access to your internal microservices
- Let employees query internal data through a chat interface without needing API knowledge
- Bridge legacy REST services into modern AI workflows without rewriting them
- Centralize all your company's APIs in a single, AI-accessible hub

## Features

- ✅ Dynamic tool registration — no redeploy needed to add new endpoints
- ✅ JSON Schema-based input validation per endpoint
- ✅ Supports any REST API (Currently GET, but other verbs in the future.)
- ✅ MCP-compliant — works with Claude Desktop and any MCP client
- ✅ Admin interface to manage services and endpoints (future)

## Getting Started

> To start the APIs exposed in the example code, run the `FakeCarApiServer` and `FakeEmployeeApiServer` classes.

## License

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
