USE BuyMe;

delete from User;

insert User (username, password, email, firstname, lastname, address, active, usertype)
VALUES ('admin', 'admin_pwd', 'admin@buyme.com', 'Fnadmin', 'Lnadmin', '123 Main St., Nowhere Town, NJ 12345', true, 1);

insert User (username, password, email, firstname, lastname, address, active, usertype)
VALUES ('rep', 'rep_pwd', 'rep@buyme.com', 'Fnrep', 'Lnrep', '123 Main St., Somewhere Town, NJ 12345', true, 2);

insert User (username, password, email, firstname, lastname, address, active, usertype)
VALUES ('user', 'user_pwd', 'user@buyme.com', 'Fnuser', 'Lnuser', '123 Main St., Nowhere Town, NJ 56789', true, 3);
