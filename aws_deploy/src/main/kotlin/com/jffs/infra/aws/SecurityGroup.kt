package com.jffs.infra.aws

import aws.sdk.kotlin.services.ec2.Ec2Client
import aws.sdk.kotlin.services.ec2.model.*

suspend fun createSecurityGroupIfNeeded(securityGroup: String, vpcId: String, myIpAddress: String) {
    println("*. Create a security group.")
    val securityGroupInAws = listSecurityGroupByNames(securityGroup)

    if (securityGroupInAws == "") {
        createEC2SecurityGroupSc(securityGroup, "Test Security Group", vpcId, myIpAddress)
    } else {
        println("Skipping. Security Group with name $securityGroup already exists\n")
    }
}


suspend fun createEC2SecurityGroupSc(groupNameVal: String?, groupDescVal: String?, vpcIdVal: String?, myIpAddress: String?): String? {
    val request = CreateSecurityGroupRequest {
        groupName = groupNameVal
        description = groupDescVal
        vpcId = vpcIdVal
    }

    Ec2Client { region = "us-west-2" }.use { ec2 ->
        val resp = ec2.createSecurityGroup(request)
        val ipRange = IpRange {
            cidrIp = "$myIpAddress/0"
        }

        val ipPerm = IpPermission {
            ipProtocol = "tcp"
            toPort = 80
            fromPort = 80
            ipRanges = listOf(ipRange)
        }

        val ipPerm2 = IpPermission {
            ipProtocol = "tcp"
            toPort = 22
            fromPort = 22
            ipRanges = listOf(ipRange)
        }

        val authRequest = AuthorizeSecurityGroupIngressRequest {
            groupName = groupNameVal
            ipPermissions = listOf(ipPerm, ipPerm2)
        }
        ec2.authorizeSecurityGroupIngress(authRequest)
        println("Successfully added ingress policy to Security Group $groupNameVal")
        return resp.groupId
    }
}

suspend fun listSecurityGroupByNames(groupName: String) : String? {
    val request = DescribeSecurityGroupsRequest {
        groupNames = listOf(groupName)
    }

    Ec2Client { region = "us-west-2" }.use { ec2 ->
        val response = ec2.describeSecurityGroups(request)
        for (group in response.securityGroups!!) {
            if (groupName == group.groupName) {
                return group.groupId
            }
        }
        return ""
    }
}