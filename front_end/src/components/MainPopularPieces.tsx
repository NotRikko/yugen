function MainPopularPieces() {
    return (
        <div className="w-full h-screen overflow-hidden flex justify-center items-center mt-[-2rem]">
          <div className="w-10/12 flex flex-col items-center h-5/6 relative">
            <div className="flex flex-1 items-center gap-14  w-2/5 z-10 absolute left-72 h-2/5">
              <img className="h-full w-5/6 object-cover shadow-xl hover:scale-110 transition-transform duration-300 ease-in-out" src="https://i.redd.it/5t43owoaw3g91.jpg"/>
              <div>
                <h3 className="whitespace-nowrap">Miku</h3>
                <h3 className="whitespace-nowrap">Matcha</h3>
              </div>
            </div>
            <div className="flex flex-row-reverse gap-10 items-center w-2/5 z-20 absolute top-1/2 right-72 transform -translate-y-1/2 h-2/5">
              <img className="h-full w-5/6 object-cover shadow-xl hover:scale-110 transition-transform duration-300 ease-in-out" src="https://w0.peakpx.com/wallpaper/271/9/HD-wallpaper-video-game-genshin-impact-inazuma-genshin-impact.jpg"/> 
              <div>
                <h3 className="whitespace-nowrap">Inazuma</h3>
                <h3 className="whitespace-nowrap">Matcha</h3>
              </div>
            </div>
            <div className="flex items-center gap-10 w-2/5  z-10 absolute left-72 bottom-0 h-2/5">
              <img className="h-full w-5/6 object-cover shadow-xl hover:scale-110 transition-transform duration-250 ease-in-out" src="https://getlivewall.com/wp-content/uploads/2023/08/stelle-honkai-star-rail-thumbnail-1500x844.jpg"/>
              <div className="w-full">
                <h3 className="whitespace-nowrap">Star Rail</h3>
                <h3 className="whitespace-nowrap">Matcha</h3>
              </div>
            </div>
          </div>
        </div>
    )
}

export default MainPopularPieces
