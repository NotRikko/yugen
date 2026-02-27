package rikko.yugen.integration.base;

import org.testcontainers.DockerClientFactory;

public class TestDocker {
    public static void main(String[] args) {
        System.out.println(DockerClientFactory.instance().client());
    }
}