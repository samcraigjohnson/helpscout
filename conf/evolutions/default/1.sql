# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table customer (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  email_address             varchar(255),
  constraint pk_customer primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table customer;

SET FOREIGN_KEY_CHECKS=1;

