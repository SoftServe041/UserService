create table users (
    id BIGINT not null AUTO_INCREMENT,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    email varchar(120) not null,
    encrypted_password varchar(255) not null,
    address varchar(255) not null,
    PRIMARY KEY (id)
);

create table roles (
    id BIGINT not null AUTO_INCREMENT,
    name varchar(50) not null,
    PRIMARY KEY (id)
);

create table users_roles (
    users_id BIGINT not null,
    roles_id BIGINT not null
);

alter table users_roles
    add foreign key (users_id) references users(id),
    add foreign key (roles_id) references roles(id);

insert into roles values (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');