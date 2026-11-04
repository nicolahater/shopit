create schema if not exists catalogue;

create table catalogue.t_product(
    id bigint generated always as identity,
    c_title varchar(50) not null check(length(trim(c_title)) >= 3),
    c_details varchar(1000)
);