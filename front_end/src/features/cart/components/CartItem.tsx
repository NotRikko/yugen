import type { CartItem } from "../types/cartTypes";

interface CartItemProps {
  item: CartItem;
  onUpdateQuantity: (productId: number, newQuantity: number) => void;
  onRemove: (cartItemId: number) => void;
}
  
const CartItem: React.FC<CartItemProps> = ({ item, onUpdateQuantity, onRemove }) => {
  const { id, product, quantity } = item;

  return (
    <div className="flex items-center justify-between border-b py-4">
      {product.image && (
        <img src={product.image} alt={product.name} className="w-16 h-16 object-cover rounded" />
      )}
      <div className="flex-1 ml-4">
        <h3 className="font-semibold">{product.name}</h3>
        <p>${product.price.toFixed(2)}</p>
      </div>
      <div className="flex items-center gap-2">
        <input
          type="number"
          min={1}
          value={quantity}
          onChange={(e) => onUpdateQuantity(id, parseInt(e.target.value))}
          className="w-16 border rounded px-2 py-1"
        />
        <button
          onClick={() => onRemove(id)}
          className="px-2 py-1 bg-red-500 text-white rounded"
        >
          Remove
        </button>
      </div>
    </div>
  );
};

export default CartItem;