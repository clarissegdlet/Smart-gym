package com.smartgym.manager.service.mapper;

import com.smartgym.manager.domain.StravaAccount;
import com.smartgym.manager.service.dto.StravaAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StravaAccount} and its DTO {@link StravaAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface StravaAccountMapper extends EntityMapper<StravaAccountDTO, StravaAccount> {}
