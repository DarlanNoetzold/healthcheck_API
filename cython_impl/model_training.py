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
from data_preparation import prepare_data
from extract_dataset_from_database import extract

np.int = np.int64

def model_training_evaluation(args):
    metric_name, X_train_scaled, X_test_scaled, y_train, y_test, model_name, mp = args
    try:
        model_filename = os.path.join("trained_models", f"{metric_name}_{model_name}.pkl")

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
    except Exception as e:
        print(f"Erro ao treinar {model_name} para {metric_name}: {e}")

input_dir = "metrics_from_database"
models_dir = "trained_models"
if not os.path.exists(models_dir):
    os.makedirs(models_dir)

models_params = {
    'RandomForestRegressor': {
        'model': RandomForestRegressor(random_state=42),
        'params': {
            'n_estimators': Integer(5, 50),
            'max_depth': Integer(1, 5),
            'min_samples_split': Integer(2, 10),
            'min_samples_leaf': Integer(1, 3)
        }
    },
    'GradientBoostingRegressor': {
        'model': GradientBoostingRegressor(random_state=42),
        'params': {
            'n_estimators': randint(5, 50),
            'learning_rate': uniform(0.01, 0.1),
            'max_depth': randint(1, 4)
        }
    },
    'SVR': {
        'model': SVR(),
        'params': {
            'C': uniform(0.1, 50),
            'gamma': ['scale', 'auto'],
            'kernel': ['linear', 'poly', 'rbf', 'sigmoid']
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

            with ProcessPoolExecutor(max_workers=os.cpu_count()) as executor:
                results = list(executor.map(model_training_evaluation, args_list))
                for result in results:
                    print(result)

if __name__ == "__main__":
    extract()

    input_dir = "metrics_from_database"
    models_dir = "trained_models"
    if not os.path.exists(models_dir):
        os.makedirs(models_dir)

    filenames = [f for f in os.listdir(input_dir) if f.endswith(".csv")]

    with ProcessPoolExecutor(max_workers=os.cpu_count()) as executor:
        future_to_filename = {executor.submit(process_metric, filename): filename for filename in filenames}

        for future in as_completed(future_to_filename):
            filename = future_to_filename[future]
            try:
                result = future.result()
                print(f"Processamento concluído para: {filename}")
            except Exception as exc:
                print(f"Erro ao processar {filename}: {exc}")