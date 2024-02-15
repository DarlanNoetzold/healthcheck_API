from sklearn.metrics import mean_squared_error, mean_absolute_error, r2_score, explained_variance_score
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split, RandomizedSearchCV
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestRegressor, GradientBoostingRegressor
from sklearn.svm import SVR
from scipy.stats import randint, uniform
import joblib
import os

def prepare_data(df, n=10):
    X, y = [], []
    for i in range(len(df) - n):
        X.append(df.iloc[i:(i + n)].values.reshape(-1))
        y.append(df.iloc[i + n])
    return np.array(X), np.array(y)

# Função para treinar, avaliar modelos e imprimir as métricas
def train_and_evaluate(X_train_scaled, X_test_scaled, y_train, y_test, random_search):
    random_search.fit(X_train_scaled, y_train)
    best_model = random_search.best_estimator_

    predictions = best_model.predict(X_test_scaled)
    mse = mean_squared_error(y_test, predictions)
    mae = mean_absolute_error(y_test, predictions)
    r2 = r2_score(y_test, predictions)
    explained_variance = explained_variance_score(y_test, predictions)

    print(f"MSE: {mse}, MAE: {mae}, R2: {r2}, Explained Variance: {explained_variance}")
    return best_model

input_dir = "metrics_from_database"
models_dir = "trained_models"
if not os.path.exists(models_dir):
    os.makedirs(models_dir)

for filename in os.listdir(input_dir):
    if filename.endswith(".csv"):
        metric_name = filename[:-4]  # Remove .csv
        df = pd.read_csv(os.path.join(input_dir, filename))['measurement_value']

        X, y = prepare_data(df)
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

        scaler = StandardScaler()
        X_train_scaled = scaler.fit_transform(X_train)
        X_test_scaled = scaler.transform(X_test)

        models_params = {
            'RandomForestRegressor': {
                'model': RandomForestRegressor(random_state=42),
                'params': {
                    'n_estimators': randint(100, 500),
                    'max_depth': [10, 20, None],
                    'min_samples_split': randint(2, 11),
                    'min_samples_leaf': randint(1, 11)
                }
            },
            'GradientBoostingRegressor': {
                'model': GradientBoostingRegressor(random_state=42),
                'params': {
                    'n_estimators': randint(100, 500),
                    'learning_rate': uniform(0.01, 0.2),
                    'max_depth': randint(3, 10)
                }
            },
            'SVR': {
                'model': SVR(),
                'params': {
                    'C': uniform(0.1, 1000),
                    'gamma': ['scale', 'auto'],
                    'kernel': ['linear', 'poly', 'rbf', 'sigmoid']
                }
            }
        }

        for model_name, mp in models_params.items():
            print(f"Treinando {model_name} para {metric_name}")
            random_search = RandomizedSearchCV(mp['model'], mp['params'], n_iter=20, cv=3, scoring='neg_mean_squared_error', random_state=42)
            best_model = train_and_evaluate(X_train_scaled, X_test_scaled, y_train, y_test, random_search)

            model_filename = os.path.join(models_dir, f"{metric_name}_{model_name}.pkl")
            joblib.dump(best_model, model_filename)
