package org.zalando.compass;

import com.google.common.base.Suppliers;
import com.google.common.reflect.TypeToken;

import javax.annotation.concurrent.Immutable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static java.util.Collections.singletonMap;

@Immutable
public interface CompassClient {

    @Immutable
    interface DimensionStage {

        default DimensionStage withDimension(final String id, final Object value) {
            return withDimension(id, Suppliers.ofInstance(value));
        }

        default DimensionStage withDimension(final String id, final Supplier<Object> value) {
            return withDimensions(singletonMap(id, value));
        }

        DimensionStage withDimensions(Map<String, Supplier<Object>> values);

        default <T> CompletableFuture<Value<T>> readAs(final Class<T> type) {
            return readAs(TypeToken.of(type));
        }

        <T> CompletableFuture<Value<T>> readAs(TypeToken<T> type);

    }

    DimensionStage search(String key);

}
