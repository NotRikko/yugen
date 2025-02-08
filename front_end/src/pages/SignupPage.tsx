import SignupForm from "../components/SignupForm"



function SignupPage():JSX.Element {
  
   

    return (
        <div className="w-full h-screen flex flex-col items-center justify-center">
            <div className="flex items-center  justify-center w-1/2 h-full m-20 border rounded-md">
                <div className="p-8 w-1/2">
                    <h1>Yugen</h1>
                    <h2>Sign Up</h2>
                    <SignupForm />
                </div>
                <img 
                    src="https://64.media.tumblr.com/a30822675f02374d7ac7846e8172bc4d/d4ee3c438ce28514-0a/s540x810/cd48daea1fa30e2d18f6af2f62b721af76bdef42.png"
                    className="h-full w-1/2"
                />
            </div>
        </div>
    )
}

export default SignupPage