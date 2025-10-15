import { House, Bookmark, Mail, Bell, Settings, Users, ShoppingCart } from "lucide-react"

 
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/shared/ui/sidebar"

import { useNavigate } from "react-router-dom";


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
}
 

 
function MainSidebar({user, cartItemCount = 0, onSelectItem} : UserProps & { onSelectItem: (item: string) => void } ) {
    const navigate = useNavigate();
    const items = [
      {
        title: "Home",
        url: "/",
        icon: House,
      },
      { title: "Cart", 
        url: "/cart", 
        icon: ShoppingCart, 
        badge: cartItemCount },
      {
        title: "Notifications",
        url: "/",
        icon: Bell
      },
      {
        title: "Following",
        url: "/",
        icon : Users
      },
      {
        title: "Saved",
        url: "/",
        icon: Bookmark, 
      },
      {
        title: "Messages",
        url: "/",
        icon: Mail,
      },
      {
        title: "Settings",
        url: "/settings",
        icon: Settings
      }
      
    ]
    return (
          <Sidebar side="left" className="border-r-2" style={{
            "--sidebar-width": "25%",
            "--sidebar-width-mobile": "25rem",
          } as React.CSSProperties}>
            <SidebarContent  className="px-16">
              <SidebarGroup>
                  <SidebarGroupContent className="flex flex-col justify-center p-3">
                      <img 
                          src={user.image}
                          className="my-3 border rounded-lg"
                      />
                      <h1 className="text-xl">{user.displayName ? user.displayName : "Guest"}</h1>
                      {user.isGuest && (
                        <button
                          onClick={() => (window.location.href = "/login")}
                          className="mt-2 px-3 py-1 rounded bg-blue-500 text-white hover:bg-blue-600"
                        >
                          Login
                        </button>
                      )}
                  </SidebarGroupContent>
              </SidebarGroup>
              <SidebarGroup>
                  <SidebarGroupContent>
                  <SidebarMenu>
                      {items.map((item) => (
                      <SidebarMenuItem key={item.title}>
                          <SidebarMenuButton asChild>
                          <button onClick={ () => {onSelectItem(item.title); if (item.title === "Home") { navigate(item.url)}}} className="gap-4">
                                            <item.icon className="scale-125"/>
                                            <span className="text-lg">{item.title}</span>
                                            {item.badge && (
                                              <span className="ml-auto px-2 py-0.5 text-xs font-semibold bg-red-500 text-white rounded-full">
                                                {item.badge}
                                              </span>
                                            )}
                                        </button>
                          </SidebarMenuButton>
                      </SidebarMenuItem>
                      ))}
                  </SidebarMenu>
                  </SidebarGroupContent>
              </SidebarGroup>
            </SidebarContent>
        </Sidebar>
    )
}

export default MainSidebar;