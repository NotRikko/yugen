import Navbar from "../components/Navbar"
import { NavLink } from 'react-router-dom';


function ProductsMain() {
    return (
        <>
        <Navbar/>
        
        <div className="flex flex-col h-[calc(100vh-4rem)]">
            <div className="lg:h-full flex flex-col justify-center items-center sm:gap-3 p-4">
                <h1 className="text-2xl lg:text-3xl">Collections</h1>
                <div className="grid grid-auto-rows lg:grid-cols-3 items-center justify-center px-3 py-0 md:p-12 md:py-0 ">
                    <div className="flex flex-col items-center justify-center gap-2 aspect-square">
                        <NavLink to="/collections/anime" className="flex flex-col items-center justify-center w-full">
                        <img className="aspect-square w-3/4"src="https://i.pinimg.com/736x/fb/cc/ae/fbccae37175c5761b82f4367e183c34a.jpg" />
                        </NavLink>
                        <h2 className="text-xl">Anime</h2>
                    </div>
                    <div className=" flex flex-col items-center justify-center gap-2 aspect-square">
                        <NavLink to="/collections/games" className="flex flex-col items-center justify-center w-full">
                        <img className="aspect-square w-3/4" src="https://upload-os-bbs.hoyolab.com/upload/2024/08/01/317655736/ed4a0f70ca2f008550cfe94cfc6ad0be_2084020113092877590.jpg?x-oss-process=image%2Fresize%2Cs_1000%2Fauto-orient%2C0%2Finterlace%2C1%2Fformat%2Cwebp%2Fquality%2Cq_70"/>
                        </NavLink>
                        <h2 className="text-xl">Games</h2>
                    </div>
                    <div className="flex flex-col items-center justify-center gap-2 aspect-square">
                        <NavLink to="/collections/original" className="flex flex-col items-center justify-center w-full">
                        <img className="aspect-square w-3/4" src="https://i.pinimg.com/originals/95/92/56/959256de9c40038c8e5db8f7be0d02b2.jpg"/>
                        </NavLink>
                        <h2 className="text-xl">Original</h2>
                    </div>
                </div>
            </div>
        </div>
        </>
    )
}

export default ProductsMain