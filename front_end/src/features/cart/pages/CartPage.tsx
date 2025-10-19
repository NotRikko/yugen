import CartItem from "../components/CartItem";
import { useCartHook } from "../hooks/useCart";

const CartPage: React.FC = () => {
  const { cart, handleUpdateQuantity, handleRemove, totalPrice } = useCartHook();
  console.log(cart);
  return (
    <div className="p-8">
      <h2 className="text-2xl font-bold mb-4">Your Cart</h2>

      {cart && cart.items.length > 0 ? (
        <div className="flex flex-col gap-4">
          {cart.items.map((item) => (
            <CartItem
              key={item.productId}
              item={item} 
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