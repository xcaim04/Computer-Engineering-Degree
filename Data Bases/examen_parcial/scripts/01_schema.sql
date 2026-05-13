create table if not exists employees (
    id serial primary key,
    name varchar(255),
    salary numeric(10, 2),
    department varchar(255)
);

create table if not exists salary_historial (
    id_employee serial primary key,
    former_salary numeric(10, 2),
    new_salary numeric(10, 2),
    date_salary timestamp default current_timestamp
);

create table if not exists sales (
    id_sale serial primary key,
    product varchar(255) not null,
    quantity integer not null,
    price numeric(10, 2) not null
);