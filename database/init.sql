DELETE FROM DbSession;
DELETE FROM DbWorker;
DELETE FROM DbStock;
DELETE FROM DbHolding;
DELETE FROM DbRecipe;
DELETE FROM DbIngridient;
DELETE FROM DbMeal;
DELETE FROM DbOrders;
DELETE FROM DbOrder;
DELETE FROM DbUser;


DROP TABLE DbSession;
DROP TABLE DbStock;
DROP TABLE DbRecipe;
DROP TABLE DbOrder;
DROP TABLE DbOrders;
DROP TABLE DbMeal;
DROP TABLE DbIngridient;
DROP TABLE DbWorker;
DROP TABLE DbUser;
DROP TABLE DbHolding;

CREATE TABLE DbHolding (
     uuid NVARCHAR2(128) NOT NULL,
     localization NVARCHAR2(64) NOT NULL,
     manager NVARCHAR2(128) NOT NULL,
     CONSTRAINT DbHolding_pk PRIMARY KEY(uuid)
);

CREATE TABLE DbUser (
    uuid NVARCHAR2(128) NOT NULL,
    isGoogleAccount NUMBER,
    mail NVARCHAR2(128) NOT NULL,
    name NVARCHAR2(128),
    surname NVARCHAR2(128),
    password NVARCHAR2(256) NOT NULL,
    type NUMBER NOT NULL,
    CONSTRAINT DbUser_pk PRIMARY KEY(uuid)
);

CREATE TABLE DbSession (
  token NVARCHAR2(64) NOT NULL,
  expiration TIMESTAMP NOT NULL,
  uuid NVARCHAR2(128) NOT NULL,
  CONSTRAINT DbSession_DbUser_fk FOREIGN KEY (uuid) REFERENCES DbUser(uuid)
);

CREATE TABLE DbMeal (
    uuid NVARCHAR2(128) NOT NULL,
    name NVARCHAR2(64) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    image NVARCHAR2(512),
    icon NVARCHAR2(512),
    isListed NUMBER,
    CONSTRAINT DbMeal_pk PRIMARY KEY(uuid)
);

CREATE TABLE DbIngridient (
    uuid NVARCHAR2(128) NOT NULL,
    name NVARCHAR2(64) NOT NULL,
    icon NVARCHAR2(512),
    CONSTRAINT DbIngridient_pk PRIMARY KEY(uuid)
);

CREATE TABLE DbStock (
    uuid NVARCHAR2(128) NOT NULL,
    holding NVARCHAR2(128) NOT NULL,
    ingridient NVARCHAR2(128) NOT NULL,
    quantity NUMERIC(10,2),
    CONSTRAINT DbStock_DbHolding_fk FOREIGN KEY (holding) REFERENCES DbHolding(uuid),
    CONSTRAINT DbStock_DbIngridient_fk FOREIGN KEY (ingridient) REFERENCES DbIngridient(uuid)
);

CREATE TABLE DbRecipe (
    uuid NVARCHAR2(128) NOT NULL,
    meal NVARCHAR2(128) NOT NULL,
    ingridient NVARCHAR2(128) NOT NULL,
    quantity NUMERIC(10,2),
    instruction NVARCHAR2(64),
    step NUMBER,
    CONSTRAINT DbRecipe_DbIngridient_fk FOREIGN KEY (ingridient) REFERENCES DbIngridient(uuid),
    CONSTRAINT DbRecipe_DbMeal_fk FOREIGN KEY (meal) REFERENCES DbMeal(uuid)
);

CREATE TABLE DbOrders (
    uuid NVARCHAR2(128) NOT NULL,
    ticket NUMBER NOT NULL,
    ctime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    owner NVARCHAR2(128) NOT NULL,
    holding NVARCHAR2(128) NOT NULL,
    isCashed NUMBER,
    isOut NUMBER,
    isTakeout NUMBER,
    isCanceled NUMBER,
    isReady NUMBER,
    CONSTRAINT DbOrders_pk PRIMARY KEY(uuid),
    CONSTRAINT DbOrders_DbHolding_fk FOREIGN KEY (holding) REFERENCES DbHolding(uuid),
    CONSTRAINT DbOrders_DbUser_fk FOREIGN KEY (owner) REFERENCES DbUser(uuid)
);

CREATE TABLE DbOrder (
    origin NVARCHAR2(128) NOT NULL,
    meal NVARCHAR2(128) NOT NULL,
    CONSTRAINT DbOrder_DbOrders_fk FOREIGN KEY (origin) REFERENCES DbOrders(uuid),
    CONSTRAINT DbOrder_DbMeal_fk FOREIGN KEY (meal) REFERENCES DbMeal(uuid)
);

CREATE TABLE DbWorker (
    uuid NVARCHAR2(128) NOT NULL,
    worker NVARCHAR2(128) NOT NULL,
    holding NVARCHAR2(128) NOT NULL,
    salary NUMERIC(10,2),
    isHardware NUMBER,
    CONSTRAINT DbWorker_pk PRIMARY KEY(uuid),
    CONSTRAINT DbWorker_DbUser_fk FOREIGN KEY (worker) REFERENCES DbUser(uuid),
    CONSTRAINT DbWorker_DbHolding_fk FOREIGN KEY (holding) REFERENCES DbHolding(uuid)
);


INSERT INTO DbUser VALUES('adm', 0, 'admin', 'Mateusz', 'Pawełkiewicz', '1234', 0);

/*INSERT INTO DbHolding VALUES ('hol1', 'Kielce, Galeria Echo', 'd3742e85-51a2-4abf-a9f0-7e1742ebf28f');*/

/*INSERT INTO DbMeal VALUES ('pos1', 'Frytki1', 21.99, 'https://cdn.mcdonalds.pl/uploads/20230328133456/351024-mccrispy-na-www-540x450px-72dpi-3-mccrispy.jpg', 1);
INSERT INTO DbMeal VALUES ('pos2', 'Kebab', 11.99, 'https://cdn.mcdonalds.pl/uploads/20230328133456/351024-mccrispy-na-www-540x450px-72dpi-3-mccrispy.jpg', 0);
INSERT INTO DbIngridient VALUES('skl1', 'Ziemniaki');
INSERT INTO DbRecipe VALUES ('prz1', 'pos1', 'skl1', 0.4, 'Pokroić', 1);
INSERT INTO DbRecipe VALUES ('prz2', 'pos1', 'skl1', 0.4, 'Wpierdolić do starego oleju', 2);*/

SELECT * FROM DbUser;
SELECT * FROM DbWorker;
SELECT * FROM DbStock;
SELECT * FROM DbSession;
SELECT * FROM DbHolding;
SELECT * FROM DbIngridient;
SELECT * FROM DbMeal;
SELECT * FROM DbRecipe;