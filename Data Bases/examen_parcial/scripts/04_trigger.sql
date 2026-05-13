create or replace function register_salary_change()
returns trigger as
$$
BEGIN
    
    
    IF TG_OP = 'INSERT' THEN
        insert into salary_historial (id_employee, former_salary, new_salary)
        values (NEW.id, NULL, NEW.salary);
    
    ELSIF TG_OP = 'UPDATE' THEN
        IF OLD.salary IS DISTINCT FROM NEW.salary THEN
            insert into salary_historial (id_employee, former_salary, new_salary)
            values (NEW.id, OLD.salary, NEW.salary);
        END IF;
    END IF;
    
    RETURN NEW;
END;
$$
LANGUAGE plpgsql;

create trigger trigger_salary_historial
after insert or update of salary on employees
for each row
execute function register_salary_change();