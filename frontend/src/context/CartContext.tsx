import React, { createContext, useContext, useState, ReactNode } from 'react';
import { Sweet, CartItem } from '../types';
import toast from 'react-hot-toast';

interface CartContextType {
  items: CartItem[];
  addToCart: (sweet: Sweet) => void;
  removeFromCart: (sweetId: number) => void;
  clearCart: () => void;
  cartTotal: number;
  itemCount: number;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [items, setItems] = useState<CartItem[]>([]);

  const addToCart = (sweet: Sweet) => {
    setItems(prev => {
      const existing = prev.find(item => item.sweetId === sweet.id);
      if (existing) {
        toast.success(`Updated ${sweet.name} quantity`);
        return prev.map(item => 
          item.sweetId === sweet.id 
            ? { ...item, quantity: item.quantity + 1 } 
            : item
        );
      }
      toast.success(`Added ${sweet.name} to cart`);
      return [...prev, { sweetId: sweet.id, name: sweet.name, price: sweet.price, quantity: 1 }];
    });
  };

  const removeFromCart = (sweetId: number) => {
    setItems(prev => prev.filter(item => item.sweetId !== sweetId));
    toast.success('Item removed');
  };

  const clearCart = () => setItems([]);

  const cartTotal = items.reduce((total, item) => total + (item.price * item.quantity), 0);
  const itemCount = items.reduce((count, item) => count + item.quantity, 0);

  return (
    <CartContext.Provider value={{ items, addToCart, removeFromCart, clearCart, cartTotal, itemCount }}>
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) throw new Error('useCart must be used within a CartProvider');
  return context;
};