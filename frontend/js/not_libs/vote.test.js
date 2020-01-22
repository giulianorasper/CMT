import {sum} from './voteTestFile.js';

import puppeteer from 'puppeteer';

// import { startVoteResult } from './voting.js';

test('adds 1 + 2 to equal 3', () => {
  expect(sum(1, 2)).toBe(3);
});

describe('Vote Panel', () => {
// test('Vote Created Successfully', async () => {
//   jest.setTimeout(50000);
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
//   // await page.waitFor(2000);
//   await page.goto('http://localhost/CMT/vote.html', {waitUntil: 'domcontentloaded'});
//   await page.click('button#createVote');
//   // await page.waitFor(3000);
//   await page.click('input#VoteText');
//   await page.type('input#VoteText', 'this is question 3');
//   await page.click('input#duration');
//   await page.type('input#duration', '1');
//   // await page.evaluate(() => {
//   //   return ('input[type=radio][value=0]').prop('checked', true);
//   // });
//   await page.$eval('input[type="radio"][value="0"]', radios => {
//     // checkboxes.forEach(chbox => chbox.click())
//     radios.click()
//  });
  

//   await page.click('button#confirm');
//   await page.waitFor(3000);
//   const confirmCreateVote = await page.waitForXPath("//td[contains(., 'this is question 3')]").then(selector => {return "this is question 3"});
//   // const confirmCreateVote = await page.$$eval('table tr td', tds => tds.map((td) => {
//   //   if(td.innerHTML == 'this is question 22'){
//   //     return td.innerHTML;
//   //   }
    
//   // }));

//   // const confirmCreateVote = await page.$eval()
//   // const confirmCreateVote = await page.$$eval('table tr td', tds => tds.map((td) => {
//   //   return td.innerHTML;
//   // }));
//   page.waitForXPath("//td[contains(., 'this is question 3')]").then(selector => selector.click());
//   await page.waitFor(2000);
//   // page.waitForXPath("//td[contains(., 'this is question 2')]").then(selector => selector.click());
//   // const [button] = await page.$x("//button[contains(., 'Add')]");
//   // if (button) {
//   //     await button.click();
//   // }
//   // await page.waitFor(3000);
//   console.log(confirmCreateVote);
//     // const allButtons = await page.$$eval('input[type="button"]', el => {
//     //   el.forEach(el1 => {return el1.innerText})
//     // });
//     var startVote;
//     var addVote;
//     var deleteVote;
//     var saveChanges;
//     const [button] = await page.$x("//button[contains(., 'Start Vote')]");
//     const [button1] = await page.$x("//button[contains(., 'Add')]");
//     const [button2] = await page.$x("//button[contains(., 'Save Changes')]");
//     const [button3] = await page.$x("//button[contains(., 'Delete')]");
    
//   if (button) {
//     return startVote = 'Start Vote';
//    }
//   if (button1) {
//     return addVote = 'Add';
//   }
//   if (button2) {
//     return deleteVote = 'Delete';
//   }
//   if (button3) {
//     return saveChanges = 'Save Changes';
//   }
//     expect(startVote).toBe('Start Vote');
//     expect(addVote).toBe('Add');
//     expect(deleteVote).toBe('Delete');
//     expect(saveChanges).toBe('Save Changes');

//   expect(confirmCreateVote).toBe('this is question 3');
//   // await browser.close();
// });
// -------------------------------------------Start Vote Test----------------------------------------
// --------------------------------------------------------------------------------------------------

// test('Start a vote', async () => {

//   jest.setTimeout(50000);
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
//   // await page.waitFor(2000);
//   await page.goto('http://localhost/CMT/vote.html', {waitUntil: 'domcontentloaded'});

//   page.waitForXPath("//td[contains(., 'this is question')]").then(selector => selector.click());
//   await page.waitFor(2000);

//   const [button] = await page.$x("//button[contains(., 'Add')]");
//   if (button) {
    
//     for(var num = 3; num >= 1; num--){
//       await button.click();
//     }
//   }
     
//     await page.$$eval('input[type=text]', el => {
//       var i = 0;   
//     el.forEach(el1 => {
//       el1.value = 'testing'+i;
//       i++;})
//   });
//   await page.waitFor(3000);
//   const [button1] = await page.$x("//button[contains(., 'Start Vote')]");
//   if (button1) {
//       await button1.click();
//   }
//   await page.waitFor(3000);
//   console.log('Vote Has been started!');
// });

// -------------------------------------------------Delete Vote Test--------------------------
// -------------------------------------------------------------------------------------------

// test("Delete Vote Test", async () => {
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
//   // await page.waitFor(2000);
//   await page.goto('http://localhost/CMT/vote.html', {waitUntil: 'domcontentloaded'});

//   page.waitForXPath("//td[contains(., 'this is question 3')]").then(selector => selector.click());
//   await page.waitFor(2000); 

//   const [button1] = await page.$x("//button[contains(., 'Delete')]");
//   if (button1) {
//       await button1.click();
//   }
//   await page.waitFor(3000);
//   console.log('Vote has been deleted successfully!');
// });

// ----------------------------------Save Changes for option test----------------------------
// ------------------------------------------------------------------------------------------

// test('Save changes for options Test', async () => {

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
//   // await page.waitFor(2000);
//   await page.goto('http://localhost/CMT/vote.html', {waitUntil: 'domcontentloaded'});

//   page.waitForXPath("//td[contains(., 'this is question 3')]").then(selector => selector.click());
//   await page.waitFor(2000); 

//   const [button] = await page.$x("//button[contains(., 'Add')]");
//   if (button) {
    
//     for(var num = 3; num >= 1; num--){
//       await button.click();
//     }
//   }
     
//     await page.$$eval('input[type=text]', el => {
//       var i = 0;   
//     el.forEach(el1 => {
//       el1.value = 'testing'+i;
//       i++;})
//   });
//   await page.waitFor(3000);

//   const [button1] = await page.$x("//button[contains(., 'Save Changes')]");
//   if (button1) {
//       await button1.click();
//   }

//   await page.evaluate(`window.confirm = () => true`);
//   await page.waitFor(3000);
//   console.log('Options saved successfully!');

// });
// --------------------------------------Submit answer for vote qeustion test ------------------------------
// -------------------------------------------------------------------------------------------------

// test('Vote submitted successfully', async () => {
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
//   // await page.waitFor(2000);
//   await page.goto('http://localhost/CMT/vote.html', {waitUntil: 'domcontentloaded'});
//   await page.waitFor(3000);
//   let voteQuestion = await page.$eval('span.d-flex', el => el.innerText );
//   console.log(voteQuestion);
//   expect(voteQuestion).toBe('newt');
//   // page.waitForXPath("//td[contains(., 'this is question 3')]").then(selector => selector.click());
//   await page.waitFor(2000);

//   await page.$eval('input[type="radio"]', radios => {
//                                                                    // checkboxes.forEach(chbox => chbox.click())
//     radios.click()
//  });
//  await page.waitFor(2000);
//   const [button1] = await page.$x("//button[contains(., 'Submit Vote')]");
//   if (button1) {
//       await button1.click();
//   } 
//   await page.waitFor(2000);
//   console.log("Vote submitted successfully");

// });

// ------------------------------------ Display Vote----------------------------------------------
// ----------------------------------------------------------------------------------------------

// test('Display Active Vote', async () => {
//   jest.setTimeout(30000);
//   const browser = await puppeteer.launch({
//     // headless: false,
//     // slowMo: 100,
//     // args: ['--window-size=1366,768']
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
//   // await page.waitFor(2000);
//   await page.goto('http://localhost/CMT/vote.html', {waitUntil: 'domcontentloaded'});
//   await page.waitFor(3000);
//   let voteQuestion = await page.$eval('span.d-flex', el => el.innerText );
//   console.log(voteQuestion);
//   expect(voteQuestion).toBeDefined();

// });

// ----------------------------Remove Particular vote option---------------------
// ------------------------------------------------------------------------------

test('remove vote option', async () => {
  jest.setTimeout(30000);
  const browser = await puppeteer.launch({
    // headless: false,
    // slowMo: 100,
    // args: ['--window-size=1366,768']
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
  // await page.waitFor(2000);
  await page.goto('http://localhost/CMT/vote.html', {waitUntil: 'domcontentloaded'});

  page.waitForXPath("//td[contains(., 'this is question 3')]").then(selector => selector.click());
  await page.waitFor(2000); 
    const [button] = await page.$x("//button[contains(., 'Remove')]");
  if (button) {
      await button.click();
  }

  const [button1] = await page.$x("//button[contains(., 'Save Changes')]");
  if (button1) {
      await button1.click();
  }
  console.log('Removed successfuly');

});

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
//   // await page.click('button#createVote');
//   await page.waitFor(3000);

//   page.waitForXPath("//td[contains(., 'this is question')]").then(selector => selector.click());
//   await page.waitFor(3000);
//   const [button] = await page.$x("//button[contains(., 'Add')]");
//   if (button) {
    
//     for(var num = 3; num >= 1; num--){
//       await button.click();
//     }
//   }
  
//   await page.waitFor(3000);


//   await page.$$eval('input[type=text]', el => {
//     el.forEach(el1 => el1.value = 'testing')
//   });
//   await page.waitFor(3000);
  
//   const [button1] = await page.$x("//button[contains(., 'Save Changes')]");
//   if (button1) {
//       await button1.click();
//   }

//   await page.waitFor(3000);
// });