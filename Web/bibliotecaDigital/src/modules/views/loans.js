import { daysLate, getActiveLoans, isOverdue } from "../model.js";
import { escapeHtml, formatDate } from "../helpers.js";

export function renderLoans(data) {
	const loans = getActiveLoans(data).slice().sort((a, b) => a.fechaLimite.localeCompare(b.fechaLimite));
	document.querySelector("#active-loans").innerHTML = loans.length ? loans.map((loan) => {
		const book = data.libros.find((item) => item.id === loan.idLibro);
		if (!book) return "";
		return `<article class="loan-row ${isOverdue(loan) ? "loan-row-overdue" : ""}"><div class="loan-date" aria-hidden="true"><span>${new Intl.DateTimeFormat("es", { day: "2-digit" }).format(new Date(`${loan.fechaLimite}T12:00:00`))}</span><small>${new Intl.DateTimeFormat("es", { month: "short" }).format(new Date(`${loan.fechaLimite}T12:00:00`))}</small></div><div class="loan-main"><div class="loan-title-line"><h3>${escapeHtml(book.titulo)}</h3>${isOverdue(loan) ? `<span class="status status-vencido">Vencido · ${daysLate(loan.fechaLimite)} días</span>` : ""}</div><p>En manos de <strong>${escapeHtml(loan.persona)}</strong><span class="dot-separator">·</span> Límite: ${formatDate(loan.fechaLimite)}</p></div><button class="button button-secondary button-small" data-action="return" data-id="${escapeHtml(book.id)}">Marcar devolución</button></article>`;
	}).join("") : `<div class="empty-state empty-state-compact"><span class="empty-mark" aria-hidden="true">✓</span><h3>Todo al día</h3><p>No hay préstamos activos en este momento.</p></div>`;

	const returned = data.prestamos.filter((loan) => loan.fechaDevolucion !== null).sort((a, b) => b.fechaDevolucion.localeCompare(a.fechaDevolucion));
	document.querySelector("#loan-history").innerHTML = returned.length ? returned.map((loan) => {
		const book = data.libros.find((item) => item.id === loan.idLibro);
		if (!book) return "";
		const late = Math.max(0, Math.floor((new Date(`${loan.fechaDevolucion}T12:00:00`) - new Date(`${loan.fechaLimite}T12:00:00`)) / 86400000));
		return `<article class="history-row"><div class="history-check" aria-hidden="true">${late ? "!" : "✓"}</div><div class="history-main"><h3>${escapeHtml(book.titulo)}</h3><p>${escapeHtml(loan.persona)} <span class="dot-separator">·</span> ${formatDate(loan.fechaPrestamo)} → ${formatDate(loan.fechaDevolucion)}</p></div><span class="history-result ${late ? "history-late" : ""}">${late ? `Devuelto con ${late} ${late === 1 ? "día" : "días"} de retraso` : "Devuelto a tiempo"}</span></article>`;
	}).join("") : `<div class="empty-state empty-state-compact"><p>El historial aparecerá aquí después de la primera devolución.</p></div>`;
}