import axios from 'axios';

// Initialize Axios instance with base URL
const API_BASE_URL = 'http://localhost:8199/healthcheck/v1/gate';
const AUTH_API_BASE_URL = 'http://localhost:8199/healthcheck/v1/auth';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: { 'Content-Type': 'application/json' }
});

// Variable to store the token fetching promise
let fetchingTokenPromise = null;

// Function to fetch authentication token
async function fetchAuthToken() {
    if (!fetchingTokenPromise) {
        fetchingTokenPromise = axios.post(`${AUTH_API_BASE_URL}/authenticate`, {
            email: "admindarlan1231@mail.com",
            password: "password"
        }).then(response => {
            const { access_token } = response.data;
            localStorage.setItem('authToken', access_token);
            fetchingTokenPromise = null; // Reset promise after fetching
            return access_token;
        }).catch(error => {
            fetchingTokenPromise = null; // Reset promise if there's an error
            throw error;
        });
    }
    return fetchingTokenPromise;
}

// Function to get the authentication token from local storage or fetch a new one
async function getAuthToken() {
    let token = localStorage.getItem('authToken');
    if (!token) {
        token = await fetchAuthToken();
    }
    return token;
}

// Request interceptor to append Authorization header to every request
api.interceptors.request.use(async config => {
    const token = await getAuthToken();
    config.headers.Authorization = `Bearer ${token}`;
    return config;
}, error => Promise.reject(error));

// Response interceptor to handle token expiration
api.interceptors.response.use(response => response, async error => {
    const originalRequest = error.config;
    if (error.response.status === 403 && !originalRequest._retry) {
        originalRequest._retry = true;
        const newToken = await fetchAuthToken();
        localStorage.setItem('authToken', newToken);
        originalRequest.headers['Authorization'] = `Bearer ${newToken}`;
        return api(originalRequest);
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
