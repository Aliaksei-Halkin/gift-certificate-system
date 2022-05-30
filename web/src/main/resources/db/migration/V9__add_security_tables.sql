begin;
drop table if exists roles;
create table roles
(
    role_id   bigint primary key auto_increment,
    role_name varchar(45) not null
);
drop table if exists permissions;
create table permissions
(
    permission_id   bigint primary key auto_increment,
    permission_name varchar(45) not null
);

alter table user
    add column role_id bigint;

alter table user
    add constraint fk_user_roles foreign key (role_id)
        references roles (role_id)
        on delete no action
        on update no action;

create table permissions_has_roles
(
    permission_id bigint,
    role_id       bigint,
    primary key (permission_id,role_id)
);
alter table permissions_has_roles
    add constraint fk_roles_permissions_has_roles foreign key (role_id)
        references roles (role_id)
        on delete no action
        on update no action;

alter table permissions_has_roles
    add constraint fk_permissions_permissions_has_roles foreign key (permission_id)
        references permissions (permission_id)
        on delete no action
        on update no action;
commit;