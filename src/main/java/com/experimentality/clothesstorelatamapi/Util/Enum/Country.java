package com.experimentality.clothesstorelatamapi.Util.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Country {
    COLOMBIA,
    CHILE,
    MEXICO,
    PERU;

    @JsonCreator
    Country fromValue(String value) {
        return valueOf((value.toUpperCase()));
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
