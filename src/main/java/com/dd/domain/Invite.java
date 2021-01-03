package com.dd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Invite.
 */
@Entity
@Table(name = "invite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Invite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "requested_user_id")
    private String requestedUserId;

    @Column(name = "invite_code")
    private String inviteCode;

    @Column(name = "invited_user_id")
    private String invitedUserId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created")
    private Instant created;

    @Column(name = "updated")
    private Instant updated;

    @ManyToOne
    @JsonIgnoreProperties(value = "invites", allowSetters = true)
    private InviteStatus invitestatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestedUserId() {
        return requestedUserId;
    }

    public Invite requestedUserId(String requestedUserId) {
        this.requestedUserId = requestedUserId;
        return this;
    }

    public void setRequestedUserId(String requestedUserId) {
        this.requestedUserId = requestedUserId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public Invite inviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
        return this;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getInvitedUserId() {
        return invitedUserId;
    }

    public Invite invitedUserId(String invitedUserId) {
        this.invitedUserId = invitedUserId;
        return this;
    }

    public void setInvitedUserId(String invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Invite createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreated() {
        return created;
    }

    public Invite created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public Invite updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public InviteStatus getInvitestatus() {
        return invitestatus;
    }

    public Invite invitestatus(InviteStatus inviteStatus) {
        this.invitestatus = inviteStatus;
        return this;
    }

    public void setInvitestatus(InviteStatus inviteStatus) {
        this.invitestatus = inviteStatus;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invite)) {
            return false;
        }
        return id != null && id.equals(((Invite) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invite{" +
            "id=" + getId() +
            ", requestedUserId='" + getRequestedUserId() + "'" +
            ", inviteCode='" + getInviteCode() + "'" +
            ", invitedUserId='" + getInvitedUserId() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
