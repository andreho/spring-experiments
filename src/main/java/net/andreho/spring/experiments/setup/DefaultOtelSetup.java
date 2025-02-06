package net.andreho.spring.experiments.setup;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.contrib.sampler.RuleBasedRoutingSampler;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.semconv.UrlAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
public class DefaultOtelSetup {

    @Bean
    public AutoConfigurationCustomizerProvider otelCustomizer() {
        return p ->
            p.addSamplerCustomizer(
                (fallback, config) ->
                    RuleBasedRoutingSampler.builder(SpanKind.SERVER, fallback)
                            .drop(UrlAttributes.URL_PATH, "^/actuator")
                            .build());
    }


    @Component
    @RequiredArgsConstructor
    static class OtelAppenderInitializer implements InitializingBean {

        private final OpenTelemetry openTelemetry;

        @Override
        public void afterPropertiesSet() throws Exception {
            OpenTelemetryAppender.install(this.openTelemetry);
        }
    }
}
