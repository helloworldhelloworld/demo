# AI Framework
This project demonstrates basic usage of **Spring AI** with function calls and
retrieval–augmented generation (RAG). The application is built with Maven and
uses Spring Boot.

The source code follows a layered design. AI interactions, RAG logic and tool
functions are implemented in dedicated services under `com.example.application`
for easier extension and testing. Conversations are tracked per session and can
be cleared using an HTTP `DELETE` request.

### Running

Set your OpenAI API key in the `OPENAI_API_KEY` environment variable and run:

```bash
mvn spring-boot:run
```

Once running you can chat with the AI over HTTP:

```bash
curl -X POST http://localhost:8080/chat/session1 -d 'Hello'
```

Use the same session id to continue the conversation. To reset a conversation
send:

```bash
curl -X DELETE http://localhost:8080/chat/session1
```

You can also call simple tools such as the current time using a `/time` command.

### Configuring Prompts

Prompt templates are defined in `application.yaml` under the `app.prompt` section.
Simple values live under `templates` while scenario specific prompts (e.g. RAG or
multi turn chat) are configured under `scenarios`. Placeholders like `{context}`
or `{history}` are replaced at runtime. You can add additional templates or
scenarios and reference them from the application code.
