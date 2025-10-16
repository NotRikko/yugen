import { useUser } from "@/features/user/UserProvider";
import { Outlet } from "react-router-dom";
import MainSidebar from "@/features/main/MainSidebar";

function MainPage(): JSX.Element {
  const { user, cart } = useUser();

  return (
    <div className="grid grid-cols-[1fr_3fr] h-screen">
      <MainSidebar
        user={user}
        cartItemCount={cart?.items.reduce((t, i) => t + i.quantity, 0) || 0}
      />

      <div className="overflow-y-auto w-full p-4">
        <Outlet />
      </div>
    </div>
  );
}

export default MainPage;