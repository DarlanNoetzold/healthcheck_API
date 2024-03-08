import axios from 'axios';

const API_BASE_URL = 'http://177.22.91.106:8199/healthcheck/v1/gate';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: { 'Content-Type': 'application/json' }
});

api.interceptors.request.use(async config => {
    let token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  }, error => Promise.reject(error));

api.interceptors.response.use(response => response, error => {
    if (error.response && error.response.status === 403) {
      window.location.href = '/login';
    }
    return Promise.reject(error);
  });

export const ApiService = {
    fetchRecords: () => api.get('/records'),
    createRecord: (record) => api.post('/records', record),
    updateRecord: (id, record) => api.put(`/records/${id}`, record),
    deleteRecord: (id) => api.delete(`/records/${id}`),
    fetchMetrics: () => api.get('/metrics'),
    createMetric: (metric) => api.post('/metrics', metric),
    updateMetric: (id, metric) => api.put(`/metrics/${id}`, metric),
    deleteMetric: (id) => api.delete(`/metrics/${id}`),
    fetchModelAccuracies: () => api.get('/model-accuracies'),
    createModelAccuracy: (modelAccuracy) => api.post('/model-accuracies', modelAccuracy),
    updateModelAccuracy: (id, modelAccuracy) => api.put(`/model-accuracies/${id}`, modelAccuracy),
    deleteModelAccuracy: (id) => api.delete(`/model-accuracies/${id}`),
};
