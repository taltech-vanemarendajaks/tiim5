-- Active: 1776106048944@@localhost@5432@studyplanner
INSERT INTO curriculums (curriculum_external_id, title, study_level, credits) VALUES
    ('03421357-a6c2-da39-8f11-eb6b24c5d0e8', 'Füüsika', 'MASTER', 120);

INSERT INTO users (name) VALUES ('Jane Doe');

INSERT INTO study_plans (curriculum_id, user_id, start_date) VALUES
    (1, 1, '2025-09-01 00:00:00');

INSERT INTO semesters(finished, semester_type, study_plan_id, year) VALUES
    (false, 'AUTUMN', 1, 2025),
    (false, 'SPRING', 1, 2026),
    (false, 'AUTUMN', 1, 2026),
    (false, 'SPRING', 1, 2027),
    (false, 'AUTUMN', 1, 2027),
    (false, 'SPRING', 1, 2028);

INSERT INTO modules (module_external_id, title, required_credits, optional_credits) VALUES
    ('49fa7f06-6752-6a70-134e-f6e2a869e2f6', 'Füüsika esimene suunamoodul', 24, 0);

INSERT INTO modules (module_external_id, title, required_credits, optional_credits) VALUES
    ('9222e4b7-5bff-7990-fe6e-e80a8e4e39f9', 'Vabaained', 6, 0);

INSERT INTO courses (course_external_id, course_version_external_id, code, title_et, credits, semester_type) VALUES
    ('c5633958-c5ae-f412-cc5f-258bb71b686f', '2507bbd2-0beb-b709-8c5d-b299e2694683', 'LOFY.01.009', 'Mikromaailma füüsika', 6, 'AUTUMN');

INSERT INTO module_courses(module_id, course_id) VALUES (1, 1);

INSERT INTO curriculum_modules (curriculum_id, module_id) VALUES (1,1);

INSERT INTO curriculum_modules (curriculum_id, module_id) VALUES (1,2);

INSERT INTO planned_courses(course_id, module_id, semester_id, status) VALUES
    (1, 2, 1, 'PLANNED')
