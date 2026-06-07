import apiClient from './authService';
import { API_ENDPOINTS } from '../config/api';

export const photoService = {
  upload: (agencyId, carId, formData) =>
    apiClient.post(
      API_ENDPOINTS.AGENCY_VEHICLE_PHOTOS
        .replace(':agencyId', agencyId)
        .replace(':carId', carId),
      formData,
      { headers: { 'Content-Type': 'multipart/form-data' } }
    ),
};

export default photoService;
