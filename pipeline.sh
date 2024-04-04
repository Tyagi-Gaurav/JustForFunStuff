#!/usr/bin/env sh

CMD="${1}"

case "$CMD" in
   "deploy") export DEPLOY_TYPE="REMOTE"
#    ansible-playbook scripts/pipeline.yml
   ansible-playbook scripts/remote-dev/pipeline.yml
   ;;
   "*") export DEPLOY_TYPE="LOCAL"
   ansible-playbook scripts/pipeline.yml
   ;;
esac

# minikube start --driver=docker
# minikube -p minikube docker-env
# minikube addons enable ingress
# minikube addons enable ingress-dns

#
# minikube service ui-app-service --url

# minikube stop