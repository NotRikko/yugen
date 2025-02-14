import { House, Bookmark, Mail, Bell } from "lucide-react"
 
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar"
 
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
  
]
 
export default function FeedSidebar() {
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
                      <h1 className="text-lg">User</h1>
                  </SidebarGroupContent>
              </SidebarGroup>
              <SidebarGroup>
                  <SidebarGroupContent>
                  <SidebarMenu>
                      {items.map((item) => (
                      <SidebarMenuItem key={item.title}>
                          <SidebarMenuButton asChild>
                          <a href={item.url}>
                              <item.icon />
                              <span>{item.title}</span>
                          </a>
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