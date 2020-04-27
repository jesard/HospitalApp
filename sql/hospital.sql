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
FOREIGN KEY (doctor_id) REFERENCES doctor (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE slot_schedule (
id INT(11) NOT NULL AUTO_INCREMENT, 
slot_start TIME NOT NULL,
slot_end TIME NOT NULL,
date_id INT(11) NOT NULL,
patient_id INT(11) DEFAULT NULL,
PRIMARY KEY (id),
FOREIGN KEY (patient_id) REFERENCES patient (id) ON DELETE SET NULL,
FOREIGN KEY (date_id) REFERENCES date_schedule (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO user VALUES(NULL,"Иван","Иванов","Иванович","admin","qwerty","admin");
INSERT INTO user VALUES(NULL,"Василий","Васильев","Васильевич","fwefwefw","qwerty","doctor");
INSERT INTO admin VALUES(NULL,"superadmin", (SELECT id FROM user WHERE login = "admin"));

SELECT * FROM user;
SELECT * FROM admin;