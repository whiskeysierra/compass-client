package org.zalando.compass;

import com.google.common.collect.ImmutableMap;
import org.zalando.riptide.Http;

import java.util.Map;
import java.util.function.Supplier;

final class CompassBuilder implements Compass.DefaultDimensionStage {

    private final Http http;
    private final ImmutableMap<String, Supplier<Object>> dimensions;

    CompassBuilder(final Http http) {
        this(http, ImmutableMap.of());
    }

    private CompassBuilder(final Http http,
            final ImmutableMap<String, Supplier<Object>> dimensions) {
        this.http = http;
        this.dimensions = dimensions;
    }

    @Override
    public Compass.DefaultDimensionStage defaultDimensions(final Map<String, Supplier<Object>> values) {
        return new CompassBuilder(http, ImmutableMap.<String, Supplier<Object>>builder()
            .putAll(dimensions)
            .putAll(values)
            .build());
    }

    @Override
    public CompassClient build() {
        return key -> new DefaultDimensionStage(http, key, dimensions);
    }

}
