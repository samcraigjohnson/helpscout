# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table customer (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  constraint pk_customer primary key (id))
;

create table customer_email (
  id                        bigint auto_increment not null,
  customer_id               bigint,
  email                     varchar(255),
  constraint pk_customer_email primary key (id))
;

create table customer_job (
  id                        bigint auto_increment not null,
  customer_id               bigint,
  position                  varchar(255),
  company                   varchar(255),
  constraint pk_customer_job primary key (id))
;

create table customer_phone (
  id                        bigint auto_increment not null,
  customer_id               bigint,
  phone_number              varchar(255),
  constraint pk_customer_phone primary key (id))
;

create table customer_username (
  id                        bigint auto_increment not null,
  customer_id               bigint,
  username                  varchar(255),
  platform                  varchar(255),
  constraint pk_customer_username primary key (id))
;

alter table customer_email add constraint fk_customer_email_customer_1 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_customer_email_customer_1 on customer_email (customer_id);
alter table customer_job add constraint fk_customer_job_customer_2 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_customer_job_customer_2 on customer_job (customer_id);
alter table customer_phone add constraint fk_customer_phone_customer_3 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_customer_phone_customer_3 on customer_phone (customer_id);
alter table customer_username add constraint fk_customer_username_customer_4 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_customer_username_customer_4 on customer_username (customer_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table customer;

drop table customer_email;

drop table customer_job;

drop table customer_phone;

drop table customer_username;

SET FOREIGN_KEY_CHECKS=1;

