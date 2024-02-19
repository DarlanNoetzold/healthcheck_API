from flask import Flask, request, jsonify
import joblib
import numpy as np

app = Flask(__name__)

# Supondo que os modelos estejam salvos no diretório 'trained_models'
modelos = {}


@app.before_first_request
def load_models():
    global modelos
    # Carregue aqui seus modelos, por exemplo:
    modelos['metrica_exemplo'] = joblib.load('trained_models/metrica_exemplo.pkl')


@app.route('/prever', methods=['POST'])
def prever():
    dados = request.json
    name = dados['name']
    values = dados['values']

    # Verifica se o modelo para a métrica solicitada está carregado
    if name in modelos:
        modelo = modelos[name]

        # Transforma os valores recebidos para o formato esperado pelo modelo
        # Isso pode variar dependendo de como você treinou seu modelo
        X = np.array(values).reshape(1, -1)

        # Faz a previsão
        previsto = modelo.predict(X)

        return jsonify({'previsto': previsto.tolist()}), 200
    else:
        return jsonify({'erro': 'Modelo não encontrado para a métrica fornecida.'}), 404


if __name__ == '__main__':
    app.run(debug=True)
