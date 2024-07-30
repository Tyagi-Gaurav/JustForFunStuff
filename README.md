# JustForFunStuff

# Local Development
 - BackEnd
   - `mvn clean package`
   - Run `jffs-acceptance-tests/src/test/kotlin/com/jffs/tests/InMemoryBackendRunner.kt`
 - FrontEnd
   - `npm start`

# Run Github pipeline Locally
 - `brew install act`
 - Execute `act` in project home directory

# Connecting to local database
 - `docker-compose up -d --build`
 - `mongosh --authenticationDatabase admin --username root` 
 - `use testDB`

# Useful database commands
 - Show Collections: `show collections;`
 - Find all in collection: `db.word.find();`

# Local testing with DockerCompose
 - `docker-compose up -d --build`
 - `./mvnw test -DskipTests=false -pl jffs-end-to-end-tests`

# AWS Config
 - Using root user create another `dev.user`
 - Create access key for `dev.user`
 - Copy access key and secured access key to `~/.aws/credentials`

# Full Deploy instructions
 - Create VPC
 - Create Security Group
 - Create Keypair with the name of `TestKeyPair` and download `.pem` into `~/.aws` folder
   - Change permissions on keypair to be 400

# Setup Instructions
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

   - Access the website using public DNS of ec2
   - Update A record for the domain on domain provider

# SSL Setup Instructions
 - `sudo yum update -y`
 - `sudo yum install -y certbot python3-certbot-nginx nginx cronie`
 - Start nginx service
   - `sudo systemctl start nginx`
 - Get certs from Lets encrypt
   - `sudo certbot --nginx -d www.justforfunstuff.com -d justforfunstuff.com`
 - `sudo vi /etc/nginx/conf.d/www.justforfunstuff.com.conf`
   - Copy contents of file `aws_deploy/nginx.conf` into the above file
 - Restart nginx service
   - `sudo systemctl reload nginx`
 - Start crond & enable it on start
   - `sudo systemctl start crond.service`
   - `sudo systemctl enable crond.service`
   - `crontab -e`
   - Set crontab to 
     - `0 12 * * * /usr/bin/certbot renew --quiet`
 - Setup auto renewal of certs (Renew should happen every 90 days)
   - `sudo /usr/bin/certbot renew --quiet`

# Prepare to release
- Create release manually from github home page
- Run release workflow

# Release instructions
  - Ensure Prepare to release instructions are complete
  - SSH into the machine
    - `docker pull chonku/jffs-ui:v1.14`
    - `docker pull chonku/jffs-backend:v1.14`
    - `docker stop jffs-backend && docker rm jffs-backend`
    - `docker run -p 8080:8080 -p 8081:8081 --log-driver=awslogs \
      --log-opt awslogs-region=eu-west-2 \
      --log-opt awslogs-group=JffsLogGroup \
      --log-opt awslogs-create-group=true \
      -d -e "DB_USER=<>" -e "DB_PWD=<>" -e "DB_NAME=Prod" -e "DB_HOST=<>" -e "DB_SCHEME=mongodb+srv" --name jffs-backend chonku/jffs-backend:v1.14`
    - Run Healthcheck for application using the command
      - `wget -O - http://localhost:8081/actuator/health` 
    - `docker stop jffs-ui && docker rm jffs-ui`
    - `docker run -p 3000:3000 -d --name jffs-ui chonku/jffs-ui:v1.14`    

# Configure cloudwatch to collect prometheus metric
  - Create or assign permissions to a role with following policies. This role should be assigned to EC2 instance.
    - AmazonSSMManagedInstanceCore policy.
    - CloudWatchAgentServerPolicy
  - Also ensure that we have the following permissions
    - cloudwatch:PutMetricData 
    - ec2:DescribeVolumes 
    - ec2:DescribeTags 
    - logs:PutLogEvents 
    - logs:DescribeLogStreams 
    - logs:DescribeLogGroups 
    - logs:CreateLogStream 
    - logs:CreateLogGroup
  - Install SSM agent on EC2 instance
    - `sudo yum install -y https://s3.amazonaws.com/ec2-downloads-windows/SSMAgent/latest/linux_amd64/amazon-ssm-agent.rpm`
  - Install Cloudwatch agent on EC2 instance
    - `sudo yum install amazon-cloudwatch-agent`
  - Create Prometheus scrape config file by copying the contents of `aws_deploy/global_prometheus_config.yaml` to `/home/ec2-user/prometheus_config.yaml`
  - Create cloud watch agent config by copying contents of `aws_deploy/cloudwatchAgentConfig.json` into
    - `sudo vi /opt/aws/amazon-cloudwatch-agent/var/cwagent-config.json`
  - Restart cloudwatch config
    - `sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -s -c file:/opt/aws/amazon-cloudwatch-agent/var/cwagent-config.json`

# Logging Config
 - Logging is enabled by passing following parameters to docker container
   - `--log-driver=awslogs`
   - `--log-opt awslogs-region=eu-west-2`
   - `--log-opt awslogs-group=JffsLogGroup`
   - `--log-opt awslogs-create-group=true`

# Other commands
   - Stop Nginx
     - `sudo systemctl stop nginx`

   
