create table if not exists travels_data.dummy_table (
    id SERIAL PRIMARY KEY,
    value VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    isTrue boolean
);