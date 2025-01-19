insert into user_table(id_user, username, email, firstname, lastname, active, creation_date, last_update_date) values (1, 'user1', 'email1@email.it', 'firstname', 'lastname', true, current_timestamp(), current_timestamp());
insert into user_table(id_user, username, email, firstname, lastname, active, creation_date, last_update_date) values (2, 'user2', 'email2@email.it', 'firstname', 'lastname', true, current_timestamp(), current_timestamp());
alter sequence user_table_seq restart with 3;
commit;

insert into card_table(id_card, id_account, card_number, expire_date, card_type, wrong_attempts, card_pin, locked, active, creation_date, last_update_date) values (1, 2, '1234123412341234', '2025-03-01', 'VISA', 1, '00213', false, true, current_timestamp(), current_timestamp());
insert into card_table(id_card, id_account, card_number, expire_date, card_type, wrong_attempts, card_pin, locked, active, creation_date, last_update_date) values (2, 2, '5678567856785678', '2026-06-01', 'MASTERCARD', 3, '33125', true, true, current_timestamp(), current_timestamp());
insert into card_table(id_card, id_account, card_number, expire_date, card_type, wrong_attempts, card_pin, locked, active, creation_date, last_update_date) values (3, 1, '7890789078907890', '2027-11-01', 'AMERICAN_EXPRESS', 0, '23893', true, false, current_timestamp(), current_timestamp());
alter sequence card_table_seq restart with 4;
commit;

insert into account_table(id_account, amount, id_user, active, creation_date, last_update_date) values(1, 2000, 1, true, current_timestamp(), current_timestamp());
insert into account_table(id_account, amount, id_user, active, creation_date, last_update_date) values(2, 4000, 2, true, current_timestamp(), current_timestamp());
alter sequence account_table_seq restart with 3;
commit;

insert into event_table(id_event, id_user, id_account, event_type, creation_date, last_update_date) values(1, 1, 1, 'ACCOUNT_DEPOSIT', current_timestamp(), current_timestamp());
alter sequence event_table_seq restart with 2;
commit;