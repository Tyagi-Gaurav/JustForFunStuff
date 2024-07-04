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
- Checkin and push everything
- `mvn clean package`
- `cd jffs-ui`
- Build image for UI (Replace tag with eg v1.9)
    - `docker build -t chonku/jffs-ui:LATEST -t chonku/jffs-ui:v1.12 .`
- `docker image push chonku/jffs-ui:v1.12`
- `cd jffs-backend`
- `docker build -t chonku/jffs-backend:LATEST -t chonku/jffs-backend:v1.12 .`
- `docker image push chonku/jffs-backend:v1.12`
- `git push origin tag v1.12`

# Release instructions
  - Ensure Prepare to release instructions are complete
  - SSH into the machine
    - `docker pull chonku/jffs-ui:v1.12`
    - `docker pull chonku/jffs-backend:v1.12`
    - `docker stop jffs-backend && docker rm jffs-backend`
    - `docker run -p 8080:8080 -p 8081:8081 -d -e "DB_USER=<>" -e "DB_PWD=<>" -e "DB_NAME=Prod" -e "DB_HOST=<>" -e "DB_SCHEME=mongodb+srv" --name jffs-backend chonku/jffs-backend:v1.12`
    - Run Healthcheck for application using the command
      - `wget -O - http://localhost:8081/actuator/health` 
    - `docker stop jffs-ui && docker rm jffs-ui`
    - `docker run -p 3000:3000 -d --name jffs-ui chonku/jffs-ui:v1.12`    

# Other commands
   - Stop Nginx
     - `sudo systemctl stop nginx`

   
