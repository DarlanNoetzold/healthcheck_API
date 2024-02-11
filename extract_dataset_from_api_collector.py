import os
import requests
import pandas as pd

def fetch_data_from_api(base_url, page, size):
    url = f"{base_url}/api/all/byname?page={page}&size={size}"
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()
    else:
        raise Exception(f"Failed to fetch data: {response.status_code}")

def process_metric_responses(metric_responses, accumulated_data):
    for metric in metric_responses:  # Ajuste para processar corretamente a estrutura da API
        for response in metric['metricResponses']:  # Corrigido para acessar 'metricResponses'
            if 'measurements' in response:  # Verificação para 'measurements'
                for measurement in response['measurements']:
                    record = {
                        "metric_id": response['id'],
                        "name": response['name'],
                        "description": response.get("description", ""),
                        "baseUnit": response.get("baseUnit", ""),
                        "statistic": measurement['statistic'],
                        "value": measurement['value'],
                        "tags": "; ".join([f"{tag['tag']}: {', '.join(tag['values'])}" for tag in response.get('availableTags', [])])  # Garante que 'availableTags' também está presente
                    }
                    if response['name'] not in accumulated_data:
                        accumulated_data[response['name']] = []
                    accumulated_data[response['name']].append(record)
            else:  # Caso 'measurements' não esteja presente
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
