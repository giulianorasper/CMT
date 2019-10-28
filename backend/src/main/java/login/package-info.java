/**
 * The package responsible for managing the login. Communicates with the database in order to do so and answers login
 * attempts by replying with response statuses @see LoginResponse
 * Responsibility delegated to Stephan (email: s8starie@stud.uni-saarland.de, gitlab:@s8starie)
 *
 * <h1>1 Login Process Description</h1><br>
 * <h2>1.1 Algorithms</h2><br>
 * The Login Process consists of following steps:<br>
 *     <ul>
 *      <li>  Each attendee tells the organiser that it wishes to attend the conference<br>
 *      <li>  A user name is generated in a simple manner from the attendees data. Example: LastName'.'FirstName<br>
 *      <li>  The organiser generates a random 'password' of reasonable size (ex. 15 characters). This password gets handed out t the attendee at the entrance<br>
 *      <li>  The attendee logs in with the username and the password.<br>
 *          <ul>
 *          <li>  If the password was already used, the server send out a warning notifying the user and the organizer of a possible identity theft. In this case all login data of the account gets blocked<br>
 *          <li>  Else the server sends back a secure token (length at least 512 characters) and invalidates the password.<br>
 *          </ul>
 *      <li>  At each request the username and the token (<b>not</b> the password) gets send with the request. This serves as identification<br>
 *       </ul>
 *<br>
 * Logging in a second device:<br>
 *     <ul>
 *   <li> The main device requests a new password<br>
 *   <li> The server checks validity and creates a new password<br>
 *   <li> The user is prompted to type in the username and the password on the second device. If they are valid the same token as on the main device gets send to the second device.<br>
 *       </ul>
 *<br>
 * Generate random seed:<br>
 *     <ul>
 *   <li> Take the current system time in nanoseconds. Hash it.<br>
 *       </ul>
 *<br>
 *  <h2>1.2 Guarantees</h2><br>
 *  Both password and token stealing are concerns. However, there is a trade-off between usability and security:<br>
 *   <ul>
 *   <li> If on goes extremely restrictive, i.e. blocks an account after a single sending of a wrong password/token then it is easy to perform a DOS attack<br>
 *   <li> On the other hand making the policy to lenient makes it easy to brute force an account.<br>
 *   <li> Recommended values for the password:<br>
 *       <ul>
 *      <li> assume alnum charset and that each login attempt gets stretched out to at least 4 seconds<br>
 *      <li> block the account after 1000 unsuccessful login attempts.<br>
 *      <li> This leads to a probability of cracking the password in the order of 2000/ (62^15) which is approx 10^{-21} %.
 *      <br>
 *      <li> If one tries to DOS the account one also needs two hours before one can successfully block the account<br>
 *         </ul>
 *   <li> Recommended values for the password:<br>
 *       <ul>
 *        <li> assume alnum charset and that each login attempt gets stretched out to at least 0.5 second<br>
 *        <li> block the account after 10^{10} unsuccessful login attempts.<br>
 *        <li> Even with this many attempts finding out the token is very, very unlikely<br>
 *        <li> If one tries to DOS the account one also needs over 9 days in order to block the account<br>
 *            </ul>
 *<br>
 *   <li> Dictionary attacks don't work against our passwords or tokens since they are securely (pseudo) random<br>
 *   <li> If someone gets handed out the wrong password, or gets it through any other way, then the users and organizer will get notified once the correct user tries to log in (since the passwords are one use only they will get an error prompting them to go to the service desk).<br>
 *       </ul>
 *<br>
 *  <h2>1.3 Other Constraints</h2><br>
 *      <ul>
 *    <li> The organizer needs to open the login phase (in order to avoid password DOS before the conference starts)<br>
 *    <li> The login system should not make visible whether or not a user is registered for a conference. Messages like "user Stephan.Ariesanu does not exist" shpuld never reach a client<br>
 *    <li> The attendees personal data (age, address etc.) never gets send on the end devices. This might lead to an design in which the users are prompted to recheck their personal data when receiving the password from the service desk (as only the service desk has access to the data)<br>
 *    <li> Notify users per email about all logins?<br>
 *    <li> The organizer must be able to remove and generate tokens without affecting the other data of the attendee<br>
 *  <li> The organizer must be able to completely remove a users data if the user notifies them that they will not attend<br>
 *  </ul>
 */
package login;