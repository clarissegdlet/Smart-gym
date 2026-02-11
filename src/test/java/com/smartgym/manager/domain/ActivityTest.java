package com.smartgym.manager.domain;

import static com.smartgym.manager.domain.ActivityTestSamples.*;
import static com.smartgym.manager.domain.StravaAccountTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartgym.manager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activity.class);
        Activity activity1 = getActivitySample1();
        Activity activity2 = new Activity();
        assertThat(activity1).isNotEqualTo(activity2);

        activity2.setId(activity1.getId());
        assertThat(activity1).isEqualTo(activity2);

        activity2 = getActivitySample2();
        assertThat(activity1).isNotEqualTo(activity2);
    }

    @Test
    void stravaAccountTest() {
        Activity activity = getActivityRandomSampleGenerator();
        StravaAccount stravaAccountBack = getStravaAccountRandomSampleGenerator();

        activity.setStravaAccount(stravaAccountBack);
        assertThat(activity.getStravaAccount()).isEqualTo(stravaAccountBack);

        activity.stravaAccount(null);
        assertThat(activity.getStravaAccount()).isNull();
    }
}
