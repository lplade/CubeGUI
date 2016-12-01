# Final lab #

Need local MySQL server running. Modify username/password below and in DB.java as desired.

    create user 'lade'@'localhost' identified by 'agram';
    create database cubes;
    grant select, insert, create, update, drop on cubes.* to 'lade'@'localhost';

To do:
* enable deletion of records
* buttons should enable/disable depending on if a record is selected or not
* enable clear form button

