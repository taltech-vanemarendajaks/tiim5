import { test, expect } from '@playwright/test';

test('app should load and display title', async ({ page }) => {
  await page.goto('/');
  await expect(page.locator('h1')).toContainText('StudyPlanner');
});
