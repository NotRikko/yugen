import { useState } from "react"
import Navbar from "../components/Navbar"

interface SignupData {
    username: string;
    name: string;
    email: string;
    password: string;
    confirmPassword: string;
    image: string;
}

interface ArtistSignupData {
    name: string;
    image: string;
    bio: string;
}

function SignupPage():JSX.Element {
    const [isArtistSignup, setIsArtistSignup] = useState(false);
    
    const [signupData, setSignupData] = useState<SignupData>({
        username: "",
        name: "",
        email: "",
        password: "",
        confirmPassword: "",
        image: ""
    });
    

    const [artistSignupData, setArtistSignupData] = useState<ArtistSignupData>({
        name: "",
        image: "",
        bio: ""
    })

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setSignupData((prevData) => ({
            ...prevData,
            [name]: value
        }))
    };

    const handleArtistChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setArtistSignupData((prevData) => ({
            ...prevData,
            [name]: value
        }))
    }

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        console.log("Signup Data submitted:", signupData)
    }

    return (
        <div className="bg-slate-100 h-screen flex flex-col items-center">
            <Navbar/>
            <div className="flex flex-col justify-center items-center bg-white w-1/3 h-full my-20 px-8 gap-12 ">
                <h1 className="text-3xl">Signup for Yugen</h1>
                <form className="flex flex-col gap-7 w-full px-14" onSubmit={handleSubmit}>
                    <div className="flex flex-col gap-2">
                        <label htmlFor="username">Username</label>
                        <input
                            className="h-9 border border-gray-400 focus:border-blue-500 focus:outline-none"
                            type="text"
                            id="username"
                            name="username"
                            value={signupData.username}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="flex flex-col gap-2">
                        <label htmlFor="name">Display Name</label>
                        <input
                            className="h-9 border border-gray-400 focus:border-blue-500 focus:outline-none"
                            type="text"
                            id="name"
                            name="name"
                            value={signupData.name}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="flex flex-col gap-2">
                        <label htmlFor="name">Email</label>
                        <input
                            className="h-9 border border-gray-400 focus:border-blue-500 focus:outline-none"
                            type="text"
                            id="email"
                            name="email"
                            value={signupData.email}
                            onChange={handleChange}
                            required
                        />
                    </div>


                    <div className="flex flex-col gap-2">
                        <label htmlFor="password">Password</label>
                        <input
                            className="h-9 border border-gray-400 focus:border-blue-500 focus:outline-none"
                            type="password"
                            id="password"
                            name="password"
                            value={signupData.password}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="flex flex-col gap-2">
                        <label htmlFor="password">Confirm Password</label>
                        <input
                            className="h-9 border border-gray-400 focus:border-blue-500 focus:outline-none"
                            type="password"
                            id="confirmPassword"
                            name="confirmPassword"
                            value={signupData.confirmPassword}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="flex flex-col items-center gap-2">
                        <label htmlFor="isArtist">Signup as an Artist?</label>
                        <input type="checkbox"></input>
                    </div>

                    <button className="text-white bg-sky-500 self-center border border-gray-400 p-2 w-full"type="submit">Create Account</button>
                </form>
            </div>
        </div>
    )
}

export default SignupPage