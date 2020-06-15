insert into users(first_name,last_name,email,encrypted_password,address,phone_number)
values('admin','admin','admin@admin.com','$2a$10$r0I1zYOy.j3mMQxrquUbUepzaJYSuqVmbk2YQJzJFbEVIZ902ua.a','admin','admin');

insert into users_roles values((select id from users where email = 'admin@admin.com'),2);
insert into users_roles values((select id from users where email = 'admin@admin.com'),3);