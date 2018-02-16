package org.zalando.compass;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.Map;

@Immutable
@lombok.Value
public final class Value<T> {

    private final Map<String, Object> dimensions;
    private final T value;

    @JsonCreator
    public Value(
            @JsonProperty("dimensions") @Nullable final Map<String, Object> dimensions,
            @JsonProperty("value") final T value) {
        this.dimensions = dimensions == null ? Collections.emptyMap() : dimensions;
        this.value = value;
    }

    public Map<String, Object> getDimensions() {
        return Collections.unmodifiableMap(dimensions);
    }
}
