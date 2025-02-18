import MainNewRelease from "./components/features/Main/MainNewRelease"
import MainPopularPieces from "./components/features/Main/MainPopularPieces"
import MainTrending from './components/features/Main/MainTrending'
import Navbar from "./components/Layout/Navbar"
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos'
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import { useState } from "react"
import { useUser } from "@/UserProvider"


function HomePage(): JSX.Element {
  const screens = [<MainNewRelease />, <MainPopularPieces />];
  const [currentScreenIndex, setCurrentScreenIndex] = useState(0);

  const {user} = useUser();

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
      <div className="h-full w-full flex flex-col">
        <Navbar user={user}/>
        <div className="h-3/5 sm:h-screen w-full overflow-hidden relative">
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
      <div className="h-32 s:h-56 text-black bg-slate-200 flex justify-center items-center">
        <h1 className="text-2xl">@2025 Yugen</h1>   
      </div>
    </>
  )
}

export default HomePage
