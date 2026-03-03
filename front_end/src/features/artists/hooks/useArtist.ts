import { useState, useEffect } from "react";
import { artistApi } from "../api/artistApi";
import type { ArtistDTO } from "../types";

export function useArtist() {
    const [trendingArtists, setTrendingArtists] = useState<ArtistDTO[]>([]);
    const [loadingTrendingArtists, setLoadingTrendingArtists] = useState<boolean>(true);

    useEffect(() => {
        const fetchTrendingArtists = async () => {
            setLoadingTrendingArtists(true);
            try {
                const result = await artistApi.getArtists();
                setTrendingArtists(result || []);
            } catch (error) {
                console.error("Error fetching artists:" , error);
            } finally {
                setLoadingTrendingArtists(false);
            }
        }
        fetchTrendingArtists();
    }, []);

    return { trendingArtists, loadingTrendingArtists }
}