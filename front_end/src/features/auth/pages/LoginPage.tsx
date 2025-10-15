import LoginForm from "../components/LoginForm"



function LoginPage():JSX.Element {
  
    return (
        <div className="w-full h-screen flex flex-col items-center justify-center bg-muted">
            <div className="flex items-center  justify-center w-1/2 h-3/4 border bg-white rounded-xl">
                <div className="w-full md:w-1/2 h-full p-8 sm:p-4 flex flex-col justify-center lg:gap-8 overflow-y-auto">
                    <h1 className="self-center lg:text-3xl ">Yugen</h1>
                    <LoginForm />
                </div>
                <img 
                    src="https://64.media.tumblr.com/a30822675f02374d7ac7846e8172bc4d/d4ee3c438ce28514-0a/s1280x1920/8efdc186fd8ad4f5518a3e47b9e48dcd66019f20.png"
                    className="hidden md:flex h-full w-1/2"
                />
            </div>
        </div>
    )
}

export default LoginPage