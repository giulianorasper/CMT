import {sum} from './voteTestFile.js';

import puppeteer from 'puppeteer';

test('adds 1 + 2 to equal 3', () => {
  expect(sum(1, 2)).toBe(3);
});

test('should', async () => {
  jest.setTimeout(30000);
  const browser = await puppeteer.launch({
    headless: false,
    slowMo: 100,
    args: ['--window-size=1366,768']
  })
  const page = await browser.newPage();
  await page.goto('http://localhost/CMT/', {waitUntil: 'domcontentloaded'});

  await page.click('input#name');
  await page.type('input#name', 'admin');
  await page.click('input#password');
  await page.type('input#password', 'admin');
  await page.click('button#submit-button');
});