DELETE FROM WebUser;
DELETE FROM WebSession;


DROP TABLE WebUser;
DROP TABLE WebSession;



CREATE TABLE WebUser (
    uuid NVARCHAR2(128) NOT NULL,
    isGoogleAccount NUMBER,
    mail NVARCHAR2(128) NOT NULL,
    password NVARCHAR2(256) NOT NULL,
    type NUMBER NOT NULL,
    CONSTRAINT WebUser_pk PRIMARY KEY(uuid)
);

CREATE TABLE WebSession (
  token NVARCHAR2(64) NOT NULL,
  expiration TIMESTAMP NOT NULL,
  uuid NVARCHAR2(128) NOT NULL
);

SELECT * FROM WebUser;
SELECT * FROM WebSession;