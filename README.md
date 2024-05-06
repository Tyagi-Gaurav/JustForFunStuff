# JustForFunStuff

# Local Installation on Minikube
# - `python3 main.py`
# - `docker build -t chonku/jffs-ui:LATEST -t chonku/jffs-ui:{tag}`
# - `docker image push chonku/jffs-ui:{tag}`
# - `minkube start`
# - Update `tag` in deployment.yml
# - `local_install.sh`

# Local testing
# - `minikube tunnel`
# - `minikube ip`
# - Ensure ingress is added `/etc/hosts` for accessing via browser
# - Access the page on http://ui-app.info