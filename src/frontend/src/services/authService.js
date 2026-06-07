import axios from 'axios';
import { API_BASE_URL, API_ENDPOINTS } from '../config/api';

// Instance axios avec credentials pour les cookies
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true, // Important pour les cookies JWT
  headers: {
    'Content-Type': 'application/json',
  },
});

// Service d'authentification
export const authService = {
  login: (email, password) =>
    apiClient.post(API_ENDPOINTS.LOGIN, { email, password }),

  logout: () =>
    apiClient.post(API_ENDPOINTS.LOGOUT),

  registerClient: (data) =>
    apiClient.post(API_ENDPOINTS.REGISTER_CLIENT, data),

  registerAgency: (data) =>
    apiClient.post(API_ENDPOINTS.REGISTER_AGENCY, data),

  verifyEmail: (token) =>
    apiClient.get(API_ENDPOINTS.VERIFY_EMAIL, { params: { token } }),

  resendVerification: (email) =>
    apiClient.post(API_ENDPOINTS.RESEND_VERIFICATION, { email }),

  getCurrentUser: () =>
    apiClient.get(API_ENDPOINTS.PROFILE_ME),

  getProfile: (role) => {
    const endpoint =
      role === 'CLIENT'
        ? API_ENDPOINTS.PROFILE_USER
        : role === 'AGENCY_MANAGER'
          ? API_ENDPOINTS.PROFILE_AGENCY_ADMIN
          : API_ENDPOINTS.PROFILE_ADMIN;

    return apiClient.get(endpoint);
  },
};

export default apiClient;
