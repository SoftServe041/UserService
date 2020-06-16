create table billing_details (
    id BIGINT not null AUTO_INCREMENT,
    billing_address varchar(255) not null,
    card_number varchar(30) not null,
    expiration_month varchar(5) not null,
    expiration_year varchar(5) not null,
    name_on_card varchar(50) not null,
    users_id BIGINT,
    primary key (id)
);

alter table billing_details add constraint FK_Users_billing_details foreign key (users_id) references users (id);
alter table users_roles add constraint FK_Roles_users_roles foreign key (roles_id) references roles (id);
alter table users_roles add constraint FK_Users_users_roles foreign key (users_id) references users (id);