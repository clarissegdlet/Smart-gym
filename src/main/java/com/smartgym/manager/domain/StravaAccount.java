package com.smartgym.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StravaAccount.
 */
@Entity
@Table(name = "strava_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StravaAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "strava_user_id", nullable = false)
    private String stravaUserId;

    @NotNull
    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stravaAccount")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "stravaAccount" }, allowSetters = true)
    private Set<Activity> activities = new HashSet<>();

    @JsonIgnoreProperties(value = { "stravaAccount", "bookings" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "stravaAccount")
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StravaAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStravaUserId() {
        return this.stravaUserId;
    }

    public StravaAccount stravaUserId(String stravaUserId) {
        this.setStravaUserId(stravaUserId);
        return this;
    }

    public void setStravaUserId(String stravaUserId) {
        this.stravaUserId = stravaUserId;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public StravaAccount accessToken(String accessToken) {
        this.setAccessToken(accessToken);
        return this;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Set<Activity> getActivities() {
        return this.activities;
    }

    public void setActivities(Set<Activity> activities) {
        if (this.activities != null) {
            this.activities.forEach(i -> i.setStravaAccount(null));
        }
        if (activities != null) {
            activities.forEach(i -> i.setStravaAccount(this));
        }
        this.activities = activities;
    }

    public StravaAccount activities(Set<Activity> activities) {
        this.setActivities(activities);
        return this;
    }

    public StravaAccount addActivity(Activity activity) {
        this.activities.add(activity);
        activity.setStravaAccount(this);
        return this;
    }

    public StravaAccount removeActivity(Activity activity) {
        this.activities.remove(activity);
        activity.setStravaAccount(null);
        return this;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.setStravaAccount(null);
        }
        if (member != null) {
            member.setStravaAccount(this);
        }
        this.member = member;
    }

    public StravaAccount member(Member member) {
        this.setMember(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StravaAccount)) {
            return false;
        }
        return getId() != null && getId().equals(((StravaAccount) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StravaAccount{" +
            "id=" + getId() +
            ", stravaUserId='" + getStravaUserId() + "'" +
            ", accessToken='" + getAccessToken() + "'" +
            "}";
    }
}
