import { useState, useEffect } from "react";
import { artistApi } from "../api/artistApi";
import type { PartialArtist } from "../types/artistTypes";

export function useArtist() {
    const [trendingArtists, setTrendingArtists] = useState<PartialArtist[]>([]);

    useEffect(() => {
        const fetchTrendingArtists = async () => {
            try {
                const result = await artistApi.getArtists();
                setTrendingArtists(result || []);
            } catch (error) {
                console.error("Error fetching artists:" , error);
            };
        }
        fetchTrendingArtists();
    }, []);

    return { trendingArtists }
}