# PLAN.md — Be Positive

## Overview

A single-page app where users submit messages evaluated for positivity by a local LLM (Ollama/qwen3:8b). The Spring Boot backend calls Ollama, stores accepted messages in-memory, and returns accept/reject responses. The React frontend displays the result. Tested with Python Playwright.

---

## Phase 1: Backend API (Spring Boot)

### Step 1: Add Maven dependencies
- `spring-boot-starter-web` for REST endpoints
- HTTP client support for calling Ollama (`RestClient` built into Spring Boot)
- **File:** `bepositive/pom.xml`

### Step 2: Configure application properties
- Ollama base URL: `http://localhost:11434`
- Model name: `qwen3:8b`
- CORS allowed origin: `http://localhost:5173`
- **File:** `bepositive/src/main/resources/application.properties`

### Step 3: Create message model
- Java record: `Message(String id, String content, LocalDateTime timestamp)`
- Java record: `AnalysisResult(boolean accepted, String reason, String suggestedRewrite)`
- **Files:** `Message.java`, `AnalysisResult.java`

### Step 4: Create in-memory message store
- `@Component` class with a `ConcurrentHashMap<String, Message>`
- Methods: `save(Message)`, `findAll()`
- **File:** `MessageStore.java`

### Step 5: Create Ollama service
- Use Spring `RestClient` to POST to Ollama `/api/generate`
- System prompt instructs LLM to evaluate positivity and respond in JSON
- Parse response into `AnalysisResult`
- **File:** `OllamaService.java`

### Step 6: Create REST controller
- `POST /api/messages` — evaluate message via Ollama, store if accepted, return result
- `GET /api/messages` — return all stored messages
- **File:** `MessageController.java`

### Step 7: Add CORS configuration
- Allow `http://localhost:5173` (Vite dev server)
- **File:** `WebConfig.java`

---

## Phase 2: Frontend (React + Vite + TypeScript)

> Can be done in parallel with Phase 1.

### Step 8: Scaffold project
- `npm create vite@latest frontend -- --template react-ts`
- **Creates:** `frontend/` directory at project root

### Step 9: Build UI
- Single component: text input, submit button, result area
- Success: green message confirming storage
- Rejection: amber/red display of reason + suggested rewrite
- **Files:** `frontend/src/App.tsx`, `frontend/src/App.css`

### Step 10: API client
- Fetch wrapper for `POST http://localhost:8080/api/messages`
- TypeScript types matching backend response
- **File:** `frontend/src/api.ts`

---

## Phase 3: Integration & Testing

### Step 11: Manual end-to-end verification
- Start Ollama: `ollama run qwen3:8b`
- Start backend: `cd bepositive && ./mvnw spring-boot:run`
- Start frontend: `cd frontend && npm run dev`
- Test positive + negative messages

### Step 12: Playwright tests (Python)
- Create `tests/` directory with Python Playwright tests
- Test: positive message → success displayed
- Test: negative message → rejection + rewrite displayed
- **Creates:** `tests/test_bepositive.py`

### Step 13: Playwright MCP demo
- Use Copilot Agent Mode with Playwright MCP
- Demonstrate AI generating tests against the running app

---

## Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| Direct Ollama REST calls (not Spring AI) | Fewer dependencies, transparent for demo |
| Java records for models | Modern Java, zero boilerplate |
| ConcurrentHashMap store | Simplest possible, no database config |
| Flat package (`guru.bonacci.bepositive`) | Only ~6 classes, sub-packages unnecessary |
| JSON-structured LLM prompt | Makes response parsing reliable |
| Frontend at project root (`frontend/`) | Separate from Maven project, clean separation |
| Python Playwright (not Node) | Per requirements, MCP demo compatibility |
| No error handling | Per constraints — keep demo flowing |

---

## LLM Prompt Design

```text
You evaluate messages for positivity. Respond ONLY with JSON:
{"accepted": true/false, "reason": "...", "suggestedRewrite": "..."}

A message is accepted if it is positive, respectful, and constructive.
If rejected, explain why briefly and suggest a positive rewrite.
```

---

## Verification Checklist

1. `cd bepositive && ./mvnw compile` — compiles clean
2. `cd bepositive && ./mvnw test` — context loads
3. `cd frontend && npm run build` — builds clean
4. Submit "You're doing amazing work!" → accepted and stored
5. Submit "This is terrible garbage" → rejected with reason + rewrite
6. `cd tests && python -m pytest` — Playwright tests pass

---

## Out of Scope

- Error handling / validation
- Authentication
- Persistent storage (database)
- Deployment / containerisation
- Multiple pages or routes
- Message history UI
