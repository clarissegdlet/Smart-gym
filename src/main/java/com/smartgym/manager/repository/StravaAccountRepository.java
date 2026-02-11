package com.smartgym.manager.repository;

import com.smartgym.manager.domain.StravaAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StravaAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StravaAccountRepository extends JpaRepository<StravaAccount, Long> {}
