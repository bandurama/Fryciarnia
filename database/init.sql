DELETE FROM WebUser;


DROP TABLE WebUser;



CREATE TABLE WebUser (
    uuid NVARCHAR2(128) NOT NULL,
    isGoogleAccount NUMBER,
    mail NVARCHAR2(128) NOT NULL,
    password NVARCHAR2(256) NOT NULL,
    type NUMBER NOT NULL
);