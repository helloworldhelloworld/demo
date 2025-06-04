# AI Framework
This project demonstrates basic usage of **Spring AI** with function calls and
retrieval–augmented generation (RAG). The application is built with Maven and
uses Spring Boot.

The source code follows a simple layered design. AI interactions, RAG logic and
tool functions are implemented in separate service classes under
`com.example.service` for easier extension and testing.

### Running

Set your OpenAI API key in the `OPENAI_API_KEY` environment variable and run:

```bash
mvn spring-boot:run
```
