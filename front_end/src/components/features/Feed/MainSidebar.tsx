import { House, Bookmark, Mail, Bell, Settings } from "lucide-react"
 
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
                          src="https://i.pinimg.com/236x/05/3b/e5/053be564a7a436ce2846acb98849ea1b.jpg"
                          className="my-3 border rounded-lg"
                      />
                      <h1 className="text-xl">{user.displayName ? user.displayName : "Guest"}</h1>
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