#!/bin/bash

kubectl delete ingress local-ingress
kubectl delete deployment ui-app-deployment
kubectl delete service ui-app-service

kubectl apply -f deployment.yml
kubectl expose deployment ui-app-deployment --type=NodePort --name=ui-app-service --port 80 --target-port 3000
kubectl apply -f scripts/local_ingress.yml