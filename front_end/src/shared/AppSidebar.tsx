import {
  Sidebar,
  SidebarHeader,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarMenu,
  SidebarMenuItem,
  SidebarMenuButton,
} from "@/components/ui/sidebar"
import { useUser } from "@/features/user/useUserContext"

import { NavLink } from "react-router-dom"
import { Home, Users, LogOut, LogIn, UserPlus } from "lucide-react"

export function AppSidebar() {
  const { user } = useUser() 
  const iconClass = "!size-6"

  const mainNavItems = [
    { to: "/", icon: Home },
    { to: "/artist/artists", icon: Users },
  ]

  const footerNavItems = [
    { to: "/login", icon: LogIn },
    { to: "/signup", icon: UserPlus },
    { to: "/logout", icon: LogOut },
  ]

  return (
    <Sidebar className="w-24 border-r">
       <SidebarHeader className="flex items-center justify-center py-6">
        <NavLink
          to="/profile"
          className="h-14 w-14 flex items-center justify-center rounded-full overflow-hidden bg-muted hover:bg-muted/70 transition"
        >
          {user?.image ? (
            <img
              src={user.image}
              alt={user.username || "User"}
              className="h-12 w-12 object-cover"
            />
          ) : (
            <div className="h-full w-full bg-gray-300 flex items-center justify-center text-white font-bold">
              {user?.username?.[0].toUpperCase() || "U"}
            </div>
          )}
        </NavLink>
      </SidebarHeader>

      <SidebarContent className="flex flex-col items-center flex-1 py-6">
        <SidebarGroup>
          <SidebarMenu className="flex flex-col items-center gap-4">

            {mainNavItems.map(({ to, icon: Icon }) => (
              <SidebarMenuItem key={to}>
                <SidebarMenuButton
                  asChild
                  className="h-20 w-20 justify-center"
                >
                  <NavLink
                    to={to}
                    className={({ isActive }) =>
                      `flex items-center justify-center rounded-2xl transition ${
                        isActive
                          ? "bg-black text-white"
                          : "text-muted-foreground hover:bg-muted"
                      }`
                    }
                  >
                    <Icon className={iconClass} />
                  </NavLink>
                </SidebarMenuButton>
              </SidebarMenuItem>
            ))}

          </SidebarMenu>
        </SidebarGroup>
      </SidebarContent>

      <SidebarFooter className="flex flex-col items-center pb-8">
        <SidebarMenu className="flex flex-col items-center gap-4">

          {footerNavItems.map(({ to, icon: Icon }) => (
            <SidebarMenuItem key={to}>
              <SidebarMenuButton
                asChild
                className="h-16 w-16 justify-center"
              >
                <NavLink
                  to={to}
                  className="flex items-center justify-center rounded-xl text-muted-foreground hover:bg-muted transition"
                >
                  <Icon className={iconClass} />
                </NavLink>
              </SidebarMenuButton>
            </SidebarMenuItem>
          ))}

        </SidebarMenu>
      </SidebarFooter>

    </Sidebar>
  )
}