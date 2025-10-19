import { useUser } from "@/features/user/UserProvider";
import { Outlet } from "react-router-dom";
import { House, Bookmark, Mail, Bell, Settings, Users, ShoppingCart } from "lucide-react";
import MainNav from "@/features/main/MainNav";
function MainPage(): JSX.Element {
  const { user, cart } = useUser();
  const cartItemCount = cart?.items.reduce((t, i) => t + i.quantity, 0) || 0;
  const items = [
    { title: "Home", url: "/", icon: House },
    { title: "Feed", url: "/feed", icon: House },
    { title: "Cart", url: "/cart", icon: ShoppingCart, badge: cartItemCount },
    { title: "Notifications", url: "/notifications", icon: Bell },
    { title: "Following", url: "/following", icon: Users },
    { title: "Saved", url: "/saved", icon: Bookmark },
    { title: "Messages", url: "/messages", icon: Mail },
    ...(user.isGuest
      ? []
      : [{ title: "Settings", url: "/settings", icon: Settings }]),
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