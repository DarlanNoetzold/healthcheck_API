import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestRegressor, GradientBoostingRegressor
from sklearn.metrics import mean_squared_error, mean_absolute_error
import joblib
import os

# Função para preparar os dados
def prepare_data(df, n=10):
    X, y = [], []
    for i in range(len(df) - n):
        X.append(df.iloc[i:(i + n)].values.reshape(-1))
        y.append(df.iloc[i + n])
    return np.array(X), np.array(y)

# Função para treinar e avaliar modelos
def train_and_evaluate(X_train, X_test, y_train, y_test, model):
    model.fit(X_train, y_train)
    predictions = model.predict(X_test)
    mse = mean_squared_error(y_test, predictions)
    mae = mean_absolute_error(y_test, predictions)
    print(f"MSE: {mse}, MAE: {mae}")
    return model

input_dir = "metrics_from_database"
models_dir = "trained_models"
if not os.path.exists(models_dir):
    os.makedirs(models_dir)

# Carregando e processando cada dataset
for filename in os.listdir(input_dir):
    if filename.endswith(".csv"):
        metric_name = filename[:-4]  # Remove .csv
        df = pd.read_csv(os.path.join(input_dir, filename))['measurement_value']

        # Prepara os dados
        X, y = prepare_data(df)
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

        # Normaliza os dados
        scaler = StandardScaler()
        X_train_scaled = scaler.fit_transform(X_train)
        X_test_scaled = scaler.transform(X_test)

        # Define os modelos e parâmetros para GridSearch
        models_params = {
            'RandomForestRegressor': {
                'model': RandomForestRegressor(random_state=42),
                'params': {'n_estimators': [100, 200], 'max_depth': [10, 20, None]}
            },
            'GradientBoostingRegressor': {
                'model': GradientBoostingRegressor(random_state=42),
                'params': {'n_estimators': [100, 200], 'learning_rate': [0.01, 0.1], 'max_depth': [3, 5]}
            }
        }

        # Treinamento e avaliação de cada modelo
        for model_name, mp in models_params.items():
            print(f"Treinando {model_name} para {metric_name}")
            grid_search = GridSearchCV(mp['model'], mp['params'], cv=3, scoring='neg_mean_squared_error')
            best_model = train_and_evaluate(X_train_scaled, X_test_scaled, y_train, y_test, grid_search)
            # Salva o melhor modelo
            joblib.dump(best_model.best_estimator_, os.path.join(models_dir, f"{metric_name}_{model_name}.pkl"))
