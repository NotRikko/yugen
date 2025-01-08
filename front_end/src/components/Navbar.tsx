import ShoppingBagIcon from '@mui/icons-material/ShoppingBag';
import React, { useState, useEffect } from "react";



function Navbar() {
    const [isMobile, setIsMobile] = useState(false);
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    useEffect(() => {
        const checkScreenSize = () => {
          setIsMobile(window.innerWidth <= 600); 
        };
    
        checkScreenSize();
        window.addEventListener("resize", checkScreenSize);
    
        return () => window.removeEventListener("resize", checkScreenSize);
      }, []);
    return(
        <nav className="h-16 absolute w-full top-0 z-10 text-white text-xl flex justify-between items-center bg-black/90">
            <h2 className="jusitfy-self-start ml-10">Yugen</h2>
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
                        <li>Products</li>
                        <li>Artists</li>
                        <li><ShoppingBagIcon/></li>
                        </ul>
                    )}
                    
                    </>
                ) : (
                    <ul className="flex justify-end gap-x-9 md:text-xl mr-10 items-center">
                    <li>Home</li>
                    <li>Products</li>
                    <li>Artists</li>
                    <li><ShoppingBagIcon/></li>
                    </ul>
                )
            }
        </nav>

    )

}

export default Navbar