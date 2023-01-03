package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Operations;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Operations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperationsRepository extends JpaRepository<Operations, Long> {}
