import {sum} from './voteTestFile.js';

import puppeteer from 'puppeteer';

test('adds 1 + 2 to equal 3', () => {
  expect(sum(1, 2)).toBe(3);
});

// test('should', async () => {
//   jest.setTimeout(30000);
//   const browser = await puppeteer.launch({
//     headless: false,
//     slowMo: 100,
//     args: ['--window-size=1366,768']
//   })
//   const page = await browser.newPage();
//   await page.goto('http://localhost/CMT/', {waitUntil: 'domcontentloaded'});

//   await page.click('input#name');
//   await page.type('input#name', 'admin');
//   await page.click('input#password');
//   await page.type('input#password', 'admin');
//   await Promise.all([
//     page.waitForNavigation(), // The promise resolves after navigation has finished
//     page.click('button#submit-button'), // Clicking the link will indirectly cause a navigation
//   ]);
//   await page.waitFor(2000);
//   await page.goto('http://localhost/CMT/vote.html', {waitUntil: 'domcontentloaded'});
//   await page.click('button#createVote');
//   await page.waitFor(3000);
//   await page.click('input#VoteText');
//   await page.type('input#VoteText', 'this is question');
//   await page.click('input#duration');
//   await page.type('input#duration', '1');
//   await page.click('button#confirm');
//   await page.waitFor(3000);
//   page.waitForXPath("//td[contains(., 'this is question')]").then(selector => selector.click());
//   await page.waitFor(3000);
//   const [button] = await page.$x("//button[contains(., 'Add')]");
//   if (button) {
//       await button.click();
//   }
//   await page.waitFor(3000);
// });


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
  await Promise.all([
    page.waitForNavigation(), // The promise resolves after navigation has finished
    page.click('button#submit-button'), // Clicking the link will indirectly cause a navigation
  ]);
  await page.waitFor(2000);
  await page.goto('http://localhost/CMT/vote.html', {waitUntil: 'domcontentloaded'});
  // await page.click('button#createVote');
  await page.waitFor(3000);

  page.waitForXPath("//td[contains(., 'this is question')]").then(selector => selector.click());
  await page.waitFor(3000);
  const [button] = await page.$x("//button[contains(., 'Add')]");
  if (button) {
    
    for(var num = 3; num >= 1; num--){
      await button.click();
    }
  }
  
  await page.waitFor(3000);
  // for(var textValue = 3; textValue>=1; textValue--){
  // await page.$$eval('input[type=text]', );
  // }

  await page.$$eval('input[type=text]', el => {
    el.forEach(el1 => el1.value = 'testing')
  });
  await page.waitFor(3000);
  
  const [button1] = await page.$x("//button[contains(., 'Save Changes')]");
  if (button1) {
      await button1.click();
  }

  await page.waitFor(3000);
});