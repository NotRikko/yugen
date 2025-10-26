import ShoppingBagIcon from '@mui/icons-material/ShoppingBag';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { useState, useEffect, useRef } from "react";
import { NavLink } from 'react-router-dom';
import { useUser } from '@/features/user/useUserContext';
import MainNav from './MainNav';
import CartSidebar from '@/features/cart/components/CartSidebar';
import { Calendar, Inbox } from "lucide-react";

function Navbar() {
  const { user, handleLogout } = useUser();
  const [activeSidebar, setActiveSidebar] = useState<"cart" | "account" | null>(null);
  const sidebarRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (sidebarRef.current && !sidebarRef.current.contains(event.target as Node)) {
        setActiveSidebar(null);
      }
    };

    if (activeSidebar) {
      document.addEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [activeSidebar]);

  const toggleSidebar = (type: "cart" | "account") => {
    setActiveSidebar(prev => (prev === type ? null : type));
  };

  const accountItems = user.isGuest
    ? [
        { title: "Login", url: "/login", icon: Inbox },
        { title: "Signup", url: "/signup", icon: Calendar },
      ]
    : [
        { title: "Settings", url: "/settings", icon: Calendar },
        { title: "Log Out", url: "/", icon: Calendar },
      ];

  return (
    <div className="relative">
      <nav className="sticky top-0 z-50 h-16 w-full bg-black/90 flex justify-between items-center px-12 text-white">
        <NavLink to="/"><h2>Yugen</h2></NavLink>

        <div className="flex items-center gap-8">
          <NavLink to="/feed">Feed</NavLink>
          <NavLink to="/artist/artists">Artists</NavLink>
          <button onClick={() => toggleSidebar("account")}>
            <AccountCircleIcon sx={{ fontSize: 28 }} />
          </button>
          <button onClick={() => toggleSidebar("cart")}>
            <ShoppingBagIcon sx={{ fontSize: 28 }} />
          </button>
        </div>
      </nav>

      {activeSidebar && (
        <div
          ref={sidebarRef}
          className="fixed top-0 right-0 h-full z-50"
        >
          {activeSidebar === "cart" ? (
            <CartSidebar onClose={() => setActiveSidebar(null)} />
          ) : (
            <MainNav
              user={user}
              items={accountItems}
              handleLogout={handleLogout}
              layout="sidebar"
              type="user"
            />
          )}
        </div>
      )}
    </div>
  );
}

export default Navbar;