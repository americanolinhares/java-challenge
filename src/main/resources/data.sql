INSERT INTO `roles` (`name`) VALUES ('ROLE_USER');
INSERT INTO `roles` (`name`) VALUES ('ROLE_ADMIN');

INSERT INTO `users` (`username`, `password`) VALUES ('fred', '$2a$10$VcdzH8Q.o4KEo6df.XesdOmXdXQwT5ugNQvu1Pl0390rmfOeA1bhS');
INSERT INTO `users` (`username`, `password`) VALUES ('gui', '$2a$10$VcdzH8Q.o4KEo6df.XesdOmXdXQwT5ugNQvu1Pl0390rmfOeA1bhS');
INSERT INTO `users` (`username`, `password`) VALUES ('breno', '$2a$10$VcdzH8Q.o4KEo6df.XesdOmXdXQwT5ugNQvu1Pl0390rmfOeA1bhS');
INSERT INTO `users` (`username`, `password`) VALUES ('rick', '$2a$10$VcdzH8Q.o4KEo6df.XesdOmXdXQwT5ugNQvu1Pl0390rmfOeA1bhS');
INSERT INTO `users` (`username`, `password`) VALUES ('ze', '$2a$10$VcdzH8Q.o4KEo6df.XesdOmXdXQwT5ugNQvu1Pl0390rmfOeA1bhS');
INSERT INTO `users` (`username`, `password`) VALUES ('cris', '$2a$10$VcdzH8Q.o4KEo6df.XesdOmXdXQwT5ugNQvu1Pl0390rmfOeA1bhS');

INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (1, 1); -- user fred has role USER
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (2, 2); -- user gui has role ADMIN
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (3, 1); -- user breno has role USER
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (3, 2); -- user breno has role ADMIN
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (4, 1); -- user rick has role USER
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (5, 1); -- user ze has role USER
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (6, 1); -- user cris has role USER