Create a detailed implementation plan for a demonstration project called Be Positive.

Context

This project is intentionally small and is being built as part of a live demo showing modern AI-assisted software engineering workflows.

The goal is not to build production-ready software.

The goal is to demonstrate how AI can help us:

- Plan a project
- Build a project
- Explore and test a project
- Generate automated tests
- Learn new technologies quickly

The application itself should remain simple so we can focus on the engineering workflow.

Application Behaviour

The application has a single page containing a text input and submit button.

A user enters a message.

The frontend submits the message to a backend API.

The backend sends the message to a local LLM running via Ollama (qwen3:8b).

The LLM determines whether the message is sufficiently positive, respectful and constructive.

If accepted:

- The message is stored.
- The frontend displays a success message.

If rejected:

- The frontend displays the rejection reason.
- The frontend displays a suggested rewrite.

Testing

- We will use python playwright to demonstrate agentic/MCP testing

Technology Stack

Frontend:
- React
- Vite
- TypeScript

Backend:
- Spring Boot
- Java 25

AI:
- Ollama
- qwen3:8b

Testing:
- Playwright
- Playwright MCP
- GitHub Copilot Agent Mode

Constraints

- Keep the solution intentionally simple.
- Optimise for demo value over production complexity.
- Prefer boring maintainable solutions.
- Avoid unnecessary enterprise patterns.
- Use in-memory storage initially.
- Design for rapid implementation during a live coding session.
- Don't worry about errors and exceptions.

Produce a complete PLAN.md containing
