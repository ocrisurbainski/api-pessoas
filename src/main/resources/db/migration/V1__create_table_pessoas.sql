create table "PESSOAS" (
    "ID" bigint auto_increment,
    "NOME" varchar(200) not null,
    "CPF" varchar(11) not null,
    "EMAIL" varchar(100),
    "SEXO" varchar(1),
    "DATANASC" date not null,
    "NATURALIDADE" varchar(100),
    "NACIONALIDADE" varchar(100)
);

ALTER TABLE "PESSOAS" ADD PRIMARY KEY ("ID");