import ShoppingBagIcon from '@mui/icons-material/ShoppingBag';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { useState, useEffect, useRef } from "react";
import { NavLink } from 'react-router-dom';
import UserSidebar from './UserSidebar';
import CartSidebar from './CartSidebar';



function Navbar() {
    const [isMobile, setIsMobile] = useState(false);
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [viewingAccountBar, setIsViewingAccountBar] = useState(false);
    const [viewingCartBar, setIsViewingCartBar] = useState(false); 

    const accountSidebarRef = useRef<HTMLDivElement | null>(null);
    const cartSidebarRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        const checkScreenSize = () => {
          setIsMobile(window.innerWidth <= 600); 
        };
    
        checkScreenSize();
        window.addEventListener("resize", checkScreenSize);
    
        return () => window.removeEventListener("resize", checkScreenSize);
      }, []);
    useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
        if (viewingAccountBar && accountSidebarRef.current && !accountSidebarRef.current.contains(event.target as Node)) {
            setIsViewingAccountBar(false);
        }
        if (viewingCartBar && cartSidebarRef.current && !cartSidebarRef.current.contains(event.target as Node)) {
            setIsViewingCartBar(false);
        }
    };

    if (viewingAccountBar || viewingCartBar) {
        document.addEventListener("mousedown", handleClickOutside);
    }

    return () => {
        document.removeEventListener("mousedown", handleClickOutside);
    };
    }, [viewingAccountBar, viewingCartBar]);

    const handleViewingAccountBar = () => {
        setIsViewingAccountBar(prevViewingAccountBar => !prevViewingAccountBar);
    };

    const handleViewingCartBar = () => {
        setIsViewingCartBar(prevViewingCartBar => !prevViewingCartBar);
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
                        <li onClick={handleViewingCartBar}><ShoppingBagIcon sx={{ fontSize: 28 }}/></li>
                    </ul>
                )
            }
            {viewingAccountBar && (
                <div ref={accountSidebarRef} className="absolute top-16 right-0 z-50">
                    <UserSidebar />
                </div>
            )}
            {viewingCartBar && (
                <div ref={cartSidebarRef} className="absolute top-16 right-0 z-50">
                    <CartSidebar />
                </div>
            )}
        </nav>

    )

}

export default Navbar