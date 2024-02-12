import os
import requests
import pandas as pd
from concurrent.futures import ThreadPoolExecutor, as_completed

# Lista de métricas de interesse
metrics_of_interest = [
    "application.ready.time", "application.started.time", "cache.gets",
    "cache.puts", "cache.removals", "hikaricp.connections",
    "hikaricp.connections.acquire", "hikaricp.connections.active",
    "hikaricp.connections.idle", "hikaricp.connections.max", "hikaricp.connections.min",
    "http.server.requests", "http.server.requests.active", "jvm.memory.used",
    "jvm.threads.live", "disk.free", "system.cpu.usage", "process.uptime"
]

# Estrutura para acumular os dados coletados, evitando duplicatas
accumulated_data = {metric: [] for metric in metrics_of_interest}
last_value = {metric: None for metric in metrics_of_interest}

base_url = "http://192.168.18.75:8194"
size = 100

def fetch_data_from_api(page):
    url = f"{base_url}/api/all/byname?page={page}&size={size}"
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()
    else:
        print(f"Failed to fetch data: {response.status_code}")
        return None

def process_metric_data(metric_data):
    global accumulated_data, last_value
    for metric in metric_data:
        if metric['name'] in metrics_of_interest:
            for response in metric['metricResponses']:
                if 'measurements' in response:
                    for measurement in response['measurements']:
                        current_value = measurement['value']
                        if last_value[metric['name']] != current_value:
                            record = {
                                "metric_id": response['id'],
                                "name": response['name'],
                                "description": response.get("description", ""),
                                "baseUnit": response.get("baseUnit", ""),
                                "statistic": measurement['statistic'],
                                "value": current_value,
                                "tags": "; ".join([f"{tag['tag']}: {', '.join(tag['values'])}" for tag in response.get('availableTags', [])])
                            }
                            accumulated_data[metric['name']].append(record)
                            last_value[metric['name']] = current_value

def save_to_csv(directory):
    if not os.path.exists(directory):
        os.makedirs(directory)
    for metric, data in accumulated_data.items():
        if data:
            df = pd.DataFrame(data)
            filename = os.path.join(directory, f"{metric.replace('.', '_')}.csv")
            df.to_csv(filename, index=False)
            print(f"Data for metric {metric} saved to {filename}")

def main():
    directory = "metrics_data"
    page = 1
    futures_list = []
    with ThreadPoolExecutor(max_workers=100) as executor:
        while True:
            future = executor.submit(fetch_data_from_api, page)
            futures_list.append(future)
            if future.result() is None:
                print("No more data available. Stopping.")
                break
            page += 1

        for future in as_completed(futures_list):
            data = future.result()
            if data:
                process_metric_data(data)

    save_to_csv(directory)

if __name__ == "__main__":
    main()