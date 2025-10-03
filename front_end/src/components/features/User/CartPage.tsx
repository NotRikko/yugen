import React from "react";
import { useUser } from "@/UserProvider";

interface CartItemProps {
  productId: number;
  name: string;
  price: number;
  quantity: number;
  image?: string;
  onUpdateQuantity: (productId: number, newQuantity: number) => void;
  onRemove: (productId: number) => void;
}

const CartItem: React.FC<CartItemProps> = ({
  productId,
  name,
  price,
  quantity,
  image,
  onUpdateQuantity,
  onRemove,
}) => {
  return (
    <div className="flex items-center justify-between border-b py-4">
      {image && <img src={image} alt={name} className="w-16 h-16 object-cover rounded" />}
      <div className="flex-1 ml-4">
        <h3 className="font-semibold">{name}</h3>
        <p>${price.toFixed(2)}</p>
      </div>
      <div className="flex items-center gap-2">
        <input
          type="number"
          min={1}
          value={quantity}
          onChange={(e) => onUpdateQuantity(productId, parseInt(e.target.value))}
          className="w-16 border rounded px-2 py-1"
        />
        <button
          onClick={() => onRemove(productId)}
          className="px-2 py-1 bg-red-500 text-white rounded"
        >
          Remove
        </button>
      </div>
    </div>
  );
};

const CartPage: React.FC = () => {
  const { cart, updateCartItem, removeFromCart } = useUser();

  const handleUpdateQuantity = (productId: number, newQuantity: number) => {
    if (newQuantity <= 0) return;
    updateCartItem(productId, newQuantity);
  };

  const handleRemove = (productId: number) => {
    removeFromCart(productId);
  };

  const totalPrice = cart?.items.reduce((sum, item) => sum + item.quantity * (item.product?.price || 0), 0) || 0;

  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold mb-4">Your Cart</h2>
      {cart && cart.items.length > 0 ? (
        <div className="flex flex-col gap-4">
          {cart.items.map((item) => (
            <CartItem
              key={item.productId}
              productId={item.productId}
              name={item.product?.name || "Product"}
              price={item.product?.price || 0}
              quantity={item.quantity}
              image={item.product?.image}
              onUpdateQuantity={handleUpdateQuantity}
              onRemove={handleRemove}
            />
          ))}
          <div className="flex justify-end mt-4 text-xl font-semibold">
            Total: ${totalPrice.toFixed(2)}
          </div>
        </div>
      ) : (
        <p>Your cart is empty.</p>
      )}
    </div>
  );
};

export default CartPage;