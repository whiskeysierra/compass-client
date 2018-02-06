package org.zalando.compass;

import com.google.common.base.Suppliers;
import org.zalando.riptide.Http;

import java.util.Map;
import java.util.function.Supplier;

import static java.util.Collections.singletonMap;

public final class Compass {

    public interface HttpStage {
        DefaultDimensionStage http(Http http);
    }

    public interface DefaultDimensionStage {

        default DefaultDimensionStage defaultDimension(final String id, final Object value) {
            return defaultDimension(id, Suppliers.ofInstance(value));
        }

        default DefaultDimensionStage defaultDimension(final String id, final Supplier<Object> value) {
            return defaultDimensions(singletonMap(id, value));
        }

        DefaultDimensionStage defaultDimensions(Map<String, Supplier<Object>> values);

        CompassClient build();

    }

    public static HttpStage builder() {
        return CompassBuilder::new;
    }

}
