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
from skopt.space import Real, Integer
import os
from concurrent.futures import ProcessPoolExecutor  # Mudança para ProcessPoolExecutor

def prepare_data(df, n=10):
    X, y = [], []
    for i in range(len(df) - n):
        X.append(df.iloc[i:(i + n)].values.reshape(-1))
        y.append(df.iloc[i + n])
    return np.array(X), np.array(y)

def model_training_evaluation(metric_name, X_train_scaled, X_test_scaled, y_train, y_test, model_name, mp):
    print(f"Treinando {model_name} para {metric_name}")

    if model_name == 'RandomForestRegressor':
        opt = BayesSearchCV(
            mp['model'],
            search_spaces=mp['params'],
            n_iter=10,
            cv=3,
            scoring='neg_mean_squared_error',
            random_state=42,
            n_jobs=-1  # Utilize todos os núcleos disponíveis
        )
    else:
        opt = RandomizedSearchCV(
            mp['model'],
            param_distributions=mp['params'],
            n_iter=10,
            cv=3,
            scoring='neg_mean_squared_error',
            random_state=42,
            n_jobs=-1  # Utilize todos os núcleos disponíveis
        )

    opt.fit(X_train_scaled, y_train)
    best_model = opt.best_estimator_

    predictions = best_model.predict(X_test_scaled)
    mse = mean_squared_error(y_test, predictions)
    mae = mean_absolute_error(y_test, predictions)
    r2 = r2_score(y_test, predictions)
    explained_variance = explained_variance_score(y_test, predictions)

    print(f"{model_name} - {metric_name} - MSE: {mse}, MAE: {mae}, R2: {r2}, Explained Variance: {explained_variance}")

    model_filename = os.path.join("trained_models", f"{metric_name}_{model_name}.pkl")
    joblib.dump(best_model, model_filename)

input_dir = "metrics_from_database"
models_dir = "trained_models"
if not os.path.exists(models_dir):
    os.makedirs(models_dir)

models_params = {
    'RandomForestRegressor': {
        'model': RandomForestRegressor(random_state=42),
        'params': {
            'n_estimators': Integer(100, 250),
            'max_depth': Integer(5, 10),
            'min_samples_split': Integer(2, 5),
            'min_samples_leaf': Integer(1, 4)
        }
    },
    'GradientBoostingRegressor': {
        'model': GradientBoostingRegressor(random_state=42),
        'params': {
            'n_estimators': randint(100, 250),
            'learning_rate': uniform(0.01, 0.1),
            'max_depth': randint(3, 7)
        }
    },
    'SVR': {
        'model': SVR(),
        'params': {
            'C': uniform(0.1, 100),
            'gamma': ['scale', 'auto'],
            'kernel': ['linear', 'poly', 'rbf', 'sigmoid']
        }
    }
}

def process_metric(filename):
    if filename.endswith(".csv"):
        metric_name = filename[:-4]  # Remove .csv
        df = pd.read_csv(os.path.join(input_dir, filename), usecols=['measurement_value']).dropna()

        if not df.empty:
            X, y = prepare_data(df['measurement_value'])
            X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

            scaler = StandardScaler()
            X_train_scaled = scaler.fit_transform(X_train)
            X_test_scaled = scaler.transform(X_test)

            for model_name, mp in models_params.items():
                model_training_evaluation(metric_name, X_train_scaled, X_test_scaled, y_train, y_test, model_name, mp)

if __name__ == "__main__":
    with ProcessPoolExecutor(max_workers=os.cpu_count()) as executor:
        files = os.listdir(input_dir)
        executor.map(process_metric, files)
