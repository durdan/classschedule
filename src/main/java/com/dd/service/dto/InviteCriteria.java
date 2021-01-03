package com.dd.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.dd.domain.Invite} entity. This class is used
 * in {@link com.dd.web.rest.InviteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InviteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter requestedUserId;

    private StringFilter inviteCode;

    private StringFilter invitedUserId;

    private StringFilter createdBy;

    private InstantFilter created;

    private InstantFilter updated;

    private LongFilter invitestatusId;

    public InviteCriteria() {
    }

    public InviteCriteria(InviteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.requestedUserId = other.requestedUserId == null ? null : other.requestedUserId.copy();
        this.inviteCode = other.inviteCode == null ? null : other.inviteCode.copy();
        this.invitedUserId = other.invitedUserId == null ? null : other.invitedUserId.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.updated = other.updated == null ? null : other.updated.copy();
        this.invitestatusId = other.invitestatusId == null ? null : other.invitestatusId.copy();
    }

    @Override
    public InviteCriteria copy() {
        return new InviteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRequestedUserId() {
        return requestedUserId;
    }

    public void setRequestedUserId(StringFilter requestedUserId) {
        this.requestedUserId = requestedUserId;
    }

    public StringFilter getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(StringFilter inviteCode) {
        this.inviteCode = inviteCode;
    }

    public StringFilter getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(StringFilter invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreated() {
        return created;
    }

    public void setCreated(InstantFilter created) {
        this.created = created;
    }

    public InstantFilter getUpdated() {
        return updated;
    }

    public void setUpdated(InstantFilter updated) {
        this.updated = updated;
    }

    public LongFilter getInvitestatusId() {
        return invitestatusId;
    }

    public void setInvitestatusId(LongFilter invitestatusId) {
        this.invitestatusId = invitestatusId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InviteCriteria that = (InviteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(requestedUserId, that.requestedUserId) &&
            Objects.equals(inviteCode, that.inviteCode) &&
            Objects.equals(invitedUserId, that.invitedUserId) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(created, that.created) &&
            Objects.equals(updated, that.updated) &&
            Objects.equals(invitestatusId, that.invitestatusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        requestedUserId,
        inviteCode,
        invitedUserId,
        createdBy,
        created,
        updated,
        invitestatusId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InviteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (requestedUserId != null ? "requestedUserId=" + requestedUserId + ", " : "") +
                (inviteCode != null ? "inviteCode=" + inviteCode + ", " : "") +
                (invitedUserId != null ? "invitedUserId=" + invitedUserId + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (created != null ? "created=" + created + ", " : "") +
                (updated != null ? "updated=" + updated + ", " : "") +
                (invitestatusId != null ? "invitestatusId=" + invitestatusId + ", " : "") +
            "}";
    }

}
