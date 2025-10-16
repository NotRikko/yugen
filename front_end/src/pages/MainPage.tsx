import { useState } from "react"
import { useUser } from "@/UserProvider";
import FeedMain from "@/features/feed/pages/FeedMain";
import MainSidebar from "@/features/feed/components/MainSidebar";
import SettingsForm from "../features/user/SettingsForm";
import CartPage from "../features/user/CartPage";

function MainPage(): JSX.Element {
    const [selectedItem, setSelectedItem] = useState<string>("Home");
    const {user, cart} = useUser();
    
    return(
        <div className="grid grid-cols-[1fr_3fr]">
            <MainSidebar user={user} cartItemCount={cart?.items.reduce((t, i) => t + i.quantity, 0) || 0}  onSelectItem={setSelectedItem} />
            <div >
                <div className="flex flex-col gap-4 w-full">
                    {selectedItem === "Home" && <FeedMain />}
                    {selectedItem === "Settings" && <SettingsForm />}
                    {selectedItem === "Cart" && <CartPage />}
                </div>
            </div>
        </div>
    )
}

export default MainPage