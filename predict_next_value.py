from flask import Flask, request, jsonify
import joblib
import numpy as np
import os

app = Flask(__name__)

def prepare_data_for_prediction_values(values, model_input_size):
    if len(values) < model_input_size:
        values += [values[-1]] * (model_input_size - len(values))
    return np.array(values).reshape(1, -1)


def prepare_data_for_prediction_alert(values, n):
    if len(values) < n:
        values += [values[-1]] * (n - len(values))
    elif len(values) > n:
        values = values[-n:]
    return np.array(values).reshape(1, -n)


models_first_layer = {}
for filename in os.listdir('cython_impl/trained_models'):
    if filename.endswith('.pkl'):
        path = os.path.join('cython_impl/trained_models', filename)
        models_first_layer[filename] = joblib.load(path)


models_second_layer = {}
for filename in os.listdir('cython_impl/trained_models'):
    if filename.endswith('.pkl'):
        path = os.path.join('cython_impl/trained_models', filename)
        models_second_layer[filename] = joblib.load(path)

@app.route('/predict-value', methods=['POST'])
def predict_value():
    dados = request.json
    name = dados.get('name')
    values = dados.get('values')

    if not name or not values:
        return jsonify({'error': 'Nome da métrica e valores são necessários.'}), 400

    model_input_size = 10
    X = prepare_data_for_prediction_values(values, model_input_size)

    if name in models_first_layer:
        modelo = models_first_layer[name]
        previsao = modelo.predict(X)
        return jsonify({'previsao': previsao[0].tolist()}), 200
    else:
        return jsonify({'error': 'Modelo para a métrica especificada não foi encontrado.'}), 404

@app.route('/predict-alert', methods=['POST'])
def predict_alert():
    data = request.json
    name = data.get('name')
    values = data.get('values')

    if name in models_second_layer:
        model = models_second_layer[name]
        X = prepare_data_for_prediction_alert(values, 1)
        prediction = model.predict(X)
        is_alert = bool(prediction[0])
        return jsonify({'is_alert': is_alert}), 200
    else:
        return jsonify({'error': 'Modelo não encontrado para a métrica especificada.'}), 404


if __name__ == '__main__':
    app.run(debug=True)
