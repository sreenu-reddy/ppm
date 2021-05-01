create table back_log (id bigint not null auto_increment, pt_sequence integer, project_identifier varchar(255), project_id bigint not null, primary key (id)) engine=InnoDB;
create table project (id bigint not null auto_increment, description longtext, end_date datetime, project_identifier varchar(255), project_name varchar(255), start_date datetime, primary key (id)) engine=InnoDB;
create table project_task (id bigint not null auto_increment, acceptance_criteria varchar(255), due_date datetime, priority integer, project_identifier varchar(255), project_sequence varchar(255), status varchar(255), summary varchar(255), back_log_id bigint not null, primary key (id)) engine=InnoDB;
alter table project add constraint UK_nh7jg4qyw1dm5y0bn2vwoq6v2 unique (project_identifier);
alter table back_log add constraint FKjoenuius4i689ynomaaulrjsq foreign key (project_id) references project (id);
alter table project_task add constraint FK589fhqwemuxxmtalbihiknxw1 foreign key (back_log_id) references back_log (id);
