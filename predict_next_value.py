import numpy as np
import joblib

def predict_next_value(model_path, last_10_values):
    model = joblib.load(model_path)
    prediction = model.predict([last_10_values])
    return prediction[0]

# Exemplo de uso
model_path = "trained_models/disk_free_model.pkl"  # Ajuste conforme o nome do seu modelo
last_10_values = np.array([...])  # Substitua [...] pelos seus últimos 10 valores
predicted_value = predict_next_value(model_path, last_10_values)
print("Valor previsto:", predicted_value)