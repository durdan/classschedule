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
 * Criteria class for the {@link com.dd.domain.ClassSchedule} entity. This class is used
 * in {@link com.dd.web.rest.ClassScheduleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-schedules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassScheduleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private InstantFilter created;

    private InstantFilter updated;

    private StringFilter createdBy;

    private StringFilter updatedBy;

    private StringFilter confirmedByStudent;

    private StringFilter confirmedByTeacher;

    private StringFilter comment;

    private BooleanFilter connected;

    private LongFilter studentId;

    private LongFilter teacherId;

    private LongFilter parentId;

    private LongFilter courseId;

    public ClassScheduleCriteria() {
    }

    public ClassScheduleCriteria(ClassScheduleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.updated = other.updated == null ? null : other.updated.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.confirmedByStudent = other.confirmedByStudent == null ? null : other.confirmedByStudent.copy();
        this.confirmedByTeacher = other.confirmedByTeacher == null ? null : other.confirmedByTeacher.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.connected = other.connected == null ? null : other.connected.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.teacherId = other.teacherId == null ? null : other.teacherId.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
    }

    @Override
    public ClassScheduleCriteria copy() {
        return new ClassScheduleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public StringFilter getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(StringFilter updatedBy) {
        this.updatedBy = updatedBy;
    }

    public StringFilter getConfirmedByStudent() {
        return confirmedByStudent;
    }

    public void setConfirmedByStudent(StringFilter confirmedByStudent) {
        this.confirmedByStudent = confirmedByStudent;
    }

    public StringFilter getConfirmedByTeacher() {
        return confirmedByTeacher;
    }

    public void setConfirmedByTeacher(StringFilter confirmedByTeacher) {
        this.confirmedByTeacher = confirmedByTeacher;
    }

    public StringFilter getComment() {
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public BooleanFilter getConnected() {
        return connected;
    }

    public void setConnected(BooleanFilter connected) {
        this.connected = connected;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public LongFilter getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(LongFilter teacherId) {
        this.teacherId = teacherId;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassScheduleCriteria that = (ClassScheduleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(created, that.created) &&
            Objects.equals(updated, that.updated) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(confirmedByStudent, that.confirmedByStudent) &&
            Objects.equals(confirmedByTeacher, that.confirmedByTeacher) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(connected, that.connected) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(teacherId, that.teacherId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        created,
        updated,
        createdBy,
        updatedBy,
        confirmedByStudent,
        confirmedByTeacher,
        comment,
        connected,
        studentId,
        teacherId,
        parentId,
        courseId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassScheduleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (created != null ? "created=" + created + ", " : "") +
                (updated != null ? "updated=" + updated + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
                (confirmedByStudent != null ? "confirmedByStudent=" + confirmedByStudent + ", " : "") +
                (confirmedByTeacher != null ? "confirmedByTeacher=" + confirmedByTeacher + ", " : "") +
                (comment != null ? "comment=" + comment + ", " : "") +
                (connected != null ? "connected=" + connected + ", " : "") +
                (studentId != null ? "studentId=" + studentId + ", " : "") +
                (teacherId != null ? "teacherId=" + teacherId + ", " : "") +
                (parentId != null ? "parentId=" + parentId + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
            "}";
    }

}
