package com.aws.tests;

import com.amazonaws.AmazonClientException;


import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;



import org.testng.annotations.AfterTest;

public class CreateEc2InstanceWFTests {
  private ProfileCredentialsProvider profileCredentialsProvider;
  private AmazonEC2 awsEc2Client;
  @BeforeTest
  public void beforeTest() 
  {
	  ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
      try {
          credentialsProvider.getCredentials();
      } catch (Exception e) {
          throw new AmazonClientException(
                  "Error in loading credential configuration",
                  e);
      }
  }  
  @Test
  public void CreateEc2InstanceWFTest() 
  {
	  awsEc2Client = AmazonEC2ClientBuilder.standard()
              .withCredentials(profileCredentialsProvider)
              .build();
	  RunInstancesRequest runRequestBuilder = new RunInstancesRequest()
			  .withImageId("ami-41c12e23")
			  .withInstanceType("t2.micro")
			  .withMaxCount(1)
			  .withMinCount(1);
	  
	  String instanceId = runInstance(runRequestBuilder);
	  waitTillInstanceStatusChangesToRunning(instanceId);
	  stopInstance(instanceId);
	  terminateInstance(instanceId);
  }
  
  
  
  
  
  private void stopInstance(String instanceId)
  {

	StopInstancesRequest request = new StopInstancesRequest()
	    .withInstanceIds(instanceId);
	
	awsEc2Client.stopInstances(request);

	        
  }
  
  private void terminateInstance(String instanceId)
  {
	  TerminateInstancesRequest deleteRequest = new TerminateInstancesRequest().withInstanceIds(instanceId);
	  awsEc2Client.terminateInstances(deleteRequest);	  
  }
  private String runInstance(RunInstancesRequest runRequestBuilder)
  {
	  RunInstancesResult runResponse = awsEc2Client.runInstances(runRequestBuilder);
	  return runResponse.getReservation().getReservationId();
  }
  private Integer getInstanceStatus(String instanceId) {
	    DescribeInstancesRequest describeInstanceRequest = new DescribeInstancesRequest().withInstanceIds(instanceId);
	    DescribeInstancesResult describeInstanceResult = awsEc2Client.describeInstances(describeInstanceRequest);
	    com.amazonaws.services.ec2.model.InstanceState state = describeInstanceResult.getReservations().get(0).getInstances().get(0).getState();
	    return state.getCode();
	}
  private void waitTillInstanceStatusChangesToRunning(String instanceId)
  {
	  Integer instanceState = -1;
	  while(instanceState != 16) { //Loop until the instance is in the "running" state.
		    instanceState = getInstanceStatus(instanceId);
		    try {
		        Thread.sleep(5000);
		    } catch(InterruptedException e) {}
		}
  }
  @AfterTest
  public void afterTest() 
  {
  }

}
