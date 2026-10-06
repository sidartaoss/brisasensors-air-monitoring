package com.brisasensors.air.monitoring.domain.model;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class DeviceId implements Serializable {

    private static final String VALUE_MUST_NOT_BE_NULL = "value must not be null";

    private TSID value;

    public DeviceId(TSID value) {
        Objects.requireNonNull(value, VALUE_MUST_NOT_BE_NULL);
        this.value = value;
    }

    public DeviceId(Long value) {
        Objects.requireNonNull(value, VALUE_MUST_NOT_BE_NULL);
        this.value = TSID.from(value);
    }

    public DeviceId(String value) {
        Objects.requireNonNull(value, VALUE_MUST_NOT_BE_NULL);
        this.value = TSID.from(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
