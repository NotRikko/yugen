import { useUser } from "../UserProvider";

function UserNavBox({ onClose }: { onClose: () => void }) {
    const { isLoggedIn, user } = useUser();

    return(
        <div className="fixed inset-y-0 right-0 w-1/6 h-screen z-50 text-black bg-white">
             <button
                    className="absolute top-4 right-4 text-gray-600"
                    onClick={onClose} 
                >
                    ✕
                </button>
            <div className="m-8 mt-14 flex flex-col gap-8">
                <img className="aspect-square"src={isLoggedIn ? user.image : "https://i.pinimg.com/736x/11/5e/b9/115eb9d1ff5a8d32c9000d3ec418d014.jpg"}/>
                <h1>{isLoggedIn ? user.username : "Guest"}</h1>
                {isLoggedIn ? null :
                    <div>
                        <h2>Log In</h2>
                        <h2>Sign Up</h2>
                    </div>
                }
            </div>
        </div>
    )
}

export default UserNavBox