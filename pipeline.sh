#!/usr/bin/env sh

CMD="${1}"

ansible-playbook pipeline.yml

case "$CMD" in
   "deploy") export DEPLOY_TYPE="REMOTE"
      ansible-playbook pipeline.yml
   ;;
   "*") ansible-playbook pipeline.yml
   ;;
esac

# minikube start --driver=docker
# minikube -p minikube docker-env
# minikube addons enable ingress
# minikube addons enable ingress-dns

#
# minikube service ui-app-service --url

# minikube stop