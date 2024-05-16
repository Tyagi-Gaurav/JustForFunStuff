#! /bin/sh

echo "########### connecting to server and run commands in sequence ###########"
ssh -i "~/.aws/TestKeyPair.pem" ec2-user@ec2-3-8-123-225.eu-west-2.compute.amazonaws.com 'sudo /usr/bin/certbot renew --quiet'