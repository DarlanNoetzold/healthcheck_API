import os
import requests
import pandas as pd

# Lista de métricas de interesse selecionadas
metrics_of_interest = [
    "application.ready.time", "application.started.time", "cache.gets",
    "cache.puts", "cache.removals", "hikaricp.connections",
    "hikaricp.connections.acquire", "hikaricp.connections.active",
    "hikaricp.connections.idle", "hikaricp.connections.max", "hikaricp.connections.min",
    "http.server.requests", "http.server.requests.active", "jvm.memory.used",
    "jvm.threads.live", "disk.free", "system.cpu.usage", "process.uptime"
]

# Estrutura para manter o último valor registrado para evitar duplicatas consecutivas
last_recorded_values = {metric: None for metric in metrics_of_interest}

def fetch_data_from_api(base_url, page, size):
    url = f"{base_url}/api/all/byname?page={page}&size={size}"
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()
    else:
        raise Exception(f"Failed to fetch data: {response.status_code}")

def process_metric_responses(metric_responses, accumulated_data):
    for metric in metric_responses:
        if metric['name'] in metrics_of_interest:  # Filtra apenas métricas de interesse
            for response in metric['metricResponses']:
                if 'measurements' in response:
                    for measurement in response['measurements']:
                        value = measurement['value']
                        # Verifica se o registro é uma duplicata consecutiva
                        if last_recorded_values[metric['name']] != value:
                            record = {
                                "metric_id": response['id'],
                                "name": response['name'],
                                "description": response.get("description", ""),
                                "baseUnit": response.get("baseUnit", ""),
                                "statistic": measurement['statistic'],
                                "value": value,
                                "tags": "; ".join([f"{tag['tag']}: {', '.join(tag['values'])}" for tag in response.get('availableTags', [])])
                            }
                            if response['name'] not in accumulated_data:
                                accumulated_data[response['name']] = []
                            accumulated_data[response['name']].append(record)
                            last_recorded_values[metric['name']] = value
                else:
                    print(f"Warning: 'measurements' not found in response for metric {response.get('name', 'Unknown')}")

def save_to_csv(accumulated_data, directory):
    if not os.path.exists(directory):
        os.makedirs(directory)
    for metric_name, data in accumulated_data.items():
        df = pd.DataFrame(data)
        filename = os.path.join(directory, f"{metric_name.replace('.', '_')}.csv")
        df.to_csv(filename, index=False)
        print(f"Data for metric {metric_name} saved to {filename}")

def main():
    base_url = "http://192.168.18.75:8194"
    page = 1
    size = 100
    accumulated_data = {}
    while True:
        data = fetch_data_from_api(base_url, page, size)
        if not data:
            break
        process_metric_responses(data, accumulated_data)
        page += 1
    save_to_csv(accumulated_data, "metrics_data")

if __name__ == "__main__":
    main()
