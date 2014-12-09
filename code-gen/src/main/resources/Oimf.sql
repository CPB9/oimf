DROP TABLE IF EXISTS trait_method_argument;
DROP TABLE IF EXISTS trait_method;
DROP TABLE IF EXISTS trait_field;
DROP TABLE IF EXISTS trait_argument;
DROP TABLE IF EXISTS trait_extend;
DROP TABLE IF EXISTS trait_application_argument;
DROP TABLE IF EXISTS trait_application;
DROP TABLE IF EXISTS trait;

CREATE TABLE trait (id INTEGER PRIMARY KEY, guid TEXT NOT NULL UNIQUE);

CREATE TABLE trait_application
    (id INTEGER PRIMARY KEY,
    name TEXT NOT NULL);

CREATE TABLE trait_application_argument
    (trait_application_id INTEGER REFERENCES trait_application(id) ON DELETE CASCADE ON UPDATE CASCADE,
    argument_index INTEGER NOT NULL,
    argument INTEGER REFERENCES trait_application(id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE(trait_application_id, argument_index));

CREATE TABLE trait_extend
    (trait_id INTEGER REFERENCES trait(id) ON DELETE CASCADE ON UPDATE CASCADE,
    extend_index INTEGER NOT NULL,
    trait_application_id INTEGER REFERENCES trait_application(id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE(trait_id, extend_index));

CREATE TABLE trait_argument
    (trait_id INTEGER REFERENCES trait(id) ON DELETE CASCADE ON UPDATE CASCADE,
    argument_index INTEGER NOT NULL,
    name TEXT NOT NULL,
    UNIQUE(trait_id, argument_index),
    UNIQUE(trait_id, name));

CREATE TABLE trait_field
    (trait_id INTEGER REFERENCES trait(id) ON DELETE CASCADE ON UPDATE CASCADE,
    field_index INTEGER NOT NULL,
    type INTEGER REFERENCES trait_application(id) ON DELETE CASCADE ON UPDATE CASCADE,
    name TEXT NOT NULL,
    UNIQUE(trait_id, field_index),
    UNIQUE(trait_id, name));

CREATE TABLE trait_method
    (id INTEGER PRIMARY KEY,
    method_index INTEGER NOT NULL,
    trait_id INTEGER REFERENCES trait(id) ON DELETE CASCADE ON UPDATE CASCADE,
    return_type INTEGER REFERENCES trait_application(id) ON DELETE CASCADE ON UPDATE CASCADE,
    name TEXT NOT NULL,
    UNIQUE(trait_id, method_index),
    UNIQUE(trait_id, name));

CREATE TABLE trait_method_argument
    (trait_method_id INTEGER REFERENCES trait_method(id) ON DELETE CASCADE ON UPDATE CASCADE,
    argument_index INTEGER NOT NULL,
    type INTEGER REFERENCES trait_application(id) ON DELETE CASCADE ON UPDATE CASCADE,
    name TEXT NOT NULL,
    UNIQUE(trait_method_id, argument_index),
    UNIQUE(trait_method_id, name));
