package com.dd.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dd.web.rest.TestUtil;

public class InviteStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InviteStatus.class);
        InviteStatus inviteStatus1 = new InviteStatus();
        inviteStatus1.setId(1L);
        InviteStatus inviteStatus2 = new InviteStatus();
        inviteStatus2.setId(inviteStatus1.getId());
        assertThat(inviteStatus1).isEqualTo(inviteStatus2);
        inviteStatus2.setId(2L);
        assertThat(inviteStatus1).isNotEqualTo(inviteStatus2);
        inviteStatus1.setId(null);
        assertThat(inviteStatus1).isNotEqualTo(inviteStatus2);
    }
}
