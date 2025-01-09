import MainNewRelease from "./components/MainNewRelease"
import MainPopularPieces from "./components/MainPopularPieces"
import MainTrending from './components/MainTrending'
import Navbar from "./components/Navbar"
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos'
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import { useState } from "react"

function HomePage() {
  const screens = [<MainNewRelease />, <MainPopularPieces />];
  const [currentScreenIndex, setCurrentScreenIndex] = useState(0);

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
      <Navbar/>
      <div className="relative h-screen w-full">
        <div className="h-full w-full overflow-hidden relative">
          <div className="absolute top-1/2 left-0 right-0 z-30 flex justify-between px-10">
            <button
              onClick={handlePrev}
              className={currentScreenIndex === 1 ? "text-black text-3xl sm:text-5xl" : "text-white text-3xl sm:text-5xl"}
            >
              <ArrowBackIosIcon fontSize="inherit" color="inherit"/>
            </button>
            <button
              onClick={handleNext}
              className={currentScreenIndex === 1 ? "text-black text-3xl sm:text-5xl" : "text-white text-3xl sm:text-5xl"}
            >
              <ArrowForwardIosIcon fontSize="inherit" color="inherit"/>
            </button>
          </div>
          {screens[currentScreenIndex]} 
        </div>
      </div>
      <MainTrending/>
    </>
  )
}

export default HomePage
