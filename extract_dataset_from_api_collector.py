import requests
import pandas as pd

def fetch_data_from_api(base_url, page, size):
    url = f"{base_url}/api/all/byname?page={page}&size={size}"
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()
    else:
        raise Exception(f"Failed to fetch data: {response.status_code}")

def process_metric_responses(metric_responses):
    data = []
    for response in metric_responses:
        for measurement in response['measurements']:
            record = {
                "metric_id": response['id'],
                "name": response['name'],
                "description": response.get("description", ""),
                "baseUnit": response.get("baseUnit", ""),
                "statistic": measurement['statistic'],
                "value": measurement['value'],
                "tags": "; ".join([f"{tag['tag']}: {', '.join(tag['values'])}" for tag in response['availableTags']])
            }
            data.append(record)
    return data

def save_to_csv(data, metric_name):
    df = pd.DataFrame(data)
    filename = f"{metric_name.replace('.', '_')}.csv"
    df.to_csv(filename, index=False)
    print(f"Data for metric {metric_name} saved to {filename}")

def main():
    base_url = "http://192.168.18.75:8194"
    page = 1
    size = 100
    while True:
        data = fetch_data_from_api(base_url, page, size)
        if not data:
            break
        for metric in data:
            metric_name = metric['name']
            metric_data = process_metric_responses(metric['metricResponses'])
            if metric_data:
                save_to_csv(metric_data, metric_name)
        page += 1

if __name__ == "__main__":
    main()