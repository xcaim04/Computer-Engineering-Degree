(() => {
	const STORAGE_KEY = "biblioteca-digital-v1";
	const MAX_ACTIVE_LOANS = 3;
	const GENRES = ["Novela", "Ciencia", "Historia", "Poesía", "Fantasía", "Ensayo", "Biografía", "Arte"];
	const today = () => new Date().toISOString().slice(0, 10);
	const shiftDate = (date, days) => {
		const value = new Date(`${date}T12:00:00`);
		value.setDate(value.getDate() + days);
		return value.toISOString().slice(0, 10);
	};
	const uid = () => globalThis.crypto?.randomUUID?.() ?? `${Date.now()}-${Math.random().toString(16).slice(2)}`;

	function makeInitialData() {
		const books = [
			{ id: "book-1", titulo: "Cien años de soledad", autor: "Gabriel García Márquez", anio: 1967, genero: "Novela", coleccion: false, comentario: "Una saga familiar inolvidable.", isbn: "9780307474728" },
			{ id: "book-2", titulo: "Breves respuestas a las grandes preguntas", autor: "Stephen Hawking", anio: 2018, genero: "Ciencia", coleccion: false, comentario: "", isbn: "9781984819192" },
			{ id: "book-3", titulo: "El infinito en un junco", autor: "Irene Vallejo", anio: 2019, genero: "Historia", coleccion: false, comentario: "", isbn: "9788417860790" },
			{ id: "book-4", titulo: "Veinte poemas de amor", autor: "Pablo Neruda", anio: 1924, genero: "Poesía", coleccion: true, comentario: "Primera edición de colección.", isbn: "9788437604947" },
			{ id: "book-5", titulo: "El nombre del viento", autor: "Patrick Rothfuss", anio: 2007, genero: "Fantasía", coleccion: false, comentario: "", isbn: "9780756404741" },
			{ id: "book-6", titulo: "Sapiens", autor: "Yuval Noah Harari", anio: 2011, genero: "Historia", coleccion: false, comentario: "", isbn: "9780062316097" },
			{ id: "book-7", titulo: "El principito", autor: "Antoine de Saint-Exupéry", anio: 1943, genero: "Novela", coleccion: true, comentario: "Edición ilustrada de colección.", isbn: "9780156012195" },
			{ id: "book-8", titulo: "Una habitación propia", autor: "Virginia Woolf", anio: 1929, genero: "Ensayo", coleccion: false, comentario: "", isbn: "9780156030410" },
		];
		const date = today();
		const loans = [
			{ id: "loan-1", idLibro: "book-1", persona: "Ana Torres", fechaPrestamo: shiftDate(date, -21), fechaLimite: shiftDate(date, -7), fechaDevolucion: null },
			{ id: "loan-2", idLibro: "book-5", persona: "Luis Pérez", fechaPrestamo: shiftDate(date, -4), fechaLimite: shiftDate(date, 10), fechaDevolucion: null },
			{ id: "loan-3", idLibro: "book-2", persona: "Marta Ríos", fechaPrestamo: shiftDate(date, -20), fechaLimite: shiftDate(date, -6), fechaDevolucion: shiftDate(date, -8) },
			{ id: "loan-4", idLibro: "book-6", persona: "Diego León", fechaPrestamo: shiftDate(date, -18), fechaLimite: shiftDate(date, -4), fechaDevolucion: shiftDate(date, -2) },
		];
		return { libros: books, prestamos: loans, tema: "claro", usuario: "", orden: "titulo", direccion: "asc" };
	}

	function loadData() {
		try {
			const saved = JSON.parse(localStorage.getItem(STORAGE_KEY));
			if (Array.isArray(saved?.libros) && Array.isArray(saved?.prestamos)) return { ...makeInitialData(), ...saved };
		} catch (error) {
			console.warn("No se pudieron leer los datos guardados.", error);
		}
		return makeInitialData();
	}

	let data = loadData();
	let currentView = "catalogo";
	let editingId = null;
	let selectedFilter = "todos";
	let searchTerm = "";
	let selectedGenre = "todos";
	let messageTimer;
	const dialog = document.querySelector("#app-modal");

	const activeLoans = () => data.prestamos.filter((loan) => loan.fechaDevolucion === null);
	const loanForBook = (bookId) => activeLoans().find((loan) => loan.idLibro === bookId);
	const isOverdue = (loan) => loan.fechaDevolucion === null && loan.fechaLimite < today();
	const daysLate = (date) => Math.max(0, Math.floor((new Date(`${date}T12:00:00`) - new Date(`${today()}T12:00:00`)) / 86400000));
	const escapeHtml = (value = "") => String(value).replace(/[&<>"']/g, (char) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[char]);
	const formatDate = (date) => new Intl.DateTimeFormat("es", { day: "2-digit", month: "short", year: "numeric" }).format(new Date(`${date}T12:00:00`));
	const normalize = (text) => text.trim().normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLocaleLowerCase("es");

	function save() { localStorage.setItem(STORAGE_KEY, JSON.stringify(data)); }

	function announce(message, type = "success") {
		const feedback = document.querySelector("#feedback");
		feedback.textContent = message;
		feedback.dataset.type = type;
		feedback.hidden = false;
		clearTimeout(messageTimer);
		messageTimer = setTimeout(() => { feedback.hidden = true; }, 5200);
	}

	function setView(view) {
		currentView = view;
		document.querySelectorAll("[data-view-link]").forEach((link) => {
			const active = link.dataset.viewLink === view;
			link.classList.toggle("active", active);
			if (active) link.setAttribute("aria-current", "page");
			else link.removeAttribute("aria-current");
		});
		document.querySelectorAll(".view").forEach((section) => {
			section.hidden = section.id !== `view-${view}`;
			if (!section.hidden) { section.classList.remove("view-enter"); requestAnimationFrame(() => section.classList.add("view-enter")); }
		});
		document.querySelector("#page-title").textContent = ({ catalogo: "El catálogo", prestamos: "Mis préstamos", libro: editingId ? "Editar libro" : "Agregar un libro", resumen: "Resumen y ajustes" })[view];
		if (view === "libro") renderBookForm();
	}

	function updateChrome() {
		const count = activeLoans().length;
		document.querySelector("#loan-count").textContent = count;
		document.querySelector("#loan-count").hidden = count === 0;
		document.querySelector("#user-greeting").textContent = data.usuario ? `Hola, ${data.usuario}` : "Biblioteca personal";
		document.documentElement.dataset.theme = data.tema;
		document.querySelector("#theme-toggle").setAttribute("aria-label", data.tema === "oscuro" ? "Cambiar a tema claro" : "Cambiar a tema oscuro");
		document.querySelector("#theme-toggle").innerHTML = data.tema === "oscuro" ? "<span aria-hidden=\"true\">☼</span>" : "<span aria-hidden=\"true\">◐</span>";
		document.querySelector("#user-name").value = data.usuario;
	}

	function filteredBooks() {
		const filtered = data.libros.filter((book) => {
			const loan = loanForBook(book.id);
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

	function renderCatalog() {
		const genreSelect = document.querySelector("#genre-filter");
		const genres = [...new Set([...GENRES, ...data.libros.map((book) => book.genero)])].sort((a, b) => a.localeCompare(b, "es"));
		genreSelect.innerHTML = `<option value="todos">Todos los géneros</option>${genres.map((genre) => `<option value="${escapeHtml(genre)}">${escapeHtml(genre)}</option>`).join("")}`;
		genreSelect.value = selectedGenre;
		document.querySelector("#search-books").value = searchTerm;
		document.querySelector("#sort-books").value = data.orden;
		document.querySelector("#sort-direction").value = data.direccion;
		document.querySelectorAll("[data-filter]").forEach((button) => {
			button.classList.toggle("selected", button.dataset.filter === selectedFilter);
			button.setAttribute("aria-pressed", String(button.dataset.filter === selectedFilter));
		});
		const books = filteredBooks();
		document.querySelector("#catalog-count").textContent = `Mostrando ${books.length} de ${data.libros.length} libros`;
		document.querySelector("#book-grid").innerHTML = books.length ? books.map((book) => {
			const loan = loanForBook(book.id);
			const status = book.coleccion ? "coleccion" : loan ? (isOverdue(loan) ? "vencido" : "prestado") : "disponible";
			const statusLabel = book.coleccion ? "Colección" : loan ? (isOverdue(loan) ? "Vencido" : "Prestado") : "Disponible";
			const cover = book.isbn ? `<img src="https://covers.openlibrary.org/b/isbn/${escapeHtml(book.isbn)}-M.jpg" alt="Portada de ${escapeHtml(book.titulo)}" loading="lazy" onerror="this.remove()">` : "";
			return `<article class="book-card ${loan ? "is-loaned" : ""}" data-status="${status}">
				<div class="book-cover cover-${(book.id.length % 5) + 1}">${cover}<span class="cover-title">${escapeHtml(book.titulo)}</span><span class="cover-author">${escapeHtml(book.autor)}</span><span class="cover-mark" aria-hidden="true">B.</span></div>
				<div class="book-card-body"><div class="book-card-heading"><div><p class="eyebrow">${escapeHtml(book.genero)} <span aria-hidden="true">·</span> ${book.anio}</p><h3>${escapeHtml(book.titulo)}</h3><p class="book-author">${escapeHtml(book.autor)}</p></div><span class="status status-${status}">${statusLabel}</span></div>
				${loan ? `<p class="loan-detail">Prestado a <strong>${escapeHtml(loan.persona)}</strong><br>Devolver antes del ${formatDate(loan.fechaLimite)}${isOverdue(loan) ? `<span class="overdue-note">${daysLate(loan.fechaLimite)} días de retraso</span>` : ""}</p>` : `<p class="book-note">${book.coleccion ? "Ejemplar de consulta en sala" : escapeHtml(book.comentario || "Disponible para préstamo")}</p>`}
				<div class="book-actions"><button class="button button-primary button-small" data-action="${loan ? "return" : "borrow"}" data-id="${escapeHtml(book.id)}" ${book.coleccion && !loan ? "disabled title=\"Los ejemplares de colección no se prestan\"" : ""}>${loan ? "Devolver" : "Prestar"}</button><button class="icon-button" data-action="edit" data-id="${escapeHtml(book.id)}" aria-label="Editar ${escapeHtml(book.titulo)}" title="Editar libro">✎</button><button class="icon-button danger-icon" data-action="delete" data-id="${escapeHtml(book.id)}" aria-label="Eliminar ${escapeHtml(book.titulo)}" title="Eliminar libro" ${loan ? "disabled" : ""}>×</button></div></div>
			</article>`;
		}).join("") : `<div class="empty-state"><span class="empty-mark" aria-hidden="true">⌕</span><h3>No encontramos libros</h3><p>Prueba con otra búsqueda o cambia los filtros.</p></div>`;
	}

	function renderLoans() {
		const loans = activeLoans().slice().sort((a, b) => a.fechaLimite.localeCompare(b.fechaLimite));
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

	function renderBookForm() {
		const form = document.querySelector("#book-form");
		const book = editingId ? data.libros.find((item) => item.id === editingId) : null;
		form.reset();
		form.elements.titulo.value = book?.titulo ?? "";
		form.elements.autor.value = book?.autor ?? "";
		form.elements.anio.value = book?.anio ?? "";
		form.elements.genero.value = book?.genero ?? "Novela";
		form.elements.comentario.value = book?.comentario ?? "";
		form.elements.coleccion.checked = book?.coleccion ?? false;
		document.querySelector("#comment-count").textContent = 120 - form.elements.comentario.value.length;
		document.querySelector("#book-submit").textContent = editingId ? "Guardar cambios" : "Agregar libro";
		document.querySelector("#cancel-edit").hidden = !editingId;
		document.querySelector("#form-heading").textContent = editingId ? "Editar ejemplar" : "Un nuevo título";
		document.querySelector("#form-intro").textContent = editingId ? "Actualiza los datos del catálogo." : "Incorpora una nueva lectura a la biblioteca.";
		form.querySelectorAll("[data-error-for]").forEach((error) => { error.textContent = ""; });
	}

	function renderSummary() {
		const loans = activeLoans();
		const available = data.libros.filter((book) => !book.coleccion && !loanForBook(book.id)).length;
		const overdue = loans.filter(isOverdue).length;
		document.querySelector("#summary-stats").innerHTML = [
			["Ejemplares", data.libros.length, "en el catálogo"], ["Disponibles", available, "listos para salir"], ["Prestados", loans.length, "en circulación"], ["Vencidos", overdue, "requieren atención"],
		].map(([label, value, note], index) => `<article class="stat-block stat-block-${index + 1}"><p>${label}</p><strong>${value}</strong><span>${note}</span></article>`).join("");
		const counts = data.libros.reduce((result, book) => { result[book.genero] = (result[book.genero] || 0) + 1; return result; }, {});
		const max = Math.max(1, ...Object.values(counts));
		document.querySelector("#genre-summary").innerHTML = Object.entries(counts).sort(([a], [b]) => a.localeCompare(b, "es")).map(([genre, count]) => `<div class="genre-row"><div><span>${escapeHtml(genre)}</span><strong>${count}</strong></div><span class="genre-bar"><span style="width:${(count / max) * 100}%"></span></span></div>`).join("") || `<p class="muted-copy">Añade libros para ver la distribución por género.</p>`;
	}

	function renderAll() {
		updateChrome(); renderCatalog(); renderLoans(); renderSummary();
		if (currentView === "libro") setView("libro");
	}

	function openBorrowModal(book) {
		dialog.dataset.mode = "borrow";
		dialog.dataset.bookId = book.id;
		dialog.innerHTML = `<form method="dialog" class="modal-content" id="borrow-form"><div class="modal-kicker">Préstamo nuevo</div><button class="modal-close" value="cancel" aria-label="Cerrar">×</button><h2>¿Quién se lleva<br><em>${escapeHtml(book.titulo)}</em>?</h2><p class="modal-copy">El préstamo tendrá un plazo de 14 días.</p><label class="field-label" for="borrower-name">Nombre de la persona</label><input id="borrower-name" name="persona" class="text-input" autocomplete="name" minlength="2" maxlength="40" required value="${escapeHtml(data.usuario)}" placeholder="Ej. Ana Torres"><p class="field-error modal-error" id="modal-error" role="alert"></p><div class="modal-actions"><button class="button button-secondary" value="cancel">Cancelar</button><button class="button button-primary" type="submit" value="confirm">Confirmar préstamo</button></div></form>`;
		dialog.showModal();
		dialog.querySelector("#borrower-name").focus();
	}

	function openDeleteModal(book) {
		dialog.dataset.mode = "delete";
		dialog.dataset.bookId = book.id;
		dialog.innerHTML = `<form method="dialog" class="modal-content" id="delete-form"><div class="modal-kicker">Quitar del catálogo</div><button class="modal-close" value="cancel" aria-label="Cerrar">×</button><h2>¿Eliminar<br><em>${escapeHtml(book.titulo)}</em>?</h2><p class="modal-copy">El ejemplar y su historial de préstamos devueltos se eliminarán permanentemente.</p><div class="modal-actions"><button class="button button-secondary" value="cancel">Cancelar</button><button class="button button-danger" type="submit" value="confirm">Eliminar libro</button></div></form>`;
		dialog.showModal();
	}

	function returnBook(bookId) {
		const loan = loanForBook(bookId);
		if (!loan) return;
		const late = Math.max(0, Math.floor((new Date(`${today()}T12:00:00`) - new Date(`${loan.fechaLimite}T12:00:00`)) / 86400000));
		loan.fechaDevolucion = today();
		save(); renderAll();
		announce(late ? `Devolución registrada con ${late} ${late === 1 ? "día" : "días"} de retraso.` : "Devolución registrada a tiempo.", late ? "warning" : "success");
	}

	function validateBook(book) {
		const errors = {};
		if (book.titulo.length < 3 || book.titulo.length > 60) errors.titulo = "Escribe entre 3 y 60 caracteres.";
		if (!book.autor.trim()) errors.autor = "El nombre del autor es obligatorio.";
		if (!Number.isInteger(book.anio) || book.anio < 1000 || book.anio > 2026) errors.anio = "Usa un año entre 1000 y 2026.";
		if (data.libros.some((item) => item.id !== editingId && normalize(item.titulo) === normalize(book.titulo) && normalize(item.autor) === normalize(book.autor))) errors.titulo = "Ya existe un libro con ese título y autor.";
		if (book.comentario.length > 120) errors.comentario = "El comentario no puede superar 120 caracteres.";
		if (editingId && loanForBook(editingId) && book.coleccion) errors.coleccion = "No puedes marcar como colección un libro prestado.";
		return errors;
	}

	function clearForm() { editingId = null; renderBookForm(); }

	document.addEventListener("click", (event) => {
		const viewLink = event.target.closest("[data-view-link]");
		if (viewLink) {
			event.preventDefault();
			if (viewLink.dataset.viewLink === "libro") clearForm();
			setView(viewLink.dataset.viewLink);
			return;
		}
		const filter = event.target.closest("[data-filter]");
		if (filter) { selectedFilter = filter.dataset.filter; renderCatalog(); return; }
		const action = event.target.closest("[data-action]");
		if (!action || action.disabled) return;
		const book = data.libros.find((item) => item.id === action.dataset.id);
		if (!book) return;
		if (action.dataset.action === "borrow") {
			if (activeLoans().length >= MAX_ACTIVE_LOANS) { announce("No se puede prestar: la biblioteca ya alcanzó el máximo de 3 préstamos activos.", "error"); return; }
			if (book.coleccion || loanForBook(book.id)) { announce("Este ejemplar no está disponible para préstamo.", "error"); return; }
			openBorrowModal(book);
		} else if (action.dataset.action === "return") returnBook(book.id);
		else if (action.dataset.action === "edit") {
			editingId = book.id; setView("libro"); document.querySelector("#book-form input[name='titulo']").focus();
		} else if (action.dataset.action === "delete") {
			if (loanForBook(book.id)) { announce("No se puede eliminar un libro con un préstamo activo.", "error"); return; }
			openDeleteModal(book);
		}
	});

	document.querySelector("#search-books").addEventListener("input", (event) => { searchTerm = event.target.value; renderCatalog(); document.querySelector("#search-books").focus(); });
	document.querySelector("#genre-filter").addEventListener("change", (event) => { selectedGenre = event.target.value; renderCatalog(); });
	document.querySelector("#sort-books").addEventListener("change", (event) => { data.orden = event.target.value; save(); renderCatalog(); });
	document.querySelector("#sort-direction").addEventListener("change", (event) => { data.direccion = event.target.value; save(); renderCatalog(); });

	document.querySelector("#book-form").addEventListener("input", (event) => {
		if (event.target.name === "comentario") document.querySelector("#comment-count").textContent = 120 - event.target.value.length;
	});
	document.querySelector("#book-form").addEventListener("submit", (event) => {
		event.preventDefault();
		const form = event.currentTarget;
		const book = { titulo: form.elements.titulo.value.trim(), autor: form.elements.autor.value.trim(), anio: Number(form.elements.anio.value), genero: form.elements.genero.value, comentario: form.elements.comentario.value.trim(), coleccion: form.elements.coleccion.checked };
		const errors = validateBook(book);
		form.querySelectorAll("[data-error-for]").forEach((node) => { node.textContent = errors[node.dataset.errorFor] || ""; });
		const firstInvalid = Object.keys(errors)[0];
		if (firstInvalid) { form.elements[firstInvalid].focus(); return; }
		if (editingId) { Object.assign(data.libros.find((item) => item.id === editingId), book); announce(`«${book.titulo}» se actualizó correctamente.`); }
		else { data.libros.push({ id: uid(), ...book }); announce(`«${book.titulo}» se agregó al catálogo.`); }
		save(); clearForm(); renderAll(); setView("catalogo");
	});

	document.querySelector("#cancel-edit").addEventListener("click", () => { clearForm(); announce("Edición cancelada.", "info"); });
	document.querySelector("#theme-toggle").addEventListener("click", () => { data.tema = data.tema === "oscuro" ? "claro" : "oscuro"; save(); updateChrome(); });
	document.querySelector("#user-name").addEventListener("input", (event) => { data.usuario = event.target.value.trim().slice(0, 40); save(); updateChrome(); });
	document.querySelector("#reset-library").addEventListener("click", () => {
		data = makeInitialData(); save(); selectedFilter = "todos"; searchTerm = ""; selectedGenre = "todos"; editingId = null;
		renderAll(); setView("resumen"); announce("La biblioteca volvió a sus datos iniciales.");
	});

	dialog.addEventListener("submit", (event) => {
		const mode = dialog.dataset.mode;
		if (event.submitter?.value === "cancel") return;
		if (mode === "borrow") {
			event.preventDefault();
			const input = dialog.querySelector("#borrower-name");
			const name = input.value.trim();
			const bookId = dialog.dataset.bookId;
			const book = data.libros.find((item) => item.id === bookId);
			const error = name.length < 2 || name.length > 40 ? "El nombre debe tener entre 2 y 40 caracteres." : activeLoans().length >= MAX_ACTIVE_LOANS ? "Ya hay 3 préstamos activos. Devuelve uno antes de continuar." : !book || book.coleccion || loanForBook(bookId) ? "Este libro ya no está disponible." : "";
			if (error) { dialog.querySelector("#modal-error").textContent = error; input.focus(); return; }
			data.prestamos.push({ id: uid(), idLibro: bookId, persona: name, fechaPrestamo: today(), fechaLimite: shiftDate(today(), 14), fechaDevolucion: null });
			save(); dialog.close(); renderAll(); announce(`Préstamo de «${book.titulo}» registrado para ${name}.`);
		} else if (mode === "delete") {
			event.preventDefault();
			const bookId = dialog.dataset.bookId;
			const book = data.libros.find((item) => item.id === bookId);
			if (book && !loanForBook(bookId)) {
				data.libros = data.libros.filter((item) => item.id !== bookId);
				data.prestamos = data.prestamos.filter((loan) => loan.idLibro !== bookId);
				save(); dialog.close(); renderAll(); announce(`«${book.titulo}» y su historial se eliminaron.`);
			}
		}
	});
	dialog.addEventListener("click", (event) => { if (event.target === dialog) dialog.close(); });
	document.addEventListener("keydown", (event) => {
		if (event.key === "Escape" && !dialog.open && currentView === "libro" && editingId) { clearForm(); setView("catalogo"); }
	});

	renderAll();
	setView("catalogo");
})();
