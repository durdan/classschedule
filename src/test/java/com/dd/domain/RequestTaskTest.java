package com.dd.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dd.web.rest.TestUtil;

public class RequestTaskTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestTask.class);
        RequestTask requestTask1 = new RequestTask();
        requestTask1.setId(1L);
        RequestTask requestTask2 = new RequestTask();
        requestTask2.setId(requestTask1.getId());
        assertThat(requestTask1).isEqualTo(requestTask2);
        requestTask2.setId(2L);
        assertThat(requestTask1).isNotEqualTo(requestTask2);
        requestTask1.setId(null);
        assertThat(requestTask1).isNotEqualTo(requestTask2);
    }
}
