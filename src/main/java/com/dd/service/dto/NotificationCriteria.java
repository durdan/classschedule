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
 * Criteria class for the {@link com.dd.domain.Notification} entity. This class is used
 * in {@link com.dd.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private StringFilter channel;

    private StringFilter recipient;

    private InstantFilter sendDate;

    private LongFilter userId;

    public NotificationCriteria() {
    }

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.channel = other.channel == null ? null : other.channel.copy();
        this.recipient = other.recipient == null ? null : other.recipient.copy();
        this.sendDate = other.sendDate == null ? null : other.sendDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getChannel() {
        return channel;
    }

    public void setChannel(StringFilter channel) {
        this.channel = channel;
    }

    public StringFilter getRecipient() {
        return recipient;
    }

    public void setRecipient(StringFilter recipient) {
        this.recipient = recipient;
    }

    public InstantFilter getSendDate() {
        return sendDate;
    }

    public void setSendDate(InstantFilter sendDate) {
        this.sendDate = sendDate;
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(channel, that.channel) &&
            Objects.equals(recipient, that.recipient) &&
            Objects.equals(sendDate, that.sendDate) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        channel,
        recipient,
        sendDate,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (channel != null ? "channel=" + channel + ", " : "") +
                (recipient != null ? "recipient=" + recipient + ", " : "") +
                (sendDate != null ? "sendDate=" + sendDate + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
