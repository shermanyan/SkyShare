
CREATE TABLE ebdb.`groups`(
    GroupID INT AUTO_INCREMENT PRIMARY KEY,
    DepartureTime TIMESTAMP,
    PickupLocation VARCHAR(255)
);

CREATE TABLE ebdb.`users` (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(255),
    Password VARCHAR(255),
    PhoneNumber VARCHAR(255),
    GroupID INT DEFAULT -1
);

