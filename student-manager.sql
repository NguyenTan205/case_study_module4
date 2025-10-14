use case_student_data;

INSERT INTO role (name) VALUES ('ROLE_STUDENT'), ('ROLE_ADMIN');

INSERT INTO users (username, password) 
VALUES ('admin', '$2a$10$Dow1rEImZ7pYcghFyOl7O.2JjWekT2QdAIQF9BNTD4aOYHExTD6La');

INSERT INTO user_role (user_id, role_id) VALUES (1, 2);

insert into role(name) values('ROLE_TEACHER');
