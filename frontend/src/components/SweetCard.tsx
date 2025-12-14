import React from 'react';
import { Sweet } from '../types';
import { useCart } from '../context/CartContext';
import { motion } from 'framer-motion';
import { Plus, ShoppingBag } from 'lucide-react';

interface SweetCardProps {
  sweet: Sweet;
}

const SweetCard: React.FC<SweetCardProps> = ({ sweet }) => {
  const { addToCart } = useCart();

  return (
    <motion.div 
      whileHover={{ y: -5 }}
      className="bg-white rounded-2xl shadow-lg overflow-hidden border border-gray-100 group"
    >
      <div className="h-48 bg-primary-50 relative overflow-hidden">
        {/* Placeholder Image Logic */}
        <div className="absolute inset-0 flex items-center justify-center text-primary-200 text-6xl font-black opacity-20 group-hover:scale-110 transition-transform duration-500">
          {sweet.name.charAt(0)}
        </div>
        <div className="absolute top-3 right-3 bg-white/90 backdrop-blur px-3 py-1 rounded-full text-xs font-bold text-chocolate-900 shadow-sm">
          {sweet.category}
        </div>
      </div>

      <div className="p-5">
        <div className="flex justify-between items-start mb-2">
          <h3 className="text-xl font-bold text-gray-800">{sweet.name}</h3>
          <span className="text-primary-600 font-bold text-lg">₹{sweet.price}</span>
        </div>
        
        <p className="text-gray-500 text-sm mb-4 line-clamp-2">
          Delicious traditional {sweet.category} made with pure ingredients.
        </p>

        <div className="flex justify-between items-center">
          <span className={`text-xs font-medium px-2 py-1 rounded ${sweet.quantity > 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
            {sweet.quantity > 0 ? `${sweet.quantity} in stock` : 'Out of Stock'}
          </span>

          <button
            onClick={() => addToCart(sweet)}
            disabled={sweet.quantity === 0}
            className="flex items-center gap-2 bg-chocolate-900 text-white px-4 py-2 rounded-lg hover:bg-primary-600 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <Plus size={16} /> Add
          </button>
        </div>
      </div>
    </motion.div>
  );
};

export default SweetCard;