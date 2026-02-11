package com.smartgym.manager.service.mapper;

import com.smartgym.manager.domain.Activity;
import com.smartgym.manager.domain.StravaAccount;
import com.smartgym.manager.service.dto.ActivityDTO;
import com.smartgym.manager.service.dto.StravaAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Activity} and its DTO {@link ActivityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActivityMapper extends EntityMapper<ActivityDTO, Activity> {
    @Mapping(target = "stravaAccount", source = "stravaAccount", qualifiedByName = "stravaAccountId")
    ActivityDTO toDto(Activity s);

    @Named("stravaAccountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StravaAccountDTO toDtoStravaAccountId(StravaAccount stravaAccount);
}
