import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: './.tsupgrader/runtime-validation/playwright-scripts',
  timeout: 60000,
  use: {
    baseURL: 'http://localhost:5173',
    video: 'on',
    viewport: { width: 1280, height: 720 },
  },
  projects: [
    {
      name: 'chromium',
      use: { browserName: 'chromium' },
    },
  ],
});
