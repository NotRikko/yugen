function MainNewRelease() {
    return (
        <div className="w-full h-screen overflow-hidden flex justify-center">
        <img className="relative w-full" src="https://pbs.twimg.com/media/GgDj3-JawAAJAL0?format=jpg&name=large"/>
        <div className="absolute text-white absolute bottom-[8%] left-1/2 transform -translate-x-1/2 flex flex-col items-center gap-4">
          <h2 className="text-6xl font-bold text-center">New release from Yueko</h2>
          <button className="w-1/4 p-2.5 text-2xl bottom 0 text-white bg-black/70 hover:bg-white/65 hover:text-black">View</button>
        </div>
      </div>
    )
}

export default MainNewRelease
