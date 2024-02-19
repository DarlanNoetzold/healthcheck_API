from flask import Flask, request, jsonify
import joblib
import numpy as np
import os
from cython_impl.data_preparation import prepare_data

app = Flask(__name__)

modelos = {}
for filename in os.listdir('trained_models'):
    if filename.endswith('.pkl'):
        path = os.path.join('trained_models', filename)
        modelos[filename.split('_')[0]] = joblib.load(path)

@app.route('/predict', methods=['POST'])
def prever():
    dados = request.get_json()
    name = dados.get('name')
    values = dados.get('values')

    if not name or not values:
        return jsonify({'erro': 'Nome da métrica e valores são necessários.'}), 400

    # Preparar os dados de entrada para o modelo
    X, _ = prepare_data(np.array(values), n=len(values))
    X = X.reshape(1, -1)

    # Selecionar e usar o modelo treinado para prever o próximo valor
    if name in modelos:
        modelo = modelos[name]
        previsao = modelo.predict(X)
        return jsonify({'previsao': previsao.tolist()}), 200
    else:
        return jsonify({'erro': 'Modelo para a métrica especificada não foi encontrado.'}), 404

if __name__ == '__main__':
    app.run(debug=True)
