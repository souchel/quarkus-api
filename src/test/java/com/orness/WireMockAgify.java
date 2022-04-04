package com.orness;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockAgify implements QuarkusTestResourceLifecycleManager {
    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        return Collections.singletonMap("quarkus.rest-client.agify-api.url", wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(wireMockServer, new TestInjector.AnnotatedAndMatchesType(InjectAgifyMock.class, WireMockServer.class));
    }
}
