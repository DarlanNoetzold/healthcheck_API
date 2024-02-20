from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import classification_report, accuracy_score
import pandas as pd
import numpy as np
import joblib
import os
from extract_dataset_from_database import extract

def prepare_data_for_classification(df, n=10):
    X, y = [], []
    for i in range(len(df) - n):
        X.append(df.iloc[i:(i + n)]['measurement_value'].values)
        y.append(df.iloc[i + n]['is_alert'])
    return np.array(X), np.array(y)

def train_and_evaluate(metric_name, X_train, X_test, y_train, y_test):
    models = {
        'RandomForestClassifier': RandomForestClassifier(),
        'GradientBoostingClassifier': GradientBoostingClassifier(),
        'LogisticRegression': LogisticRegression(solver='liblinear')
    }

    params = {
        'RandomForestClassifier': {'n_estimators': [50, 100, 150], 'max_depth': [5, 10, None]},
        'GradientBoostingClassifier': {'n_estimators': [50, 100], 'learning_rate': [0.01, 0.1]},
        'LogisticRegression': {'C': [0.1, 1, 10]}
    }

    for name, model in models.items():
        clf = GridSearchCV(model, params[name], cv=5, scoring='accuracy')
        clf.fit(X_train, y_train)
        predictions = clf.predict(X_test)
        print(f"Model: {name}, Metric: {metric_name}, Accuracy: {accuracy_score(y_test, predictions)}")
        print(classification_report(y_test, predictions))
        # Salvar o modelo
        joblib.dump(clf.best_estimator_, f'trained_models_classification/{metric_name}_{name}.pkl')

# Processamento principal
def process_metrics():
    input_dir = "metrics_from_database"
    for filename in os.listdir(input_dir):
        if filename.endswith(".csv"):
            df = pd.read_csv(os.path.join(input_dir, filename))
            if 'is_alert' in df.columns:
                X, y = prepare_data_for_classification(df)
                X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
                scaler = StandardScaler().fit(X_train)
                X_train_scaled = scaler.transform(X_train)
                X_test_scaled = scaler.transform(X_test)
                metric_name = filename.split('.')[0]
                train_and_evaluate(metric_name, X_train_scaled, X_test_scaled, y_train, y_test)

if __name__ == "__main__":
    extract()
    process_metrics()
