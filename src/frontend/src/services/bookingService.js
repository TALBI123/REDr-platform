import axios from 'axios';
import { API_BASE_URL, API_ENDPOINTS } from '../config/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Service pour les réservations
export const bookingService = {
  getMyBookings: () =>
    apiClient.get(API_ENDPOINTS.MY_BOOKINGS),

  create: (data) =>
    apiClient.post(API_ENDPOINTS.CREATE_BOOKING, data),

  createBooking: (data) =>
    apiClient.post(API_ENDPOINTS.CREATE_BOOKING, data),

  updateBooking: (bookingId, data) =>
    apiClient.put(`/bookings/${bookingId}`, data),

  cancelBooking: (bookingId) =>
    apiClient.delete(`/bookings/${bookingId}`),
};

export default bookingService;
