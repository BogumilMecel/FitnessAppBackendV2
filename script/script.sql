CREATE TABLE IF NOT EXISTS price(
id INT auto_increment NOT NULL,
value DOUBLE,
currency varchar(10),
productId INT,
PRIMARY KEY (id),
foreign key (productId) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS fitness_app.nutritionValues (
	id INT auto_increment NOT NULL,
	calories INT NULL,
	carbohydrates DOUBLE NULL,
	protein DOUBLE NULL,
	fat DOUBLE NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS fitness_app.product(
	id INT auto_increment NOT NULL,
    name VARCHAR(20),
    containerWeight INT,
    position INT,
    unit VARCHAR(10),
    nutritionValuesId INT,
    barcode VARCHAR(20),
    PRIMARY KEY (id),
    foreign key (nutritionValuesId) REFERENCES nutritionValues(id)
);

CREATE TABLE IF NOT EXISTS fitness_app.user (
	id INT auto_increment NOT NULL,
	username VARCHAR(20),
	password VARCHAR(255),
	salt VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS fitness_app.diaryEntry(
	id INT auto_increment NOT NULL,
    productId INT,
    userId INT,
    date VARCHAR(20),
    timestamp LONG,
    weight INT,
    mealName varchar(20),
    PRIMARY KEY (id),
    FOREIGN KEY (productId) REFERENCES product(id),
    FOREIGN KEY (userId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS fitness_app.logTable(
	id INT auto_increment NOT NULL,
    timestamp LONG,
    userId INT,
    streak INT,
    PRIMARY KEY (id),
    FOREIGN KEY (userId) REFERENCES user(id)
);


SELECT * from diaryEntry;

DROP TABLE user;

SHOW DATABASES;
USE fitness_app;

DROP TABLE price;
DROP TABLE nutritionValues;
DROP TABLE product;
DROP TABLE diaryEntry;

INSERT INTO price(id, value, forHowMuch) VALUES (1,10,100);

SELECT * from nutritionValues;

INSERT INTO nutritionValues(calories, carbohydrates, protein, fat) VALUES (285,100,50,10);

INSERT INTO product(name, containerWeight, position, unit, nutritionValuesId, barcode, priceId) VALUES("rice", 100, 1, "g", 1, "1234567890","1");

SELECT * from product AS p INNER JOIN nutritionValues AS n on (n.id=p.nutritionValuesId);

SELECT * from user;

SELECT * from product;
SELECT * FROM price;
SELECT * FROM nutritionValues;
SELECT * FROM logTable;
DELETE FROM logTable
WHERE id = 3;

DROP TABLE weightTable;
DROP TABLE logTable;

SELECT * FROM user;

CREATE TABLE IF NOT EXISTS fitness_app.weightTable(
	id INT auto_increment NOT NULL,
    timestamp LONG,
    value DOUBLE,
    userId INT,
    PRIMARY KEY (id),
    FOREIGN KEY (userId) REFERENCES user(id)
);



