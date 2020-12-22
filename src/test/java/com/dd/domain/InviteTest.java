package com.dd.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dd.web.rest.TestUtil;

public class InviteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invite.class);
        Invite invite1 = new Invite();
        invite1.setId(1L);
        Invite invite2 = new Invite();
        invite2.setId(invite1.getId());
        assertThat(invite1).isEqualTo(invite2);
        invite2.setId(2L);
        assertThat(invite1).isNotEqualTo(invite2);
        invite1.setId(null);
        assertThat(invite1).isNotEqualTo(invite2);
    }
}
