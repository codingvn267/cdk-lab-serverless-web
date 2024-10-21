package com.myorg;

import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.rds.*;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import java.util.Arrays;

public class ServerStack extends Stack {

    public ServerStack(final Construct scope, final String id, final Vpc vpc) {
        this(scope, id, vpc, null);
    }

    public ServerStack(final Construct scope, final String id, final Vpc vpc, final StackProps props) {
        super(scope, id, props);

        SecurityGroup webServerSG = SecurityGroup.Builder.create(this, "WebServerSG")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();

        webServerSG.addIngressRule(Peer.anyIpv4(), Port.tcp(80), "Allow HTTP traffic");

        for (int i = 1; i <= 2; i++) {
            Instance.Builder.create(this, "WebServer" + i)
                    .vpc(vpc)
                    .instanceType(software.amazon.awscdk.services.ec2.InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MEDIUM))
                    .machineImage(MachineImage.latestAmazonLinux2()) 
                    .securityGroup(webServerSG)
                    .vpcSubnets(SubnetSelection.builder().subnetType(SubnetType.PUBLIC).build())
                    .build();
        }

        SecurityGroup rdsSG = SecurityGroup.Builder.create(this, "RDSSG")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();

        rdsSG.addIngressRule(webServerSG, Port.tcp(3306), "Allow MySQL traffic from Web Servers");

        DatabaseInstance rdsInstance = DatabaseInstance.Builder.create(this, "RDSInstance")
                .engine(DatabaseInstanceEngine.mysql(MySqlInstanceEngineProps.builder()
                        .version(MysqlEngineVersion.VER_8_0_39) 
                        .build()))
                .vpc(vpc)
                .vpcSubnets(SubnetSelection.builder()
                        .subnetType(SubnetType.PRIVATE_WITH_EGRESS) 
                        .build())
                .credentials(Credentials.fromGeneratedSecret("admin")) 
                .instanceType(software.amazon.awscdk.services.ec2.InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MEDIUM)) 
                .multiAz(true)  
                .securityGroups(Arrays.asList(rdsSG))
                .build();
    }
}