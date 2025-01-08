import MainNewRelease from "./components/MainNewRelease"
import MainPopularPieces from "./components/MainPopularPieces"
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos'
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import { useState } from "react"

function HomePage() {
  const screens = [<MainNewRelease />, <MainPopularPieces />];
  const [currentScreenIndex, setCurrentScreenIndex] = useState(1);

  const handlePrev = () => {
    setCurrentScreenIndex((prevIndex) =>
      prevIndex === 0 ? screens.length - 1 : prevIndex - 1
    );
  };

  const handleNext = () => {
    setCurrentScreenIndex((prevIndex) =>
      prevIndex === screens.length - 1 ? 0 : prevIndex + 1
    );
  };

  return (
    <>
      <nav className="h-16 absolute w-full top-0 z-10 text-white text-xl flex justify-between items-center bg-black/90">
        <h2 className="jusitfy-self-start ml-10">Yugen</h2>
        <ul className="flex justify-end gap-x-9 text-xl mr-10">
          <li>Home</li>
          <li>Products</li>
          <li>Artists</li>
        </ul>
      </nav>
      <div className="relative h-screen w-full">
        <div className="absolute top-1/2 left-0 right-0 z-30 flex justify-between px-10">
          <button
            onClick={handlePrev}
            className={currentScreenIndex === 1 ? "text-black text-5xl" : "text-white text-5xl"}
          >
            <ArrowBackIosIcon fontSize="inherit" color="inherit"/>
          </button>
          <button
            onClick={handleNext}
            className={currentScreenIndex === 1 ? "text-black text-5xl" : "text-white text-5xl"}
          >
            <ArrowForwardIosIcon fontSize="inherit" color="inherit"/>
          </button>
        </div>
        <div className="w-full h-full overflow-hidden">
          {screens[currentScreenIndex]}
        </div>
      </div>
    </>
  )
}

export default HomePage
