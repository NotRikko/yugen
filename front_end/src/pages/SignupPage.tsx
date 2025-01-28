import { useForm } from "react-hook-form"
import Navbar from "../components/Navbar"

interface UserSignupData {
    username: string;
    displayName: string;
    email: string;
    password: string;
    confirmPassword: string;
}
  
interface ArtistSignupData extends UserSignupData {
    artistName: string;
    bio: string;
}
  
type SignupFormData = (UserSignupData | ArtistSignupData) & {
    isArtistSignup: boolean; 
    artistName?: string;
};


function SignupPage():JSX.Element {
    const { register, handleSubmit, watch, formState: { errors } } = useForm<SignupFormData>({
        mode: "onChange", 
    });

    const isArtistSignup = watch("isArtistSignup");

    const onSubmit = async (data: SignupFormData) => {
        const payload = isArtistSignup ? (data as ArtistSignupData) : (data as UserSignupData);
      
        try {
          const response = await fetch("http://localhost:8080/users/create", {
            mode: "cors",
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
          });
      
          // Log the response
          console.log(await response.json());
        } catch (error) {
          console.error("Error during signup:", error);
        }
      };

    return (
        <div className="bg-slate-100 h-screen flex flex-col items-center">
            <Navbar/>
            <div className="flex flex-col justify-center items-center bg-white w-1/3 h-full my-20 px-8 gap-12 ">
                <h1 className="text-3xl">Signup for Yugen</h1>
                <form className="flex flex-col gap-7 w-full px-14" onSubmit={handleSubmit(onSubmit)}>
                    <div className="flex flex-col gap-2">
                        <label htmlFor="username">Username</label>
                        <input
                            className="h-9 border border-gray-400 focus:border-blue-500 focus:outline-none"
                            type="text"
                            {...register("username", { 
                                required: "Username is required", 
                                maxLength: { value: 15, message: "Username can not exceed 15 characters"}
                            })}
                            
                        />
                        {errors.username && <span className="text-red-500">{errors.username.message}</span>}
                    </div>

                    <div className="flex flex-col gap-2">
                        <label htmlFor="name">Display Name</label>
                        <input
                            className="h-9 border border-gray-400 focus:border-blue-500 focus:outline-none"
                            type="text"
                            {...register("displayName", { 
                                required: "Display name is required", 
                                maxLength: { value: 15, message: "Display name can not exceed 15 characters"}
                            })}
                        />
                        {errors.displayName && <span className="text-red-500">{errors.displayName.message}</span>}
                    </div>

                    <div className="flex flex-col gap-2">
                        <label htmlFor="name">Email</label>
                        <input
                            className="h-9 border border-gray-400 focus:border-blue-500 focus:outline-none"
                            type="text"
                            {...register("email", { required: "Email is required" })}
                        />
                        {errors.email && <span className="text-red-500">{errors.email.message}</span>}
                    </div>


                    <div className="flex flex-col gap-2">
                        <label htmlFor="password">Password</label>
                        <input
                            className="h-9 border border-gray-400 focus:border-blue-500 focus:outline-none"
                            type="password"
                            {...register("password", {
                                required: "Password is required",
                                minLength: { value: 8, message: "Password must be at least 8 characters" },
                                maxLength: { value: 25, message: "Password cannot exceed 25 characters" },
                            })}
                        />
                        {errors.password && <span className="text-red-500">{errors.password.message}</span>}
                    </div>

                    <div className="flex flex-col gap-2">
                        <label htmlFor="password">Confirm Password</label>
                        <input
                            className="h-9 border border-gray-400 focus:border-blue-500 focus:outline-none"
                            type="password"
                            {...register("confirmPassword", {
                                validate: (value) =>
                                  value === watch("password") || "Passwords do not match",
                              })}
                        />
                        {errors.confirmPassword && (
                            <span className="text-red-500">{errors.confirmPassword.message}</span>
                        )}
                    </div>

                    <div className="flex flex-col items-center gap-2">
                        <label htmlFor="isArtistSignup">Signup as an Artist?</label>
                        <input
                            type="checkbox"
                            {...register("isArtistSignup")}
                        />
                    </div>

                    {isArtistSignup && (
                        <div className="flex flex-col gap-2">
                            <label htmlFor="artistName">Artist Name</label>
                            <input
                                className="h-9 border border-gray-400 focus:border-blue-500 focus:outline-none"
                                type="text"
                                {...register("artistName", {
                                    required: isArtistSignup ? "Artist name is required" : false,
                                    maxLength: isArtistSignup
                                        ? { value: 15, message: "Artist name cannot exceed 15 characters" }
                                        : undefined,
                                })}
                            ></input>
                            {errors.artistName && <span className="text-red-500">{errors.artistName.message}</span>}
                        </div>
                    )}

                    <button className="text-white bg-sky-500 self-center border border-gray-400 p-2 w-full"type="submit">Create Account</button>
                </form>
            </div>
        </div>
    )
}

export default SignupPage