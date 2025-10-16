import type { PartialArtist } from "../types/artistTypes";

interface ArtistCardProps {
    artist: PartialArtist;
    onClick?: () => void;
}

const ArtistCard: React.FC<ArtistCardProps> = ({ artist, onClick }) => {
    return (
        <div
            onClick={onClick}
            className="flex items-center gap-3 p-2 rounded-lg cursor-pointer hover:bg-gray-100"
        >
            <img
                src={artist.profilePictureUrl || "https://i.pravatar.cc/150?img=1"}
                alt={artist.artistName}
                className="w-10 h-10 rounded-full object-cover"
            />
            <span className="text-sm font-medium text-gray-800">{artist.artistName}</span>
        </div>
    );
};

export default ArtistCard;