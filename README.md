This is a REST API based on Grails 4.0.2 for managing Jitsi Rooms and SIP mappings developed at the 
Helmholtz-Zentrum Berlin für Materialien und Energie GmbH.

Feel free to share, fork and improve this project.

For general Grails Documentation see: http://docs.grails.org/4.0.2/

---

The main reason I developed this application was for mapping SIP Pins to Jitsi Room Names, so our Users can dial-in via SIP, get asked for a PIN by IVR, dial it and get connected to the corresponding room.
Another reason was to provide some kind of room management, which disables any public user to create rooms.
So when you enable the reservations API in Jicofo, Jitsi asks this API if a room exists first.
Our users need to login to a frontend and create Rooms first, after that anyone can join/open these rooms, no matter if they got an account from our organization or not.
As we got 2 different Frontends there is the possibility to create Rooms directly after logging in, or via another application which creates them via API Key.
(See API Documentation for further information).

Setup:

For development you will need Grails 4.0.2 and a MySQL database.
You can configure your database connection in the file grails-app/conf/development.yml 
The initial Database layout is placed at Extra/Releases/v1.0.0/migrate-1.0.0.sql
After that, you should be able to run the application with the command "grails run-app" from the project root.
An admin account will be created, credentials are admin:admin

Usage:

API Documentation is found at Extra/Docs/OpenAPI/OpenApi.yml

User creates a Room via Frontend -> Gets Jitsi Link and SIP PIN -> Can join
User received a SIP Pin from the meeting host -> Call our SIP Number -> Enter Pin -> Callserver sends request to API -> Join Room

Flaws:

As the application was designed to work in our specific environment, it contains some flaws due to cross-dependencies to other applications.

- The Roomname attribute of the "Room" Class is named "jitsiRoomName", this allows easier parsing at our callserver
- There are some strange Timezone mismatches, but other applications in our environment need it like this
- User Object references are handled by username not by foreign key, so we don't need to save any user information but a pseudonymized string
- Other Poor design choices
