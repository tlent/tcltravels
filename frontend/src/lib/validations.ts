import { z } from 'zod';

// Auth schemas
export const loginSchema = z.object({
  email: z.string().email('Please enter a valid email address'),
  password: z.string().min(6, 'Password must be at least 6 characters'),
});

export const registerSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().min(1, 'Last name is required'),
  address: z.string().min(1, 'Address is required'),
  city: z.string().min(1, 'City is required'),
  state: z
    .string()
    .length(2, 'State must be 2 characters')
    .regex(/^[A-Z]{2}$/, 'State must be uppercase letters'),
  zipcode: z
    .string()
    .length(5, 'ZIP code must be 5 digits')
    .regex(/^\d{5}$/, 'ZIP code must contain only digits'),
  telephone: z.string().min(1, 'Phone number is required'),
  email: z.string().email('Please enter a valid email address'),
  password: z.string().min(6, 'Password must be at least 6 characters'),
  confirmPassword: z.string(),
}).refine((data) => data.password === data.confirmPassword, {
  message: "Passwords don't match",
  path: ['confirmPassword'],
});

// User profile schema
export const updateProfileSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().min(1, 'Last name is required'),
  address: z.string().min(1, 'Address is required'),
  city: z.string().min(1, 'City is required'),
  state: z
    .string()
    .length(2, 'State must be 2 characters')
    .regex(/^[A-Z]{2}$/, 'State must be uppercase letters'),
  zipcode: z
    .string()
    .length(5, 'ZIP code must be 5 digits')
    .regex(/^\d{5}$/, 'ZIP code must contain only digits'),
  telephone: z.string().min(1, 'Phone number is required'),
  currentPassword: z.string().min(1, 'Current password is required to make changes'),
});

// Passenger schema for reservations
export const passengerSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().min(1, 'Last name is required'),
  seatClass: z.enum(['Economy', 'Business', 'First'], {
    message: 'Please select a seat class',
  }),
  meal: z.string().optional(),
});

// New reservation schema
export const newReservationSchema = z.object({
  origin: z.number().min(1, 'Please select origin'),
  destination: z.number().min(1, 'Please select destination'),
  departureDate: z.string().min(1, 'Departure date is required'),
  other: z.string().optional(),
  passengers: z
    .array(passengerSchema)
    .min(1, 'At least one passenger is required')
    .max(5, 'Maximum 5 passengers allowed'),
}).refine((data) => data.destination > data.origin, {
  message: 'Destination must be after origin',
  path: ['destination'],
});

// Place bid schema
export const placeBidSchema = z.object({
  departureDate: z.string().min(1, 'Departure date is required'),
  flightClass: z.enum(['Economy', 'Business', 'First'], {
    message: 'Please select a class',
  }),
  bid: z
    .number()
    .min(0.01, 'Bid amount must be greater than 0')
    .max(999999.99, 'Bid amount is too large'),
  food: z.string().optional(),
  other: z.string().optional(),
});

// Employee schemas (for admin)
export const employeeCreateSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().min(1, 'Last name is required'),
  address: z.string().min(1, 'Address is required'),
  city: z.string().min(1, 'City is required'),
  state: z
    .string()
    .length(2, 'State must be 2 characters')
    .regex(/^[A-Z]{2}$/, 'State must be uppercase letters'),
  zipcode: z
    .string()
    .length(5, 'ZIP code must be 5 digits')
    .regex(/^\d{5}$/, 'ZIP code must contain only digits'),
  telephone: z.string().min(1, 'Phone number is required'),
  ssn: z
    .string()
    .regex(/^\d{9}$/, 'SSN must be 9 digits (no dashes)'),
  startDate: z.string().min(1, 'Start date is required'),
  hourlyRate: z
    .string()
    .regex(/^\d+(\.\d{1,2})?$/, 'Hourly rate must be a valid number'),
  isManager: z.boolean(),
  email: z.string().email('Please enter a valid email address'),
  password: z.string().min(6, 'Password must be at least 6 characters'),
});

export const employeeEditSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().min(1, 'Last name is required'),
  address: z.string().min(1, 'Address is required'),
  city: z.string().min(1, 'City is required'),
  state: z
    .string()
    .length(2, 'State must be 2 characters')
    .regex(/^[A-Z]{2}$/, 'State must be uppercase letters'),
  zipcode: z
    .string()
    .length(5, 'ZIP code must be 5 digits')
    .regex(/^\d{5}$/, 'ZIP code must contain only digits'),
  telephone: z.string().min(1, 'Phone number is required'),
  ssn: z
    .string()
    .regex(/^\d{9}$/, 'SSN must be 9 digits (no dashes)'),
  startDate: z.string().min(1, 'Start date is required'),
  hourlyRate: z
    .string()
    .regex(/^\d+(\.\d{1,2})?$/, 'Hourly rate must be a valid number'),
  isManager: z.boolean(),
});

// Type exports for TypeScript
export type LoginFormData = z.infer<typeof loginSchema>;
export type RegisterFormData = z.infer<typeof registerSchema>;
export type UpdateProfileFormData = z.infer<typeof updateProfileSchema>;
export type NewReservationFormData = z.infer<typeof newReservationSchema>;
export type PlaceBidFormData = z.infer<typeof placeBidSchema>;
export type PassengerFormData = z.infer<typeof passengerSchema>;
export type EmployeeCreateFormData = z.infer<typeof employeeCreateSchema>;
export type EmployeeEditFormData = z.infer<typeof employeeEditSchema>;
