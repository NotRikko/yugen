import { Calendar,  Inbox, } from "lucide-react"
 
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar"
 
// Menu items.
const items = [
  {
    title: "Login",
    url: "#",
    icon: Inbox,
  },
  {
    title: "Signup",
    url: "#",
    icon: Calendar,
  },
]
 
export default function UserSidebar() {
    return (
      <div className="fixed">
        <Sidebar className="w-1/6">
            <SidebarContent>
            <SidebarGroup>
                <SidebarGroupContent className="flex flex-col justify-center p-3">
                    <img 
                        src="https://i.pinimg.com/236x/05/3b/e5/053be564a7a436ce2846acb98849ea1b.jpg"
                        className="mx-6 my-3"
                    />
                    <h1 className="text-lg mx-6">User</h1>
                </SidebarGroupContent>
            </SidebarGroup>
            <SidebarGroup>
                <SidebarGroupContent className="mx-4 p-3">
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
      </div>  
    )
}