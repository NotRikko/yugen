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
                    src="https://64.media.tumblr.com/127053e83f24d3dc142dda81bec22692/d4ee3c438ce28514-d6/s1280x1920/5917556a8d511bda13ae6107874f742905b8b126.png"
                    className="hidden md:flex h-full w-1/2"
                />
            </div>
        </div>
    )
}

export default SignupPage