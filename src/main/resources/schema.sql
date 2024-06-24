create extension if not exists pgcrypto;

create table if not exists processing_record (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    uri text not null,
    timestamp timestamp with time zone,
    status_code text not null,
    ip_address text not null,
    country_code text,
    ip_provider text,
    time_lapsed numeric not null
) ;