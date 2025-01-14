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
            image: "https://i.pinimg.com/736x/53/0e/68/530e6805edd40c7b25e27be219d919e6.jpg",
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
        {
            id: 6,
            name: "Original Artwork",
            category: "Original",
            price: 199.99,
            description: "Limited edition hand-painted artwork.",
            image: "https://i.pinimg.com/originals/95/92/56/959256de9c40038c8e5db8f7be0d02b2.jpg",
            stock: 5,
        },
        {
            id: 7,
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
                <div className="grid grid-cols-2 lg:grid-cols-4 auto-rows-fr gap-5 md:p-10 justify-start">
                    {products.map((product) => (
                        <div key={product.id} className="sm:p-6 w-full h-4/5 flex flex-col items-center">
                            <img src={product.image} className="w-full h-4/5"/>
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