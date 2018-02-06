package org.zalando.compass;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import org.zalando.riptide.Http;
import org.zalando.riptide.capture.Capture;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.google.common.collect.Maps.transformValues;
import static com.google.common.collect.Multimaps.forMap;
import static org.springframework.http.HttpStatus.Series.SUCCESSFUL;
import static org.zalando.riptide.Bindings.anySeries;
import static org.zalando.riptide.Bindings.on;
import static org.zalando.riptide.Navigators.series;
import static org.zalando.riptide.problem.ProblemRoute.problemHandling;

final class DefaultDimensionStage implements CompassClient.DimensionStage {

    private final Http http;
    private final String key;
    private final ImmutableMap<String, Supplier<Object>> dimensions;

    DefaultDimensionStage(final Http http, final String key, final ImmutableMap<String, Supplier<Object>> dimensions) {
        this.http = http;
        this.key = key;
        this.dimensions = dimensions;
    }

    @Override
    public CompassClient.DimensionStage withDimensions(final Map<String, Supplier<Object>> values) {
        return new DefaultDimensionStage(http, key, ImmutableMap.<String, Supplier<Object>>builder()
                .putAll(dimensions)
                .putAll(values)
                .build());
    }

    @Override
    public <T> CompletableFuture<Value<T>> readAs(final TypeToken<T> type) {
        final Capture<Value<T>> capture = Capture.empty();

        return http.get("/keys/{key}/value", key)
                .queryParams(forMap(transformValues(dimensions, this::stringify)))
                .dispatch(series(),
                        on(SUCCESSFUL).call(valueOf(type), capture),
                        anySeries().call(problemHandling()))
                .thenApply(capture);
    }

    private String stringify(final Supplier<Object> supplier) {
        return supplier.get().toString();
    }

    private <T> TypeToken<Value<T>> valueOf(final TypeToken<T> type) {
        return new TypeToken<Value<T>>() {
            // nothing to implement!
        }.where(new TypeParameter<T>() {
            // nothing to implement!
        }, type);
    }

}
