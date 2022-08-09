import {App, Fn, Tags} from 'aws-cdk-lib'
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as ecs_patterns from 'aws-cdk-lib/aws-ecs-patterns';
import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';

export class SpringbootFargateCdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);
    
    const vpc = new ec2.Vpc(this, "example-springboot-application-vpc", {
      maxAzs: 2,
      natGateways: 1
    })
    
    const exampleApplicationCluster = new ecs.Cluster(this, "example-springboot-application-cluster", {
      vpc,
      clusterName: "application-cluster"
    })

    const exampleApp = new ecs_patterns.ApplicationLoadBalancedFargateService(this, 'example-springboot-load-balanced-application', {
      cluster: exampleApplicationCluster,
      desiredCount: 2,
      cpu: 256,
      memoryLimitMiB: 512,
      taskImageOptions: {
        image: ecs.ContainerImage.fromAsset('../springboot-application'),
        containerPort: 8081,
      }
    })
    
    exampleApp.targetGroup.configureHealthCheck({
      port: 'traffic-port',
      path: '/actuator/health',
      interval: cdk.Duration.seconds(5),
      timeout: cdk.Duration.seconds(4),
      healthyThresholdCount: 2,
      unhealthyThresholdCount: 2,
      healthyHttpCodes: "200,301,302"
    })
    
    const springbootAutoScaling = exampleApp.service.autoScaleTaskCount({
      maxCapacity: 4,
      minCapacity: 2
    })
    
    springbootAutoScaling.scaleOnCpuUtilization('cpu-autoscaling', {
      targetUtilizationPercent: 45,
      policyName: "cpu-autoscaling-policy",
      scaleInCooldown: cdk.Duration.seconds(30),
      scaleOutCooldown: cdk.Duration.seconds(30)
    })
  }
}
