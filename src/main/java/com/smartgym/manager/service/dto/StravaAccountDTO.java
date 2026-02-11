package com.smartgym.manager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartgym.manager.domain.StravaAccount} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StravaAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String stravaUserId;

    @NotNull
    private String accessToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStravaUserId() {
        return stravaUserId;
    }

    public void setStravaUserId(String stravaUserId) {
        this.stravaUserId = stravaUserId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StravaAccountDTO)) {
            return false;
        }

        StravaAccountDTO stravaAccountDTO = (StravaAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stravaAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StravaAccountDTO{" +
            "id=" + getId() +
            ", stravaUserId='" + getStravaUserId() + "'" +
            ", accessToken='" + getAccessToken() + "'" +
            "}";
    }
}
