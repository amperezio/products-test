#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { SpringbootFargateCdkStack } from '../lib/springboot-fargate-cdk-stack';

const app = new cdk.App();
new SpringbootFargateCdkStack(app, 'ExampleApp');
