#!/bin/bash

kubectl --kubeconfig=/Users/gauravtyagi/.kube/k8s-1-29-1-do-0-lon1-1710889039000-kubeconfig.yaml delete deployment ui-app-deployment
kubectl --kubeconfig=/Users/gauravtyagi/.kube/k8s-1-29-1-do-0-lon1-1710889039000-kubeconfig.yaml delete service ui-app-service

kubectl --kubeconfig=/Users/gauravtyagi/.kube/k8s-1-29-1-do-0-lon1-1710889039000-kubeconfig.yaml apply -f deployment.yml
kubectl --kubeconfig=/Users/gauravtyagi/.kube/k8s-1-29-1-do-0-lon1-1710889039000-kubeconfig.yaml expose deployment ui-app-deployment --type=NodePort --name=ui-app-service --port 80 --target-port 3000
kubectl --kubeconfig=/Users/gauravtyagi/.kube/k8s-1-29-1-do-0-lon1-1710889039000-kubeconfig.yaml apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.1.1/deploy/static/provider/do/deploy.yaml