from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import classification_report, accuracy_score
import pandas as pd
import numpy as np
import joblib
import os
from concurrent.futures import ProcessPoolExecutor
from data_management.extract_dataset_from_database import extract
import requests
import datetime

API_BASE_URL = "http://192.168.18.75:8199/healthcheck/v1"
LOGIN_URL = f"{API_BASE_URL}/auth/authenticate"
METRICS_URL = f"{API_BASE_URL}/gate/model-accuracies"

auth_data = {
    "email": "admindarlan@mail.com",
    "password": "password"
}

def get_auth_token():
    response = requests.post(LOGIN_URL, json=auth_data)
    if response.status_code == 200:
        return response.json()['access_token']
    else:
        print("Falha ao autenticar")
        return None

def send_metric(metric_name, model_name, accuracy_name, accuracy_value, training_date):
    token = get_auth_token()
    if token:
        headers = {"Authorization": f"Bearer {token}"}
        data = {
            "modelName": model_name,
            "accuracyName": accuracy_name,
            "metricName": metric_name,
            "accuracyValue": accuracy_value,
            "trainingDate": training_date
        }
        response = requests.post(METRICS_URL, json=data, headers=headers)
        if response.status_code == 200:
            print(f"{accuracy_name} enviado com sucesso para {model_name} - {metric_name}")
        else:
            print(f"Falha ao enviar {accuracy_name} para {model_name} - {metric_name}: {response.text}")
    else:
        print("Não foi possível obter o token de autenticação.")


def prepare_data_for_classification(df, n=10):
    X, y = [], []
    for i in range(len(df) - n):
        X.append(df.iloc[i:(i + n)]['measurement_value'].values)
        y.append(int(df.iloc[i + n]['is_alert']))
    return np.array(X), np.array(y)


def train_and_evaluate(metric_name):
    input_dir = "metrics_from_database"
    filename = f"{metric_name}.csv"
    df = pd.read_csv(os.path.join(input_dir, filename))

    if 'is_alert' not in df.columns:
        return

    X, y = prepare_data_for_classification(df)
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    scaler = StandardScaler().fit(X_train)
    X_train_scaled = scaler.transform(X_train)
    X_test_scaled = scaler.transform(X_test)

    models = {
        'RandomForestClassifier': RandomForestClassifier(),
        'GradientBoostingClassifier': GradientBoostingClassifier(),
        'LogisticRegression': LogisticRegression(solver='liblinear')
    }

    params = {
        'RandomForestClassifier': {
            'n_estimators': [200, 300],
            'max_depth': [None, 15, 20],
        },
        'GradientBoostingClassifier': {
            'n_estimators': [200, 300],
            'learning_rate': [0.1, 0.2],
        },
        'LogisticRegression': {
            'C': [10, 100, 1000],
        }
    }

    models_dir = 'trained_models_second_layer'
    if not os.path.exists(models_dir):
        os.makedirs(models_dir)

    for name, model in models.items():
        clf = GridSearchCV(model, params[name], cv=5, scoring='accuracy')
        clf.fit(X_train_scaled, y_train)
        predictions = clf.predict(X_test_scaled)
        accuracy = accuracy_score(y_test, predictions)
        print(f"Model: {name}, Metric: {metric_name}, Accuracy: {accuracy}")

        model_path = os.path.join(models_dir, f'{metric_name}_{name}.pkl')
        joblib.dump(clf.best_estimator_, model_path)
        print(f"Model saved: {model_path}")

        training_date = datetime.datetime.now().strftime('%Y-%m-%d')
        send_metric(metric_name, name, "Accuracy", accuracy, training_date)


def process_metrics_parallel():
    input_dir = "metrics_from_database"
    metric_names = [filename[:-4] for filename in os.listdir(input_dir) if filename.endswith(".csv")]

    with ProcessPoolExecutor() as executor:
        executor.map(train_and_evaluate, metric_names)


if __name__ == "__main__":
    extract()
    process_metrics_parallel()
