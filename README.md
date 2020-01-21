# Project 23 - Conference Management Tool

## Project Team (Students):
(1) Muhammad Kamran, s8mukamr@stud.uni-saarland.de

(2) Jessica Werner, jessica.werner@stud.uni-saarland.de

(3) Stephan Ariesanu, s8starie@stud.uni-saarland.de

(4) Giuliano Rasper, s8girasp@stud.uni-saarland.de

(5) Alexander Dincher, s8aldinc@stud.uni-saarland.de

(6) Simon Fedick, s8sifedi@stud.uni-saarland.de

## Tutor:
(1) Shubham Agarwal, shubham.agarwal@cispa.saarland

## Client:
Timo P. Gros, timopgros@cs.uni-saarland.de

## Building the Project

### Building the Backend
Building the project requires gradle. For this please refer to: [https://gradle.org/install/](https://gradle.org/install/)
Inside the 'implementation' folder type in the command `gradle :fatJar`
This will produce a jar under `./build/libs/fullCMS-1.0.jar

### Building the Frontend
The frontend can be found in the 'frontend' folder.  The 'config/config.js' file must be edited so that it contains the correct domain. This is the domain at which the ***jar*** is hosted, which may or may not be the same as the domain which hosts the frontend itself.
Changing the port is only necessary if one changed it inside the backend.
The property `useSSL` must be set to true (`false` is only permitted for debugging purposes)

One can customize the frontend if they wish to do so. For example it is possible to chage the images inside the  'img' folder as long as the new images have the same name. Please note that the images might get scaled or cropped in the process of rendering the page. As such it is recomended to provide images that have the same dimiension as the original images.

## Deploying the Project

Delpoy the frontend like any other webpage.

It is recomended but not necesarry to have the root pointing to 'frontend/index.html'. 
 
Copy the jar created by the `gradle :fatJar` command in an ***empty*** directory.
Add a configuration file to the directory. The file provided in `examples/config.txt` provides a well documented description on what config files should look like. 
The connection must be encrypted using SSL. The SSL certificate must be placed inside the `pem` directory and have the names: `cert.pem` and ``privkey.pem``

Start the backend using the command `java -jar fullCMS-1.0.jar start ./config.txt`. If everything was set up properly the application should print the data and the passwords of all admins in a format similar to:
```
Name : Test
Email : test@test.test 
Residence : test
Group : test
Function : test
Username : Test
password : test
```

Additionally this information and login QR codes are also stored inside the "tmp/conference/qrs" directory. The login codes can be scanned using any QR code scanner.
`Note:` Admins aren't permitted to interfere with other admins, meaning that the config file is the only method to edit an admin. Also, admins can login multiple times using the same password while attendee passwords get invalidated after being used once.

### Additional File Support

The application supports two types of files that are meant to ease conference management:

 - Agenda files: agenda files end with ".txt" and contain the TOPs of the conference. Each line represents a individual TOP and must be prefixed with its order. The example provided looks as follows:

```
1 The first TOP
1.1 The first sub-TOP
2 Second Order
2.1 An other subtopic
2.2 An an other
2.2.1 The last

```

- Attendee files: attendee files end with ".csv" and contain attendee data. They can be used to add large number of attendees. The format is the same as the admin format in the config files. The example provided looks as follows:

```
name:email:group:residence:function
test:test:test:test:test
```


## Usage

### Agenda

For ordinary **attendees**, the agenda can be accessed, but not edited or interacted with in any way.

While the agenda is empty, **admins** can create its first topic using the "Add topic" button on the bottom left.

Admins can furthermore edit the agenda, using the plus icon to **add a topic** on the same level, while the down arrow can be used to **add a subtopic** to that respective topic. The new topic will always get the next ID after the ID of the clicked topic on the level it is appended to. The topics with an equal or higher ID will just move one spot further accordingly.

The pencil/trash icons **edit**/**delete** their respective topic. Note that deleting a topic will also delete its subtopics automatically.

Also, the file icon in the header can be clicked to upload an agenda file as specified in the "Additional File Support" section. Clicking the delete icon in the header will result in deleting the whole agenda.


### Documents

tbc


### Voting

This page is divided into three sections: "Active Voting", "Previous Votings" and "Edit/Create Votings".

In the "Active Voting" section, attendees can submit their vote to the **active voting** by selecting one of the radio buttons on the left and clicking the "Submit Vote" button afterwards. If there is no active voting or if the active voting message expired, an according message will show instead of the voting here.
When there is a new active voting, all attendees will get redirected to this page once and a **timer** will appear representing the remaining time to vote. This timer will show up in the header as well and clicking it will redirect attendees to this page.
Once the timer has run out, the voting counts as expired and no more votes can be submitted for it.

In the "Previous Votings" section, the results of old votings will display. The clickable **show details** text next to the voting questions will make that section expand and display the amount of votes that has been submitted for each option. For named votings, the name of every person that has picked that option will display as well.

In the "Edit/Create Votings" section which is only accessible for admins, new votings can be created, edited, deleted or started. Clicking on an existing voting will make that section expand and show the voting options as well as buttons to interact with that voting.

When **creating** a voting by clicking the "New Voting" button, the user can decide whether it shall be a named vote, what the voting question should be and how long the voting should take by filing in the respective fields in the popup dialog.

Existing votings can be **started** by clicking the "Start" button. There can only be one active voting at the same time and starting a voting will make it the active one, removing it from this section and displaying it in the first section, handling it as described above.

Clicking the "Add" button will **add new voting options** to that voting, making a text field appear to input the name of the voting option. This input field can always be modified and clicking the "Save Changes" button will save these modifications.
Starting a voting will save all changes automatically, as well. Clicking the **Remove** button next to an option will remove that option from the voting. Note that a voting can't be started unless it has two or more options.
Furthermore, if abstentions should be displayed in the results of the "Previous Votings" section, a voting option for that has to be added manually.



### Requests

tbc


### Profile

The "Profile" page displays the data of the attendee which is currently logged in. This data includes the **name**, **username**, **email**, **group**, **residence** and **function** of that attendee.
That data cannot be interacted with and (in case it is necessary) has to be edited by admins in the "User Management" page.


### User Management

The "User Management" page is only accessible by admins.

A list of attendees will display in a table and clicking on any attendee will make their section expand, showing the remaining data that's not displayed in the table itself as well as icons that can be clicked to interact with that attendee. The data displayed here is consistent with the data being shown in the "Profile" section of the respective attendees, giving additional information whether that attendee is currently **present** at the conference or not.

Changing the sorting relation in the dropdown menu at the top will cause the page to refresh and **sort** the attendee list below by that selected category. Sorting by **group** will sort the attendees by their group, in case they have the same group by function and after that by name.
Sorting by **function** will sort by function first, after that by group and after that by name. Sorting by name doesn't provide any secondary sorting relation.



## Contributers:
(1) Muhammad Kamran - Frontend Layout + Frontend Voting Functionality

(2) Jessica Werner - Database + Testing + Backend + Minor Frontend Functionality Implementation

(3) Stephan Ariesanu - Backend + Testing + Minor Frontend Functionality Implementation

(4) Giuliano Rasper - Communication

(5) Alexander Dincher - Database + Backend

(6) Simon Fedick - Backend + Frontend User Management Functionality
