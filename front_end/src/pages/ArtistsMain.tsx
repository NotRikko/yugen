import Navbar from '../components/Layout/Navbar'
import { useState, useEffect } from 'react'

interface Artist {
    id: number;
    name: string;
    image: string;
}

function ArtistsMain(): JSX.Element {
    const [artists, setArtists] = useState<Artist[]>([
        ]);
        
        useEffect(() => {
            const fetchArtists = async () => {
                const API_URL = import.meta.env.VITE_API_URL;

                try {
                    const artistsResponse = await fetch(`${API_URL}/artists/all`, { mode: "cors"});

                    if (!artistsResponse.ok) {
                        throw new Error("Issue with network response")
                    }

                    const artistsData = await artistsResponse.json()
                    setArtists(artistsData);
                } catch (error) {
                    console.error("Error fetching artists", error)
                }
             
            };
            fetchArtists();
        }, [])


    return (
        <>
            <Navbar/>
            <div className="sm:w-full h-full flex flex-col gap-6">
                <div className="h-full self-center flex flex-col items-center w-full">
                    <div className="flex flex-col items-center gap-16 h-full sm:p-28">
                        <h2 className="text-3xl">Featured Artist</h2>
                        <div className="w-4/5 sm:w-3/4 flex flex-col sm:flex-row items-center">
                        {artists.length > 0 ? (
                            <>
                                <img className="aspect-square h-3/4" src={artists[0].image} alt={artists[0].name} />
                                <div className="sm:p-14 flex sm:flex-col gap-4">
                                    <h3 className="text-2xl">{artists[0].name}</h3>
                                    <p>A short description is a brief summary of a topic that conveys the most important information in a clear and concise way. It can be used for books, movies, products, or other topics.</p>
                                </div>
                            </>
                        ) : (
                            <div className="text-center">
                                <p>Loading featured artist...</p>
                            </div>
                        )}
                        </div>
                    </div>
                </div>
                <div className="h-full w-full flex">
                    <div className="sticky top-[64px] h-screen w-1/5">
                        <ul className="hidden sm:flex flex-col ml-14 mt-32 h-full gap-4 text-lg">
                            <li>All</li>
                            <li>Trending</li>
                            <li>New</li>
                        </ul>
                    </div>
                    <div className="w-4/5 p-4">
                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 items-center justify-center">
                            {artists.map((artist) => (
                                <div
                                    key={artist.id}
                                    className="sm:p-6 w-full h-full flex flex-col items-center justify-center"
                                    style={{
                                        height: '500px', // Ensures all items have the same height
                                    }}
                                    
                                >
                                    <img src={artist.image} className="aspect-square h-3/4" />
                                    <h2>{artist.name}</h2>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default ArtistsMain