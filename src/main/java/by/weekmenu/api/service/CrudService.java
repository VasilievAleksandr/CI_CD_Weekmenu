package by.weekmenu.api.service;

import java.io.Serializable;
import java.util.List;

public interface CrudService<E,PK extends Serializable> {

    E save(E entityDto);

    E findById(PK id);

    void delete(PK id);

    List<E> findAll();
}
