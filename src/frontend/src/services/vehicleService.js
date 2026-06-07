import axios from 'axios';
import { API_BASE_URL, API_ENDPOINTS } from '../config/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Service pour les véhicules publics
export const vehicleService = {
  // Véhicules publics (sans authentification)
  getPublicVehicles: (filters = {}) =>
    apiClient.get(API_ENDPOINTS.PUBLIC_VEHICLES, { params: filters }),

  getPublicVehicleDetail: (carId) =>
    apiClient.get(API_ENDPOINTS.PUBLIC_VEHICLE_DETAIL.replace(':carId', carId)),

  getPublicVehiclePhotos: (carId) =>
    apiClient.get(API_ENDPOINTS.PUBLIC_VEHICLE_PHOTOS.replace(':carId', carId)),

  // Véhicules admin (authentification requise)
  getAdminVehicles: () =>
    apiClient.get(API_ENDPOINTS.ADMIN_VEHICLES),

  getAdminVehicleDetail: (carId) =>
    apiClient.get(API_ENDPOINTS.ADMIN_VEHICLE_DETAIL.replace(':carId', carId)),

  createVehicle: (data) =>
    apiClient.post(API_ENDPOINTS.ADMIN_VEHICLES, data),

  updateVehicle: (carId, data) =>
    apiClient.put(API_ENDPOINTS.ADMIN_VEHICLE_DETAIL.replace(':carId', carId), data),

  updateVehicleStatus: (carId, data) =>
    apiClient.patch(API_ENDPOINTS.ADMIN_VEHICLE_STATUS.replace(':carId', carId), data),

  deleteVehicle: (carId) =>
    apiClient.delete(API_ENDPOINTS.ADMIN_VEHICLE_DETAIL.replace(':carId', carId)),

  // Véhicules par agence
  getAgencyVehicles: (agencyId) =>
    apiClient.get(API_ENDPOINTS.AGENCY_VEHICLES.replace(':agencyId', agencyId)),

  getAgencyVehicleDetail: (agencyId, carId) =>
    apiClient.get(
      API_ENDPOINTS.AGENCY_VEHICLE_DETAIL
        .replace(':agencyId', agencyId)
        .replace(':carId', carId)
    ),

  createAgencyVehicle: (agencyId, data) =>
    apiClient.post(API_ENDPOINTS.AGENCY_VEHICLES.replace(':agencyId', agencyId), data),

  updateAgencyVehicle: (agencyId, carId, data) =>
    apiClient.put(
      API_ENDPOINTS.AGENCY_VEHICLE_DETAIL
        .replace(':agencyId', agencyId)
        .replace(':carId', carId),
      data
    ),

  deleteAgencyVehicle: (agencyId, carId) =>
    apiClient.delete(
      API_ENDPOINTS.AGENCY_VEHICLE_DETAIL
        .replace(':agencyId', agencyId)
        .replace(':carId', carId)
    ),

  // Photos
  uploadPhoto: (agencyId, carId, formData) =>
    apiClient.post(
      API_ENDPOINTS.AGENCY_VEHICLE_PHOTOS
        .replace(':agencyId', agencyId)
        .replace(':carId', carId),
      formData,
      { headers: { 'Content-Type': 'multipart/form-data' } }
    ),

  updatePhoto: (agencyId, carId, photoId, data) =>
    apiClient.patch(
      API_ENDPOINTS.AGENCY_VEHICLE_PHOTOS_DETAIL
        .replace(':agencyId', agencyId)
        .replace(':carId', carId)
        .replace(':photoId', photoId),
      data
    ),

  deletePhoto: (agencyId, carId, photoId) =>
    apiClient.delete(
      API_ENDPOINTS.AGENCY_VEHICLE_PHOTOS_DETAIL
        .replace(':agencyId', agencyId)
        .replace(':carId', carId)
        .replace(':photoId', photoId)
    ),
};

export default vehicleService;
