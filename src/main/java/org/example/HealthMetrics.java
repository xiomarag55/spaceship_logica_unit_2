package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HealthMetrics(@JsonProperty("heartRate") int heartRate, @JsonProperty("bloodPressure") int bloodPressure,
                            @JsonProperty("temperature") int temperature,
                            @JsonProperty("oxygenLevel") int oxygenLevel) {
    public HealthMetrics(int heartRate, int bloodPressure, int temperature, int oxygenLevel) {
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
    }

    @Override
    public int heartRate() {
        return heartRate;
    }

    @Override
    public int bloodPressure() {
        return bloodPressure;
    }

    @Override
    public int temperature() {
        return temperature;
    }

    @Override
    public int oxygenLevel() {
        return oxygenLevel;
    }

}
