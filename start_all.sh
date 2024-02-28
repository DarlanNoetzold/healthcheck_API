#!/bin/bash

declare -A ports_apps=(
    [8193]="healthcheck-collector"
    [8194]="daily-API"
    [8199]="healthcheck-gate"
    [5000]="predict_next_value"
    [3001]="healthcheck-dashboard"
)

kill_process_by_port() {
    port=$1
    pid=$(lsof -ti:$port)
    if [ ! -z "$pid" ]; then
        echo "Encerrando processo na porta $port..."
        kill -9 $pid
    fi
}

for port in "${!ports_apps[@]}"; do
    kill_process_by_port $port
done

sleep 5

docker start fa9167a4bbb4  # Redis
docker start 18b78a64099b  # PostgreSQL

base_dir="/home/darlan/projetos/healthcheck_API"

cd $base_dir
git pull

cd "$base_dir/healthcheck-collector"
mvn clean install
java -jar target/healthcheck-API-0.0.1-SNAPSHOT.jar &

cd "$base_dir/healthcheck-gate"
mvn clean install
java -jar target/healthcheck-gate-0.0.1-SNAPSHOT.jar &

cd "$base_dir/../daily-API"
mvn clean install
java -jar target/daily-API-0.0.1-SNAPSHOT.jar &

python3 "$base_dir/predict_next_value.py" &

cd "$base_dir/healthcheck-dashboard"
npm start &

jmeter_path="/home/darlan/projetos/apache-jmeter-5.5/bin/jmeter.sh"
test_plan="/home/darlan/projetos/apache-jmeter-5.5/bin/testeApi.jmx"
$jmeter_path -n -t $test_plan
