document.addEventListener("DOMContentLoaded", () => {
    const nameInput = document.querySelector("#name");
    const nameCounter = document.querySelector("#name-counter");

    const updateNameCounter = () => {
        if (nameInput && nameCounter) {
            nameCounter.textContent = `${nameInput.value.length}/20`;
        }
    };

    nameInput?.addEventListener("input", updateNameCounter);
    updateNameCounter();

    const searchInput = document.querySelector("#language-search");
    const clearSearchButton = document.querySelector("#clear-search");
    const rows = [...document.querySelectorAll("[data-language-row]")];
    const searchEmptyState = document.querySelector("#search-empty-state");
    const searchStatus = document.querySelector("#search-status");
    const table = document.querySelector(".table-responsive");

    const normalizeText = (value) => value
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .toLocaleLowerCase("pt-BR")
        .trim();

    const filterLanguages = () => {
        if (!searchInput) {
            return;
        }

        const query = normalizeText(searchInput.value);
        let visibleRows = 0;

        rows.forEach((row) => {
            const name = normalizeText(row.querySelector(".language-name")?.textContent ?? "");
            const isVisible = name.includes(query);
            row.classList.toggle("d-none", !isVisible);
            visibleRows += isVisible ? 1 : 0;
        });

        clearSearchButton?.classList.toggle("d-none", query.length === 0);
        searchEmptyState?.classList.toggle("d-none", visibleRows !== 0);
        table?.classList.toggle("d-none", visibleRows === 0);

        if (searchStatus) {
            searchStatus.textContent = `${visibleRows} ${visibleRows === 1 ? "idioma exibido" : "idiomas exibidos"}`;
        }
    };

    searchInput?.addEventListener("input", filterLanguages);
    clearSearchButton?.addEventListener("click", () => {
        searchInput.value = "";
        filterLanguages();
        searchInput.focus();
    });

    const deleteModal = document.querySelector("#delete-language-modal");
    const deleteForm = document.querySelector("#delete-language-form");
    const deleteLanguageName = document.querySelector("#delete-language-name");

    deleteModal?.addEventListener("show.bs.modal", (event) => {
        const trigger = event.relatedTarget;
        if (!(trigger instanceof HTMLElement)) {
            return;
        }

        if (deleteForm instanceof HTMLFormElement) {
            deleteForm.action = trigger.dataset.deleteUrl ?? "";
        }
        if (deleteLanguageName) {
            deleteLanguageName.textContent = trigger.dataset.languageName ?? "este idioma";
        }
    });
});
