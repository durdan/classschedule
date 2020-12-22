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
 * Criteria class for the {@link com.dd.domain.UserExtra} entity. This class is used
 * in {@link com.dd.web.rest.UserExtraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-extras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserExtraCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userType;

    private LongFilter userId;

    public UserExtraCriteria() {
    }

    public UserExtraCriteria(UserExtraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userType = other.userType == null ? null : other.userType.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public UserExtraCriteria copy() {
        return new UserExtraCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUserType() {
        return userType;
    }

    public void setUserType(StringFilter userType) {
        this.userType = userType;
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
        final UserExtraCriteria that = (UserExtraCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(userType, that.userType) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        userType,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserExtraCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userType != null ? "userType=" + userType + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
