package com.smartgym.manager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartgym.manager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StravaAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StravaAccountDTO.class);
        StravaAccountDTO stravaAccountDTO1 = new StravaAccountDTO();
        stravaAccountDTO1.setId(1L);
        StravaAccountDTO stravaAccountDTO2 = new StravaAccountDTO();
        assertThat(stravaAccountDTO1).isNotEqualTo(stravaAccountDTO2);
        stravaAccountDTO2.setId(stravaAccountDTO1.getId());
        assertThat(stravaAccountDTO1).isEqualTo(stravaAccountDTO2);
        stravaAccountDTO2.setId(2L);
        assertThat(stravaAccountDTO1).isNotEqualTo(stravaAccountDTO2);
        stravaAccountDTO1.setId(null);
        assertThat(stravaAccountDTO1).isNotEqualTo(stravaAccountDTO2);
    }
}
