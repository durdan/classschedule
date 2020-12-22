package com.dd.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dd.web.rest.TestUtil;

public class ClassScheduleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassSchedule.class);
        ClassSchedule classSchedule1 = new ClassSchedule();
        classSchedule1.setId(1L);
        ClassSchedule classSchedule2 = new ClassSchedule();
        classSchedule2.setId(classSchedule1.getId());
        assertThat(classSchedule1).isEqualTo(classSchedule2);
        classSchedule2.setId(2L);
        assertThat(classSchedule1).isNotEqualTo(classSchedule2);
        classSchedule1.setId(null);
        assertThat(classSchedule1).isNotEqualTo(classSchedule2);
    }
}
