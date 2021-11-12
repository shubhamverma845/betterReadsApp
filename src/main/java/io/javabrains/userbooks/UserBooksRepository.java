package io.javabrains.userbooks;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserBooksRepository extends CassandraRepository<UserBooks, UserBooksPrimaryKey> {
}
