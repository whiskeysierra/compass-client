package org.zalando.compass;

import org.junit.Test;
import org.zalando.riptide.Http;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;

public final class CompassTest {

    @Test
    public void shouldUseBuilder() {
        final CompassClient client = Compass.builder()
                .http(mock(Http.class))
                .defaultDimension("host", "localhost")
                .defaultDimension("before", OffsetDateTime::now)
                .defaultDimension("after", OffsetDateTime::now)
                .build();

        final CompassClient.DimensionStage root = client.search("tax-rate");

        final CompassClient.DimensionStage germany = root
                .withDimension("country", "DE");

        final CompassClient.DimensionStage today = germany
                .withDimension("after", OffsetDateTime::now);

        final CompletableFuture<BigDecimal> taxRate = today.readAs(BigDecimal.class)
                .thenApply(Value::getValue);
    }

}
