import Navbar from "./Navbar";
import { useParams } from "react-router-dom";
import { useState } from "react";

function CollectionPage() {
    const { collectionName } = useParams();

    const [products, setProducts] = useState([
        {
            id: 1,
            name: "Anime Figure",
            category: "Anime",
            price: 29.99,
            description: "High-quality anime figure with intricate details.",
            image: "https://i.pinimg.com/736x/fb/cc/ae/fbccae37175c5761b82f4367e183c34a.jpg",
            stock: 10,
        },
        {
            id: 2,
            name: "Gaming Mouse",
            category: "Games",
            price: 49.99,
            description: "Ergonomic gaming mouse with customizable buttons.",
            image: "https://upload-os-bbs.hoyolab.com/upload/2024/08/01/317655736/ed4a0f70ca2f008550cfe94cfc6ad0be_2084020113092877590.jpg",
            stock: 25,
        },
        {
            id: 3,
            name: "Original Artwork",
            category: "Original",
            price: 199.99,
            description: "Limited edition hand-painted artwork.",
            image: "https://i.pinimg.com/originals/95/92/56/959256de9c40038c8e5db8f7be0d02b2.jpg",
            stock: 5,
        },
        {
            id: 4,
            name: "Original Artwork",
            category: "Original",
            price: 199.99,
            description: "Limited edition hand-painted artwork.",
            image: "https://i.pinimg.com/originals/95/92/56/959256de9c40038c8e5db8f7be0d02b2.jpg",
            stock: 5,
        },
        {
            id: 5,
            name: "Original Artwork",
            category: "Original",
            price: 199.99,
            description: "Limited edition hand-painted artwork.",
            image: "https://i.pinimg.com/originals/95/92/56/959256de9c40038c8e5db8f7be0d02b2.jpg",
            stock: 5,
        },
    ]);

    return(
        <>
        <Navbar/>
        <div className="h-screen">
            <div className="flex flex-col items-center justify-center gap-4 p-8">
                <h1 className="text-4xl capitalize">
                    {collectionName} 
                </h1>
                <div className="grid lg:grid-cols-4 gap-5">
                    {products.map((product) => (
                        <div key={product.id} className="p-10 w-full h-full flex flex-col items-center">
                            <img src={product.image} className="h-5/6"/>
                            <h2>{product.name}</h2>
                            <h3>${product.price}</h3>
                        </div>
                    ))}
                </div>
            </div>
        </div>
        </>
    )
}

export default CollectionPage