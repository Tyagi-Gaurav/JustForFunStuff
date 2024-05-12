# JustForFunStuff

# Local Installation on Minikube
 - `python3 main.py`
 - `docker build -t chonku/jffs-ui:LATEST -t chonku/jffs-ui:{tag}`
 - `docker image push chonku/jffs-ui:{tag}`
 - `minkube start`
 - Update `tag` in deployment.yml
 - `local_install.sh`

# Local testing
 - `minikube tunnel`
 - `minikube ip`
 - Ensure ingress is added `/etc/hosts` for accessing via browser
 - Access the page on http://ui-app.info

# AWS Config
 - Using root user create another `dev.user`
 - Create access key for `dev.user`
 - Copy access key and secured access key to `~/.aws/credentials`

# Full Deploy instructions
 - Create VPC
 - Create Security Group
 - Create Keypair with the name of `TestKeyPair` and download `.pem` into `~/.aws` folder
   - Change permissions on keypair to be 400
 
# Release Instructions
 - Launch New Instance using Free Amazon Linux AMI
   - Instance Type: t2.micro
   - Use keypair created earlier
   - Allow SSH from your IP
   - Allow HTTP from internet
 - Connect to instance
   - ssh -i "~/.aws/TestKeyPair.pem" ec2-user@<public_dns>
   - `sudo yum update -y`
   - `sudo yum install -y docker`
   - `sudo service docker start`
   - `sudo usermod -a -G docker ec2-user`
 - Logout and Login to machine again
   - `docker pull chonku/jffs-ui:`<Tag>
   - `docker run -p 80:3000 -d --name jffs-ui chonku/jffs-ui:`<Tag>
   