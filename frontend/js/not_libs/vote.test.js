import {sum} from './voteTestFile.js';
// import { readyTesing, checkPacketValidation, displayActiveVote } from './displayActiveVote.js';
// import WS from "jest-websocket-mock";
// import CommunicationManager from '../../communication/CommunicationManager.js';
// import LoginRequestPacket from "../../communication/packets/LoginRequestPacket.js";


jest.dontMock('jquery')

var $ = require('jquery');

test('adds 1 + 2 to equal 3', () => {
  expect(sum(1, 2)).toBe(3);
});

// test('comm test', () => {
//   var pack = new LoginRequestPacket("","");
//   var answer = false;
//   function success(packet) {
//     // return true;
//     console.log("1");
//     expect(true);
//     answer =true;
//   }
//   function fail() {
//     console.log("1");
//       expect(false);
//       answer=true;
//   }
//   CommunicationManager.send(pack, success, fail);
//   console.log("2");
//   // expect(success).toBe(true);
//   await answer;
// });


test('ready function testing', () => {
  
  jest.mock('request-promise-native', () => {


  });

	 
	
    
      
      
  
});

// test('checking packet validation', () => {
//   const checkPacket = checkPacketValidation('Valid');
//   expect(checkPacket).toBe(true);
// });

// test('checking packet Failure', () => {
//   const checkPacket = checkPacketValidation('');
//   expect(checkPacket).toBe(false);
// });

// test('display vote submission message', () => {
//   $('#submitButton').click();
//   expect($('#submit-message').text()).toEqual('Vote Submitted!');

// });

