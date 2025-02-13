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
                    src="https://mfiles.alphacoders.com/101/thumb-1920-1012405.png"
                    className="hidden md:flex h-full w-1/2"
                />
            </div>
        </div>
    )
}

export default SignupPage