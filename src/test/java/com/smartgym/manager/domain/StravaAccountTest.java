package com.smartgym.manager.domain;

import static com.smartgym.manager.domain.ActivityTestSamples.*;
import static com.smartgym.manager.domain.MemberTestSamples.*;
import static com.smartgym.manager.domain.StravaAccountTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartgym.manager.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StravaAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StravaAccount.class);
        StravaAccount stravaAccount1 = getStravaAccountSample1();
        StravaAccount stravaAccount2 = new StravaAccount();
        assertThat(stravaAccount1).isNotEqualTo(stravaAccount2);

        stravaAccount2.setId(stravaAccount1.getId());
        assertThat(stravaAccount1).isEqualTo(stravaAccount2);

        stravaAccount2 = getStravaAccountSample2();
        assertThat(stravaAccount1).isNotEqualTo(stravaAccount2);
    }

    @Test
    void activityTest() {
        StravaAccount stravaAccount = getStravaAccountRandomSampleGenerator();
        Activity activityBack = getActivityRandomSampleGenerator();

        stravaAccount.addActivity(activityBack);
        assertThat(stravaAccount.getActivities()).containsOnly(activityBack);
        assertThat(activityBack.getStravaAccount()).isEqualTo(stravaAccount);

        stravaAccount.removeActivity(activityBack);
        assertThat(stravaAccount.getActivities()).doesNotContain(activityBack);
        assertThat(activityBack.getStravaAccount()).isNull();

        stravaAccount.activities(new HashSet<>(Set.of(activityBack)));
        assertThat(stravaAccount.getActivities()).containsOnly(activityBack);
        assertThat(activityBack.getStravaAccount()).isEqualTo(stravaAccount);

        stravaAccount.setActivities(new HashSet<>());
        assertThat(stravaAccount.getActivities()).doesNotContain(activityBack);
        assertThat(activityBack.getStravaAccount()).isNull();
    }

    @Test
    void memberTest() {
        StravaAccount stravaAccount = getStravaAccountRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        stravaAccount.setMember(memberBack);
        assertThat(stravaAccount.getMember()).isEqualTo(memberBack);
        assertThat(memberBack.getStravaAccount()).isEqualTo(stravaAccount);

        stravaAccount.member(null);
        assertThat(stravaAccount.getMember()).isNull();
        assertThat(memberBack.getStravaAccount()).isNull();
    }
}
