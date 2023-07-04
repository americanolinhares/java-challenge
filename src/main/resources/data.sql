INSERT INTO `roles` (`name`) VALUES ('USER');
INSERT INTO `roles` (`name`) VALUES ('ADMIN');

INSERT INTO `users` (`username`, `password`) VALUES ('fred', '123456');
INSERT INTO `users` (`username`, `password`) VALUES ('gui', '123456');
INSERT INTO `users` (`username`, `password`) VALUES ('breno', '123456');

INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (1, 1); -- user fred has role USER
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (2, 2); -- user gui has role ADMIN
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (3, 1); -- user breno has role USER
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (3, 2); -- user breno has role ADMIN