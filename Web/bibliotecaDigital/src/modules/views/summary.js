import { getActiveLoanForBook, getActiveLoans, isOverdue } from "../model.js";
import { escapeHtml } from "../helpers.js";

export function renderSummary(data) {
	const loans = getActiveLoans(data);
	const available = data.libros.filter((book) => !book.coleccion && !getActiveLoanForBook(data, book.id)).length;
	const overdue = loans.filter(isOverdue).length;
	document.querySelector("#summary-stats").innerHTML = [
		["Ejemplares", data.libros.length, "en el catálogo"],
		["Disponibles", available, "listos para salir"],
		["Prestados", loans.length, "en circulación"],
		["Vencidos", overdue, "requieren atención"],
	].map(([label, value, note], index) => `<article class="stat-block stat-block-${index + 1}"><p>${label}</p><strong>${value}</strong><span>${note}</span></article>`).join("");
	const counts = data.libros.reduce((result, book) => {
		result[book.genero] = (result[book.genero] || 0) + 1;
		return result;
	}, {});
	const max = Math.max(1, ...Object.values(counts));
	document.querySelector("#genre-summary").innerHTML = Object.entries(counts).sort(([a], [b]) => a.localeCompare(b, "es")).map(([genre, count]) => `<div class="genre-row"><div><span>${escapeHtml(genre)}</span><strong>${count}</strong></div><span class="genre-bar"><span style="width:${(count / max) * 100}%"></span></span></div>`).join("") || `<p class="muted-copy">Añade libros para ver la distribución por género.</p>`;
}