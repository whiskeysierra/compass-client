package org.zalando.compass;

import com.github.restdriver.clientdriver.ClientDriverRule;
import org.junit.Rule;
import org.junit.Test;
import org.zalando.riptide.Http;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.zalando.riptide.HttpBuilder.simpleRequestFactory;

public final class CompassTest {

    @Rule
    public final ClientDriverRule driver = new ClientDriverRule();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final Http http = Http.builder()
            .configure(simpleRequestFactory(executor))
            .baseUrl(driver.getBaseUrl())
            .build();

    @Test
    public void shouldUseBuilder() {
        final CompassClient client = Compass.builder()
                .http(http)
                .defaultDimension("host", "localhost")
                .defaultDimension("before", () -> OffsetDateTime.parse("2018-02-16T11:00:00+01:00"))
                .defaultDimension("after", () -> OffsetDateTime.parse("2018-02-14T11:00:00+01:00"))
                .build();

        driver.addExpectation(onRequestTo("/keys/tax-rate/value")
                .withParam("host", "localhost")
                .withParam("country", "DE")
                .withParam("before", "2018-02-16T11:00+01:00")
                .withParam("after", "2018-02-15T11:00+01:00"),
                giveResponse("{\"value\":0.19}", "application/json"));

        final Value<BigDecimal> taxRate = client.search("tax-rate")
                .withDimension("country", "DE")
                .withDimension("after", () -> OffsetDateTime.parse("2018-02-15T11:00:00+01:00"))
                .readAs(BigDecimal.class)
                .join();

        assertThat(taxRate.getDimensions(), is(emptyMap()));
        assertThat(taxRate.getValue(), comparesEqualTo(new BigDecimal("0.19")));
    }

}
