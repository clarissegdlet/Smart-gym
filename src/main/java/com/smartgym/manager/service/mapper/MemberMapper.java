package com.smartgym.manager.service.mapper;

import com.smartgym.manager.domain.Member;
import com.smartgym.manager.domain.StravaAccount;
import com.smartgym.manager.service.dto.MemberDTO;
import com.smartgym.manager.service.dto.StravaAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Member} and its DTO {@link MemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {
    // ENTITY -> DTO
    @Mapping(target = "stravaAccount", source = "stravaAccount", qualifiedByName = "stravaAccountId")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    MemberDTO toDto(Member s);

    // DTO -> ENTITY  🔥 OBLIGATOIRE
    @Mapping(target = "stravaAccount", source = "stravaAccount")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    Member toEntity(MemberDTO memberDTO);

    @Named("stravaAccountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StravaAccountDTO toDtoStravaAccountId(StravaAccount stravaAccount);
}
