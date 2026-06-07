// Configuration centralisée de l'API
export const API_BASE_URL = 'https://urchin-app-2xvml.ondigitalocean.app';

export const API_ENDPOINTS = {
  // Auth
  LOGIN: '/auth/login',
  LOGOUT: '/auth/logout',
  REGISTER_CLIENT: '/auth/register/client',
  REGISTER_AGENCY: '/auth/register/agency',

  // Email verification
  VERIFY_EMAIL: '/verification/verify-email',
  RESEND_VERIFICATION: '/verification/resend',

  // Profile
  PROFILE_ME: '/profile/me',
  PROFILE_USER: '/profile/user',
  PROFILE_ADMIN: '/profile/admin',
  PROFILE_AGENCY_ADMIN: '/profile/agency-admin',

  // Public vehicles
  PUBLIC_VEHICLES: '/public/vehicles',
  PUBLIC_VEHICLE_DETAIL: '/public/vehicles/:carId',
  PUBLIC_VEHICLE_PHOTOS: '/public/vehicles/:carId/photos',

  // Client bookings
  CREATE_BOOKING: '/bookings',
  MY_BOOKINGS: '/bookings/my',

  // Agencies
  AGENCIES_LIST: '/agencies',
  AGENCY_DETAIL: '/agencies/:agencyId',
  AGENCY_VEHICLES: '/agencies/:agencyId/vehicles',
  AGENCY_VEHICLE_DETAIL: '/agencies/:agencyId/vehicles/:carId',
  AGENCY_VEHICLE_PHOTOS: '/agencies/:agencyId/vehicles/:carId/photos',
  AGENCY_VEHICLE_PHOTOS_DETAIL: '/agencies/:agencyId/vehicles/:carId/photos/:photoId',

  // Admin
  ADMIN_VEHICLES: '/admin/vehicles',
  ADMIN_VEHICLE_DETAIL: '/admin/vehicles/:carId',
  ADMIN_VEHICLE_STATUS: '/admin/vehicles/:carId/status',
  ADMIN_USERS: '/admin/users',
  ADMIN_AGENCIES: '/admin/agencies',
  ADMIN_AGENCIES_PENDING: '/admin/agencies/pending',
  ADMIN_APPROVE_AGENCY: '/admin/agencies/:agencyId/approve',
  ADMIN_REJECT_AGENCY: '/admin/agencies/:agencyId/reject',
  ADMIN_SUSPEND_AGENCY: '/admin/agencies/:agencyId/suspend',

};
