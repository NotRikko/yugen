import Navbar from "../components/Navbar"


function ProductsMain() {
    return (
        <div className="flex flex-col h-screen">
            <Navbar/>
            <div className="md:h-full flex flex-col justify-center items-center p-4 lg:p-28">
                <h1 className="text-lg lg:text-3xl">Collections</h1>
                <div className="grid grid-auto-rows md:grid-cols-3 gap-y-5 gap-x-11 items-center justify-center p-5">
                    <div className="flex flex-col items-center aspect-square">
                        <img className="aspect-square"src="https://i.pinimg.com/736x/fb/cc/ae/fbccae37175c5761b82f4367e183c34a.jpg " />
                        <h2 className="text-xl">Anime</h2>
                    </div>
                    <div className=" flex flex-col items-center aspect-square	">
                        <img className="aspect-square" src="https://upload-os-bbs.hoyolab.com/upload/2024/08/01/317655736/ed4a0f70ca2f008550cfe94cfc6ad0be_2084020113092877590.jpg?x-oss-process=image%2Fresize%2Cs_1000%2Fauto-orient%2C0%2Finterlace%2C1%2Fformat%2Cwebp%2Fquality%2Cq_70"/>
                        <h2 className="text-xl">Games</h2>
                    </div>
                    <div className="flex flex-col items-center aspect-square	">
                        <img className="aspect-square" src="https://i.pinimg.com/originals/95/92/56/959256de9c40038c8e5db8f7be0d02b2.jpg"/>
                        <h2 className="text-xl">Original</h2>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ProductsMain