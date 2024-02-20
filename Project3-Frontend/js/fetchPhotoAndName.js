export async function fetchPhotoNameAndRedirect(username, password) {
    await fetch('http://localhost:8080/tiago-daniel-proj2/rest/user/getphotoandname',
    {
        method: 'GET',
        headers: { 
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'username' : username,
            'password': password
        },
        credentials: 'include',
      }
    ).then(response => response.json())
    .then(function (response){
        console.log(response)
        localStorage.setItem("photoUrl",response.photoUrl);
        localStorage.setItem("name", response.name); 
    });
    window.location.href= "homepage.html";
}