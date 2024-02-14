export function loadPhoto() {
  const photoUrl = localStorage.getItem("photoUrl");
  if (photoUrl !== null) {
    const userPhoto = document.getElementById("userPhoto");
    userPhoto.setAttribute("src", photoUrl);
  }
}
