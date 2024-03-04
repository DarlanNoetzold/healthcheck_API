import axios from 'axios';

const API_BASE_URL = 'http://localhost:8199/healthcheck/v1/gate';
const PREDICT_API_BASE_URL = 'http://localhost:5000/healthcheck/v1';

async function getAuthenticationToken() {
  try {
    const response = await axios.post('http://localhost:8199/healthcheck/v1/auth/authenticate', {
      email: "admindarlan@mail.com",
      password: "password"
    }, {
      headers: { 'Content-Type': 'application/json' }
    });
    console.log(response.data.access_token)
    return response.data.access_token;
  } catch (error) {
    console.error("Erro ao obter token de autenticação:", error);
    return null;
  }
}

async function getAuthHeader() {
  const token = await getAuthenticationToken();
  if (!token) throw new Error("Não foi possível obter o token de autenticação");
  return {
    headers: {
      Authorization: `Bearer ${token}`
    }
  };
}

export const ApiService = {
  fetchRecords: async () => {
    return axios.get(`${API_BASE_URL}/records`, await getAuthHeader());
  },
  createRecord: async (record) => {
    return axios.post(`${API_BASE_URL}/records`, record, await getAuthHeader());
  },
  updateRecord: async (id, record) => {
    return axios.put(`${API_BASE_URL}/records/${id}`, record, await getAuthHeader());
  },
  deleteRecord: async (id) => {
    return axios.delete(`${API_BASE_URL}/records/${id}`, await getAuthHeader());
  },

  fetchMetrics: async () => {
    return axios.get(`${API_BASE_URL}/metrics`, await getAuthHeader());
  },
  createMetric: async (metric) => {
    return axios.post(`${API_BASE_URL}/metrics`, metric, await getAuthHeader());
  },
  updateMetric: async (id, metric) => {
    return axios.put(`${API_BASE_URL}/metrics/${id}`, metric, await getAuthHeader());
  },
  deleteMetric: async (id) => {
    return axios.delete(`${API_BASE_URL}/metrics/${id}`, await getAuthHeader());
  },

  fetchModelAccuracies: async () => {
    return axios.get(`${API_BASE_URL}/model-accuracies`, await getAuthHeader());
  },
  createModelAccuracy: async (modelAccuracy) => {
    return axios.post(`${API_BASE_URL}/model-accuracies`, modelAccuracy, await getAuthHeader());
  },
  updateModelAccuracy: async (id, modelAccuracy) => {
    return axios.put(`${API_BASE_URL}/model-accuracies/${id}`, modelAccuracy, await getAuthHeader());
  },
  deleteModelAccuracy: async (id) => {
    return axios.delete(`${API_BASE_URL}/model-accuracies/${id}`, await getAuthHeader());
  },

  predictValue: async (data) => {
    return axios.post(`${PREDICT_API_BASE_URL}/predict-value`, data, await getAuthHeader());
  },

  predictAlert: async (data) => {
    return axios.post(`${PREDICT_API_BASE_URL}/predict-alert`, data, await getAuthHeader());
  },
};
