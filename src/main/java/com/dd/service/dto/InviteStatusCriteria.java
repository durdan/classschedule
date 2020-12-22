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

/**
 * Criteria class for the {@link com.dd.domain.InviteStatus} entity. This class is used
 * in {@link com.dd.web.rest.InviteStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invite-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InviteStatusCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter statusCode;

    private StringFilter status;

    public InviteStatusCriteria() {
    }

    public InviteStatusCriteria(InviteStatusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.statusCode = other.statusCode == null ? null : other.statusCode.copy();
        this.status = other.status == null ? null : other.status.copy();
    }

    @Override
    public InviteStatusCriteria copy() {
        return new InviteStatusCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(IntegerFilter statusCode) {
        this.statusCode = statusCode;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InviteStatusCriteria that = (InviteStatusCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(statusCode, that.statusCode) &&
            Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        statusCode,
        status
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InviteStatusCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (statusCode != null ? "statusCode=" + statusCode + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
            "}";
    }

}
