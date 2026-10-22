import { createInitialData } from "./model.js";

const STORAGE_KEY = "biblioteca-digital-v1";

export function loadData() {
	try {
		const saved = JSON.parse(localStorage.getItem(STORAGE_KEY));
		if (Array.isArray(saved?.libros) && Array.isArray(saved?.prestamos)) {
			return { ...createInitialData(), ...saved };
		}
	} catch (error) {
		console.warn("No se pudieron leer los datos guardados.", error);
	}
	return createInitialData();
}

export function saveData(data) {
	localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
}