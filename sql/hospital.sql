DROP DATABASE IF EXISTS hospital;
CREATE DATABASE hospital; 
USE hospital;

CREATE TABLE user (
id INT(11) NOT NULL AUTO_INCREMENT,
firstName VARCHAR(50) NOT NULL,
lastName VARCHAR(50) NOT NULL,
patronymic VARCHAR(50) DEFAULT NULL,
login VARCHAR(50) NOT NULL,
password VARCHAR(50) NOT NULL,
descriptor ENUM('admin','doctor','patient') NOT NULL,
PRIMARY KEY (id),
KEY firstName (firstName),
KEY lastName (lastName),
KEY patronymic (patronymic),
UNIQUE KEY login (login)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE session (
id INT(11) NOT NULL AUTO_INCREMENT,
user_id INT(11) NOT NULL,
token VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY token (token),
FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE speciality (
id INT(11) NOT NULL AUTO_INCREMENT,
title VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY title (title)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE room (
id INT(11) NOT NULL AUTO_INCREMENT,
name VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY name (name)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE admin (
id INT(11) NOT NULL AUTO_INCREMENT,
position VARCHAR(50) NOT NULL,
user_id INT(11) NOT NULL,
PRIMARY KEY (id),
KEY position (position),
FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE doctor (
id INT(11) NOT NULL AUTO_INCREMENT,
speciality_id INT(11) DEFAULT NULL,
room_id INT(11) DEFAULT NULL,
user_id INT(11) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE SET NULL,
FOREIGN KEY (speciality_id) REFERENCES speciality (id) ON DELETE SET NULL,
FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE patient (
id INT(11) NOT NULL AUTO_INCREMENT,
email VARCHAR(50) NOT NULL,
address VARCHAR(50) NOT NULL,
phone VARCHAR(50) NOT NULL,
user_id INT(11) DEFAULT NULL,
PRIMARY KEY (id),
UNIQUE KEY email (email),
KEY address (address),
KEY phone (phone),
FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE date_schedule (
id INT(11) NOT NULL AUTO_INCREMENT,
date DATE NOT NULL,
doctor_id INT(11) NOT NULL,
PRIMARY KEY (id),
KEY date (date),
FOREIGN KEY (doctor_id) REFERENCES doctor (id) ON DELETE CASCADE,
UNIQUE KEY date_doctor_id (date, doctor_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE slot_schedule (
id INT(11) NOT NULL AUTO_INCREMENT, 
slot_start TIME NOT NULL,
slot_end TIME NOT NULL,
date_id INT(11) NOT NULL,
ticket_number VARCHAR(50) DEFAULT NULL,
patient_id INT(11) DEFAULT NULL,
PRIMARY KEY (id),
KEY ticket_number (ticket_number),
FOREIGN KEY (patient_id) REFERENCES patient (id) ON DELETE SET NULL,
FOREIGN KEY (date_id) REFERENCES date_schedule (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE room_occupation (
id INT(11) NOT NULL AUTO_INCREMENT,
date DATE NOT NULL,
time_start TIME NOT NULL,
time_end TIME NOT NULL,
doctor_id INT(11) NOT NULL,
room_id INT(11) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY date_doctor_id (date, doctor_id),
FOREIGN KEY (doctor_id) REFERENCES doctor (id) ON DELETE CASCADE,
FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE commission (
id INT(11) NOT NULL AUTO_INCREMENT,
time_start TIME NOT NULL,
time_end TIME NOT NULL,
room VARCHAR(50) NOT NULL,
ticket_number VARCHAR(50) NOT NULL,
patient_id INT(11) DEFAULT NULL,
PRIMARY KEY (id),
UNIQUE ticket_number (ticket_number),
FOREIGN KEY (patient_id) REFERENCES patient (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8; 

INSERT INTO user VALUES(NULL,"Иван","Иванов","Иванович","admin","qwerty","admin");
INSERT INTO admin VALUES(NULL,"superadmin", (SELECT id FROM user WHERE login = "admin"));
INSERT INTO speciality VALUES(NULL, "therapist");
INSERT INTO speciality VALUES(NULL, "surgeon");
INSERT INTO room VALUES(NULL, "302a");
INSERT INTO room VALUES(NULL, "555a");
INSERT INTO room VALUES(NULL, "111");

/*SELECT * FROM user;
SELECT * FROM session;
SELECT * FROM admin;
SELECT * FROM speciality;
SELECT * FROM room;
SELECT * FROM room_occupation;
SELECT * FROM doctor;
SELECT * FROM patient;
SELECT * FROM date_schedule;
SELECT * FROM slot_schedule;
SELECT * FROM commission;

SELECT timediff(slot_end, slot_start) FROM slot_schedule WHERE date_id IN (SELECT id FROM date_schedule WHERE date >= '2020-05-02' AND date <= '2020-05-03' AND doctor_id = 66);
SELECT (TIME_TO_SEC(time_end) - TIME_TO_SEC(time_start))/60 FROM room_occupation WHERE date >= '2020-05-02' AND date <= '2020-05-03' AND doctor_id = 66;*/