import Navbar from "../components/Navbar"
import SignupForm from "../components/SignupForm"



function SignupPage():JSX.Element {
  
   

    return (
        <div className="bg-slate-100 h-screen flex flex-col items-center">
            <Navbar/>
            <SignupForm />
        </div>
    )
}

export default SignupPage