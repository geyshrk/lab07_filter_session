package ru.msnigirev.oris.authorisation.session.repository.interfaces;

import java.util.List;
import java.util.Optional;


interface CrudRepository<T, ID> {

    Optional<T> getById(ID id);

    List<T> getAll();

    List<T> getAll(int offset, int size);

    boolean update(T type);

    boolean delete(T type);

}
