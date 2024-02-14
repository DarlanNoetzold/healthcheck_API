import os
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error
from sklearn.model_selection import train_test_split
import numpy as np
import joblib

# Diretório onde os CSVs estão armazenados
input_dir = "metrics_from_database"


# Preparando os dados
def prepare_data(df, n=10):
    X, y = [], []
    for i in range(len(df) - n):
        X.append(df.iloc[i:(i + n)].values.reshape(-1))
        y.append(df.iloc[i + n])
    return np.array(X), np.array(y)


# Carregando e processando cada dataset
for filename in os.listdir(input_dir):
    if filename.endswith(".csv"):
        metric_name = filename.replace('.csv', '')
        df = pd.read_csv(os.path.join(input_dir, filename))

        # Supondo que 'value' é a coluna de interesse
        if 'measurement_value' in df.columns:
            df = df['measurement_value'].dropna()
        else:
            print(f"Coluna 'measurement_value' não encontrada em {filename}. Pulando.")
            continue

        X, y = prepare_data(df)

        # Dividindo os dados em conjuntos de treinamento e teste
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

        # Treinamento do modelo
        model = RandomForestRegressor(n_estimators=100, random_state=42)
        model.fit(X_train, y_train)

        # Avaliação do modelo
        predictions = model.predict(X_test)
        mse = mean_squared_error(y_test, predictions)
        print(f"Modelo para {metric_name}: MSE = {mse}")

        # Salvando o modelo
        model_filename = os.path.join("trained_models", f"{metric_name}_model.pkl")
        joblib.dump(model, model_filename)
