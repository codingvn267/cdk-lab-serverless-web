package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class CdkLabServerlessWebApp {
    public static void main(final String[] args) {
        App app = new App();

        CdkLabServerlessWebStack networkStack = new CdkLabServerlessWebStack(app, "CdkLabServerlessWebStack", 
                StackProps.builder()
                        .env(Environment.builder()
                                .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                                .region(System.getenv("CDK_DEFAULT_REGION"))
                                .build())
                        .build());
        new ServerStack(app, "ServerStack", networkStack.getVpc(),
                StackProps.builder()
                        .env(Environment.builder()
                                .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                                .region(System.getenv("CDK_DEFAULT_REGION"))
                                .build())
                        .build());

        app.synth();
    }
}

