create table customer (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  email_address             varchar(255),
  constraint pk_customer primary key (id))
;



