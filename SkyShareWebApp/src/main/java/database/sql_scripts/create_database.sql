
CREATE SCHEMA `SkyShare` ;

CREATE TABLE SkyShare.`groups`(
    GroupID INT PRIMARY KEY,
    DepartureTime TIMESTAMP,
    PickupLocation TEXT
);

CREATE TABLE SkyShare.`users` (
    UserID INT PRIMARY KEY,
    FirstName VARCHAR(255),
    LastName VARCHAR(255),
    PhoneNumber BIGINT
);

CREATE TABLE SkyShare.`group_users` (
    GroupID INT,
    UserID INT,
    PRIMARY KEY (GroupID, UserID),
    FOREIGN KEY (GroupID) REFERENCES SkyShare.`groups`(GroupID),
    FOREIGN KEY (UserID) REFERENCES SkyShare.`users`(UserID)
);