import ArtistCard from "@/features/artists/components/ArtistCard";
import { PartialArtist } from "@/features/artists/types/artistTypes";

interface FeedTrendingBarProps {
  trendingArtists: PartialArtist[];
  loadingTrendingArtists: boolean;
}

const TrendingArtistsBar: React.FC<FeedTrendingBarProps> = ({ trendingArtists, loadingTrendingArtists }) => {
  return (
    <div className="sticky top-0 hidden lg:flex flex-col w-full border-l-2 bg-sidebar p-4 overflow-y-auto max-h-screen">
      <h2 className="mb-4 text-lg font-semibold">Trending Artists</h2>

      {loadingTrendingArtists ? (
        <div className="text-center py-4">Loading trending artists...</div>
      ) : trendingArtists.length > 0 ? (
        <div className="flex flex-col gap-2">
          {trendingArtists.map((artist) => (
            <ArtistCard
              key={artist.id}
              artist={artist}
              onClick={() => console.log("Clicked", artist.artistName)}
            />
          ))}
        </div>
      ) : (
        <div className="text-center py-4">No trending artists found.</div>
      )}
    </div>
  );
};

export default TrendingArtistsBar;