import { useState } from "react"
import { useUser } from "@/UserProvider";
import FeedMain from "./FeedMain";
import MainSidebar from "@/components/features/Feed/MainSidebar";
import SettingsForm from "../components/features/User/SettingsForm";

function MainPage(): JSX.Element {
    const [selectedItem, setSelectedItem] = useState<string>("Home");
    const {user} = useUser();
    
    return(
        <div className="grid grid-cols-[1fr_3fr]">
            <MainSidebar user={user} onSelectItem={setSelectedItem} />
            <div >
                <div className="flex flex-col gap-4 w-full">
                    {selectedItem === "Home" && <FeedMain />}
                    {selectedItem === "Settings" && <SettingsForm />}
                </div>
            </div>
        </div>
    )
}

export default MainPage