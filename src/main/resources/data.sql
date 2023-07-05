INSERT INTO `roles` (`name`) VALUES ('USER');
INSERT INTO `roles` (`name`) VALUES ('ADMIN');

INSERT INTO `users` (`username`, `password`) VALUES ('fred', '$2a$12$JQDN8Wzy3EEusp4AJyDs3.BOvV3EX3Cg9siE4PkFCtm3apE5iFA5S');
INSERT INTO `users` (`username`, `password`) VALUES ('gui', '$2a$12$JQDN8Wzy3EEusp4AJyDs3.BOvV3EX3Cg9siE4PkFCtm3apE5iFA5S');
INSERT INTO `users` (`username`, `password`) VALUES ('breno', '$2a$12$JQDN8Wzy3EEusp4AJyDs3.BOvV3EX3Cg9siE4PkFCtm3apE5iFA5S');

INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (1, 1); -- user fred has role USER
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (2, 2); -- user gui has role ADMIN
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (3, 1); -- user breno has role USER
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (3, 2); -- user breno has role ADMIN