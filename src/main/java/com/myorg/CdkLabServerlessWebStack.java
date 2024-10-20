package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;

import java.util.Arrays;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class CdkLabServerlessWebStack extends Stack {
    private final Vpc vpc; 

    public CdkLabServerlessWebStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CdkLabServerlessWebStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        vpc = Vpc.Builder.create(this, "Vpc")
            .maxAzs(2)  
            .subnetConfiguration(Arrays.asList(
                SubnetConfiguration.builder()
                    .name("PublicSubnet1")
                    .subnetType(SubnetType.PUBLIC)
                    .cidrMask(24)
                    .build(),
                SubnetConfiguration.builder()
                    .name("PrivateSubnet1")
                    .subnetType(SubnetType.PRIVATE_WITH_NAT)
                    .cidrMask(24)
                    .build(),
                SubnetConfiguration.builder()
                    .name("PublicSubnet2")
                    .subnetType(SubnetType.PUBLIC)
                    .cidrMask(24)
                    .build(),
                SubnetConfiguration.builder()
                    .name("PrivateSubnet2")
                    .subnetType(SubnetType.PRIVATE_WITH_NAT)
                    .cidrMask(24)
                    .build()
            ))
            .build();
    }

    public Vpc getVpc() {
        return vpc;
    }
}

    
