import { test, expect } from '@playwright/test';

// One example, so you can see that the setup works. Replace it, or delete it.
test('a created team can be fetched again', async ({ request }) => {
  const created = await request.post('/api/teams', {
    data: { name: `demo-${Date.now()}`, members: ['Alice'] },
  });
  const team = await created.json();

  const fetched = await request.get(`/api/teams/${team.id}`);
  expect(fetched.status()).toBe(200);
  expect((await fetched.json()).id).toBe(team.id);
});
