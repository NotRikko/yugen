import { Heart, MessageCircle, ShoppingBag, Share2 } from "lucide-react"
import { useState } from "react"
import { useUser } from "@/UserProvider";


interface Like {
    userId: number;
    likedAt: string;
  }
  
  interface Comment {
    userId: number;
    content: string;
    postedAt: string;
  }

  interface Product {
    id: number;
    name: string;
    price: number;
  }  
  
  interface Post {
    id: number;
    content: string;
    likes: Like[]; 
    comments: Comment[];
    product?: Product;
  }
  
  interface PostFooterProps {
    post: Post;
  }

const PostFooter = ({ post }: PostFooterProps) => {
    const {user} = useUser();
    const [likes, setLikes] = useState(post.likes ? post.likes.length : 0);
    const [liked, setLiked] = useState(false);

    const handleLike = async () => {
      try {
          const response = await fetch(`http://localhost:8080/posts/${post.id}/like`, {
              mode: "cors",
              method: "POST",
              headers: {
                  "Content-Type": "application/json",
              },
              body: JSON.stringify({ userId: user.id }),
          });

          if (response.ok) {
              const data = await response.json();
              setLikes(data.likes); 
              setLiked(data.liked); 
          } else {
              console.error("Failed to update like");
          }
      } catch (error) {
          console.error("Error liking post:", error);
      }
  };

  const handlePurchase = async () => {
    if (!post.product) {
      console.warn("No product associated with this post");
      return;
    }
  
    try {
      const token = localStorage.getItem("accessToken");
      const response = await fetch(`http://localhost:8080/cart/add?productId=${post.product.id}&quantity=1`, {
        mode: "cors",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
      });
  
      if (response.ok) {
        const cart = await response.json(); 
        console.log("Updated cart:", cart);
        alert(`Added ${post.product.name} to your cart! Total items: ${cart.items.length}`);
      } else {
        console.error("Failed to add product to cart");
        const errData = await response.json();
        console.error(errData);
      }
    } catch (error) {
      console.error("Error purchasing product:", error);
    }
  };

  

    return (
      <div className="flex justify-between">
        <div className="flex items-center gap-2 text-gray-600 text-base">
                <button onClick={handleLike}>
                    <Heart className={`w-6 h-6 ${liked ? "text-red-500" : ""}`} />
                </button>
                <span>{likes}</span>
          </div>
        <div className="flex items-center gap-2 text-gray-600 text-base">
            <button><MessageCircle className="w-6 h-6" /></button>
            <span>{post.comments ? post.comments.length  : 0}</span>
        </div>
        <div className="flex items-center gap-2 text-gray-600 text-base">
            <button><Share2 className="w-6 h-6" /></button>
            <span>{post.comments ? post.comments.length  : 0}</span>
        </div>
        <div className="flex items-center gap-2 text-gray-600 text-base">
            <button onClick={handlePurchase}><ShoppingBag className="w-6 h-6" /></button>
            <span>{post.comments ? post.comments.length  : 0}</span>
        </div>
      </div>
    );
  };

  export default PostFooter