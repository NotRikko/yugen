import { useUser } from "@/features/user/useUserContext";
import { useCart } from "@/features/cart/useCartContext";
import { Outlet } from "react-router-dom";
import { House, Bookmark, Mail, Bell, Settings, Users, ShoppingCart, Newspaper, Package, LogOut, LogIn } from "lucide-react";
import MainNav from "@/shared/components/MainNav";
function MainPage(): JSX.Element {
  const { user } = useUser();
  const { cart } = useCart();
  const cartItemCount = cart?.items.reduce((t, i) => t + i.quantity, 0) || 0;
  const items = [
    ...(user.isGuest ? [{ title: "Login", url: "/login", icon: LogIn }] : []),
    { title: "Home", url: "/", icon: House },
    { title: "Feed", url: "/feed", icon: Newspaper },
    { title: "Cart", url: "/cart", icon: ShoppingCart, badge: cartItemCount },
    { title: "Notifications", url: "/notifications", icon: Bell },
    { title: "Following", url: "/following", icon: Users },
    ...(user.artistId ? [{ title: "Products", url: "/products", icon: Package}] : [] ),
    { title: "Saved", url: "/saved", icon: Bookmark },
    { title: "Messages", url: "/messages", icon: Mail },
    ...(user.isGuest ? [] : [{ title: "Settings", url: "/settings", icon: Settings }]),
    ...( !user.isGuest ? [{ title: "Logout", url: "/logout", icon: LogOut }] : [] ),
  ];


  return (
    <div className="grid grid-cols-[1fr_4fr] h-screen">
      <MainNav user={user} items={items} layout="sidebar" />
      <div className="overflow-y-auto w-full">
        <Outlet />
      </div>
    </div>
  );
}

export default MainPage;