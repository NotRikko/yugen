import ShoppingBagIcon from '@mui/icons-material/ShoppingBag';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { useState, useEffect } from "react";
import { NavLink } from 'react-router-dom';
import UserSidebar from './UserSidebar';



function Navbar() {
    const [isMobile, setIsMobile] = useState(false);
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [viewingAccountBar, setIsViewingAccountBar] = useState(false);

    useEffect(() => {
        const checkScreenSize = () => {
          setIsMobile(window.innerWidth <= 600); 
        };
    
        checkScreenSize();
        window.addEventListener("resize", checkScreenSize);
    
        return () => window.removeEventListener("resize", checkScreenSize);
      }, []);

    const handleViewingAccountBar = () => {
        setIsViewingAccountBar(prevViewingAccountBar => !prevViewingAccountBar);
    };

    return(
        <nav className="sticky top-0 z-50 h-16 w-full top-0 z-10 text-white text-xl flex justify-between items-center bg-black/90">
            <NavLink to="/"><h2 className="jusitfy-self-start ml-10">Yugen</h2></NavLink>
            {
                isMobile ? (
                    <>
                    <button
                        className="mr-5"
                        onClick={() => setIsMenuOpen(!isMenuOpen)}
                    >
                        ☰
                    </button>
                    {isMenuOpen && (
                        <ul className="flex flex-col  md:text-xl mr-10">
                            <li>Feed</li>
                            <NavLink to="/collections"><li>Collections</li></NavLink>
                            <NavLink to="/artists"><li>Artists</li></NavLink>
                            <li onClick={handleViewingAccountBar}><AccountCircleIcon/></li>
                            <li><ShoppingBagIcon/></li>
                        </ul>
                    )}
                    
                    </>
                ) : (
                    <ul className="flex justify-end gap-x-9 md:text-xl mr-10 items-center">
                        <NavLink to="/feed"><li>Feed</li></NavLink>
                        <NavLink to="/artists"><li>Artists</li></NavLink>
                        <li onClick={handleViewingAccountBar}><AccountCircleIcon sx={{ fontSize: 28 }}/></li>
                        <li><ShoppingBagIcon sx={{ fontSize: 28 }}/></li>
                    </ul>
                )
            }
            {viewingAccountBar ? <UserSidebar /> : null}
        </nav>

    )

}

export default Navbar