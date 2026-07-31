"""
Playwright end-to-end tests for Be Positive app.

Prerequisites:
  - Ollama running with qwen3:8b model
  - Backend running: cd bepositive && ./mvnw spring-boot:run
  - Frontend running: cd frontend && npm run dev

Install test dependencies:
  pip install pytest playwright
  playwright install chromium
"""

import re

import pytest
from playwright.sync_api import Page, expect


FRONTEND_URL = "http://localhost:5173"


@pytest.fixture(autouse=True)
def navigate_to_app(page: Page):
    """Navigate to the app before each test."""
    page.goto(FRONTEND_URL)
    expect(page.locator("h1")).to_have_text("Be Positive ✨")


def test_positive_message_is_accepted(page: Page):
    """A clearly positive message should be accepted and a success message displayed."""
    page.locator("textarea").fill("I love helping people and spreading kindness!")
    page.locator("button[type='submit']").click()

    # Wait for analysis (LLM call can take a few seconds)
    success = page.locator(".result.success")
    success.wait_for(state="visible", timeout=30000)

    expect(success.locator("h2")).to_have_text("Accepted!")
    expect(success.locator("p")).not_to_be_empty()


def test_negative_message_is_rejected(page: Page):
    """A clearly negative message should be rejected with a reason and suggested rewrite."""
    page.locator("textarea").fill("I hate everything and everyone is terrible")
    page.locator("button[type='submit']").click()

    # Wait for analysis
    rejected = page.locator(".result.rejected")
    rejected.wait_for(state="visible", timeout=30000)

    expect(rejected.locator("h2")).to_have_text("Not Positive Enough")
    expect(rejected.locator("text=Reason:")).to_be_visible()
    expect(rejected.locator("text=Try instead:")).to_be_visible()


def test_empty_message_cannot_be_submitted(page: Page):
    """The submit button should be disabled when textarea is empty."""
    button = page.locator("button[type='submit']")
    expect(button).to_be_disabled()


def test_loading_state_shown_during_analysis(page: Page):
    """While the LLM analyzes, the button should show loading text."""
    page.locator("textarea").fill("This is a wonderful day!")
    page.locator("button[type='submit']").click()

    # Button should briefly show "Analyzing..."
    button = page.locator("button[type='submit']")
    expect(button).to_have_text("Analyzing...")
    expect(button).to_be_disabled()

    # Wait for result to appear (confirming analysis completed)
    page.locator(".result").wait_for(state="visible", timeout=30000)


def test_input_cleared_after_acceptance(page: Page):
    """After a message is accepted, the textarea should be cleared."""
    textarea = page.locator("textarea")
    textarea.fill("You are amazing and the world is better with you in it!")
    page.locator("button[type='submit']").click()

    # Wait for acceptance
    page.locator(".result.success").wait_for(state="visible", timeout=30000)

    expect(textarea).to_have_value("")


def test_input_kept_after_rejection(page: Page):
    """After a message is rejected, the textarea should retain the text."""
    message = "I despise mornings and everything about them"
    textarea = page.locator("textarea")
    textarea.fill(message)
    page.locator("button[type='submit']").click()

    # Wait for rejection
    page.locator(".result.rejected").wait_for(state="visible", timeout=30000)

    expect(textarea).to_have_value(message)
