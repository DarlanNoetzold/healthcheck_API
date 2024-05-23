#!/bin/bash

SCRIPT_DIR=$(dirname "$(realpath "$BASH_SOURCE")")

MODEL_TRAINING_FIRST_LAYER="$SCRIPT_DIR/cython_impl/model_training_first_layer.py"
MODEL_TRAINING_SECOND_LAYER="$SCRIPT_DIR/cython_impl/model_training_second_layer.py"

CRON_JOBS="
0 0 * * * /usr/bin/python3 $MODEL_TRAINING_FIRST_LAYER >> $SCRIPT_DIR/logs/log_first_layer.txt 2>&1
0 12 * * * /usr/bin/python3 $MODEL_TRAINING_SECOND_LAYER >> $SCRIPT_DIR/logs/log_second_layer.txt 2>&1
"

(crontab -l 2>/dev/null; echo "$CRON_JOBS") | crontab -


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

docker start e3f6bca18032  # Redis
docker start 1e61b17136ce  # PostgreSQL
docker start f5b367414fe7  # RabbitMQ

base_dir="/home/umbrel/projetos/healthcheck_API"

cd $base_dir
git reset --hard
git pull
chmod +x start_all.sh

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
npm install
npm start &


jmeter_path="/home/umbrel/projetos/apache-jmeter-5.5/bin/jmeter.sh"
test_plan="/home/umbrel/projetos/apache-jmeter-5.5/bin/testeApi.jmx"
$jmeter_path -n -t $test_plan
