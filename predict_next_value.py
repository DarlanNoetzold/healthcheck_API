from flask import Flask, request, jsonify
import joblib
import numpy as np
import os

app = Flask(__name__)

models_dir_first_layer = 'cython_impl/trained_models_first_layer'
models_dir_second_layer = 'cython_impl/trained_models_second_layer'

model_names_value = ['_GradientBoostingRegressor.pkl', '_RandomForestRegressor.pkl', '_SVR.pkl']
model_names_alert = ['_GradientBoostingClassifier.pkl', '_LogisticRegression.pkl', '_RandomForestClassifier.pkl']

def prepare_data_for_prediction(values, model_input_size):
    if len(values) < model_input_size:
        values += [values[-1]] * (model_input_size - len(values))
    return np.array(values).reshape(1, -1)

def prepare_data_for_prediction_alert(values, n=10):
    if len(values) < n:
        values += [values[-1]] * (n - len(values))
    elif len(values) > n:
        values = values[-n:]
    return np.array(values).reshape(1, -1)

@app.route('/predict-value', methods=['POST'])
def predict_value():
    data = request.json
    metric_name = data.get('name')
    values = data.get('values')
    model_input_size = 10
    X = prepare_data_for_prediction(values, model_input_size)

    predictions = {}
    for model_name_suffix in model_names_value:
        model_filename = f'{metric_name}{model_name_suffix}'
        model_path = os.path.join(models_dir_first_layer, model_filename)
        if os.path.exists(model_path):
            model = joblib.load(model_path)
            prediction = model.predict(X)
            formatted_model_name = model_name_suffix.lstrip('_').replace('.pkl', '')
            predictions[formatted_model_name] = prediction[0].tolist()

    if predictions:
        return jsonify(predictions), 200
    else:
        return jsonify({'error': 'Nenhum modelo encontrado para a métrica especificada.'}), 404

@app.route('/predict-alert', methods=['POST'])
def predict_alert():
    data = request.json
    metric_name = data.get('name')
    value = data.get('value')
    values = [value]
    X = prepare_data_for_prediction_alert(values, 10)

    predictions = {}
    for model_name_suffix in model_names_alert:
        model_filename = f'{metric_name}{model_name_suffix}'
        model_path = os.path.join(models_dir_second_layer, model_filename)
        if os.path.exists(model_path):
            model = joblib.load(model_path)
            prediction = model.predict(X)
            is_alert = bool(prediction[0])
            formatted_model_name = model_name_suffix.lstrip('_').replace('.pkl', '')
            predictions[formatted_model_name] = is_alert

    if predictions:
        return jsonify(predictions), 200
    else:
        return jsonify({'error': 'Nenhum modelo encontrado para a métrica especificada.'}), 404

@app.route('/ping', methods=['GET'])
def ping():
    return 200


if __name__ == '__main__':
    app.run(host="0.0.0.0")
