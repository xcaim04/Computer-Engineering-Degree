import { GENRES, daysLate, getActiveLoanForBook, isOverdue } from "../model.js";
import { escapeHtml, formatDate } from "../helpers.js";

export function getFilteredBooks(data, { selectedFilter, searchTerm, selectedGenre }) {
	const filtered = data.libros.filter((book) => {
		const loan = getActiveLoanForBook(data, book.id);
		const matchesSearch = `${book.titulo} ${book.autor}`.toLocaleLowerCase("es").includes(searchTerm.toLocaleLowerCase("es"));
		const matchesFilter = selectedFilter === "todos" || (selectedFilter === "disponibles" && !book.coleccion && !loan) || (selectedFilter === "prestados" && Boolean(loan));
		return matchesSearch && matchesFilter && (selectedGenre === "todos" || book.genero === selectedGenre);
	});
	const collator = new Intl.Collator("es", { sensitivity: "base", numeric: true });
	return filtered.sort((a, b) => {
		const comparison = typeof a[data.orden] === "number" ? a[data.orden] - b[data.orden] : collator.compare(a[data.orden], b[data.orden]);
		return data.direccion === "asc" ? comparison : -comparison;
	});
}

export function renderCatalog(data, filters) {
	const genreSelect = document.querySelector("#genre-filter");
	const genres = [...new Set([...GENRES, ...data.libros.map((book) => book.genero)])].sort((a, b) => a.localeCompare(b, "es"));
	genreSelect.innerHTML = `<option value="todos">Todos los géneros</option>${genres.map((genre) => `<option value="${escapeHtml(genre)}">${escapeHtml(genre)}</option>`).join("")}`;
	genreSelect.value = filters.selectedGenre;
	document.querySelector("#search-books").value = filters.searchTerm;
	document.querySelector("#sort-books").value = data.orden;
	document.querySelector("#sort-direction").value = data.direccion;
	document.querySelectorAll("[data-filter]").forEach((button) => {
		button.classList.toggle("selected", button.dataset.filter === filters.selectedFilter);
		button.setAttribute("aria-pressed", String(button.dataset.filter === filters.selectedFilter));
	});

	const books = getFilteredBooks(data, filters);
	document.querySelector("#catalog-count").textContent = `Mostrando ${books.length} de ${data.libros.length} libros`;
	document.querySelector("#book-grid").innerHTML = books.length ? books.map((book) => {
		const loan = getActiveLoanForBook(data, book.id);
		const status = book.coleccion ? "coleccion" : loan ? (isOverdue(loan) ? "vencido" : "prestado") : "disponible";
		const statusLabel = book.coleccion ? "Colección" : loan ? (isOverdue(loan) ? "Vencido" : "Prestado") : "Disponible";
		const cover = book.isbn ? `<img src="../public/covers/${escapeHtml(book.isbn)}.jpg" alt="Portada de ${escapeHtml(book.titulo)}" loading="lazy" onerror="this.remove()">` : "";
		return `<article class="book-card ${loan ? "is-loaned" : ""}" data-status="${status}">
			<div class="book-cover cover-${(book.id.length % 5) + 1}">${cover}<span class="cover-title">${escapeHtml(book.titulo)}</span><span class="cover-author">${escapeHtml(book.autor)}</span><span class="cover-mark" aria-hidden="true">B.</span></div>
			<div class="book-card-body"><div class="book-card-heading"><div><p class="eyebrow">${escapeHtml(book.genero)} <span aria-hidden="true">·</span> ${book.anio}</p><h3>${escapeHtml(book.titulo)}</h3><p class="book-author">${escapeHtml(book.autor)}</p></div><span class="status status-${status}">${statusLabel}</span></div>
			${loan ? `<p class="loan-detail">Prestado a <strong>${escapeHtml(loan.persona)}</strong><br>Devolver antes del ${formatDate(loan.fechaLimite)}${isOverdue(loan) ? `<span class="overdue-note">${daysLate(loan.fechaLimite)} días de retraso</span>` : ""}</p>` : `<p class="book-note">${book.coleccion ? "Ejemplar de consulta en sala" : escapeHtml(book.comentario || "Disponible para préstamo")}</p>`}
			<div class="book-actions"><button class="button button-primary button-small" data-action="${loan ? "return" : "borrow"}" data-id="${escapeHtml(book.id)}" ${book.coleccion && !loan ? "disabled title=\"Los ejemplares de colección no se prestan\"" : ""}>${loan ? "Devolver" : "Prestar"}</button><button class="icon-button" data-action="edit" data-id="${escapeHtml(book.id)}" aria-label="Editar ${escapeHtml(book.titulo)}" title="Editar libro">✎</button><button class="icon-button danger-icon" data-action="delete" data-id="${escapeHtml(book.id)}" aria-label="Eliminar ${escapeHtml(book.titulo)}" title="Eliminar libro">×</button></div></div>
		</article>`;
	}).join("") : `<div class="empty-state"><span class="empty-mark" aria-hidden="true">⌕</span><h3>No encontramos libros</h3><p>Prueba con otra búsqueda o cambia los filtros.</p></div>`;
}