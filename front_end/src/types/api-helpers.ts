import { components } from "./api";

export type Schema<T extends keyof components["schemas"]> = components["schemas"][T];