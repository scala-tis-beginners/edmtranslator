CREATE SCHEMA IF NOT EXISTS LABO;
CREATE TABLE LABO.DICTIONARY
(
    COLUMN_JA                      VARCHAR2(200) NOT NULL,
    COLUMN_EN                      VARCHAR2(200)
)
;
ALTER TABLE LABO.DICTIONARY ADD CONSTRAINT PK_DICTIONARY PRIMARY KEY(COLUMN_JA);

COMMENT ON TABLE LABO.DICTIONARY IS '単語辞書';
COMMENT ON COLUMN LABO.DICTIONARY.COLUMN_JA IS '日本語';
COMMENT ON COLUMN LABO.DICTIONARY.COLUMN_EN IS '英語';
