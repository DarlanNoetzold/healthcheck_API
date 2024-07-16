import datetime
import joblib
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split, RandomizedSearchCV
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestRegressor, GradientBoostingRegressor
from sklearn.svm import SVR
from sklearn.metrics import mean_squared_error, mean_absolute_error, r2_score, explained_variance_score
from scipy.stats import randint, uniform
from skopt import BayesSearchCV
from skopt.space import Integer, Real
import os
from concurrent.futures import ProcessPoolExecutor, as_completed
from data_management.data_preparation import prepare_data
from data_management.extract_dataset_from_database import extract
import requests
import time


np.int = np.int64

API_BASE_URL = "http://localhost:8199/healthcheck/v1"
LOGIN_URL = f"{API_BASE_URL}/auth/authenticate"
METRICS_URL = f"{API_BASE_URL}/gate/model-accuracies"

auth_data = {
    "email": "teste@mail.com",
    "password": "password"
}

def get_auth_token():
    response = requests.post(LOGIN_URL, json=auth_data)
    if response.status_code == 200:
        return response.json()['access_token']
    else:
        print("Falha ao autenticar")
        return None

def send_metric(metric_name, model_name, accuracy_name, accuracy_value, training_date, token):
    max_attempts = 5
    attempt = 0
    success = False

    while attempt < max_attempts and not success:
        if token:
            headers = {"Authorization": f"Bearer {token}"}
            data = {
                "modelName": model_name,
                "accuracyName": accuracy_name,
                "metricName": metric_name,
                "accuracyValue": accuracy_value,
                "trainingDate": training_date
            }
            try:
                response = requests.post(METRICS_URL, json=data, headers=headers)
                if response.status_code == 200:
                    print(f"{accuracy_name} enviado com sucesso para {model_name} - {metric_name}")
                    success = True
                else:
                    print(f"Tentativa {attempt + 1}: Falha ao enviar {accuracy_name} para {model_name} - {metric_name}: {response.text}")
            except requests.exceptions.RequestException as e:
                print(f"Tentativa {attempt + 1}: Erro de conexão ao enviar {accuracy_name} para {model_name} - {metric_name}: {str(e)}")
        else:
            print("Não foi possível obter o token de autenticação.")
            break

        attempt += 1
        if not success and attempt < max_attempts:
            time.sleep(1)

    if not success:
        print(f"Falha ao enviar {accuracy_name} após {max_attempts} tentativas.")


def model_training_evaluation(args):
    metric_name, X_train_scaled, X_test_scaled, y_train, y_test, model_name, mp = args
    try:
        model_filename = os.path.join("trained_models_first_layer", f"{metric_name}_{model_name}.pkl")

        if os.path.exists(model_filename):
            print(f"Modelo {model_name} para {metric_name} já treinado. Pulando...")
            return

        print(f"Treinando {model_name} para {metric_name}")

        if model_name == 'RandomForestRegressor':
            search = BayesSearchCV(mp['model'], mp['params'], n_iter=10, cv=3, scoring='neg_mean_squared_error', random_state=42, n_jobs=-1)
        else:
            search = RandomizedSearchCV(mp['model'], mp['params'], n_iter=10, cv=3, scoring='neg_mean_squared_error', random_state=42, n_jobs=-1)

        search.fit(X_train_scaled, y_train)
        best_model = search.best_estimator_

        predictions = best_model.predict(X_test_scaled)
        mse = mean_squared_error(y_test, predictions)
        mae = mean_absolute_error(y_test, predictions)
        r2 = r2_score(y_test, predictions)
        explained_variance = explained_variance_score(y_test, predictions)

        print(f"{model_name} - {metric_name} - MSE: {mse}, MAE: {mae}, R2: {r2}, Explained Variance: {explained_variance}")
        joblib.dump(best_model, model_filename)

        training_date = datetime.datetime.now().strftime('%Y-%m-%d')
        token = get_auth_token()
        send_metric(metric_name, model_name, "MSE",mse, training_date,token)
        send_metric(metric_name, model_name, "MAE",mae, training_date,token)
        send_metric(metric_name, model_name, "R2",r2, training_date,token)
        send_metric(metric_name, model_name, "Explained Variance",explained_variance, training_date,token)

    except Exception as e:
        print(f"Erro ao treinar {model_name} para {metric_name}: {e}")

input_dir = "metrics_from_database"
models_dir = "trained_models_first_layer"
if not os.path.exists(models_dir):
    os.makedirs(models_dir)

models_params = {
    'RandomForestRegressor': {
        'model': RandomForestRegressor(random_state=42),
        'params': {
            'n_estimators': Integer(100, 200),
            'max_depth': Integer(5, 10),
            'min_samples_split': Integer(2, 5),
            'min_samples_leaf': Integer(1, 4)
        }
    },
    'GradientBoostingRegressor': {
        'model': GradientBoostingRegressor(random_state=42),
        'params': {
            'n_estimators': randint(100, 200),
            'learning_rate': uniform(0.01, 0.05),
            'max_depth': randint(3, 6)
        }
    },
    'SVR': {
        'model': SVR(),
        'params': {
            'C': uniform(1, 100),
            'gamma': ['scale', 'auto'],
            'kernel': ['rbf']
        }
    }
}


def process_metric(filename):
    if filename.endswith(".csv"):
        metric_name = filename[:-4]
        df = pd.read_csv(os.path.join(input_dir, filename), usecols=['measurement_value']).dropna()

        if not df.empty:
            X, y = prepare_data(df['measurement_value'].values.astype(np.float64), n=10)
            X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

            scaler = StandardScaler()
            X_train_scaled = scaler.fit_transform(X_train)
            X_test_scaled = scaler.transform(X_test)

            args_list = [(metric_name, X_train_scaled, X_test_scaled, y_train, y_test, model_name, mp) for model_name, mp in models_params.items()]

            with ProcessPoolExecutor(max_workers=5) as executor:
                results = list(executor.map(model_training_evaluation, args_list))
                for result in results:
                    print(result)

if __name__ == "__main__":
    try:
        extract()

        input_dir = "metrics_from_database"
        models_dir = "trained_models_first_layer"
        if not os.path.exists(models_dir):
            os.makedirs(models_dir)

        filenames = [f for f in os.listdir(input_dir) if f.endswith(".csv")]

        with ProcessPoolExecutor(max_workers=5) as executor:
            future_to_filename = {executor.submit(process_metric, filename): filename for filename in filenames}

            for future in as_completed(future_to_filename):
                filename = future_to_filename[future]
                try:
                    result = future.result()
                    print(f"Processamento concluído para: {filename}")
                except Exception as exc:
                    print(f"Erro ao processar {filename}: {exc}")
    except Exception as e:
        print(f"Erro geral no script: {e}")
    finally:
        print("Script concluído.")