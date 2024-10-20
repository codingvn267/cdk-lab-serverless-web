package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Instance;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.MachineImage;
import software.amazon.awscdk.services.ec2.AmazonLinuxGeneration;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine;
import software.amazon.awscdk.services.rds.MySqlEngineVersion;
import software.amazon.awscdk.services.rds.Credentials;
import software.constructs.Construct;

import java.util.Arrays;

public class ServerStack extends Stack {
  public ServerStack(final Construct scope, final String id, final Vpc vpc) {
      this(scope, id, vpc, null);
  }

  public ServerStack(final Construct scope, final String id, final Vpc vpc, final StackProps props) {
      super(scope, id, props);

      SecurityGroup webServerSG = SecurityGroup.Builder.create(this, "WebServerSG")
          .vpc(vpc)
          .allowAllOutBound(true)
          .build();
      
      webServerSG.addIngressRule(Port.tcp(80), "Allow HTTP traffic");

      for (int i = 1; i <= 2; i++) {
          Instance.Builder.create(this, "WebServer" + i)
                          .vpc(vpc)
                          .instanceType(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MEDIUM))  
                          .machineImage(MachineImage.latestAmazonLinux(AmazonLinuxGeneration.AMAZON_LINUX_2023))  
                          .securityGroup(webServerSG)  
                          .build();
      }

    }
}