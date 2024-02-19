from flask import Flask, request, jsonify
import joblib
import numpy as np
import os
import sklearn.ensemble
from cython_impl.data_preparation import prepare_data

app = Flask(__name__)

def prepare_data_for_prediction(values, model_input_size):
    if len(values) < model_input_size:
        values += [values[-1]] * (model_input_size - len(values))
    return np.array(values).reshape(1, -1)


modelos = {}
for filename in os.listdir('cython_impl/trained_models'):
    if filename.endswith('.pkl'):
        path = os.path.join('cython_impl/trained_models', filename)
        modelos[filename] = joblib.load(path)

@app.route('/predict', methods=['POST'])
def prever():
    dados = request.json
    name = dados.get('name')
    values = dados.get('values')

    if not name or not values:
        return jsonify({'error': 'Nome da métrica e valores são necessários.'}), 400

    model_input_size = 10
    X = prepare_data_for_prediction(values, model_input_size)

    if name in modelos:
        modelo = modelos[name]
        previsao = modelo.predict(X)
        return jsonify({'previsao': previsao[0].tolist()}), 200
    else:
        return jsonify({'error': 'Modelo para a métrica especificada não foi encontrado.'}), 404


if __name__ == '__main__':
    app.run(debug=True)
