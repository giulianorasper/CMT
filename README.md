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

## Building the project

### Building the backend
Building the project requires gradle. For this please refer to: [https://gradle.org/install/](https://gradle.org/install/)
Inside the 'implementation' folder type in the command `gradle :fatJar`
This will produce a jar under `./build/libs/fullCMS-1.0.jar

### Building the frontend
The frontend can be found in the 'frontend' folder.  The 'config/config.js' file must be changed to point to the correct domain. This is the domain at which the ***jar*** is hosted, which may or may not be the same as the domain which hosts the frontend itself.
Changing the port is only necessary if one changed it inside the backend.
The property `useSSL` must be set to true (`false` is only permitted for debugging purposes)

One can customize the frontend if one wishes. For example it is possible to chage the images inside the  'img' folder as long as the new images have the same name. Please note that the images might get scaled or cropped in the process of rendering the page. As such it is recomended to provide images that have the same dimiension as the original images

## Deploying the project

Delpoy the frontend like any other webpage.

 It is recomended but not necesarry to have the root pointing to 'frontend/index.html'. 
 
Copy the jar created by the `gradle :fatJar` command in an ***empty*** directory.
Add a configuration file to the directory. The file provided in `examples/config.txt` provides a well documented description on how config files should look like. 
The connection must be encrypted using SSL. The SSL certificate must be placed inside the `pem` directory and have the names: `cert.pem` and ``privkey.pem`

Start the backend using the command `java -jar fullCMS-1.0.jar start ./config.txt`. If everything was set up properly the application should print the data and the passwords of all admins in a format simmilar to:
```
Name : Test
Email : test@test.test 
Residence : test
Group : test
Function : test
Username : Test
password : test
```

Additionally this information and qr login codes are also stored inside the "tmp/conference/qrs" directory. The login codes can be scanned using any qr code scanner. Admins don't have the right to interfere with other admins, meaning that the onfig file is the only method to edit the admins. Also, unlike attendees, admins can login multiple ties using the same password.

## Contributers:
(1) Muhammad Kamran - Most of the Frontend

(2) Jessica Werner - Database + Testing + Backend + Minor frontend implementation

(3) Stephan Ariesanu - Backend + Testing + Minor frontend implementation

(4) Giuliano Rasper - Communication 

(5) Alexander Dincher - Database + Backend

(6) Simon Fedick - Backend + User management frontend
