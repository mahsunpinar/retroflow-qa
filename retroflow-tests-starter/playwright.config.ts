import { defineConfig } from '@playwright/test';
import 'dotenv/config';

// The URL of your group's RetroFlow instance, without a slash at the end.
// Put it in a file called .env (see .env.example), or export RETROFLOW_URL in the terminal.
const baseURL = process.env.RETROFLOW_URL;
if (!baseURL) {
  throw new Error('RETROFLOW_URL is not set. Copy .env.example to .env and fill in your group URL.');
}

export default defineConfig({
  testDir: './tests',
  // API tests only: no browser needed
  use: {
    baseURL,
    extraHTTPHeaders: { 'Content-Type': 'application/json' },
  },
});
