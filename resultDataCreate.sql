BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE RESULT_DATA';
EXCEPTION
  WHEN OTHERS THEN
     NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE "RESULT_DATA_SEQ"';
EXCEPTION
  WHEN OTHERS THEN
     NULL;
END;
/
 CREATE SEQUENCE   "RESULT_DATA_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE
/

CREATE TABLE  "RESULT_DATA"
   (	"ID" NUMBER NOT NULL ENABLE, 
	"RESULT_DATE" DATE NOT NULL ENABLE, 
	"SECONDS" NUMBER NOT NULL ENABLE, 
	"USER_ID" NUMBER NOT NULL ENABLE, 
	 CONSTRAINT "RESULT_PK" PRIMARY KEY ("ID") ENABLE
   )
/


CREATE OR REPLACE TRIGGER  "BI_RESULT_DATA"
BEFORE
insert on "RESULT_DATA"
for each row
 WHEN (NEW.ID is null) begin
select "RESULT_DATA_SEQ".nextval into :NEW.ID from dual;
end;
/
ALTER TRIGGER  "BI_RESULT_DATA" ENABLE
/
