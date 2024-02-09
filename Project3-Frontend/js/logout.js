

export function clickOnLogout(){
    const logoutbtn = document.getElementById("nav-exit");
    logoutbtn.addEventListener('click',function(event){
        const username = localStorage.getItem('username');

        logout(username);
    })  
}

/* Pedido de logout ao backend */ 
async function logout(username){
    console.log(username);
    await fetch('http://localhost:8080/Project3-Backend/rest/user/logout',
        {
            method: 'POST',
            headers:
        {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'username' : username
        },
            credentials: 'include',
        }
        ).then(function (response) {
            if (response.status == 200) { 
                alert('Sucessfull Logout');
                console.log("Logout");
                localStorage.removeItem('username'); 
                localStorage.removeItem("photoUrl"); 
                window.location.href = "index.html";  
            } else {
                alert('something went wrong :(');
         }
    });
}
