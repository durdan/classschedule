package com.dd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A RequestTask.
 */
@Entity
@Table(name = "request_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RequestTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "requested_user_id")
    private String requestedUserId;

    @Column(name = "request_code")
    private String requestCode;

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "required_action_from_user_id")
    private String requiredActionFromUserId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created")
    private Instant created;

    @Column(name = "updated")
    private Instant updated;

    @ManyToOne
    @JsonIgnoreProperties(value = "requestTasks", allowSetters = true)
    private RequestStatus requestStatus;

    @ManyToOne
    @JsonIgnoreProperties(value = "requestTasks", allowSetters = true)
    private User user;

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

    public RequestTask requestedUserId(String requestedUserId) {
        this.requestedUserId = requestedUserId;
        return this;
    }

    public void setRequestedUserId(String requestedUserId) {
        this.requestedUserId = requestedUserId;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public RequestTask requestCode(String requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestType() {
        return requestType;
    }

    public RequestTask requestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequiredActionFromUserId() {
        return requiredActionFromUserId;
    }

    public RequestTask requiredActionFromUserId(String requiredActionFromUserId) {
        this.requiredActionFromUserId = requiredActionFromUserId;
        return this;
    }

    public void setRequiredActionFromUserId(String requiredActionFromUserId) {
        this.requiredActionFromUserId = requiredActionFromUserId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public RequestTask createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreated() {
        return created;
    }

    public RequestTask created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public RequestTask updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public RequestTask requestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public User getUser() {
        return user;
    }

    public RequestTask user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestTask)) {
            return false;
        }
        return id != null && id.equals(((RequestTask) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestTask{" +
            "id=" + getId() +
            ", requestedUserId='" + getRequestedUserId() + "'" +
            ", requestCode='" + getRequestCode() + "'" +
            ", requestType='" + getRequestType() + "'" +
            ", requiredActionFromUserId='" + getRequiredActionFromUserId() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
