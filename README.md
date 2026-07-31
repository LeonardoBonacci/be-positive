## Overview

This demo is intentionally small, silly, and not useful.

We're going to build a tiny application called **Be Positive**. It has one very important job: deciding whether a message is constructive enough to be accepted by the all-powerful positivity robot.

If the message is positive, we'll save it.

If it isn't, we'll explain why and suggest a friendlier version.

That's it.

The app itself isn't the interesting part.

The interesting part is how we build it.

We'll use AI to help us plan, design, implement and test the application. Along the way we'll happily generate code in technologies, frameworks and libraries that we may not know particularly well. Modern AI tools are surprisingly good at helping us venture outside our comfort zones, and that's exactly what we want to demonstrate.

This is **not** a session about writing enterprise-grade, production-ready software.

There will be shortcuts.

There will be questionable decisions.

There will almost certainly be a moment where the AI confidently does something creative.

And that's perfectly fine.

The goal is experimentation, learning and having a bit of fun.

Because while reading articles, watching videos and attending presentations can be useful, the fastest way to learn is still:

> Build something.

So we'll build something.

We'll start by using a more capable reasoning model to generate a detailed implementation plan. Once we have that plan, we'll switch into execution mode and use cheaper models, local models, GitHub Copilot and agentic tooling to do most of the heavy lifting.

Along the way we'll explore several modern AI engineering patterns:

- Using an expensive model for planning and architecture.
- Using cheaper models for implementation work.
- Generating code from prompts.
- Running a local LLM via Ollama and qwen3:8b.
- Exploring and testing applications using Playwright MCP.
- Turning AI-driven exploratory testing into real automated tests.
- Capturing traces, recordings and evidence of test runs.

By the end of the session we should have:

- A working full-stack application.
- A local AI model making runtime decisions.
- An agent exploring and testing the application.
- Generated Playwright tests.
- Repeatable evidence showing what was tested.

Most importantly, the goal is not for people to walk away thinking:

> Wow, what a great positivity app.

The goal is for people to walk away thinking:

> That looked fun. I'm going to build something myself.

Because the best way to learn AI-assisted software engineering isn't to read about it.

It's to open an editor, start prompting, break a few things, and build something.

---

## Recorded Playwright Tests

Automated browser tests were recorded using the Playwright MCP tooling. These specs live in the frontend project and can be replayed with `npx playwright test`.

### Prerequisites

- Backend running: `cd bepositive && ./mvnw spring-boot:run`
- Frontend running: `cd frontend && npm run dev`
- Ollama running with `qwen3:8b`

### Test Flows

| Flow | File | What it tests |
|------|------|---------------|
| Positive message | [`positive-message.spec.ts`](frontend/.tsupgrader/runtime-validation/playwright-scripts/positive-message.spec.ts) | Submits a kind message, asserts "Accepted!" is displayed |
| Negative message | [`negative-message.spec.ts`](frontend/.tsupgrader/runtime-validation/playwright-scripts/negative-message.spec.ts) | Submits a negative message, asserts rejection with reason and suggested rewrite |

### Test Recordings

**Positive message flow:**

https://github.com/LeonardoBonacci/be-positive/raw/main/docs/videos/positive-message.mp4

**Negative message flow:**

https://github.com/LeonardoBonacci/be-positive/raw/main/docs/videos/negative-message.mp4

### Running

```bash
cd frontend
npx playwright install chromium
npx playwright test .tsupgrader/runtime-validation/playwright-scripts/
```