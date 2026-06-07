import axios from 'axios';
import { API_BASE_URL, API_ENDPOINTS } from '../config/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Service pour l'admin
export const adminService = {
  getUsers: () =>
    apiClient.get(API_ENDPOINTS.ADMIN_USERS),

  getAgencies: () =>
    apiClient.get(API_ENDPOINTS.ADMIN_AGENCIES),

  getPendingAgencies: () =>
    apiClient.get(API_ENDPOINTS.ADMIN_AGENCIES_PENDING),

  approveAgency: (agencyId, comment) =>
    apiClient.post(
      API_ENDPOINTS.ADMIN_APPROVE_AGENCY.replace(':agencyId', agencyId),
      { comment }
    ),

  rejectAgency: (agencyId, reason) =>
    apiClient.post(
      API_ENDPOINTS.ADMIN_REJECT_AGENCY.replace(':agencyId', agencyId),
      { reason }
    ),

  suspendAgency: (agencyId, reason) =>
    apiClient.post(
      API_ENDPOINTS.ADMIN_SUSPEND_AGENCY.replace(':agencyId', agencyId),
      { reason }
    ),
};

export default adminService;
