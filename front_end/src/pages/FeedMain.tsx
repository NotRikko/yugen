import { useState, useEffect } from "react"
import Navbar from "../components/Navbar"

function FeedMain(): JSX.Element {
    const [posts, setPosts] = useState([])
    return(
        <>
            <Navbar/>
        </>
    )
}

export default FeedMain