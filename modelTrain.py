import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestClassifier
from xgboost import XGBClassifier
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score
from sklearn.pipeline import Pipeline
import joblib



# Geração de dados mais realista e equilibrada
np.random.seed(42)
n_samples = 1000

response_time_0 = np.random.normal(1000, 150, n_samples // 2)
error_rate_0 = np.random.uniform(0.1, 0.5, n_samples // 2)
api_status_0 = np.zeros(n_samples // 2)

response_time_1 = np.random.normal(500, 100, n_samples // 2)
error_rate_1 = np.random.uniform(0, 0.3, n_samples // 2)
api_status_1 = np.ones(n_samples // 2)

response_time = np.concatenate((response_time_0, response_time_1))
error_rate = np.concatenate((error_rate_0, error_rate_1))
api_status = np.concatenate((api_status_0, api_status_1))

data = pd.DataFrame({'response_time': response_time, 'error_rate': error_rate, 'api_status': api_status})

# Divisão em features (X) e target (y)
X = data[['response_time', 'error_rate']]
y = data['api_status']

# Divisão em conjunto de treinamento e teste
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Treinamento e otimização dos modelos
models = [
    ('XGBoost', XGBClassifier(random_state=42)),
    ('Random Forest', RandomForestClassifier(random_state=42)),
    ('SVM', SVC(random_state=42))
]

accuracies = []

for name, model in models:
    pipeline = Pipeline([
        ('scaler', StandardScaler()),
        ('model', model)
    ])

    # Definindo grid de hiperparâmetros para otimização
    param_grid = {}
    if name == 'XGBoost':
        param_grid = {
            'model__n_estimators': [50, 100, 200],
            'model__learning_rate': [0.01, 0.1, 0.2],
            'model__max_depth': [3, 4, 5]
        }
    elif name == 'Random Forest':
        param_grid = {
            'model__n_estimators': [50, 100, 200],
            'model__max_depth': [3, 4, 5]
        }
    elif name == 'SVM':
        param_grid = {
            'model__C': [0.1, 1, 10],
            'model__kernel': ['linear', 'rbf']
        }

    grid_search = GridSearchCV(pipeline, param_grid, cv=3)
    grid_search.fit(X_train, y_train)

    best_model = grid_search.best_estimator_
    y_pred = best_model.predict(X_test)
    accuracy = accuracy_score(y_test, y_pred)
    accuracies.append(accuracy)
    models = [
        ('XGBoost', XGBClassifier(random_state=42)),
        ('Random Forest', RandomForestClassifier(random_state=42)),
        ('SVM', SVC(random_state=42))
    ]

    model_filenames = []

    for name, model in models:
        pipeline = Pipeline([
            ('scaler', StandardScaler()),
            ('model', model)
        ])

        # Definindo grid de hiperparâmetros para otimização
        param_grid = {}
        if name == 'XGBoost':
            param_grid = {
                'model__n_estimators': [50, 100, 200],
                'model__learning_rate': [0.01, 0.1, 0.2],
                'model__max_depth': [3, 4, 5]
            }
        elif name == 'Random Forest':
            param_grid = {
                'model__n_estimators': [50, 100, 200],
                'model__max_depth': [3, 4, 5]
            }
        elif name == 'SVM':
            param_grid = {
                'model__C': [0.1, 1, 10],
                'model__kernel': ['linear', 'rbf']
            }

        grid_search = GridSearchCV(pipeline, param_grid, cv=3)
        grid_search.fit(X_train, y_train)

        best_model = grid_search.best_estimator_
        y_pred = best_model.predict(X_test)
        accuracy = accuracy_score(y_test, y_pred)

        # Exportar modelo treinado em formato .pkl
        model_filename = f'{name}_model.pkl'
        joblib.dump(best_model, model_filename)
        model_filenames.append(model_filename)

    print("Models saved as:")
    print(model_filenames)

# Gráfico de acurácia
plt.bar([name for name, _ in models], accuracies, color='skyblue')
plt.xlabel('Model')
plt.ylabel('Accuracy')
plt.title('Accuracy of Different Models')
plt.ylim(0.5, 1.0)
plt.show()
