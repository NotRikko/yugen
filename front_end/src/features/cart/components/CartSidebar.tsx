import { useUser } from "@/features/user/UserProvider";
import { X } from "lucide-react";

interface CartSidebarProps {
  onClose: () => void;
}

export default function CartSidebar({ onClose }: CartSidebarProps) {
  const { cart } = useUser();

  return (
    <div className="w-full min-h-screen bg-white px-8 py-8">
      <div className="flex justify-between items-center p-4 border-b">
        <h2 className="text-xl font-semibold">Your Cart</h2>
        <button onClick={onClose}>
          <X className="w-6 h-6" />
        </button>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-3">
        {cart?.items.length ? (
          cart.items.map((item) => (
            <div key={item.productId} className="flex justify-between items-center border-b pb-2">
              <div className="flex flex-col">
                <span className="font-medium">{item.product.name}</span>
                <span className="text-sm text-gray-500">Qty: {item.quantity}</span>
              </div>
              <span className="font-semibold">${(item.product.price * item.quantity).toFixed(2)}</span>
            </div>
          ))
        ) : (
          <p className="text-gray-500">Your cart is empty</p>
        )}
      </div>

      {cart?.items.length ? (
        <div className="p-4 border-t">
          <button className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700">
            Checkout
          </button>
        </div>
      ) : null}
    </div>
  );
}