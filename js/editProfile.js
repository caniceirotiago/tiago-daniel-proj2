import * as registration from "./registration.js";

document.addEventListener("DOMContentLoaded", function() {
    const registrationForm = document.getElementById('editProfileForm');

    registrationForm.addEventListener('submit', function(event) {
        // Previne o envio do formulário antes da validação
        event.preventDefault();
        // Se todas as validações passaram, permite o envio do formulário
        if (registration.isValid()) {
            registrationForm.submit();
            window.location.href(index.html);
        }
    });
});

