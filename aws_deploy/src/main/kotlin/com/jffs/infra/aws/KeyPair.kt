package com.jffs.infra.aws

import aws.sdk.kotlin.services.ec2.Ec2Client
import aws.sdk.kotlin.services.ec2.model.CreateKeyPairRequest
import aws.sdk.kotlin.services.ec2.model.DescribeKeyPairsRequest
import com.jffs.infra.aws.Config.Companion.REGION
import java.io.File

suspend fun createKeyPairIfNeeded(fileName: String, keyNameVal: String) {
    println("1. Creating an RSA key pair and save the private key material as a .pem file.")
    if (!doesKeyPairExist(keyNameVal)) {
        createKeyPairSc(keyNameVal, fileName)
    } else {
        println("Skipping. Key pair $keyNameVal already exists")
    }
}


suspend fun createKeyPairSc(keyNameVal: String, fileNameVal: String) {
    val request = CreateKeyPairRequest {
        keyName = keyNameVal
    }

    Ec2Client { region = REGION }.use { ec2 ->
        val response = ec2.createKeyPair(request)
        val content = response.keyMaterial
        if (content != null) {
            File(fileNameVal).writeText(content)
        }
        println("Successfully created key pair named $keyNameVal")
    }
}

suspend fun doesKeyPairExist(keyNameVal: String): Boolean {
    val request = DescribeKeyPairsRequest {
    }

    Ec2Client { region = REGION }.use { ec2 ->
        val response = ec2.describeKeyPairs(request)
        val content = response.keyPairs
        if (content != null) {
            return content.stream()
                .anyMatch { keyNameVal == it.keyName }
        }

        return false
    }
}
