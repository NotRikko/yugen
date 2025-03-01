import { UserPlus,  Key, LogOut, Settings } from "lucide-react"
 
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar"
import { useNavigate } from "react-router-dom"
import { useUser } from "@/UserProvider";

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
    title: "Login",
    url: "/login",
    icon: Key
  },
  {
    title: "Signup",
    url: "/signup",
    icon: UserPlus
  },
]

const loggedInItems = [
  {
    title: "Settings",
    url: "/settings",
    icon: Settings
  },
]
 
export default function UserSidebar({user} : UserProps) {

  const {setUser, guestUser} = useUser();
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("accessToken"); 
    setUser(guestUser); 
    navigate('/')
  };

    return (
        <Sidebar className="w-1/6 ">
            <SidebarContent className="overflow-hidden">
            <SidebarGroup>
                <SidebarGroupContent className="flex flex-col justify-center p-3">
                    <img 
                        src="https://i.pinimg.com/236x/05/3b/e5/053be564a7a436ce2846acb98849ea1b.jpg"
                        className="mx-6 my-3"
                    />
                    <h1 className="text-lg mx-6">{user.displayName ? user.displayName : "Guest"}</h1>
                </SidebarGroupContent>
            </SidebarGroup>
            <SidebarGroup>
                <SidebarGroupContent className="mx-4 p-3">
                <SidebarMenu>
                  {(user.isGuest ? items : loggedInItems).map((item) => (
                    <SidebarMenuItem key={item.title}>
                        <SidebarMenuButton asChild>
                            <a href={item.url} className="gap-4">
                                <item.icon className="scale-125" />
                                <span className="text-lg">{item.title}</span>
                            </a>
                        </SidebarMenuButton>
                    </SidebarMenuItem>
                    
                  ))}
                  {!user.isGuest && (
                      <SidebarMenuItem>
                          <SidebarMenuButton asChild>
                              <button onClick={handleLogout} className="gap-4 flex items-center w-full text-left">
                                  <LogOut className="scale-125" />
                                  <span className="text-lg">Log Out</span>
                              </button>
                          </SidebarMenuButton>
                      </SidebarMenuItem>
                  )}
                </SidebarMenu>
                </SidebarGroupContent>
            </SidebarGroup>
            </SidebarContent>
        </Sidebar>
    )
}