// AUTH
export interface RegisterRequest {
  username: string;
  password: string;
  role: 'USER' | 'ADMIN';
}

export interface AuthResponse {
  token: string;
  username: string;
  role: 'USER' | 'ADMIN';
}

// SWEETS
export interface Sweet {
  id: number;
  name: string;
  category: string;
  price: number;
  quantity: number;
}

// ORDERS
export interface OrderItemRequest {
  sweetId: number;
  quantity: number;
}

export interface OrderRequest {
  items: OrderItemRequest[];
}

export interface OrderResponse {
  id: number;
  totalPrice: number; // Changed from totalAmount
  orderDate: string;  // Changed from createdAt
}

// ERROR
export interface ApiError {
  message: string;
}