import Navbar from '../components/Navbar'
import { useState } from 'react'

function ArtistsMain() {
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
    return (
        <>
            <Navbar/>
            <div className="w-full h-full flex flex-col">
                <div className="h-full self-center flex flex-col items-center">
                    <h2>Featured Artist</h2>
                    <div className="w-3/4 flex items-center p-28">
                        <img className="aspect-square h-3/4"src={products[0].image}/>
                        <div className="p-14">
                            <h3>Artist Name</h3>
                            <p>A short description is a brief summary of a topic that conveys the most important information in a clear and concise way. It can be used for books, movies, products, or other topics.</p>
                        </div>
                    </div>
                </div>
                <div className="h-full w-full">
                    <div>
                        <ul>
                            <li>All</li>
                            <li>Trending</li>
                            <li>New</li>
                        </ul>
                    </div>
                    <div className="grid">
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

export default ArtistsMain