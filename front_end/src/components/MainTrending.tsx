function MainTrending() {
    return(
        <div className="w-full h-4/5 s:h-full lg:h-screen flex flex-col items-center  gap-5 overflow-hidden">
            <h1 className="text-2xl">Trending Pieces</h1>
            <div className="w-4/5 flex justify-center gap-5">
                {[...Array(4)].map((_, index) => (
                    <div key={index} className="w-1/2 aspect-square">
                        <img className="h-full w-full object-cover rounded-md overflow-hidden" src="https://i.pinimg.com/736x/a5/ff/46/a5ff4626697504cb24acc1b1139b5f6c.jpg" />
                        <div>
                        <h2>Firefly</h2>
                        <h2>Hoyoverse</h2>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default MainTrending