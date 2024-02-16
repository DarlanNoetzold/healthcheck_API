from skopt import BayesSearchCV
from skopt.space import Real, Categorical, Integer
from sklearn.ensemble import RandomForestRegressor

# Definindo o espaço de busca
search_spaces = {
    'n_estimators': Integer(100, 500),
    'max_depth': Integer(10, 20),
    'min_samples_split': Integer(2, 11),
    'min_samples_leaf': Integer(1, 11)
}

# Configuração do BayesSearchCV
opt = BayesSearchCV(
    RandomForestRegressor(random_state=42),
    search_spaces,
    n_iter=50,
    cv=3,
    n_jobs=-1,
    random_state=42
)

# Supondo X_train_scaled, y_train já definidos
opt.fit(X_train_scaled, y_train)

print(f"Melhores parâmetros: {opt.best_params_}")