CREATE USER MGD IDENTIFIED BY mgd DEFAULT TABLESPACE USERS;
GRANT CONNECT, RESOURCE, DBA TO MGD;
GRANT UNLIMITED TABLESPACE TO MGD;
GRANT All PRIVILEGE TO MGD;