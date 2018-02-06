package org.zalando.compass;

import com.google.common.collect.ImmutableMap;

import javax.annotation.concurrent.Immutable;

@Immutable
@lombok.Value
public final class Value<T> {

    ImmutableMap<String, Object> dimensions;
    T value;

}
