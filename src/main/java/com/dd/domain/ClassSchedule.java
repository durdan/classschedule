package com.dd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ClassSchedule.
 */
@Entity
@Table(name = "class_schedule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created")
    private Instant created;

    @Column(name = "schedule")
    private Instant schedule;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "confirmed_by_student")
    private String confirmedByStudent;

    @Column(name = "confirmed_by_teacher")
    private String confirmedByTeacher;

    @Column(name = "comment")
    private String comment;

    @Column(name = "payment")
    private Boolean payment;

    @Column(name = "connected")
    private Boolean connected;

    @Column(name = "reoccurring")
    private Boolean reoccurring;

    @Column(name = "reoccurring_type")
    private String reoccurringType;

    @ManyToOne
    @JsonIgnoreProperties(value = "classSchedules", allowSetters = true)
    private Student student;

    @ManyToOne
    @JsonIgnoreProperties(value = "classSchedules", allowSetters = true)
    private Teacher teacher;

    @ManyToOne
    @JsonIgnoreProperties(value = "classSchedules", allowSetters = true)
    private Parent parent;

    @ManyToOne
    @JsonIgnoreProperties(value = "classSchedules", allowSetters = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ClassSchedule name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return created;
    }

    public ClassSchedule created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getSchedule() {
        return schedule;
    }

    public ClassSchedule schedule(Instant schedule) {
        this.schedule = schedule;
        return this;
    }

    public void setSchedule(Instant schedule) {
        this.schedule = schedule;
    }

    public Instant getUpdated() {
        return updated;
    }

    public ClassSchedule updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ClassSchedule createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public ClassSchedule updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getConfirmedByStudent() {
        return confirmedByStudent;
    }

    public ClassSchedule confirmedByStudent(String confirmedByStudent) {
        this.confirmedByStudent = confirmedByStudent;
        return this;
    }

    public void setConfirmedByStudent(String confirmedByStudent) {
        this.confirmedByStudent = confirmedByStudent;
    }

    public String getConfirmedByTeacher() {
        return confirmedByTeacher;
    }

    public ClassSchedule confirmedByTeacher(String confirmedByTeacher) {
        this.confirmedByTeacher = confirmedByTeacher;
        return this;
    }

    public void setConfirmedByTeacher(String confirmedByTeacher) {
        this.confirmedByTeacher = confirmedByTeacher;
    }

    public String getComment() {
        return comment;
    }

    public ClassSchedule comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean isPayment() {
        return payment;
    }

    public ClassSchedule payment(Boolean payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(Boolean payment) {
        this.payment = payment;
    }

    public Boolean isConnected() {
        return connected;
    }

    public ClassSchedule connected(Boolean connected) {
        this.connected = connected;
        return this;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public Boolean isReoccurring() {
        return reoccurring;
    }

    public ClassSchedule reoccurring(Boolean reoccurring) {
        this.reoccurring = reoccurring;
        return this;
    }

    public void setReoccurring(Boolean reoccurring) {
        this.reoccurring = reoccurring;
    }

    public String getReoccurringType() {
        return reoccurringType;
    }

    public ClassSchedule reoccurringType(String reoccurringType) {
        this.reoccurringType = reoccurringType;
        return this;
    }

    public void setReoccurringType(String reoccurringType) {
        this.reoccurringType = reoccurringType;
    }

    public Student getStudent() {
        return student;
    }

    public ClassSchedule student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public ClassSchedule teacher(Teacher teacher) {
        this.teacher = teacher;
        return this;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Parent getParent() {
        return parent;
    }

    public ClassSchedule parent(Parent parent) {
        this.parent = parent;
        return this;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Course getCourse() {
        return course;
    }

    public ClassSchedule course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassSchedule)) {
            return false;
        }
        return id != null && id.equals(((ClassSchedule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassSchedule{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", schedule='" + getSchedule() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", confirmedByStudent='" + getConfirmedByStudent() + "'" +
            ", confirmedByTeacher='" + getConfirmedByTeacher() + "'" +
            ", comment='" + getComment() + "'" +
            ", payment='" + isPayment() + "'" +
            ", connected='" + isConnected() + "'" +
            ", reoccurring='" + isReoccurring() + "'" +
            ", reoccurringType='" + getReoccurringType() + "'" +
            "}";
    }
}
