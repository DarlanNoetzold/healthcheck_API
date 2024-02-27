import axios from 'axios';

const API_BASE_URL = 'http://192.168.18.75:8199/gate';
const PREDICT_API_BASE_URL = 'http://192.168.18.75:5000';

export const ApiService = {
  // Registros
  fetchRecords: () => axios.get(`${API_BASE_URL}/records`),
  createRecord: (record) => axios.post(`${API_BASE_URL}/records`, record),
  updateRecord: (id, record) => axios.put(`${API_BASE_URL}/records/${id}`, record),
  deleteRecord: (id) => axios.delete(`${API_BASE_URL}/records/${id}`),

  // Métricas
  fetchMetrics: () => axios.get(`${API_BASE_URL}/metrics`),
  createMetric: (metric) => axios.post(`${API_BASE_URL}/metrics`, metric),
  updateMetric: (id, metric) => axios.put(`${API_BASE_URL}/metrics/${id}`, metric),
  deleteMetric: (id) => axios.delete(`${API_BASE_URL}/metrics/${id}`),

  // Precisão dos Modelos
  fetchModelAccuracies: () => axios.get(`${API_BASE_URL}/model-accuracies`),
  createModelAccuracy: (modelAccuracy) => axios.post(`${API_BASE_URL}/model-accuracies`, modelAccuracy),
  updateModelAccuracy: (id, modelAccuracy) => axios.put(`${API_BASE_URL}/model-accuracies/${id}`, modelAccuracy),
  deleteModelAccuracy: (id) => axios.delete(`${API_BASE_URL}/model-accuracies/${id}`),

  // Previsão de Valores
  predictValue: (data) => axios.post(`${PREDICT_API_BASE_URL}/predict-value`, data),

  // Previsão de Alerta
  predictAlert: (data) => axios.post(`${PREDICT_API_BASE_URL}/predict-alert`, data),
};
