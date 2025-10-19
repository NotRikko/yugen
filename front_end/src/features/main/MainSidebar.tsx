import { House, Bookmark, Mail, Bell, Settings, Users, ShoppingCart } from "lucide-react";
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/shared/ui/sidebar";
import { useNavigate, useLocation } from "react-router-dom";

interface User {
  username: string;
  displayName: string;
  email: string;
  image: string;
  isGuest: boolean;
}

interface UserProps {
  user: User;
  cartItemCount?: number;
  handleLogout?: () => void;
}

function MainSidebar({ user, cartItemCount = 0, handleLogout }: UserProps) {
  const navigate = useNavigate();
  const location = useLocation();

  const items = [
    { title: "Home", url: "/", icon: House },
    { title: "Feed", url: "/feed", icon: House },
    { title: "Cart", url: "/cart", icon: ShoppingCart, badge: cartItemCount },
    { title: "Notifications", url: "/notifications", icon: Bell },
    { title: "Following", url: "/following", icon: Users },
    { title: "Saved", url: "/saved", icon: Bookmark },
    { title: "Messages", url: "/messages", icon: Mail },
  ];

  if (!user.isGuest) {
    items.push({ title: "Settings", url: "/settings", icon: Settings });
  }

  if (!user.isGuest && (user as any).artistId) {
    items.push({ title: "Dashboard", url: "/artist/dashboard", icon: House });
  }

  return (
    <Sidebar
      side="left"
      className="border-r-2"
      style={{ "--sidebar-width": "25%", "--sidebar-width-mobile": "25rem" } as React.CSSProperties}
    >
      <SidebarContent className="px-16">
        <SidebarGroup>
          <SidebarGroupContent className="flex flex-col justify-center p-3">
            <img src={user.image} className="my-3 border rounded-lg" alt="User image" />
            <h1 className="text-xl">{user.displayName || "Guest"}</h1>
            {user.isGuest ? (
              <button
                onClick={() => navigate("/login")}
                className="mt-3 px-4 py-1 rounded bg-blue-500 text-white hover:bg-blue-600"
              >
                Login
              </button>
            ) : handleLogout ? (
              <button
                onClick={handleLogout}
                className="mt-3 px-4 py-1 rounded bg-red-500 text-white hover:bg-red-600"
              >
                Log Out
              </button>
            ) : null}
          </SidebarGroupContent>
        </SidebarGroup>

        <SidebarGroup>
          <SidebarGroupContent>
            <SidebarMenu>
              {items.map((item) => {
                const isActive = location.pathname === item.url;

                return (
                  <SidebarMenuItem key={item.title}>
                    <SidebarMenuButton asChild>
                      <button
                        onClick={() => navigate(item.url)}
                        className={`gap-4 w-full text-left ${
                          isActive ? "bg-gray-200 rounded" : ""
                        }`}
                      >
                        <item.icon className="scale-125" />
                        <span className="text-lg">{item.title}</span>
                        {item.badge && (
                          <span className="ml-auto px-2 py-0.5 text-xs font-semibold bg-red-500 text-white rounded-full">
                            {item.badge}
                          </span>
                        )}
                      </button>
                    </SidebarMenuButton>
                  </SidebarMenuItem>
                );
              })}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  );
}

export default MainSidebar;