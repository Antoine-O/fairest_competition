package com.qc.competition.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qc.competition.ws.simplestructure.CounterDeserializer;
import com.qc.competition.ws.simplestructure.CounterSerializer;

/**
 * Created by Duncan on 03/12/2015.
 */
@JsonDeserialize(using = CounterDeserializer.class)
@JsonSerialize(using = CounterSerializer.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Counter implements Comparable<Counter> {
    public int value = 0;

    public Counter() {
        this.value = 0;
    }

    public Counter(int value) {
        this.value = value;
    }

    public static Counter valueOf(String textContent) {
//        Counter counter = new Counter();
//        counter.value = Integer.valueOf(textContent);
        return parse(textContent);
    }

    public static Counter parse(String value) {
        Counter counter = new Counter(Integer.valueOf(value));
        return counter;
    }

    public void decrement() {
        value--;
    }

    public boolean isZero() {
        return value == 0;
    }

    public int value() {
        return value;
    }

    synchronized public int increment() {
        value++;
        return value;
    }

    public void reset() {
        reset(0);
    }

    public void reset(Integer value) {
        this.value = value;
    }

    @Override
    public int compareTo(Counter counter) {
        return Integer.valueOf(this.value).compareTo(Integer.valueOf(counter.value));
    }
}
