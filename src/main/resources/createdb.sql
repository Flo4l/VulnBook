DROP DATABASE IF EXISTS VulnBook;
CREATE DATABASE VulnBook;
USE VulnBook;

CREATE OR REPLACE TABLE User (
  userId BIGINT NOT NULL AUTO_INCREMENT,
  username TEXT,
  password TEXT,
  email TEXT,
  PRIMARY KEY(userId)
)Engine=InnoDb;

CREATE OR REPLACE TABLE Post (
  postid BIGINT NOT NULL AUTO_INCREMENT,
  `time` TIMESTAMP,
  text TEXT,
  likes BIGINT,
  userId BIGINT,
  PRIMARY KEY (postid),
  FOREIGN KEY (userId) REFERENCES user(userId)
)Engine=InnoDb;

CREATE OR REPLACE TABLE Session (
  sessionid BIGINT NOT NULL AUTO_INCREMENT,
  `key` TEXT,
  expires TIMESTAMP,
  userId BIGINT,
  PRIMARY KEY (sessionid),
  FOREIGN KEY (userId) REFERENCES user(userId)
)Engine=INNODB;

CREATE OR REPLACE TABLE User_Likes_Post(
  userid BIGINT NOT NULL,
  postid BIGINT NOT NULL,
  FOREIGN KEY (userid) REFERENCES user(userid),
  FOREIGN KEY (postid) REFERENCES post(postid),
  PRIMARY KEY (userid, postid)
)Engine=InnoDb;