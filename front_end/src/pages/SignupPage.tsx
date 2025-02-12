import SignupForm from "../components/SignupForm"



function SignupPage():JSX.Element {
  
    return (
        <div className="w-full h-screen flex flex-col items-center justify-center">
            <div className="flex items-center  justify-center w-full h-full">
                <div className="w-full md:w-1/2 h-full p-8 sm:p-4 flex flex-col justify-center lg:gap-8 overflow-y-auto">
                    <h1 className="self-center lg:text-3xl ">Yugen</h1>
                    <SignupForm />
                </div>
                <img 
                    src="https://w0.peakpx.com/wallpaper/747/501/HD-wallpaper-summer-anime-scenery-japan.jpg/"
                    className="hidden md:flex h-full w-1/2"
                />
            </div>
        </div>
    )
}

export default SignupPage