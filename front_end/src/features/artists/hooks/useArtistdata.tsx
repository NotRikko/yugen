import { useState, useEffect } from "react";
import { artistApi } from "../api/artistApi";
import type { PartialArtist } from "../types/artistTypes";

export function useArtist() {
    const [trendingArtists, setTrendingArtists] = useState<PartialArtist[]>([]);
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