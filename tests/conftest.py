"""Pytest configuration for Playwright tests."""

import pytest


@pytest.fixture(scope="session")
def browser_context_args():
    """Configure browser context for all tests."""
    return {
        "viewport": {"width": 1280, "height": 720},
    }
