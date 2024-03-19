import os
import requests
import pandas as pd
from concurrent.futures import ThreadPoolExecutor, as_completed

# Lista de métricas de interesse
metrics_of_interest = [
    "disk.free", "hikaricp.connections.acquire",
    "http.server.requests", "http.server.requests.active",
    "jvm.buffer.count", "jvm.buffer.memory.used", "jvm.buffer.total.capacity",
    "jvm.classes.loaded", "jvm.compilation.time", "jvm.gc.memory.allocated",
    "jvm.gc.memory.promoted", "jvm.gc.overhead", "jvm.gc.pause",
    "jvm.memory.committed", "jvm.memory.usage.after.gc",
    "jvm.memory.used", "jvm.threads.daemon", "jvm.threads.live", "jvm.threads.peak",
    "jvm.threads.started", "jvm.threads.states", "logback.events", "process.cpu.usage",
    "process.uptime", "system.cpu.usage", "system.load.average.1m"
]

base_url = "http://192.168.18.75:8194"
size = 100

def fetch_data_from_api(session, page):
    url = f"{base_url}/api/all/byname?page={page}&size={size}"
    response = session.get(url)
    if response.status_code == 200 and response.json():
        print(response.json())
        return response.json()
    print(response.json())
    return None

def process_metric_data(metric_data, accumulated_data, last_value):
    for metric in metric_data:
        if metric['name'] in metrics_of_interest:
            for response in metric['metricResponses']:
                if 'measurements' in response:
                    for measurement in response['measurements']:
                        current_value = measurement['value']
                        metric_key = metric['name']
                        if last_value.get(metric_key, None) != current_value:
                            record = {
                                "metric_id": response['id'],
                                "name": response['name'],
                                "description": response.get("description", ""),
                                "baseUnit": response.get("baseUnit", ""),
                                "statistic": measurement['statistic'],
                                "value": current_value,
                                "tags": "; ".join([f"{tag['tag']}: {', '.join(tag['values'])}" for tag in response.get('availableTags', [])])
                            }
                            if metric_key not in accumulated_data:
                                accumulated_data[metric_key] = []
                            accumulated_data[metric_key].append(record)
                            last_value[metric_key] = current_value

def save_to_csv(accumulated_data, directory="metrics_data_from_api"):
    if not os.path.exists(directory):
        os.makedirs(directory)
    for metric, records in accumulated_data.items():
        if records:
            df = pd.DataFrame(records)
            filename = os.path.join(directory, f"{metric.replace('.', '_')}.csv")
            df.to_csv(filename, index=False)
            print(f"Data for metric {metric} saved to {filename}")

def main():
    accumulated_data = {metric: [] for metric in metrics_of_interest}
    last_value = {}

    with requests.Session() as session:
        page = 1
        with ThreadPoolExecutor(max_workers=5000) as executor:
            while True:
                future = executor.submit(fetch_data_from_api, session, page)
                data = future.result()
                if not data:
                    break
                process_metric_data(data, accumulated_data, last_value)
                page += 1
                print(str(page))

    save_to_csv(accumulated_data)

if __name__ == "__main__":
    main()
