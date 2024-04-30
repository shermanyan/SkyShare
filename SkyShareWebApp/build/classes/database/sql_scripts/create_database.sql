CREATE SCHEMA `SkyShare`;

CREATE TABLE SkyShare.`groups`(
    GroupID INT AUTO_INCREMENT PRIMARY KEY,
    DepartureTime TIMESTAMP,
    PickupLocation TEXT
);

CREATE TABLE SkyShare.`users` (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(255),
    Password VARCHAR(255),
    PhoneNumber BIGINT,
    GroupID INT DEFAULT NULL,
    FOREIGN KEY (`GroupID`) REFERENCES SkyShare.`groups`(`GroupID`)
);

CREATE TABLE SkyShare.`group_users` (
    GroupID INT,
    UserID INT,
    PRIMARY KEY (GroupID, UserID),
    FOREIGN KEY (GroupID) REFERENCES SkyShare.`groups`(GroupID),
    FOREIGN KEY (UserID) REFERENCES SkyShare.`users`(UserID)
);