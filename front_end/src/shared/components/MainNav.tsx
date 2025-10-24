import { useNavigate, useLocation } from "react-router-dom";
import { LucideIcon } from "lucide-react";

interface NavItem {
  title: string;
  url: string;
  icon: LucideIcon;
  badge?: number;
}

interface User {
  username: string;
  displayName: string;
  email: string;
  image: string;
  isGuest: boolean;
}

interface MainNavProps {
  user: User;
  items: NavItem[];
  cartItemCount?: number;
  handleLogout?: () => void;
  layout?: "sidebar" | "topbar";
}

export default function MainNav({
  user,
  items,
  handleLogout,
  layout = "sidebar",
}: MainNavProps) {
  const navigate = useNavigate();
  const location = useLocation();

  const isSidebar = layout === "sidebar";

  return (
    <nav
      className={`${
        isSidebar
          ? "w-full min-h-screen bg-white flex flex-col items-center px-24 py-8"
          : "w-full flex items-center justify-between border-b bg-white px-8 py-3"
      }`}
    >
      <div
  className={`${
    isSidebar
      ? "flex flex-col items-center mb-8"
      : "flex items-center gap-3"
    }`}
    >
  <div className="flex items-center gap-4">
    <img
      src={user.image}
      alt="User avatar"
      className={`rounded-lg border object-cover ${
        isSidebar ? "w-20 h-20" : "w-14 h-14"
      }`}
    />
    <h2 className="font-semibold">{user.displayName || "Guest"}</h2>
  </div>

  {isSidebar && (
    <>
      {user.isGuest ? (
        <button
          onClick={() => navigate("/login")}
          className="mt-2 px-4 py-1 rounded bg-blue-500 text-white hover:bg-blue-600"
        >
          Login
        </button>
      ) : handleLogout ? (
        <button
          onClick={handleLogout}
          className="mt-2 px-4 py-1 rounded bg-red-500 text-white hover:bg-red-600"
        >
          Log Out
        </button>
      ) : null}
    </>
  )}
</div>

      <ul
        className={`${
          isSidebar
            ? "flex flex-col gap-2 w-full"
            : "flex gap-6 items-center"
        }`}
      >
        {items.map((item) => {
          const isActive = location.pathname === item.url;
          return (
            <li key={item.title} className="relative">
              <button
                onClick={() => navigate(item.url)}
                className={`flex items-center gap-3 ${
                  isSidebar ? "w-full px-4 py-2" : ""
                } ${
                  isActive ? "bg-gray-100 rounded-md" : "hover:bg-gray-50"
                }`}
              >
                <item.icon className="w-5 h-5" />
                <span>{item.title}</span>
                {item.badge && (
                  <span className="ml-auto text-xs bg-red-500 text-white rounded-full px-2">
                    {item.badge}
                  </span>
                )}
              </button>
            </li>
          );
        })}
      </ul>

      {!isSidebar && !user.isGuest && handleLogout && (
        <button
          onClick={handleLogout}
          className="ml-6 px-3 py-1 rounded bg-red-500 text-white hover:bg-red-600"
        >
          Logout
        </button>
      )}
    </nav>
  );
}