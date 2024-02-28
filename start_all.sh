#!/bin/bash

# Inicia os serviços Docker
docker start fa9167a4bbb4  # Redis
docker start 18b78a64099b # PostgreSQL

# Atualiza o repositório e inicia o healthcheck-collector
cd /home/darlan/projetos/healthcheck_API/
git pull
cd /home/darlan/projetos/healthcheck_API/healthcheck-collector/
mvn clean install
nohup java -jar /home/darlan/projetos/healthcheck_API/healthcheck-collector/target/healthcheck-API-0.0.1-SNAPSHOT.jar &

# Inicia o healthcheck-gate
cd /home/darlan/projetos/healthcheck_API/healthcheck-gate/
mvn clean install
nohup java -jar /home/darlan/projetos/healthcheck_API/healthcheck-gate/target/healthcheck-gate-0.0.1-SNAPSHOT.jar &

# Inicia o daily-API
cd /home/darlan/projetos/daily-API/
mvn clean install
nohup java -jar /home/darlan/projetos/daily-API/target/daily-API-0.0.1-SNAPSHOT.jar &

# Inicia o script predict_next_value.py
nohup python3 /home/darlan/projetos/healthcheck_API/predict_next_value.py &

# Inicia o servidor de desenvolvimento React
cd /home/darlan/projetos/healthcheck_API/healthcheck-dashboard/
npm start &

# Executa o JMeter em background
nohup /home/darlan/projetos/apache-jmeter-5.5/bin/jmeter.sh -n -t /home/darlan/projetos/apache-jmeter-5.5/bin/testeApi.jmx &
