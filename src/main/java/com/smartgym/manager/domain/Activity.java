package com.smartgym.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Activity.
 */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "distance", nullable = false)
    private Double distance;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "duration", nullable = false)
    private Double duration;

    @NotNull
    @Column(name = "activity_date", nullable = false)
    private Instant activityDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "activities", "member" }, allowSetters = true)
    private StravaAccount stravaAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Activity type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getDistance() {
        return this.distance;
    }

    public Activity distance(Double distance) {
        this.setDistance(distance);
        return this;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDuration() {
        return this.duration;
    }

    public Activity duration(Double duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Instant getActivityDate() {
        return this.activityDate;
    }

    public Activity activityDate(Instant activityDate) {
        this.setActivityDate(activityDate);
        return this;
    }

    public void setActivityDate(Instant activityDate) {
        this.activityDate = activityDate;
    }

    public StravaAccount getStravaAccount() {
        return this.stravaAccount;
    }

    public void setStravaAccount(StravaAccount stravaAccount) {
        this.stravaAccount = stravaAccount;
    }

    public Activity stravaAccount(StravaAccount stravaAccount) {
        this.setStravaAccount(stravaAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activity)) {
            return false;
        }
        return getId() != null && getId().equals(((Activity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activity{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", distance=" + getDistance() +
            ", duration=" + getDuration() +
            ", activityDate='" + getActivityDate() + "'" +
            "}";
    }
}
