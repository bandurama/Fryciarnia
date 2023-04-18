DELETE FROM DbUser;
DELETE FROM DbSession;
DELETE FROM DbHolding;
DELETE FROM DbMeal;
DELETE FROM DbIngridient;
DELETE FROM DbIngridients;
DELETE FROM DbRecipe;
DELETE FROM DbOrders;
DELETE FROM DbOrder;


DROP TABLE DbSession;
DROP TABLE DbIngridients;
DROP TABLE DbRecipe;
DROP TABLE DbOrder;
DROP TABLE DbOrders;
DROP TABLE DbMeal;
DROP TABLE DbIngridient;
DROP TABLE DbHolding;
DROP TABLE DbUser;



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

CREATE TABLE DbHolding (
     uuid NVARCHAR2(128) NOT NULL,
     localization NVARCHAR2(64) NOT NULL,
     manager NVARCHAR2(128) NOT NULL,
     CONSTRAINT DbHolding_pk PRIMARY KEY(uuid),
     CONSTRAINT DbHolding_DbUser_fk FOREIGN KEY (manager) REFERENCES DbUser(uuid)
);

CREATE TABLE DbMeal (
    uuid NVARCHAR2(128) NOT NULL,
    name NVARCHAR2(64) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    CONSTRAINT DbMeal_pk PRIMARY KEY(uuid)
);

CREATE TABLE DbIngridient (
    uuid NVARCHAR2(128) NOT NULL,
    name NVARCHAR2(64) NOT NULL,
    CONSTRAINT DbIngridient_pk PRIMARY KEY(uuid)
);

CREATE TABLE DbIngridients (
    holding NVARCHAR2(128) NOT NULL,
    ingridient NVARCHAR2(128) NOT NULL,
    quantity NUMERIC(10,2),
    CONSTRAINT DbIngridients_DbHolding_fk FOREIGN KEY (holding) REFERENCES DbHolding(uuid),
    CONSTRAINT DbIngridients_DbIngridient_fk FOREIGN KEY (ingridient) REFERENCES DbIngridient(uuid)
);

CREATE TABLE DbRecipe (
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

SELECT * FROM DbUser;
SELECT * FROM DbSession;