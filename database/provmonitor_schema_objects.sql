drop database ProvMonitorTesteDB1;

create database ProvMonitorTesteDB1;

grant all privileges on ProvMonitorTesteDB1.* to provmonitor@localhost identified by 'provmonitor';

--
-- DROP ALL TABLES BEFORE CREATING
--
DROP TABLE IF EXISTS EXECUTION_STATUS;
DROP TABLE IF EXISTS ARTIFACT;
DROP TABLE IF EXISTS ARTIFACT_VALUE;
DROP TABLE IF EXISTS ARTIFACT_VALUE_LOCATION;

CREATE TABLE EXECUTION_STATUS
(
 id_element int unsigned not null,
 type_element int unsigned not null,
 status smallint unsigned not null,
 start_time bigint unsigned,
 end_time bigint unsigned,
 path varchar(255),
 performers varchar(255)
);

CREATE TABLE PROCESS_INSTANCE
(
 instance_id int unsigned not null,
 process_id int unsigned not null,
 name varchar(255),
 swfms_id int unsigned,
 primary key (instance_id),
 foreign key (process_id) references PROCESS (id),
 foreign key (swfms_id) references SWFMS (id)
);


CREATE TABLE ACTIVITY_INSTANCE
(
 instance_id int unsigned not null,
 activity_id int unsigned not null,
 name varchar(255),
 primary key (instance_id),
 foreign key (activity_id) references ACTIVITY (id)
);

CREATE TABLE ARTIFACT
(
 id int unsigned not null,
 primary key (id)
);

CREATE TABLE ARTIFACT_VALUE
(
 artifact int unsigned not null,
 activity_instance int unsigned not null,
 value varchar(255),
 path varchar(255),
 foreign key (artifact) references ARTIFACT (id)
);

--
-- TABLE DEFINITION FOR ARTIFACT_VALUE_LOCATION
--

CREATE TABLE ARTIFACT_VALUE_LOCATION
(
 artifact int unsigned not null,
 host_url varchar(255),
 host_local_path varchar(255),
 path varchar(255),
 foreign key (artifact) references ARTIFACT (id)
);

CREATE TABLE ARTIFACT_PORT_ACTIVITY_INSTANCE
(
 activity_instance int unsigned not null,
 artifact int unsigned not null,
 foreign key (activity_instance) references ACTIVITY_INSTANCE (instance_id),
 foreign key (artifact) references ARTIFACT (id),
);

