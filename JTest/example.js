function validateUsername() {
  // função que valida o username
  const username = document.getElementById("username-field").value;
  if (username.length < 2 || username.length > 25) {
    alert("Username must be between 2 and 25 characters.");
    return false;
  }
  return true;
}
function validatePassword() {
  const password = document.getElementById("password-field").value;
  const password2 = document.getElementById("password2-field").value;
  // Validar passwords
  if (password !== password2 || password.length < 4) {
    alert("Passwords must match and be at least 4 characters long.");
    return false;
  }
  return true;
}

function validateEmail() {
  const email = document.getElementById("email-field").value;
  // Validar email de maneira simples
  if (
    !email.includes("@") ||
    !email.substring(email.indexOf("@")).includes(".")
  ) {
    alert("Please enter a valid email address.");
    return false;
  }
  return true;
}
function validateName() {
  const firstName = document.getElementById("firstname-field").value;
  const lastName = document.getElementById("lastname-field").value;
  // Validar first name e last name
  if (
    firstName.length < 1 ||
    firstName.length > 25 ||
    lastName.length < 1 ||
    lastName.length > 25
  ) {
    alert("First name and last name must be between 1 and 25 characters.");
    return false;
  }
  return true;
}

function validatePhotoURL() {
  const photoURL = document.getElementById("photo-field").value;
  if (photoURL.length < 3 || photoURL.length > 500) {
    alert("Photo URL must be between 3 and 500 characters.");
    return false;
  }
  return true;
}

module.exports = {
  validateUsername,
  validatePassword,
  validateEmail,
  validateName,
  validatePhotoURL,
};
