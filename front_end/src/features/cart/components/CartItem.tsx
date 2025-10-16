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
  
  export default CartItem;