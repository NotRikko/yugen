import { useState } from "react";
import { PartialArtist } from "../types/artistTypes";

interface ArtistProfileHeaderProps {
  artist: PartialArtist;
}

const ArtistProfileHeader: React.FC<ArtistProfileHeaderProps> = ({ artist }) => {
    const [isFollowing, setIsFollowing] = useState(false);
    const handleFollowToggle = () => {
    setIsFollowing((prev) => !prev);
    };

    return (
        <div className="w-full flex flex-col items-center bg-neutral-900 text-white rounded-2xl overflow-hidden shadow-lg">
        <div className="relative w-full h-48 sm:h-64 bg-gray-800">
            {artist.bannerPictureUrl ? (
            <img
                src={artist.bannerPictureUrl}
                alt={`${artist.artistName || "Artist"} banner`}
                className="object-cover w-full h-full"
            />
            ) : (
            <div className="w-full h-full bg-gradient-to-r from-gray-700 to-gray-900" />
            )}

            <div className="absolute -bottom-16 left-1/2 transform -translate-x-1/2">
            <img
                src={artist.profilePictureUrl}
                alt={`${artist.artistName || "Artist"} profile`}
                className="w-32 h-32 rounded-full border-4 border-neutral-900 object-cover shadow-xl"
            />
            </div>
        </div>

        <div className="mt-20 px-6 pb-6 text-center max-w-3xl">
            <h1 className="text-2xl font-semibold mb-2">
                {artist.artistName || "Unknown Artist"}
            </h1>
            <button
                onClick={handleFollowToggle}
                className={`px-6 py-2 rounded-full font-medium mb-4 transition ${
                    isFollowing
                    ? "bg-neutral-700 hover:bg-neutral-600"
                    : "bg-indigo-600 hover:bg-indigo-500"
                }`}
            >
                {isFollowing ? "Following" : "Follow"}
            </button>
            {artist.bio ? (
            <p className="text-gray-300 text-sm sm:text-base leading-relaxed whitespace-pre-line">
                {artist.bio}
            </p>
            ) : (
            <p className="text-gray-500 italic">No bio available.</p>
            )}
        </div>
        </div>
    );
};

export default ArtistProfileHeader;