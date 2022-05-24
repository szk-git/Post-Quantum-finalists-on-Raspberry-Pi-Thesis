#!/bin/bash

echo "======================= SYSTEM UNDER UPDATING ======================="
sudo apt update
echo "======================= INSTALL <libssl-dev> PACKAGE ======================="
sudo apt-get install libssl-dev -y
echo "======================= INSTALL <bitstring> PACKAGE ======================="
pip install bitstring
echo "======================= INSTALL <default-jre> PACKAGE ======================="
sudo apt install default-jre -y
echo "======================= INSTALL <maven> PACKAGE ======================="
sudo apt install maven -y
echo "======================= INSTALL <nodejs> PACKAGE ======================="
sudo apt install nodejs -y
echo "======================= INSTALL <npm> PACKAGE ======================="
sudo apt install npm -y

mv database.mv.db ~

(cd frontend/; npm install)