import { House, Bookmark, Mail, Bell, Settings, Users } from "lucide-react"
 
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar"

interface User {
  username: string;
  displayName: string;
  email: string;
  image: string;
  isGuest: boolean;
}

interface UserProps {
  user: User;
}
 
// Menu items.
const items = [
  {
    title: "Home",
    url: "/",
    icon: House,
  },
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
 
function MainSidebar({user, onSelectItem} : UserProps & { onSelectItem: (item: string) => void } ) {
    return (
          <Sidebar side="left" className="border-r-2" style={{
            "--sidebar-width": "25%",
            "--sidebar-width-mobile": "25rem",
          }}>
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
                          <button onClick={() => onSelectItem(item.title)} className="gap-4">
                                            <item.icon className="scale-125"/>
                                            <span className="text-lg">{item.title}</span>
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