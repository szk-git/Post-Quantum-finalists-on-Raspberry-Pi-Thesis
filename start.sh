#!/bin/bash

cd backend/
docker rmi django-backend:1.0
echo "========================================================="
docker build -t django-backend:1.0 .
echo "========================================================="

echo "========================================================="
kubectl create ns jfirbj-thesis
echo "========================================================="

echo "========================================================="
kubectl run django-backend --image=django-backend:1.0 --port=8000 -n jfirbj-thesis
echo "========================================================="

cd ..

echo "DONE"