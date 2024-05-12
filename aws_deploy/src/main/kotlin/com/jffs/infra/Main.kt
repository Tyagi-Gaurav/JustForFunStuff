package com.jffs.infra

import aws.sdk.kotlin.services.ec2.Ec2Client
import aws.sdk.kotlin.services.ec2.model.*
import aws.sdk.kotlin.services.ssm.SsmClient
import aws.sdk.kotlin.services.ssm.model.GetParametersByPathRequest
import com.jffs.infra.aws.Config.Companion.REGION
import com.jffs.infra.aws.createKeyPairIfNeeded
import com.jffs.infra.aws.createSecurityGroupIfNeeded


suspend fun main() {
    val fileName = "test.pem"
    val keyNameVal = "TestKeyPair"
    val securityGroup = "TestSecurityGroup"
    val myIpAddress = "86.15.0.216"
    val vpcId = getVpcId()

    createKeyPairIfNeeded(fileName, keyNameVal)
    createSecurityGroupIfNeeded(securityGroup, vpcId, myIpAddress)
    val instanceIdSelected = getParaValuesSc()
    val amiValue = instanceIdSelected?.let { describeImageSc(it) }
    print("AMI Value for $instanceIdSelected is $amiValue")
}

suspend fun getParaValuesSc(): String? {
    val parameterRequest = GetParametersByPathRequest {
        path = "/aws/service/ami-amazon-linux-latest"
    }

    SsmClient { region = REGION }.use { ssmClient ->
        val response = ssmClient.getParametersByPath(parameterRequest)
        response.parameters?.forEach { para ->
            println ("Checking for para ${para.name}")
            if (para.name?.let { filterName(it) } == true && !describeImageSc(para.value).isNullOrEmpty()) {
                return para.value
            }
        }
    }
    return ""
}

suspend fun runInstanceSc(instanceTypeVal: String, keyNameVal: String, groupNameVal: String, amiIdVal: String): String {
    val runRequest = RunInstancesRequest {
        instanceType = InstanceType.fromValue(instanceTypeVal)
        keyName = keyNameVal
        securityGroups = listOf(groupNameVal)
        maxCount = 1
        minCount = 1
        imageId = amiIdVal
    }

    Ec2Client { region = REGION }.use { ec2 ->
        val response = ec2.runInstances(runRequest)
        val instanceId = response.instances?.get(0)?.instanceId
        println("Successfully started EC2 Instance $instanceId based on AMI $amiIdVal")
        return instanceId.toString()
    }
}

suspend fun describeImageSc(instanceId: String?): String? {
    println ("Checking AMI for $instanceId")
    if (!instanceId.isNullOrEmpty()) {
        val imagesRequest = DescribeImagesRequest {
            imageIds = listOf(instanceId.toString())
        }

        Ec2Client { region = REGION }.use { ec2 ->
            try {
                val response = ec2.describeImages(imagesRequest)
                println ("AMI found for $instanceId ${response.images}")
                return response.images?.get(0)?.imageId
            } catch (e: Ec2Exception) {
                println ("No AMI found for $instanceId")
                return ""
            }
        }
    } else return ""
}

fun filterName(name: String): Boolean {
    val parts = name.split("/").toTypedArray()
    val myValue = parts[4]
    return myValue.contains("amzn2")
}

suspend fun getVpcId(): String {
    val request = DescribeVpcsRequest {}
    Ec2Client { region = REGION }.use { ec2 ->
        val resp = ec2.describeVpcs(request)
        if (resp.vpcs!!.isNotEmpty()) {
            return resp.vpcs!![0].vpcId!!
        }
    }
    return ""
}

