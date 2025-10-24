import { UserPlus,  Key, LogOut, Settings } from "lucide-react"
 
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/shared/ui/sidebar"
import { useNavigate, Link } from "react-router-dom";
import { useUser } from "../useUserContext";
import { User } from "../types/userTypes";
  
interface UserProps {
  user: User;
}

const guestItems = [
  { title: "Login", url: "/login", icon: Key },
  { title: "Signup", url: "/signup", icon: UserPlus },
];

const loggedInItems = [
  { title: "Settings", url: "/settings", icon: Settings },
];

 
export default function UserSidebar({ user }: UserProps) {
  const { handleLogout, isLoggedIn } = useUser();
  const navigate = useNavigate();

  const logout = () => {
    handleLogout();
    navigate("/");
  };

  return (
    <Sidebar className="w-1/6">
      <SidebarContent className="overflow-hidden">
        <SidebarGroup>
          <SidebarGroupContent className="flex flex-col justify-center p-3">
            <img src={user.image} className="mx-6 my-3 rounded-full" />
            <h1 className="text-lg mx-6">
              {user.displayName || "Guest"}
            </h1>
          </SidebarGroupContent>
        </SidebarGroup>

        <SidebarGroup>
          <SidebarGroupContent className="mx-4 p-3">
            <SidebarMenu>
              {(isLoggedIn ? loggedInItems : guestItems).map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton asChild>
                    <Link to={item.url} className="gap-4 flex items-center">
                      <item.icon className="scale-125" />
                      <span className="text-lg">{item.title}</span>
                    </Link>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}

              {isLoggedIn && (
                <SidebarMenuItem>
                  <SidebarMenuButton asChild>
                    <button
                      onClick={logout}
                      className="gap-4 flex items-center w-full text-left"
                    >
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
  );
}