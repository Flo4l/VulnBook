DROP DATABASE IF EXISTS VulnBook;
CREATE DATABASE VulnBook;
USE VulnBook;

CREATE OR REPLACE TABLE User (
  userid BIGINT NOT NULL AUTO_INCREMENT,
  username TEXT,
  password TEXT,
  email TEXT,
  PRIMARY KEY(userid)
)Engine=InnoDb;

CREATE OR REPLACE TABLE Post (
  postid BIGINT NOT NULL AUTO_INCREMENT,
  `time` TIMESTAMP,
  text TEXT,
  likes BIGINT,
  userid BIGINT,
  PRIMARY KEY (postid),
  FOREIGN KEY (userid) REFERENCES user(userid)
)Engine=InnoDb;

CREATE OR REPLACE TABLE Session (
  sessionid BIGINT NOT NULL AUTO_INCREMENT,
  `key` TEXT,
  expires TIMESTAMP,
  userid BIGINT,
  PRIMARY KEY (sessionid),
  FOREIGN KEY (userid) REFERENCES user(userid)
)Engine=INNODB