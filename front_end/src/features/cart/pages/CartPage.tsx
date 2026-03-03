import CartItem from "../components/CartItem";
import { useCart } from "../useCartContext";

const CartPage: React.FC = () => {
  const { cart, updateQuantity, removeFromCart, totalPrice } = useCart();

  const validItems = cart?.items?.filter(
    (item) => item.id != null && item.product && item.product.price != null
  ) ?? [];

  return (
    <div className="py-12 px-24">
      <h2 className="text-2xl font-bold mb-4">Your Cart</h2>

      {validItems.length > 0 ? (
        <div className="flex flex-col gap-4">
          {validItems.map((item) => (
            <CartItem
              key={item.id}
              item={item}
              onUpdateQuantity={updateQuantity}
              onRemove={removeFromCart}
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