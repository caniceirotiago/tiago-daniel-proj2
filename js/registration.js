document.addEventListener("DOMContentLoaded", function() {
    const registrationForm = document.getElementById('registrationForm');

    registrationForm.addEventListener('submit', function(event) {
        // Previne o envio do formulário antes da validação
        event.preventDefault();
        // Se todas as validações passaram, permite o envio do formulário
        if (isValid()) {
            registrationForm.submit();
            window.location.href(index.html);
        }
    });
});

export function isValid(){
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const password2 = document.getElementById('password2').value;
    const email = document.getElementById('email').value;
    const firstName = document.getElementById('first-name').value;
    const lastName = document.getElementById('last-name').value;

     // Validar username
    if (username.length < 2 || username.length > 20) {
        alert("Username must be between 2 and 20 characters.");
        return false;
    }

    // Validar passwords
    if (password !== password2 || password.length < 6) {
        alert("Passwords must match and be at least 6 characters long.");
        return false;
    }

    // Validar email de maneira simples
    if (!email.includes('@') || !email.substring(email.indexOf('@')).includes('.')) {
        alert("Please enter a valid email address.");
        return false;
    }

    // Validar first name e last name
    if (firstName.length < 3 || firstName.length > 25 || lastName.length < 3 || lastName.length > 25) {
        alert("First name and last name must be between 3 and 25 characters.");
        return false;
    }
    return true;
}
