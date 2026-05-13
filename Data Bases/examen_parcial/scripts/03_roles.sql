CREATE ROLE administrador;

CREATE ROLE analista;

GRANT ALL PRIVILEGES ON TABLE employees,
salary_historial,
sales TO administrador;

GRANT SELECT ON TABLE employees, salary_historial, sales TO analista;