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
 * Criteria class for the {@link com.dd.domain.RequestTask} entity. This class is used
 * in {@link com.dd.web.rest.RequestTaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /request-tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RequestTaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter requestedUserId;

    private StringFilter requestCode;

    private StringFilter requestType;

    private StringFilter requiredActionFromUserId;

    private StringFilter createdBy;

    private InstantFilter created;

    private InstantFilter updated;

    private LongFilter requestStatusId;

    private LongFilter userId;

    public RequestTaskCriteria() {
    }

    public RequestTaskCriteria(RequestTaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.requestedUserId = other.requestedUserId == null ? null : other.requestedUserId.copy();
        this.requestCode = other.requestCode == null ? null : other.requestCode.copy();
        this.requestType = other.requestType == null ? null : other.requestType.copy();
        this.requiredActionFromUserId = other.requiredActionFromUserId == null ? null : other.requiredActionFromUserId.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.updated = other.updated == null ? null : other.updated.copy();
        this.requestStatusId = other.requestStatusId == null ? null : other.requestStatusId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public RequestTaskCriteria copy() {
        return new RequestTaskCriteria(this);
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

    public StringFilter getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(StringFilter requestCode) {
        this.requestCode = requestCode;
    }

    public StringFilter getRequestType() {
        return requestType;
    }

    public void setRequestType(StringFilter requestType) {
        this.requestType = requestType;
    }

    public StringFilter getRequiredActionFromUserId() {
        return requiredActionFromUserId;
    }

    public void setRequiredActionFromUserId(StringFilter requiredActionFromUserId) {
        this.requiredActionFromUserId = requiredActionFromUserId;
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

    public LongFilter getRequestStatusId() {
        return requestStatusId;
    }

    public void setRequestStatusId(LongFilter requestStatusId) {
        this.requestStatusId = requestStatusId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RequestTaskCriteria that = (RequestTaskCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(requestedUserId, that.requestedUserId) &&
            Objects.equals(requestCode, that.requestCode) &&
            Objects.equals(requestType, that.requestType) &&
            Objects.equals(requiredActionFromUserId, that.requiredActionFromUserId) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(created, that.created) &&
            Objects.equals(updated, that.updated) &&
            Objects.equals(requestStatusId, that.requestStatusId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        requestedUserId,
        requestCode,
        requestType,
        requiredActionFromUserId,
        createdBy,
        created,
        updated,
        requestStatusId,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestTaskCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (requestedUserId != null ? "requestedUserId=" + requestedUserId + ", " : "") +
                (requestCode != null ? "requestCode=" + requestCode + ", " : "") +
                (requestType != null ? "requestType=" + requestType + ", " : "") +
                (requiredActionFromUserId != null ? "requiredActionFromUserId=" + requiredActionFromUserId + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (created != null ? "created=" + created + ", " : "") +
                (updated != null ? "updated=" + updated + ", " : "") +
                (requestStatusId != null ? "requestStatusId=" + requestStatusId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
