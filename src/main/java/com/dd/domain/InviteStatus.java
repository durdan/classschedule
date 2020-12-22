package com.dd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A InviteStatus.
 */
@Entity
@Table(name = "invite_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InviteStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "status")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public InviteStatus statusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public InviteStatus status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InviteStatus)) {
            return false;
        }
        return id != null && id.equals(((InviteStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InviteStatus{" +
            "id=" + getId() +
            ", statusCode=" + getStatusCode() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
