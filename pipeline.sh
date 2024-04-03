#!/usr/bin/env sh

CMD="${1}"

ansible-playbook local-dev.yml

# case "$CMD" in
#    "start") export LOCAL_STACK_OPERATION="START"
#       ansible-playbook local-dev.yml
#    ;;
#    "stop") unset LOCAL_STACK_OPERATION
#       ansible-playbook local-dev.yml
#    ;;
#    *) echo "`basename ${0}`:usage: [start]|[stop]"
#         exit 1 # Command to come out of the program with status 1
#    ;;
# esac

# minikube start --driver=docker
# minikube -p minikube docker-env
# minikube addons enable ingress
# minikube addons enable ingress-dns

#
# minikube service ping-app-service --url

# minikube stop